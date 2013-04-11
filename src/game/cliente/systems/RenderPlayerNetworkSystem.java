package game.cliente.systems;

import game.cliente.components.PlayerNetwork;
import game.cliente.components.Transform;
import game.cliente.core.ManagerMap;
import game.cliente.spatials.Spatial;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class RenderPlayerNetworkSystem extends EntitySystem {

	private Graphics graphics;
	private ComponentMapper<Transform> transformMapper;
	private ManagerMap managerMap;
	private CameraPlayerSystem cameraSystem;	

	@SuppressWarnings("unchecked")
	public RenderPlayerNetworkSystem(GameContainer container, ManagerMap managerMap) {
		super(PlayerNetwork.class);
		this.graphics = container.getGraphics();
		this.managerMap = managerMap;
	}

	@Override
	public void initialize() {
		transformMapper = new ComponentMapper<Transform>(Transform.class,
				world.getEntityManager());
		cameraSystem = world.getSystemManager().getSystem(
				CameraPlayerSystem.class);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> playersNetwork) {
		if (playersNetwork!=null){
			for (int i = 0; i < playersNetwork.size(); i++) {
				Entity playerNetwork = playersNetwork.get(i);
				Transform transform = transformMapper.get(playerNetwork);
				Spatial spatial = this.managerMap.getSpatials().get(playerNetwork.getId());
				if (transform.getX() >= 0 && transform.getY() >= 0 && spatial != null) {
					spatial.render(this.graphics);
				}	
			}
		}
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

	@Override
	protected boolean checkProcessing() {
		return true;
	}

}
