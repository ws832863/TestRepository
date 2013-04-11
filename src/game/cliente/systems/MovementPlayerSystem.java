package game.cliente.systems;

import game.cliente.components.Player;
import game.cliente.components.Transform;
import game.cliente.components.Velocity;
import game.cliente.connections.HostConnect;
import game.cliente.core.ManagerMap;
import game.cliente.core.PathFinder;
import game.cliente.hero.CharacterPosition;
import game.cliente.utils.Util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.Path;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

/**
 * @author Michel Montenegro - Criador do 1�sistema de movimento.
 * 
 * @Agradecimentos - Pedro Silva Moreira [PeJuGe]: Por ter desenvolvido a 2�
 *                 vers鉶 do sistema de movimento, usando uma Thread externa. -
 *                 Erickzanardo: Por ter sido o meu co-desenvolvedor na 3�
 *                 vers鉶 do sistema de movimento atual.
 */
public class MovementPlayerSystem extends EntityProcessingSystem {
	private GameContainer container;
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Velocity> velocityMapper;
	private HostConnect hostConnect;

	// Guarda em ordem, todos os passos (Steps) que devem ser percorridos
	// para se chegar no local desejado.
	private Path caminho;

	// Guarda o passo(Step) atual do personagem ()
	private int countSteps = Util.NO_CLICK_OR_PRESS_KEY;

	// Guarda a dire玢o que o personagem ira se mover
	private CharacterPosition position;

	public MovementPlayerSystem(GameContainer container, HostConnect hostConnect) {
		super(Player.class);
		this.container = container;
		this.hostConnect = hostConnect;
	}

	@Override
	public void initialize() {
		velocityMapper = new ComponentMapper<Velocity>(Velocity.class,
				world.getEntityManager());
		transformMapper = new ComponentMapper<Transform>(Transform.class,
				world.getEntityManager());
	}

	// // Source X = sX
	public CharacterPosition directionPath(int sX, int sY, int tX, int tY) {
		if (sX < tX) {
			return CharacterPosition.FACE_RIGHT;
		} else if (sX > tX) {
			return CharacterPosition.FACE_LEFT;
		} else if (sY < tY) {
			return CharacterPosition.FACE_DOWN;
		} else if (sY > tY) {
			return CharacterPosition.FACE_UP;
		}
		// null = parado
		return null;
	}

	@Override
	protected void process(Entity player) {
		Transform transform = transformMapper.get(player);
		Velocity velocity = velocityMapper.get(player);
		Player compPlayer = player.getComponent(Player.class);
		
		int mouseClickX = Util.MOUSE_CLICK_POSITION_XY.x;
		int mouseClickY = Util.MOUSE_CLICK_POSITION_XY.y;

		if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
			if (transform.getDistanceTraveled() == 0.0f) {
				Util.MOUSE_CLICK_POSITION_XY.setLocation(
						Util.NO_CLICK_OR_PRESS_KEY, Util.NO_CLICK_OR_PRESS_KEY);
				countSteps = Util.NO_CLICK_OR_PRESS_KEY;
				caminho = null;
				position = null;
				if (ManagerMap.entityWallCollisionWith(transform.getTileX(),
						transform.getTileY(), CharacterPosition.FACE_LEFT)==false) {
					transform.setFuturePositionTileX(transform.getTileX() - 1);
					transform.setFuturePositionTileY(transform.getTileY());
					// "m/loginId/ClasseId/PlayerName/x/y/keyArrow")
					this.hostConnect.sendChannel("m/"+compPlayer.getLoginId()+"/"+compPlayer.getClasseId()+"/"+compPlayer.getName()+"/"+transform.getFuturePositionTileX()+"/"+transform.getFuturePositionTileY()+"/1", Util.CHANNEL_MAP);
					transform.setPosition(CharacterPosition.FACE_LEFT);
					transform.setMoving(true);
				}
			}
		} else if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
			if (transform.getDistanceTraveled() == 0.0f) {
				Util.MOUSE_CLICK_POSITION_XY.setLocation(
						Util.NO_CLICK_OR_PRESS_KEY, Util.NO_CLICK_OR_PRESS_KEY);
				countSteps = Util.NO_CLICK_OR_PRESS_KEY;
				caminho = null;
				position = null;
				if (ManagerMap.entityWallCollisionWith(transform.getTileX(),
						transform.getTileY(), CharacterPosition.FACE_RIGHT)==false) {
					transform.setFuturePositionTileX(transform.getTileX() + 1);
					transform.setFuturePositionTileY(transform.getTileY());
					this.hostConnect.sendChannel("m/"+compPlayer.getLoginId()+"/"+compPlayer.getClasseId()+"/"+compPlayer.getName()+"/"+transform.getFuturePositionTileX()+"/"+transform.getFuturePositionTileY()+"/2", Util.CHANNEL_MAP);
					transform.setPosition(CharacterPosition.FACE_RIGHT);
					transform.setMoving(true);
				}
			}
		} else if (container.getInput().isKeyDown(Input.KEY_UP)) {
			if (transform.getDistanceTraveled() == 0.0f) {
				Util.MOUSE_CLICK_POSITION_XY.setLocation(
						Util.NO_CLICK_OR_PRESS_KEY, Util.NO_CLICK_OR_PRESS_KEY);
				countSteps = Util.NO_CLICK_OR_PRESS_KEY;
				caminho = null;
				position = null;
				if (ManagerMap.entityWallCollisionWith(transform.getTileX(),
						transform.getTileY(), CharacterPosition.FACE_UP)==false) {
					transform.setFuturePositionTileX(transform.getTileX());
					transform.setFuturePositionTileY(transform.getTileY()-1);
					this.hostConnect.sendChannel("m/"+compPlayer.getLoginId()+"/"+compPlayer.getClasseId()+"/"+compPlayer.getName()+"/"+transform.getFuturePositionTileX()+"/"+transform.getFuturePositionTileY()+"/3", Util.CHANNEL_MAP);
					transform.setPosition(CharacterPosition.FACE_UP);
					transform.setMoving(true);
				}
			}
		} else if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
			if (transform.getDistanceTraveled() == 0.0f) {
				Util.MOUSE_CLICK_POSITION_XY.setLocation(
						Util.NO_CLICK_OR_PRESS_KEY, Util.NO_CLICK_OR_PRESS_KEY);
				countSteps = Util.NO_CLICK_OR_PRESS_KEY;
				caminho = null;
				position = null;
				if (ManagerMap.entityWallCollisionWith(transform.getTileX(),
						transform.getTileY(), CharacterPosition.FACE_DOWN)==false) {
					transform.setFuturePositionTileX(transform.getTileX());
					transform.setFuturePositionTileY(transform.getTileY()+1);
					this.hostConnect.sendChannel("m/"+compPlayer.getLoginId()+"/"+compPlayer.getClasseId()+"/"+compPlayer.getName()+"/"+transform.getFuturePositionTileX()+"/"+transform.getFuturePositionTileY()+"/0", Util.CHANNEL_MAP);
					transform.setPosition(CharacterPosition.FACE_DOWN);
					transform.setMoving(true);
				}
			}
		} else if (mouseClickX >= 0 && mouseClickY >= 0) {
			Util.MOUSE_CLICK_POSITION_XY.setLocation(
					Util.NO_CLICK_OR_PRESS_KEY, Util.NO_CLICK_OR_PRESS_KEY);
			PathFinder pathFind = new PathFinder();
			try {
				int pathFinderStartX = 0;
				int pathFinderStartY = 0;
				if (transform.getFuturePositionTileX() != Util.NO_CLICK_OR_PRESS_KEY
						&& transform.getFuturePositionTileY() != Util.NO_CLICK_OR_PRESS_KEY) {
					pathFinderStartX = transform.getFuturePositionTileX();
					pathFinderStartY = transform.getFuturePositionTileY();
				} else {
					pathFinderStartX = transform.getTileX();
					pathFinderStartY = transform.getTileY();
				}

				caminho = pathFind.updatePath(pathFinderStartX,
						pathFinderStartY, mouseClickX / Util.TILE_SIZE,
						mouseClickY / Util.TILE_SIZE);

				if (caminho != null) {
					countSteps = 1;
				} else {
					countSteps = Util.NO_CLICK_OR_PRESS_KEY;
				}
			} catch (SlickException e1) {
				e1.printStackTrace();
			}
		}

		if (caminho != null && countSteps != Util.NO_CLICK_OR_PRESS_KEY
				&& countSteps < caminho.getLength()
				&& transform.isMoving() == false) {

			position = directionPath(transform.getTileX(), transform.getTileY(),
					caminho.getStep(countSteps).getX(),
					caminho.getStep(countSteps).getY());

			if (position == null) { // Esta andando?
				transform.setMoving(false);
				countSteps = Util.NO_CLICK_OR_PRESS_KEY;
				caminho = null;
				transform.setFuturePositionTileX(Util.NO_CLICK_OR_PRESS_KEY);
				transform.setFuturePositionTileY(Util.NO_CLICK_OR_PRESS_KEY);
			} else {
				int sendPosition = 0;
				
				switch (position) {
				case FACE_DOWN:
					transform.setFuturePositionTileX(transform.getTileX());
					transform.setFuturePositionTileY(transform.getTileY() + 1);
					sendPosition=0;
					break;
				case FACE_LEFT:
					transform.setFuturePositionTileX(transform.getTileX() - 1);
					transform.setFuturePositionTileY(transform.getTileY());
					sendPosition=1;
					break;
				case FACE_RIGHT:
					transform.setFuturePositionTileX(transform.getTileX()+1);
					transform.setFuturePositionTileY(transform.getTileY());
					sendPosition=2;
					break;
				case FACE_UP:
					transform.setFuturePositionTileX(transform.getTileX());
					transform.setFuturePositionTileY(transform.getTileY() - 1);
					sendPosition=3;
					break;
				}

				transform.setPosition(position);
				if (transform.isMoving() == false) {
					transform.setMoving(true);
				}
				this.hostConnect.sendChannel("m/"+compPlayer.getLoginId()+"/"+compPlayer.getClasseId()+"/"+compPlayer.getName()+"/"+transform.getFuturePositionTileX()+"/"+transform.getFuturePositionTileY()+"/"+sendPosition+"/", Util.CHANNEL_MAP);
				countSteps += 1;
			}
		} else if (caminho != null && countSteps > caminho.getLength()) {
			countSteps = Util.NO_CLICK_OR_PRESS_KEY;
			caminho = null;
			position = null;
			transform.setFuturePositionTileX(Util.NO_CLICK_OR_PRESS_KEY);
			transform.setFuturePositionTileY(Util.NO_CLICK_OR_PRESS_KEY);
		}

		move(player, world.getDelta(), transform, velocity);
	}

	private float calcSpeed(int delta, float time, float pixels) {
		float returnValue = 0;

		// Regra de tr阺 simples, para saber quantos pixels vale esta
		// atualiza玢o
		returnValue = (pixels * delta) / time;

		return returnValue;
	}

	private void move(Entity player, int delta, Transform transform,
			Velocity velocity) {
		if (transform.isMoving()) {
			float speed = calcSpeed(delta, 1000f,
					(float) velocity.getVelocity());
			// speed = (int)speed;
			switch (transform.getPosition()) {
			case FACE_DOWN:
				transform.addY(speed);
				break;
			case FACE_LEFT:
				transform.addX(-speed);
				break;
			case FACE_RIGHT:
				transform.addX(speed);
				break;
			case FACE_UP:
				transform.addY(-speed);
				break;
			}

			//distanceTraveled += speed;
			transform.setDistanceTraveled(transform.getDistanceTraveled()+speed);

			if (Math.round(transform.getDistanceTraveled()) >= Util.TILE_SIZE) {
				if (transform.getFuturePositionTileX() != Util.NO_CLICK_OR_PRESS_KEY
						&& transform.getFuturePositionTileY() != Util.NO_CLICK_OR_PRESS_KEY) {
					transform.setLocation(transform.getFuturePositionTileX()
							* Util.TILE_SIZE, transform.getFuturePositionTileY()
							* Util.TILE_SIZE);
				}
				transform.setFuturePositionTileX(Util.NO_CLICK_OR_PRESS_KEY);
				transform.setFuturePositionTileY(Util.NO_CLICK_OR_PRESS_KEY);
				transform.setDistanceTraveled(0.0f);
				transform.setMoving(false);
			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
}
