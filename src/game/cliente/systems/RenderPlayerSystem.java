package game.cliente.systems;

import game.cliente.components.Player;
import game.cliente.components.Transform;
import game.cliente.core.ManagerMap;
import game.cliente.spatials.Spatial;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class RenderPlayerSystem extends EntityProcessingSystem {

	@SuppressWarnings("unused")
	private GameContainer container;

	private Graphics graphics;
	private ComponentMapper<Transform> transformMapper;
	private CameraPlayerSystem cameraSystem;
	private ManagerMap managerMap;
	private UnicodeFont myFont;

	public RenderPlayerSystem(GameContainer container, ManagerMap managerMap) {
		super(Player.class);
		this.container = container;
		this.graphics = container.getGraphics();
		this.managerMap = managerMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		transformMapper = new ComponentMapper<Transform>(Transform.class,
				world.getEntityManager());
		cameraSystem = world.getSystemManager().getSystem(
				CameraPlayerSystem.class);
		Font awtFont = new Font("Arial", java.awt.Font.PLAIN, 14);
		myFont = new UnicodeFont(awtFont);
		myFont.addGlyphs("ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz1234567890");
		myFont.getEffects().add(new ColorEffect(java.awt.Color.white));

		try {
			myFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void process(Entity player) {
		Transform transform = transformMapper.get(player);
		Spatial spatial = this.managerMap.getSpatials().get(player.getId());
		if (transform.getX() >= 0 && transform.getY() >= 0 && spatial != null) {
			spatial.render(this.graphics);
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

}
