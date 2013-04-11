package game.cliente.utils;

import java.io.FileWriter;
import java.io.IOException;

import org.newdawn.slick.tiled.TiledMap;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class CreateGeoMapForServer {

	private TiledMap tmap;
	public CreateGeoMapForServer() {
		super();
	}

	/**
	 * Salva o conteúdo de uma variável em um arquivo
	 * 
	 * @param arquivo
	 * @param conteudo
	 * @param adicionar
	 *            se true adicionar no final do arquivo
	 * @throws IOException
	 */
	public static void salvar(String arquivo, String conteudo, boolean adicionar)
			throws IOException {

		FileWriter fw = new FileWriter(arquivo, adicionar);

		fw.write(conteudo);
		fw.close();
	}

	public void createWall(String patchFile, String nameMap) {
		String texto = "";
		this.tmap = ResourceManager.getMap(nameMap);

		try {
			salvar(patchFile, texto, false);
			texto = " INSERT INTO Map (name, widthInTiles, heightInTiles, sizeTile) VALUES ('"+nameMap+"', "+this.tmap.getHeight()+", "+this.tmap.getWidth()+", "+this.tmap.getTileWidth()+"); \n";
			salvar(patchFile, texto, true);

			texto = " DELETE FROM GeoMap; \n";
			salvar(patchFile, texto, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int x = 0; x < this.tmap.getWidth(); x++) {
			for (int y = 0; y < this.tmap.getHeight(); y++) {
				int tileID = this.tmap.getTileId(x, y, 0); // 1° Layer= colisao
				if (tileID == 1) {// É o 1° Tile(TileA4.png = "X")
					try {
						texto = " INSERT INTO GeoMap (Map_id, tileX, tileY, TerrainType) VALUES ( 1, " + x + ", " + y + ", "+ Util.TERRAIN_WATER + " ); \n";
						salvar(patchFile, texto, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (tileID == 2) {
					try {
						texto = " INSERT INTO GeoMap (Map_id, tileX, tileY, TerrainType) VALUES ( 1, " + x + ", " + y + ", "+ Util.TERRAIN_MONTAIN + " ); \n";
						salvar(patchFile, texto, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (tileID == 3) {
					try {
						texto = " INSERT INTO GeoMap (Map_id, tileX, tileY, TerrainType) VALUES ( 1, " + x + ", " + y + ", "+ Util.TERRAIN_WALL + " ); \n";
						salvar(patchFile, texto, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (tileID == 5) {
					try {
						texto = " INSERT INTO GeoMap (Map_id, tileX, tileY, TerrainType) VALUES ( 1, " + x + ", " + y + ", "+ Util.TERRAIN_MOB_SPAWN + " ); \n";
						salvar(patchFile, texto, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (tileID == 8) {
					try {
						texto = " INSERT INTO GeoMap (Map_id, tileX, tileY, TerrainType) VALUES ( 1, " + x + ", " + y + ", "+ Util.TERRAIN_OBJECT_NPC + " ); \n";
						salvar(patchFile, texto, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {
						texto = " INSERT INTO GeoMap (Map_id, tileX, tileY, TerrainType) VALUES ( 1, " + x + ", " + y + ", "+ Util.TERRAIN_GROUND + " ); \n";
						salvar(patchFile, texto, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
					
			}
		}
	}

}
