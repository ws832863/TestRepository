package game.cliente.systems;

import game.cliente.components.Player;
import game.cliente.core.ManagerMap;
import game.cliente.utils.Util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class RenderMapCloudSystem extends EntityProcessingSystem {
	
	private GameContainer container;
	
	private Graphics graphics;
	private CameraPlayerSystem cameraSystem;
	private ManagerMap managerMap;

	public RenderMapCloudSystem(GameContainer container, ManagerMap managerMap) {
		super(Player.class);
		this.container = container;
		this.graphics = container.getGraphics();
		this.managerMap = managerMap;
	}

	@Override
	public void initialize() {
		cameraSystem = world.getSystemManager().getSystem(CameraPlayerSystem.class);
	}

	@Override
	protected void process(Entity player) {
		
		cameraSystem = world.getSystemManager().getSystem(CameraPlayerSystem.class);

		int cameraOffSetPixelX = (int) -cameraSystem.getOffsetX();
		int cameraOffSetPixelY = (int) -cameraSystem.getOffsetY();
		cameraOffSetPixelX = cameraOffSetPixelX - cameraOffSetPixelX
				% Util.TILE_SIZE;
		cameraOffSetPixelY = cameraOffSetPixelY - cameraOffSetPixelY
				% Util.TILE_SIZE;

		int cameraOffSetTileX = (cameraOffSetPixelX) / Util.TILE_SIZE;
		int cameraOffSetTileY = (cameraOffSetPixelY) / Util.TILE_SIZE;
		int visionX = (this.container.getWidth() / Util.TILE_SIZE) + 2;
		int visionY = (this.container.getHeight() / Util.TILE_SIZE) + 2;

		this.managerMap.getTmap().render(cameraOffSetPixelX,
				cameraOffSetPixelY, cameraOffSetTileX, cameraOffSetTileY,
				visionX, visionY, Util.TILED_LAYER_CEU, true);


	}

	@Override
	protected void begin() {
		graphics.translate(cameraSystem.getOffsetX(), cameraSystem.getOffsetY());
		super.begin();
	}

	@Override
	protected void end() {
		graphics.translate(-cameraSystem.getOffsetX(),
				-cameraSystem.getOffsetY());
		super.end();
	}

}
