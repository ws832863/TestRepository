package game.cliente.core;

import game.cliente.components.SpatialForm;
import game.cliente.components.Transform;
import game.cliente.connections.HostConnect;
import game.cliente.hero.CharacterPosition;
import game.cliente.spatials.PlayerNetworkSpatial;
import game.cliente.spatials.PlayerSpatial;
import game.cliente.spatials.Spatial;
import game.cliente.utils.ResourceManager;
import game.cliente.utils.Util;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;

/**
 * 
 * @author Michel Montenegro
 * @edited Pedro Silva Moreira - PeJuGe
 * 
 */
public class ManagerMap {

	private World world;
	private TiledMap tmap;

	private int mapWidthInPixel = 0;
	private int mapHeightInPixel = 0;

	private Image minimapBg;
	private boolean atualizandoMapa;
	private ComponentMapper<SpatialForm> spatialFormMapper;
	private Bag<Spatial> spatials;
	private HostConnect hostConnect;

	public ManagerMap(GameContainer container, World world,
			HostConnect hostConnect) {
		this.world = world;
		this.hostConnect = hostConnect;
		this.atualizandoMapa = false;

		this.spatialFormMapper = new ComponentMapper<SpatialForm>(
				SpatialForm.class, world.getEntityManager());
		this.spatials = new Bag<Spatial>();
	}

	public void getUpdateMap(String nomeMap) {
		this.minimapBg = ResourceManager.getImage("minimapBg");
		this.tmap = ResourceManager.getMap(nomeMap);
		this.mapWidthInPixel = tmap.getWidth() * tmap.getTileWidth(); // Tiles
																		// s鉶
																		// 32
																		// por
																		// 32
																		// pixels
		this.mapHeightInPixel = tmap.getHeight() * tmap.getTileHeight();
		Util.WIDTH_MAP_IN_TILES = tmap.getWidth();
		Util.HEIGHT_MAP_IN_TILES = tmap.getHeight();
		cleanEntitys();
		// Inicializando entidades
		if (hostConnect.getPlayer().getClasseId() != 0) {
			
			initPlayerHero();
		}else{
			
			System.out.println("Can not initial player");
		}

		initMapWall();
	}

	public void cleanEntitys() {
		// Remove a entidade heroi
		Entity hero = world.getTagManager().getEntity("Hero");
		if (hero != null) {
			removeSpatial(hero);
			world.deleteEntity(hero);
			hero.delete();
			// Ap髎 o uso do "world.loopStart();" tudo que foi marcado pra remo玢o
			// �
			// efetivamente removido.
			world.loopStart();
		}
	}

	private void initPlayerHero() {
		// getObjectProperty(int groupID, int objectID, java.lang.String
		// propertyName, java.lang.String def)
		System.out.println(hostConnect.getPlayer().toString());
		if (hostConnect.getPlayer().getClasseId() == 0) {
			while (hostConnect.getPlayer().getClasseId() == 0) {
				try {
					System.out.println("waiting for player information");
					this.wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		int x = this.hostConnect.getPlayer().getStartTileHeroPosX()
				* Util.TILE_SIZE;
		int y = this.hostConnect.getPlayer().getStartTileHeroPosY()
				* Util.TILE_SIZE;
		int position = this.hostConnect.getPlayer().getPosition();

		Transform transform = new Transform(x, y);
		if (position == 0) {
			transform.setPosition(CharacterPosition.FACE_DOWN);
		} else if (position == 1) {
			transform.setPosition(CharacterPosition.FACE_LEFT);
		} else if (position == 2) {
			transform.setPosition(CharacterPosition.FACE_RIGHT);
		} else if (position == 3) {
			transform.setPosition(CharacterPosition.FACE_UP);
		}
		Entity hero = EntityFactory.createPlayer(this.world, transform,
				Util.SPEED_PLAYER, new Color(51, 204, 69),
				this.hostConnect.getPlayer());
		hero.setTag("Hero");
		hero.refresh();
		addSpatial(hero, this.hostConnect.getPlayer().getClasseId());
	}

	private void cleanMapWall() {
		for (int x = 0; x < Util.OBJECTS_OF_WORLD.length; x++) {
			for (int y = 0; y < Util.OBJECTS_OF_WORLD[0].length; y++) {
				Util.OBJECTS_OF_WORLD[x][y] = Util.TERRAIN_GROUND;
			}
		}
	}

	private void initMapWall() {
		Util.OBJECTS_OF_WORLD = new int[this.tmap.getWidth()][this.tmap
				.getHeight()];

		cleanMapWall();

		for (int x = 0; x < this.tmap.getWidth(); x++) {
			for (int y = 0; y < this.tmap.getHeight(); y++) {
				int tileID = this.tmap.getTileId(x, y, 0); // 1�Layer= colisao
				if (tileID == 1)// �o 1�Tile(TileA4.png = "X")
				{
					Util.OBJECTS_OF_WORLD[x][y] = Util.TERRAIN_WATER;
				} else if (tileID == 2) {
					Util.OBJECTS_OF_WORLD[x][y] = Util.TERRAIN_MONTAIN;
				} else if (tileID == 3) {
					Util.OBJECTS_OF_WORLD[x][y] = Util.TERRAIN_WALL;
				} else {
					Util.OBJECTS_OF_WORLD[x][y] = Util.TERRAIN_GROUND;
				}
			}
		}

	}

	// Retorna true para colisao
	public static boolean entityWallCollisionWith(int posHeroTileX,
			int posHeroTileY, CharacterPosition position) {
		int posHeroY = posHeroTileY;
		int posHeroX = posHeroTileX;
		switch (position) {
		case FACE_DOWN:
			posHeroY++;
			break;
		case FACE_UP:
			posHeroY--;
			break;
		case FACE_LEFT:
			posHeroX--;
			break;
		case FACE_RIGHT:
			posHeroX++;
			break;
		default:
			System.out
					.println("Invalid CharacterPosition at entityWallCollisionWith - ManagerMap.java");
			return true;
		}

		if (!(posHeroX > 0 && posHeroX + 1 < Util.WIDTH_MAP_IN_TILES
				&& posHeroY > 0 && posHeroY + 1 < Util.HEIGHT_MAP_IN_TILES)) {
			return true;
		} else if (Util.OBJECTS_OF_WORLD[posHeroX][posHeroY] == Util.TERRAIN_GROUND) {
			return false;
		} else if (Util.OBJECTS_OF_WORLD[posHeroX][posHeroY] == Util.TERRAIN_OBJECT_NPC) {
			return true;
		} else if (Util.OBJECTS_OF_WORLD[posHeroX][posHeroY] == Util.TERRAIN_WALL) {
			return true;
		} else if (Util.OBJECTS_OF_WORLD[posHeroX][posHeroY] == Util.TERRAIN_WATER) {
			return true;
		} else if (Util.OBJECTS_OF_WORLD[posHeroX][posHeroY] == Util.TERRAIN_MONTAIN) {
			return true;
		} else if (Util.OBJECTS_OF_WORLD[posHeroX][posHeroY] == Util.TERRAIN_MOB_SPAWN) {
			return false;
		} else if (Util.OBJECTS_OF_WORLD[posHeroX][posHeroY] == Util.TERRAIN_OBJECT_PLAYER_NETWORK) {
			return false;
		} else {
			return true;
		}
	}

	public void removeSpatial(Entity e) {
		spatials.set(e.getId(), null);
	}

	public void addSpatial(Entity e, int classe) {
		Spatial spatial = createSpatial(e, classe);
		if (spatial != null) {
			spatial.initalize();
			spatials.set(e.getId(), spatial);
		}
	}

	private Spatial createSpatial(Entity e, int classe) {
		SpatialForm spatialForm = spatialFormMapper.get(e);
		String spatialFormFile = spatialForm.getSpatialFormFile();
		if ("Spatial: Hero".equalsIgnoreCase(spatialFormFile)) {
			return new PlayerSpatial(world, e);
		}
		if ("Spatial: PlayerNetWork".equalsIgnoreCase(spatialFormFile)) {
			return new PlayerNetworkSpatial(world, e);
		}
		return null;
	}

	public boolean isAtualizandoMapa() {
		return atualizandoMapa;
	}

	public void setAtualizandoMapa(boolean atualizandoMapa) {
		this.atualizandoMapa = atualizandoMapa;
	}

	public TiledMap getTmap() {
		return tmap;
	}

	public void setTmap(TiledMap tmap) {
		this.tmap = tmap;
	}

	public Image getMinimapBg() {
		return minimapBg;
	}

	public void setMinimapBg(Image minimapBg) {
		this.minimapBg = minimapBg;
	}

	public Bag<Spatial> getSpatials() {
		return spatials;
	}

	public void setSpatials(Bag<Spatial> spatials) {
		this.spatials = spatials;
	}

	public int getMapWidthInPixel() {
		return mapWidthInPixel;
	}

	public void setMapWidthInPixel(int mapWidthInPixel) {
		this.mapWidthInPixel = mapWidthInPixel;
	}

	public int getMapHeightInPixel() {
		return mapHeightInPixel;
	}

	public void setMapHeightInPixel(int mapHeightInPixel) {
		this.mapHeightInPixel = mapHeightInPixel;
	}

}
