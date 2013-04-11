package game.cliente.systems;

import game.cliente.components.PlayerNetwork;
import game.cliente.components.Transform;
import game.cliente.components.Velocity;
import game.cliente.utils.Util;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;

/**
 * @author Michel Montenegro - Criador do 1° sistema de movimento.
 */
public class MovementPlayerNetWorkSystem extends EntitySystem {
	private ComponentMapper<Transform> transformMapper;
	private ComponentMapper<Velocity> velocityMapper;

	@SuppressWarnings("unchecked")
	public MovementPlayerNetWorkSystem() {
		super(PlayerNetwork.class);
	}

	@Override
	public void initialize() {
		velocityMapper = new ComponentMapper<Velocity>(Velocity.class,
				world.getEntityManager());
		transformMapper = new ComponentMapper<Transform>(Transform.class,
				world.getEntityManager());
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> playersNetwork) {
		
		if (playersNetwork!=null){
			for (int i = 0; i < playersNetwork.size(); i++) {
				Entity playerNetwork = playersNetwork.get(i);
				Transform transform = transformMapper.get(playerNetwork);
				Velocity velocity = velocityMapper.get(playerNetwork);
				move(playerNetwork, world.getDelta(), transform, velocity);
			}
		}
	}

	private float calcSpeed(int delta, float time, float pixels) {
		float returnValue = 0;

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

			transform.setDistanceTraveled(transform.getDistanceTraveled() + speed);

			if (Math.round(transform.getDistanceTraveled()) >= Util.TILE_SIZE) {
				if ((transform.getFuturePositionTileX() != Util.NO_CLICK_OR_PRESS_KEY
						&& transform.getFuturePositionTileY() != Util.NO_CLICK_OR_PRESS_KEY)) {
					transform.setLocation(transform.getFuturePositionTileX()
							* Util.TILE_SIZE, transform.getFuturePositionTileY()
							* Util.TILE_SIZE);
				}
				transform.setMoving(false);
				transform.setFuturePositionTileX(Util.NO_CLICK_OR_PRESS_KEY);
				transform.setFuturePositionTileY(Util.NO_CLICK_OR_PRESS_KEY);
				transform.setDistanceTraveled(0.0f);

			}
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
}
