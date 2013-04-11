package game.cliente.states;

import java.util.UUID;

import game.cliente.components.Player;
import game.cliente.components.PlayerNetwork;
import game.cliente.components.Transform;
import game.cliente.connections.HostConnect;
import game.cliente.core.EntityFactory;
import game.cliente.core.ManagerMap;
import game.cliente.hero.CharacterPosition;
import game.cliente.systems.BoundaryPlayerSystem;
import game.cliente.systems.CameraPlayerSystem;
import game.cliente.systems.MovementPlayerNetWorkSystem;
import game.cliente.systems.MovementPlayerSystem;
import game.cliente.systems.RenderGUISystem;
import game.cliente.systems.RenderMapCloudSystem;
import game.cliente.systems.RenderMapSystem;
import game.cliente.systems.RenderPlayerNetworkSystem;
import game.cliente.systems.RenderPlayerSystem;
import game.cliente.utils.Util;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.SystemManager;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class InGameState extends BasicGameState implements
		IDeferredResourceState {

	public static final int ID = 2;

	private World world;
	private GameContainer container;
	private SystemManager systemManager;

	private EntitySystem renderMapSystem;
	private EntitySystem renderPlayerSystem;
	private EntitySystem renderPlayerNetworkSystem;
	private EntitySystem renderCloudSystem;
	private EntitySystem renderGUISystem;
	private EntitySystem movementPlayerSystem;
	private EntitySystem movementPlayerNetworkSystem;
	private EntitySystem boundarySystem;
	private EntitySystem cameraSystem;
	private ManagerMap managerMap;

	private HostConnect hostConnect;

	public InGameState(HostConnect hostConnect) {
		this.hostConnect = hostConnect;
	}

	@Override
	public void init(GameContainer container, StateBasedGame sbGame)
			throws SlickException {
		this.container = container;
	}

	@Override
	public void initDeferredResources() {
		// Cria o mundo
		world = new World();

		managerMap = new ManagerMap(container, this.world, this.hostConnect);
		managerMap.getUpdateMap("map_1");

		// "systemManager" = gerenciar os sitemas
		systemManager = world.getSystemManager();

		// Come鏰 a instanciar os sistemas
		renderMapSystem = systemManager.setSystem(new RenderMapSystem(
				this.container, this.managerMap));

		renderPlayerNetworkSystem = systemManager
				.setSystem(new RenderPlayerNetworkSystem(this.container,
						this.managerMap));

		renderPlayerSystem = systemManager.setSystem(new RenderPlayerSystem(
				this.container, this.managerMap));

		movementPlayerSystem = systemManager.setSystem(new MovementPlayerSystem(
				this.container, this.hostConnect));
		
		movementPlayerNetworkSystem = systemManager.setSystem(new MovementPlayerNetWorkSystem());
		

		// Faz o "Hero" n鉶 se mover fora dos limites do mapa (Ex:TutorialMap),
		// "RenderSystem" tem que ser instanciado antes!
		boundarySystem = systemManager.setSystem(new BoundaryPlayerSystem(
				this.managerMap));
		// Limita a camera ao tamanho do mapa, ele n鉶 passa a vis鉶 al閙 do
		// tamanho do mapa (BoundarySystem deve ser instanciado antes)
		cameraSystem = systemManager.setSystem(new CameraPlayerSystem(
				this.container));

		renderCloudSystem = systemManager.setSystem(new RenderMapCloudSystem(
				this.container, this.managerMap));

		renderGUISystem = systemManager.setSystem(new RenderGUISystem(
				this.container, this.hostConnect));

		// Inicializa todos os sistemas
		systemManager.initializeAll();
		// world.getSystemManager().getSystems().remove(world.getSystemManager().getSystems().get(4));

		// CreateGeoMapForServer cgmfs = new CreateGeoMapForServer();
		// cgmfs.createWall("doc/sqlGeoMap.sql", "map_1");

	}

	@Override
	public void render(GameContainer container, StateBasedGame sbGame,
			Graphics g) throws SlickException {
		if (this.managerMap.isAtualizandoMapa() == false) {
			renderMapSystem.process();
			renderPlayerNetworkSystem.process();
			renderPlayerSystem.process();
			renderCloudSystem.process();
			renderGUISystem.process();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame sbGame, int delta)
			throws SlickException {
		if (container.getInput().isKeyDown(Input.KEY_P)) {
			sbGame.enterState(BattleState.ID, new FadeOutTransition(),
					new FadeInTransition());
		}

		if (this.managerMap.isAtualizandoMapa() == false) {
			world.loopStart();
			world.setDelta(delta);
			boundarySystem.process();
			cameraSystem.process();
			movementPlayerSystem.process();
			movementPlayerNetworkSystem.process();
			((RenderGUISystem) renderGUISystem).update();
			checkReceivedChannelMessage(this.hostConnect
					.getReceivedChannelMessage());
		}
	}

	public void checkReceivedChannelMessage(String receivedChannelMessage) {
		if (receivedChannelMessage != null) {
			String[] msg = receivedChannelMessage.split("/");
			if (msg[0].equals("m")) {

				if (msg[1].equalsIgnoreCase("loggout")) {
					// formato: "m/loggout/loginId"
					// Remove um dos players da rede do seu jogo (Player perdeu
					// conex鉶)
					UUID loginId = UUID.fromString(msg[2]);
					Entity playerNetwork = selectPlayerNetwork(loginId);
					int x = playerNetwork.getComponent(Transform.class)
							.getTileX();
					int y = playerNetwork.getComponent(Transform.class)
							.getTileY();
					removePlayerNetwork(loginId);
					Util.OBJECTS_OF_WORLD[x][y] = Util.TERRAIN_GROUND; // Libera
																		// o
																		// espa鏾
					this.hostConnect.setReceivedChannelMessage(null);
				} else {
					Entity playerLocal = world.getTagManager()
							.getEntity("Hero");
					UUID playerLoginId = playerLocal.getComponent(Player.class)
							.getLoginId();
					UUID playerNetworkLoginId =UUID.fromString(msg[1]);
					if (playerNetworkLoginId != playerLoginId) {

						// checa se estou enviando a mensagem para mim mesmo
						if (selectPlayerNetwork(UUID.fromString(msg[1])) != null) {
							// Player j�existe, s�atualiza
							// Formato:
							// "m/loginId/ClasseId/PlayerName/x/y/keyArrow")

							// Libera a posi玢o anterior
							UUID loginId = UUID.fromString(msg[1]);
							Entity playerNetworkOld = selectPlayerNetwork(loginId);
							int x = playerNetworkOld.getComponent(
									Transform.class).getTileX();
							int y = playerNetworkOld.getComponent(
									Transform.class).getTileY();
							Util.OBJECTS_OF_WORLD[x][y] = Util.TERRAIN_GROUND;

							// Atualiza a nova posi玢o
							updatePlayerNetwork(msg);
							Entity playerNetworkNew = selectPlayerNetwork(loginId);
							x = playerNetworkNew.getComponent(Transform.class)
									.getTileX();
							y = playerNetworkNew.getComponent(Transform.class)
									.getTileY();
							Util.OBJECTS_OF_WORLD[x][y] = Util.TERRAIN_OBJECT_PLAYER_NETWORK;

						} else {
							addPlayerNetwork(msg);
							int x = Integer.valueOf(msg[4]);
							int y = Integer.valueOf(msg[5]);		

							Util.OBJECTS_OF_WORLD[x][y] = Util.TERRAIN_OBJECT_PLAYER_NETWORK;
							
							Player compPlayer = playerLocal.getComponent(Player.class);
							Transform transform = playerLocal.getComponent(Transform.class);
							int position = 0;
							if (transform.getPosition().equals(CharacterPosition.FACE_DOWN)){
								position = 0;
							} else if (transform.getPosition().equals(CharacterPosition.FACE_LEFT)){
								position = 1;
							} else if (transform.getPosition().equals(CharacterPosition.FACE_RIGHT)){
								position = 2;
							} else if (transform.getPosition().equals(CharacterPosition.FACE_UP)){
								position = 3;
							}
							int tileX=transform.getTileX();
							int tileY=transform.getTileY();
							if (transform.getFuturePositionTileX()!=Util.NO_CLICK_OR_PRESS_KEY && transform.getFuturePositionTileY()!=Util.NO_CLICK_OR_PRESS_KEY){
								tileX = transform.getFuturePositionTileX();
								tileY = transform.getFuturePositionTileY();
							}
							this.hostConnect.sendChannel("m/"+compPlayer.getLoginId().toString()+"/"+compPlayer.getClasseId()+"/"+compPlayer.getName()+"/"+tileX+"/"+tileY+"/"+position+"/", Util.CHANNEL_MAP);
						}
					}
					this.hostConnect.setReceivedChannelMessage(null);
				}
			}
		}
	}

	public Entity addPlayerNetwork(String[] msg) {
		// Formato: "m/loginId/ClasseId/PlayerName/x/y/keyArrow")
		PlayerNetwork playerNetwork = new PlayerNetwork();
		playerNetwork.setLoginId(UUID.fromString(msg[1]));
		playerNetwork.setClasseId(Integer.valueOf(msg[2]));
		playerNetwork.setName(msg[3]);
		int x = Integer.valueOf(msg[4]);
		int y = Integer.valueOf(msg[5]);		
		int position = Integer.valueOf(msg[6]);
		Transform transform = new Transform(x * 32, y * 32);
		transform.setFuturePositionTileX(Util.NO_CLICK_OR_PRESS_KEY);
		transform.setFuturePositionTileY(Util.NO_CLICK_OR_PRESS_KEY);
		if (position == 0) {
			transform.setPosition(CharacterPosition.FACE_DOWN);
		} else if (position == 1) {
			transform.setPosition(CharacterPosition.FACE_LEFT);
		} else if (position == 2) {
			transform.setPosition(CharacterPosition.FACE_RIGHT);
		} else if (position == 3) {
			transform.setPosition(CharacterPosition.FACE_UP);
		}
		Entity entityPlayerNetwork = EntityFactory.createPlayerNetwork(world,
				transform, Util.SPEED_PLAYER_NETWORK, Color.orange,
				playerNetwork);

		this.managerMap.addSpatial(entityPlayerNetwork,
				playerNetwork.getClasseId());
		return entityPlayerNetwork;
	}

	public void updatePlayerNetwork(String[] msg) {
		// Formato: "m/loginId/ClasseId/PlayerName/x/y/CharacterPosition")
		UUID loginId = UUID.fromString(msg[1]);
		int classeId = Integer.valueOf(msg[2]);
		String playerName = msg[3];
		int x = Integer.valueOf(msg[4]);
		int y = Integer.valueOf(msg[5]);
		int position = Integer.valueOf(msg[6]);

		ImmutableBag<Entity> playersNetwork = world.getGroupManager()
				.getEntities("Group: PlayerNetWork");
		for (int i = 0; i < playersNetwork.size(); i++) {
			Entity playerNetwork = playersNetwork.get(i);
			if (playerNetwork.getComponent(PlayerNetwork.class).getLoginId() == loginId) {

				playerNetwork.getComponent(PlayerNetwork.class).setClasseId(
						classeId);
				playerNetwork.getComponent(PlayerNetwork.class).setName(
						playerName);
				Transform transform = playerNetwork.getComponent(Transform.class);
				transform.setFuturePositionTileX(x);
				transform.setFuturePositionTileY(y);
				if (position == 0) {
					transform.setPosition(CharacterPosition.FACE_DOWN);
				} else if (position == 1) {
					transform.setPosition(CharacterPosition.FACE_LEFT);
				} else if (position == 2) {
					transform.setPosition(CharacterPosition.FACE_RIGHT);
				} else if (position == 3) {
					transform.setPosition(CharacterPosition.FACE_UP);
				}
				if ((transform.getFuturePositionTileX()) != transform.getTileX() || 
						(transform.getFuturePositionTileY()) != transform.getTileY()){
				  transform.setMoving(true);
				}

			}
		}
	}

	public void removePlayerNetwork(UUID loginId) {
		ImmutableBag<Entity> playersNetwork = world.getGroupManager()
				.getEntities("Group: PlayerNetWork");
		for (int i = 0; i < playersNetwork.size(); i++) {
			Entity playerNetwork = playersNetwork.get(i);
			if (playerNetwork.getComponent(PlayerNetwork.class).getLoginId() == loginId) {
				this.managerMap.removeSpatial(playerNetwork);
				world.deleteEntity(playerNetwork);
				playerNetwork.delete();
				world.loopStart();
			}
		}
	}

	public Entity selectPlayerNetwork(UUID loginId) {
		// Remove um dos players da rede do seu jogo (Player perdeu conex鉶)
		ImmutableBag<Entity> playersNetwork = world.getGroupManager()
				.getEntities("Group: PlayerNetWork");
		if (playersNetwork != null) {
			for (int i = 0; i < playersNetwork.size(); i++) {
				Entity playerNetwork = playersNetwork.get(i);
				if (playerNetwork.getComponent(PlayerNetwork.class)
						.getLoginId() == loginId) {
					return playerNetwork;
				}
			}
		}
		return null;
	}

	@Override
	public int getID() {
		return ID;
	}

	public ManagerMap getManagerMap() {
		return managerMap;
	}
}