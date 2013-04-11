package game.cliente.systems;

import game.cliente.components.Player;
import game.cliente.components.Transform;
import game.cliente.utils.Util;

import org.newdawn.slick.GameContainer;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class CameraPlayerSystem extends EntityProcessingSystem {
	private ComponentMapper<Transform> transformMapper;
	private GameContainer container;
	private float offsetX;
	private float offsetY;
	private BoundaryPlayerSystem boundarySystem; // boundary = fronteira

	public CameraPlayerSystem(GameContainer container) {
		super(Player.class);
		this.container = container;
	}

	@Override
	public void initialize() {
		transformMapper = new ComponentMapper<Transform>(Transform.class,
				world.getEntityManager());
		boundarySystem = world.getSystemManager().getSystem(
				BoundaryPlayerSystem.class);
	}

	@Override
	protected void process(Entity player) {
			Transform transform = transformMapper.get(player);
			Util.CAMERA_POSITION_XY.setLocation(transform.getX(),
					transform.getY());
			offsetX = transform.getX() - container.getWidth() / 2;
			offsetY = transform.getY() - container.getHeight() / 2;

			if (offsetX < boundarySystem.getBoundsStartX()) {
				offsetX = boundarySystem.getBoundsStartX();
			} else if (offsetX > boundarySystem.getBoundsEndX()
					- container.getWidth()) {
				offsetX = boundarySystem.getBoundsEndX() - container.getWidth();
			}

			if (offsetY < boundarySystem.getBoundsStartY()) {
				offsetY = boundarySystem.getBoundsStartY();
			} else if (offsetY > boundarySystem.getBoundsEndY()
					- container.getHeight()) {
				offsetY = boundarySystem.getBoundsEndY()
						- container.getHeight();
			}

			container.getInput().setOffset(-getOffsetX(), -getOffsetY());
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	public float getOffsetX() {
		return -offsetX;
	}

	public float getOffsetY() {
		return -offsetY;
	}

}
