package game.cliente.systems;

import game.cliente.components.Player;
import game.cliente.components.Transform;
import game.cliente.core.ManagerMap;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class BoundaryPlayerSystem extends EntityProcessingSystem {

	private ComponentMapper<Transform> transformMapper;
	private int boundsStartX;
	private int boundsStartY;
	private int boundsEndX;
	private int boundsEndY;
	private ManagerMap managerMap;

	public BoundaryPlayerSystem(ManagerMap managerMap) {
		super(Player.class);
		this.managerMap = managerMap;
		this.boundsStartX = 0;
		this.boundsStartY = 0;
		this.boundsEndX = 0;
		this.boundsEndY = 0;
	}

	@Override
	public void initialize() {
		transformMapper = new ComponentMapper<Transform>(Transform.class,
				world.getEntityManager());
		this.boundsStartX = 0;
		this.boundsStartY = 0;
		this.boundsEndX = this.managerMap.getMapWidthInPixel() - 32; // 32 e o
																		// tamanho
																		// do
																		// sprite,
																		// para
																		// ele
																		// não
																		// sair
																		// 32
																		// pixels
																		// fora
																		// da
																		// tela
		this.boundsEndY = this.managerMap.getMapHeightInPixel() - 32;
	}

	@Override
	protected void process(Entity player) {
		Transform transform = transformMapper.get(player);
		if (transform.getX() < boundsStartX) {
			transform.setLocation(boundsStartX, transform.getY());
		} else if (transform.getX() + 32 > boundsEndX) {
			transform.setLocation(boundsEndX - 32, transform.getY());
		}

		if (transform.getY() < boundsStartY) {
			transform.setLocation(transform.getX(), boundsStartY);
		} else if (transform.getY() + 32 > boundsEndY) {
			transform.setLocation(transform.getX(), boundsEndY - 32);
		}

	}

	public int getBoundsEndX() {
		return boundsEndX;
	}

	public int getBoundsEndY() {
		return boundsEndY;
	}

	public int getBoundsStartX() {
		return boundsStartX;
	}

	public int getBoundsStartY() {
		return boundsStartY;
	}

	public int getBoundaryWidth() {
		return boundsEndX - boundsStartX;
	}

	public int getBoundaryHeight() {
		return boundsEndY - boundsStartY;
	}

}
