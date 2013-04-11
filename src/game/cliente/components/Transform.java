package game.cliente.components;

import game.cliente.hero.CharacterPosition;
import game.cliente.utils.Util;

import com.artemis.Component;
import com.artemis.utils.Utils;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class Transform extends Component {

	private float rotation;
	
	private float x;
	private float y;
	
	//Guarda o status do personagem (Se movendo ou Parado)
	private boolean moving;

	// Guarda a direção que o personagem ira se mover
	private CharacterPosition position;

	// Guarda a distancia em pixels, que foi percorrido pelo personagem
	private float distanceTraveled;
	
	// Guarda a posição futura do personagem (Em Tile)
	private int futurePositionTileX = Util.NO_CLICK_OR_PRESS_KEY;
	private int futurePositionTileY = Util.NO_CLICK_OR_PRESS_KEY;

	public Transform() {
	}

	public Transform(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Transform(float x, float y, float rotation) {
		this(x, y);
		this.rotation = rotation;
	}

	public void addX(float x) {
		this.x += x;
	}

	public void addY(float y) {
		this.y += y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}


	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void addRotation(float angle) {
		rotation = (rotation + angle) % 360;
	}

	public float getRotationAsRadians() {
		return (float) Math.toRadians(rotation);
	}

	public float getDistanceTo(Transform transform) {
		return Utils.distance(transform.getX(), transform.getY(), x, y);
	}

	public int getTileX() {
		int tile = (int)(Math.round(x) / Util.TILE_SIZE);
		return tile;
	}

	public int getTileY() {
		int tile = (int)(Math.round(y) / Util.TILE_SIZE);
		return tile;
	}

	public CharacterPosition getPosition() {
		return position;
	}

	public void setPosition(CharacterPosition position) {
		this.position = position;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public float getDistanceTraveled() {
		return distanceTraveled;
	}

	public void setDistanceTraveled(float distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}

	public int getFuturePositionTileX() {
		return futurePositionTileX;
	}

	public void setFuturePositionTileX(int futurePositionTileX) {
		this.futurePositionTileX = futurePositionTileX;
	}

	public int getFuturePositionTileY() {
		return futurePositionTileY;
	}

	public void setFuturePositionTileY(int futurePositionTileY) {
		this.futurePositionTileY = futurePositionTileY;
	}
}
