package game.cliente.spatials;

import game.cliente.components.PlayerNetwork;
import game.cliente.components.Transform;
import game.cliente.core.CharacterHero;
import game.cliente.utils.Util;

import java.awt.Font;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.artemis.Entity;
import com.artemis.World;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class PlayerNetworkSpatial extends Spatial{

	private Animation playerCorrente;
	private CharacterHero characterHero;	
	private String namePlayer;	
	private UnicodeFont font_large;
	private Transform transform;	
	private int type = Util.TERRAIN_OBJECT_PLAYER_NETWORK;	

	public PlayerNetworkSpatial(World world, Entity owner) {
		super(world, owner);
		this.characterHero = new CharacterHero(owner.getComponent(PlayerNetwork.class).getClasseId());
		this.playerCorrente = this.characterHero.getPlayerCorrente();
		this.namePlayer =  owner.getComponent(PlayerNetwork.class).getName();
		this.transform = owner.getComponent(Transform.class);
	}


	@Override
	public void initalize() {
		try {
			font_large = get_font("Verdana", 12);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private UnicodeFont get_font(String name, int size) throws SlickException {
		Font font = new Font(name, Font.PLAIN, size);
		UnicodeFont ufont = new UnicodeFont(font);
		ufont.getEffects().add(new ColorEffect(java.awt.Color.white));
		ufont.addAsciiGlyphs();
		ufont.loadGlyphs();

		return ufont;
	}
	
	public void render(Graphics g) {
		this.playerCorrente = characterHero.playerAnimationMove(transform.getPosition(),transform.isMoving());
		g.drawAnimation(this.playerCorrente, transform.getX(), transform.getY());
		font_large.drawString(transform.getX() - 8, transform.getY() - 16, this.namePlayer, Color.white);
		g.setColor(new Color(.3f, .4f, .5f, .6f));
		g.fillRect(transform.getX() - 4, transform.getY() + 36, 40, 5);
	}
	
	public int getType() {
		return type;
	}

}
