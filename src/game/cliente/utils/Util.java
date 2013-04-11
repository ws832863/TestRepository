package game.cliente.utils;

import java.awt.Point;

/**
 * @author Michel Montenegro
 */
public class Util {

	// Layer do mapa
	public static final int TILED_LAYER_COLISAO = 0;
	public static final int TILED_LAYER_OCEANO = 1;
	public static final int TILED_LAYER_ILHA = 2;
	public static final int TILED_LAYER_OBSTACULOS = 3;
	public static final int TILED_LAYER_ENFEITES = 4;
	public static final int TILED_LAYER_CEU = 5;

	// Layer Objetos
	public static final int TILED_LAYER_OBJECT_EVENTOS = 0;
	public static final int TILED_LAYER_OBJECT_OBJETOS = 1;

	// Id do objeto Heroi no tiledLayerObjectObjetos
	public static int TILED_LAYER_OBJECT_OBJETOS_HEROI = 0;

	// Tamanho padr鉶 de todos os Tiles do mapa
	public static final int TILE_SIZE = 32;

	// Tamanho padr鉶 de todos os Sprites (Character)
	public static final int SPRITE_SIZE = 32;

	// Tamanho padr鉶 dos Sprites (Faces)
	public static final int SPRITE_FACE_SIZE = 96;

	// Canal responsavel em atualizar a movimenta玢o dos players
	public static String CHANNEL_MAP = null;

	// Velocidade do heroi
	public static final int SPEED_PLAYER = 32;
	
	// Velocidade dos outros Players da rede
	public static final int SPEED_PLAYER_NETWORK = 48;

	//
	public static String CHAT_HERO = null;

	// Id das Classes cadastras no Banco de Dados do Servidor
	// Usado para montar as anima珲es
	public static final int CHARACTER_WARRIOR_M = 1;
	public static final int CHARACTER_WARRIOR_f = 2;
	public static final int CHARACTER_MONJE_M = 3;
	public static final int CHARACTER_MONJE_F = 4;
	public static final int CHARACTER_MAGE_F = 5;
	public static final int CHARACTER_ASSASSIN_F = 6;
	public static final int CHARACTER_ASSASSIN_M = 7;
	public static final int CHARACTER_MAGE_M = 8;

	// Configura玢o inicial do jogo
	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 600;
	public static final int TARGET_FRAME_RATE = 300;
	public static final boolean DEBUG = true;
	public static final boolean FULLSCREEN = false;
	public static final boolean VSYNC = true;

	// Altura e largura do mapa corrente em Tiles, �atualizado a cada mudan鏰
	// de mapa
	public static int WIDTH_MAP_IN_TILES = 0;
	public static int HEIGHT_MAP_IN_TILES = 0;

	// Informa ao RenderSystem a posi玢o que a Camera tem do player
	// Evitando dessincronismo entre a Camera e a renderiza玢o do player
	public static Point CAMERA_POSITION_XY = new Point(0, 0);
	
	//Guarda os tipos de Terrenos em um determinado local do mapa.
	public static int TERRAIN_GROUND = 0;
	public static int TERRAIN_WATER = 1;
	public static int TERRAIN_MONTAIN = 2;
	public static int TERRAIN_WALL = 3;
	public static int TERRAIN_MOB_SPAWN = 4;
	
	//Guarda o tipo de Objeto que esta no terreno
	public static int TERRAIN_OBJECT_MOB = 4;
	public static int TERRAIN_OBJECT_NPC = 5;
	public static int TERRAIN_OBJECT_PLAYER = 6;
	public static int TERRAIN_OBJECT_PLAYER_NETWORK = 7;
	
	//-1 = Valor para indicar que n鉶 existe click para ser checado
	// MOUSE_CLICK_POSITION_XY for diferente de -1, ele guarda a 
	//posi玢o corrente de onde houve o click do mouse
	public static Point MOUSE_CLICK_POSITION_XY = new Point(-1, -1);
	
	//Guarda todos os objetos fixos do mapa (Obstaculos ou ch鉶 livre)
	public static int[][] OBJECTS_OF_WORLD;
	
	//Indica que n鉶 houve click nem precionar de mouse
	public static int NO_CLICK_OR_PRESS_KEY = -1;
	
	//Tem a informa玢o do bot鉶 do mouse
	public static final int MOUSE_BUTTON_LEFT =0;
	public static final int MOUSE_BUTTON_RIGHT =1;
	
	public static final float SPEED_ANIMATION_PLAYER =1.0f;
}
