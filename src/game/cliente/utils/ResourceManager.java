package game.cliente.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.tiled.TiledMap;

/**
 * This class provide access to all resources contents in a single jar.
 * 
 * Use the init() method with the jar path location to initialize the resources,
 * then the loadNextResource() and isLoadComplete() to load data progressively.
 * 
 * After loading you can use data with getters method.
 * 
 * Example: SpriteSheet sheet = Resources.getSprite("mySpriteSheet");
 * 
 * BE CAREFUL !
 * 
 * The jar must contain the following directory structure:
 * 
 * Width and height of sprite sheet must be specified in the file name like
 * this:
 * 
 * width_height_name.png
 * 
 * Example: 40_40_sprites.png
 * 
 * /resources /fonts /images /maps /musics /sounds /sprites /systems /faces
 * 
 * @author Vin789
 * @Edited Michel Montenegro
 * 
 */
public class ResourceManager {

	private static final String TILESET_LOCATION = "resources/maps";
	private static final boolean FONT_WITH_CACHE = false;

	private static HashMap<String, AngelCodeFont> fonts = new HashMap<String, AngelCodeFont>();
	private static HashMap<String, Image> images = new HashMap<String, Image>();
	private static HashMap<String, TiledMap> maps = new HashMap<String, TiledMap>();
	private static HashMap<String, Music> musics = new HashMap<String, Music>();
	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	private static HashMap<String, SpriteSheet> sprites = new HashMap<String, SpriteSheet>();
	private static HashMap<String, String> systemsAndEmitters = new HashMap<String, String>();
	private static HashMap<String, Image> faces = new HashMap<String, Image>();
	private static HashMap<String, SpriteSheet> characters = new HashMap<String, SpriteSheet>();
	private static HashMap<String, Image> battlers = new HashMap<String, Image>();
	private static HashMap<String, Image> parallaxes = new HashMap<String, Image>();
	private static HashMap<String, Image> battlebacks = new HashMap<String, Image>();
	private static HashMap<String, Image> transitions = new HashMap<String, Image>();
	private static HashMap<String, Image> fogs = new HashMap<String, Image>();

	private static LoadingList list;
	private static int advancement;

	/**
	 * Initialize the content of the resources list.
	 * 
	 * @param jarLocation
	 *            The location of the jar that contain resources.
	 * @throws IOException
	 *             If the jar location is false.
	 * @throws SlickException
	 *             If a resource can't be loaded from the jar.
	 */
	public static void init(String jarLocation) throws IOException,
			SlickException {
		list = LoadingList.get();
		JarFile jarFile = new JarFile(jarLocation);
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry je = e.nextElement();
			String path = je.getName();
			if (path.startsWith("resources") && path.length() > 10) {
				String dataInfo = path.split("/")[1];
				if (!path.endsWith("/")) {
					String name = path.split("/")[2].split("[.]")[0];
					if (dataInfo.equals("images")) {
						list.add(new DeferredImage(name, path));
					}
					if (dataInfo.equals("faces")) {
						list.add(new Faces(name, path));
					} else if (dataInfo.equals("battlers")) {
						list.add(new Battlers(name, path));
					} else if (dataInfo.equals("parallaxes")) {
						list.add(new Parallaxes(name, path));
					}
					if (dataInfo.equals("battlebacks")) {
						list.add(new Battlebacks(name, path));
					} else if (dataInfo.equals("transitions")) {
						list.add(new Transitions(name, path));
					} else if (dataInfo.equals("fogs")) {
						list.add(new Fogs(name, path));
					} else {
						if (dataInfo.equals("sprites")) {
							String[] info = name.split("_");
							if (info.length > 1) {
								int tw = Integer.parseInt(info[0]);
								int th = Integer.parseInt(info[1]);
								if (info.length == 4) {
									list.add(new DeferredSprite(info[2] + "_"
											+ info[3], path, tw, th));
								} else
									list.add(new DeferredSprite(info[2], path,
											tw, th));
							}
						}
						if (dataInfo.equals("characters")) {
							String[] info = name.split("_");
							if (info.length > 1) {
								int tw = Integer.parseInt(info[0]);
								int th = Integer.parseInt(info[1]);
								if (info.length == 4) {
									list.add(new DeferredCharacter(info[2]
											+ "_" + info[3], path, tw, th));
								} else
									list.add(new DeferredCharacter(info[2],
											path, tw, th));
							}
						} else {
							if (dataInfo.equals("sounds")) {
								list.add(new DeferredSound(name, path));
							} else {
								if (dataInfo.equals("musics")) {
									list.add(new DeferredMusic(name, path));
								} else {
									if (dataInfo.equals("systems")) {
										systemsAndEmitters.put(name, path);
									} else {
										if (dataInfo.equals("maps")
												&& path.endsWith(".tmx")) {
											list.add(new DeferredMap(name,
													path, TILESET_LOCATION));
										} else {
											if (dataInfo.equals("fonts")
													&& !path.endsWith(".png")) {
												list.add(new DeferredFont(name,
														path.split("[.]")[0]));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Load the next resource of the list.
	 */
	public static void loadNextResource() {
		if (LoadingList.get().getRemainingResources() > 0) {
			DeferredResource nextResource = LoadingList.get().getNext();
			try {
				nextResource.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (LoadingList.get().getTotalResources() > 0)
			advancement = (100 * (LoadingList.get().getTotalResources() - LoadingList
					.get().getRemainingResources()))
					/ LoadingList.get().getTotalResources();
	}

	/**
	 * Check if there is no more remaining resource to load.
	 * 
	 * @return true if there is no more resource to load, false otherwise.
	 */
	public static boolean isLoadComplete() {
		return LoadingList.get().getRemainingResources() == 0;
	}

	/**
	 * The advancement of the resource list loading in percentage.
	 * 
	 * @return the advancement in percentage ( 0 - 100 ).
	 */
	public static int getAdvancement() {
		return advancement;
	}

	/*
	 * Delegate Getters/Setters to access data by name.
	 */

	/**
	 * Get a font from the resources.
	 * 
	 * @param name
	 *            the name of the font.
	 * @return the requested font or null if the resource was not found.
	 */
	public static AngelCodeFont getFont(String name) {
		return fonts.get(name);
	}

	/**
	 * Get an image from the resources.
	 * 
	 * @param name
	 *            the name of the image.
	 * @return the requested image or null if the resource was not found.
	 */
	public static Image getImage(String name) {
		return images.get(name);
	}

	/**
	 * Get a map from the resources.
	 * 
	 * @param name
	 *            the name of the map.
	 * @return the requested map or null if the resource was not found.
	 */
	public static TiledMap getMap(String name) {
		return maps.get(name);
	}

	/**
	 * Get all the map loaded by the ResourceManager.
	 * 
	 * @return a hashmap with all loaded maps.
	 */
	public static HashMap<String, TiledMap> getAllMaps() {
		return maps;
	}

	/**
	 * Get a music from the resources.
	 * 
	 * @param name
	 *            the name of the music.
	 * @return the requested music or null if the resource was not found.
	 */
	public static Music getMusic(String name) {
		return musics.get(name);
	}

	/**
	 * Get a sound from the resources.
	 * 
	 * @param name
	 *            the name of the sound.
	 * @return the requested sound or null if the resource was not found.
	 */
	public static Sound getSound(String name) {
		return sounds.get(name);
	}

	/**
	 * Get a sprite from the resources.
	 * 
	 * @param name
	 *            the name of the sprite.
	 * @return the requested sprite or null if the resource was not found.
	 */
	public static SpriteSheet getSpriteSheet(String name) {
		return sprites.get(name);
	}

	/**
	 * Get a particle system from the resources.
	 * 
	 * @param name
	 *            the name of the particle system.
	 * @return the requested particle system or null if the resource was not
	 *         found.
	 * @throws IOException
	 *             If the system can't be loaded.
	 */
	public static ParticleSystem getSystem(String name) throws IOException {
		return ParticleIO.loadConfiguredSystem(systemsAndEmitters.get(name));
	}

	/**
	 * Get a particle emitter from the resources.
	 * 
	 * @param name
	 *            the name of the particle emitter.
	 * @return the requested particle emitter or null if the resource was not
	 *         found.
	 * @throws IOException
	 *             If the emitter can't be loaded.
	 */
	public static ConfigurableEmitter getEmitter(String name)
			throws IOException {
		return ParticleIO.loadEmitter(systemsAndEmitters.get(name));
	}

	/*
	 * Useful classes to load deferred resources.
	 */

	private static class DeferredFont implements DeferredResource {

		private String name;
		private String path;

		public DeferredFont(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred font";
		}

		public void load() throws IOException {
			try {
				AngelCodeFont font = new AngelCodeFont(name, Thread
						.currentThread().getContextClassLoader()
						.getResourceAsStream(path + ".fnt"), Thread
						.currentThread().getContextClassLoader()
						.getResourceAsStream(path + ".png"), FONT_WITH_CACHE);
				ResourceManager.fonts.put(name, font);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	private static class DeferredImage implements DeferredResource {

		private String name;
		private String path;

		public DeferredImage(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred image";
		}

		public void load() throws IOException {
			try {
				Image image = new Image(path);
				ResourceManager.images.put(name, image);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	private static class DeferredMap implements DeferredResource {

		private String name;
		private String path;
		private String tilePath;

		public DeferredMap(String name, String path, String tilePath) {
			this.name = name;
			this.path = path;
			this.tilePath = tilePath;
		}

		public String getDescription() {
			return "Deferred Map";
		}

		public void load() throws IOException {
			try {
				TiledMap map = new TiledMap(Thread.currentThread()
						.getContextClassLoader().getResourceAsStream(path),
						tilePath);
				ResourceManager.maps.put(name, map);
			} catch (SlickException e1) {
				e1.printStackTrace();
			}
		}

	}

	private static class DeferredSprite implements DeferredResource {

		private String name;
		private String path;
		private int tileWidth;
		private int tileHeight;

		public DeferredSprite(String name, String path, int tileWidth,
				int tileHeight) {
			this.name = name;
			this.path = path;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
		}

		public String getDescription() {
			return "Deferred sprite";
		}

		public void load() throws IOException {
			try {
				ResourceManager.sprites.put(name, new SpriteSheet(name, Thread
						.currentThread().getContextClassLoader()
						.getResourceAsStream(path), tileWidth, tileHeight));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	private static class DeferredSound implements DeferredResource {

		private String name;
		private String path;

		public DeferredSound(String name, String path) {
			super();
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred sound";
		}

		public void load() throws IOException {
			try {
				ResourceManager.sounds.put(name, new Sound(path));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	private static class DeferredMusic implements DeferredResource {

		private String name;
		private String path;

		public DeferredMusic(String name, String path) {
			super();
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred music";
		}

		public void load() throws IOException {
			try {
				ResourceManager.musics.put(name, new Music(path));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	private static class Faces implements DeferredResource {

		private String name;
		private String path;

		public Faces(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred image";
		}

		public void load() throws IOException {
			try {
				Image image = new Image(path);
				ResourceManager.faces.put(name, image);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	public static Image getFace(String name) {
		return faces.get(name);
	}

	private static class DeferredCharacter implements DeferredResource {

		private String name;
		private String path;
		private int tileWidth;
		private int tileHeight;

		public DeferredCharacter(String name, String path, int tileWidth,
				int tileHeight) {
			this.name = name;
			this.path = path;
			this.tileWidth = tileWidth;
			this.tileHeight = tileHeight;
		}

		public String getDescription() {
			return "Deferred sprite";
		}

		public void load() throws IOException {
			try {
				ResourceManager.characters.put(name, new SpriteSheet(name,
						Thread.currentThread().getContextClassLoader()
								.getResourceAsStream(path), tileWidth,
						tileHeight));
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
	}

	public static SpriteSheet getCharacter(String name) {
		return characters.get(name);
	}

	private static class Battlers implements DeferredResource {

		private String name;
		private String path;

		public Battlers(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred image";
		}

		public void load() throws IOException {
			try {
				Image image = new Image(path);
				ResourceManager.battlers.put(name, image);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	public static Image getBattler(String name) {
		return battlers.get(name);
	}

	private static class Parallaxes implements DeferredResource {

		private String name;
		private String path;

		public Parallaxes(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred image";
		}

		public void load() throws IOException {
			try {
				Image image = new Image(path);
				ResourceManager.parallaxes.put(name, image);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	public static Image getParallaxe(String name) {
		return parallaxes.get(name);
	}

	private static class Battlebacks implements DeferredResource {

		private String name;
		private String path;

		public Battlebacks(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred image";
		}

		public void load() throws IOException {
			try {
				Image image = new Image(path);
				ResourceManager.battlebacks.put(name, image);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	public static Image getBattleback(String name) {
		return battlebacks.get(name);
	}

	private static class Transitions implements DeferredResource {

		private String name;
		private String path;

		public Transitions(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred image";
		}

		public void load() throws IOException {
			try {
				Image image = new Image(path);
				ResourceManager.transitions.put(name, image);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	public static Image getTransition(String name) {
		return transitions.get(name);
	}

	private static class Fogs implements DeferredResource {

		private String name;
		private String path;

		public Fogs(String name, String path) {
			this.name = name;
			this.path = path;
		}

		public String getDescription() {
			return "Deferred image";
		}

		public void load() throws IOException {
			try {
				Image image = new Image(path);
				ResourceManager.fogs.put(name, image);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	public static Image getFog(String name) {
		return fogs.get(name);
	}

}
