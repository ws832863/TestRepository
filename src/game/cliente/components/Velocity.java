package game.cliente.components;

import com.artemis.Component;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class Velocity extends Component {
	private int velocity;
	private float angle;

	public Velocity() {
	}

	public Velocity(int vector) {
		this.velocity = vector;
	}

	public Velocity(int velocity, float angle) {
		this.velocity = velocity;
		this.angle = angle;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAngle() {
		return angle;
	}

	public void addAngle(float a) {
		angle = (angle + a) % 360;
	}

	public float getAngleAsRadians() {
		return (float) Math.toRadians(angle);
	}

}
