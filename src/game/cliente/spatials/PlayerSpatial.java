package game.cliente.spatials;

import game.cliente.components.Player;
import game.cliente.components.Transform;
import game.cliente.core.CharacterHero;
import game.cliente.systems.BoundaryPlayerSystem;
import game.cliente.utils.Util;

import java.awt.Font;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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
public class PlayerSpatial extends Spatial{

	private Animation playerCorrente;
	private CharacterHero characterHero;	
	private float posicalAtualX;
	private float posicalAtualY;	
	private float posAnteriorX;
	private float posAnteriorY;	
	private String namePlayer;
	private UnicodeFont font_large;
	private Transform transform;	
	private int type = Util.TERRAIN_OBJECT_PLAYER;
	
	//PlayerNetworkN�o tem estes quatro Atributos
	private BoundaryPlayerSystem boundarySystem;
	private boolean isNoUpdatePosition = false;
	private UnicodeFont font_small;
	private Image playerFace;	


	/*
	 * => Mapa do Sprite (24x12 imagens): -> x = Heroi 1, o = Heroi 2, * = Heroi
	 * 3, # = Heroi 4, xxxooo***### xxxooo***### xxxooo***### xxxooo***### 2�
	 * Linha xxxooo***### xxxooo***### xxxooo***### xxxooo***###
	 */

	public PlayerSpatial(World world, Entity owner) {
		super(world, owner);
		this.characterHero = new CharacterHero(owner.getComponent(Player.class).getClasseId());
		this.playerCorrente = this.characterHero.getPlayerCorrente();
		this.namePlayer = owner.getComponent(Player.class).getName();
		this.playerFace = characterHero.getPlayerFace();

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

	@Override
	public void initalize() {
		transform = owner.getComponent(Transform.class);
		try {
			font_large = get_font("Verdana", 12);
			font_small = get_font("Verdana", 10);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.isNoUpdatePosition = false;
		this.posicalAtualX = transform.getX();
		this.posicalAtualY = transform.getY();
		this.posAnteriorY = transform.getX();
		this.posAnteriorX = transform.getY();
	}

	public boolean isLimitScreenX() {
		boundarySystem = world.getSystemManager().getSystem(
				BoundaryPlayerSystem.class);
		if (transform.getX() + ((Util.FRAME_WIDTH / 2) - Util.SPEED_PLAYER) >= boundarySystem
				.getBoundsEndX()) {
			return true;
		} else if (transform.getX()
				- ((Util.FRAME_WIDTH / 2) - Util.SPEED_PLAYER) <= boundarySystem
				.getBoundsStartX()) {
			return true;
		}
		return false;
	}

	public boolean isLimitScreenY() {
		boundarySystem = world.getSystemManager().getSystem(
				BoundaryPlayerSystem.class);
		if (transform.getY() + ((Util.FRAME_HEIGHT / 2) - Util.SPEED_PLAYER) >= boundarySystem
				.getBoundsEndY()) {
			return true;
		} else if (transform.getY()
				- ((Util.FRAME_HEIGHT / 2) - Util.SPEED_PLAYER) <= boundarySystem
				.getBoundsStartY()) {
			return true;
		}
		return false;
	}

	public void setNoUpdatePosition(boolean isNoUpdatePosition) {
		this.isNoUpdatePosition = isNoUpdatePosition;
	}

	@Override
	public void render(Graphics g) {
		float posXPlayer;
		float posYPlayer;
		Player player = owner.getComponent(Player.class);
		// (curr*100)/max
		int porcentExpCurr = (player.getExpCurr() * 100)
				/ player.getExpMax();

		if (this.isNoUpdatePosition == false) {
			posXPlayer = transform.getX();
			posYPlayer = transform.getY();
		} else {
			posXPlayer = this.posicalAtualX;
			posYPlayer = this.posicalAtualY;
		}

		// int faceWidth = 96;
		int faceHeight = 96;
		// Checa se a camera parou nos limites do mapa
		if (isLimitScreenX() == false) {
			posAnteriorX = posXPlayer - 394;
		}
		if (isLimitScreenY() == false) {
			posAnteriorY = posYPlayer - 294;
		}
		// Retangulo que encobre a face
		g.setColor(Color.darkGray);
		g.fillRect(posAnteriorX - 3, posAnteriorY - 3, 102, 136);
		// Face
		playerFace.draw(posAnteriorX, posAnteriorY);
		// Textos da Exp
		g.setColor(Color.white);
		this.font_large.drawString(posAnteriorX, posAnteriorY + faceHeight + 3,
				"Xp:", Color.white);
		// Barra exp
		g.setColor(Color.lightGray);
		g.fillRect(posAnteriorX + 22, posAnteriorY + faceHeight + 6,
				(75 * porcentExpCurr) / 100, 12);
		// Valor em % da exp correntes
		g.setColor(Color.white);
		this.font_small.drawString(posAnteriorX + 45, posAnteriorY + faceHeight + 5,
				porcentExpCurr + "%", Color.white);

		g.setColor(Color.white);
		this.font_large.drawString(posAnteriorX, posAnteriorY + faceHeight + 18,
				"Lv: 1", Color.white);

		// this.graphics.setColor(new Color(.3f, .4f, .5f, .6f));

		this.playerCorrente = characterHero.playerAnimationMove(transform.getPosition(),transform.isMoving());
		g.drawAnimation(playerCorrente, posXPlayer, posYPlayer);
		this.font_large.drawString(posXPlayer - 8, posYPlayer - 16, this.namePlayer,
				Color.green);
		this.posicalAtualX = posXPlayer;
		this.posicalAtualY = posYPlayer;
	}

	public int getType() {
		return type;
	}

}