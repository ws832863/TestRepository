package game.cliente.core;

import game.cliente.hero.CharacterPosition;
import game.cliente.utils.ResourceManager;
import game.cliente.utils.Util;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class CharacterHero {
	private Animation playerDireita;
	private Animation playerEsquerda;
	private Animation playerCima;
	private Animation playerBaixo;
	private Animation playerCorrente;
	private int startColSprite;
	private int startRowSprite;
	private SpriteSheet sheetCharacter;
	private Image playerFace;
	private int startPosXSpriteFace;
	private int startPosYSpriteFace;

	// Após usar o metodo construtor, chame os metodos get dos animatios
	public CharacterHero(int characterHero) {
		this.playerDireita = new Animation();
		this.playerEsquerda = new Animation();
		this.playerCima = new Animation();
		this.playerBaixo = new Animation();
		this.playerFace = null;
		this.playerCorrente = new Animation();

		if (characterHero == Util.CHARACTER_WARRIOR_M) {
			buildWarriorM();
		} else if (characterHero == Util.CHARACTER_WARRIOR_f) {
			buildWarriorF();
		} else if (characterHero == Util.CHARACTER_MONJE_M) {
			buildMonjeM();
		} else if (characterHero == Util.CHARACTER_MONJE_F) {
			buildMonjeF();
		} else if (characterHero == Util.CHARACTER_MAGE_F) {
			buildMageF();
		} else if (characterHero == Util.CHARACTER_ASSASSIN_F) {
			buildAssassinF();
		} else if (characterHero == Util.CHARACTER_ASSASSIN_M) {
			buildAssassinM();
		} else if (characterHero == Util.CHARACTER_MAGE_M) {
			buildMageM();
		}

		buildAnimations();
	}

	public void buildAnimations() {
		for (int col = this.startColSprite; col <= (this.startColSprite + 2); col++) { // Colunas
			this.playerBaixo.addFrame(
					sheetCharacter.getSprite(col, this.startRowSprite), 150); // linhas
			this.playerEsquerda
					.addFrame(sheetCharacter.getSprite(col,
							this.startRowSprite + 1), 150);
			this.playerDireita
					.addFrame(sheetCharacter.getSprite(col,
							this.startRowSprite + 2), 150);
			this.playerCima
					.addFrame(sheetCharacter.getSprite(col,
							this.startRowSprite + 3), 150);
		}
		this.playerCorrente = this.playerBaixo;
	}

	public Animation getPlayerCorrente() {
		return playerCorrente;
	}

	public void buildWarriorM() {
		this.startPosXSpriteFace = 0;
		this.startPosYSpriteFace = 0;
		this.playerFace = ResourceManager.getFace("96_96_FaceActor1")
				.getSubImage(startPosXSpriteFace, startPosYSpriteFace, 96, 96);

		this.sheetCharacter = ResourceManager.getCharacter("Actor1");
		this.startColSprite = 0;
		this.startRowSprite = 0;
	}

	public void buildWarriorF() {
		this.startPosXSpriteFace = Util.SPRITE_FACE_SIZE;
		this.startPosYSpriteFace = 0;
		this.playerFace = ResourceManager.getFace("96_96_FaceActor1")
				.getSubImage(startPosXSpriteFace, startPosYSpriteFace, 96, 96);
		;

		this.sheetCharacter = ResourceManager.getCharacter("Actor1");
		this.startColSprite = 3;
		this.startRowSprite = 0;
	}

	public void buildMonjeM() {
		this.startPosXSpriteFace = Util.SPRITE_FACE_SIZE * 2;
		this.startPosYSpriteFace = 0;
		this.playerFace = ResourceManager.getFace("96_96_FaceActor1")
				.getSubImage(startPosXSpriteFace, startPosYSpriteFace, 96, 96);
		;

		this.sheetCharacter = ResourceManager.getCharacter("Actor1");
		this.startColSprite = 6;
		this.startRowSprite = 0;
	}

	public void buildMonjeF() {
		this.startPosXSpriteFace = Util.SPRITE_FACE_SIZE * 3;
		this.startPosYSpriteFace = 0;
		this.playerFace = ResourceManager.getFace("96_96_FaceActor1")
				.getSubImage(startPosXSpriteFace, startPosYSpriteFace, 96, 96);
		;

		this.sheetCharacter = ResourceManager.getCharacter("Actor1");
		this.startColSprite = 9;
		this.startRowSprite = 0;
	}

	public void buildMageF() {
		this.startPosXSpriteFace = 0;
		this.startPosYSpriteFace = Util.SPRITE_FACE_SIZE;
		this.playerFace = ResourceManager.getFace("96_96_FaceActor1")
				.getSubImage(startPosXSpriteFace, startPosYSpriteFace, 96, 96);
		;

		this.sheetCharacter = ResourceManager.getCharacter("Actor1");
		this.startColSprite = 0;
		this.startRowSprite = 4;
	}

	public void buildAssassinF() {
		this.startPosXSpriteFace = Util.SPRITE_FACE_SIZE;
		this.startPosYSpriteFace = Util.SPRITE_FACE_SIZE;
		this.playerFace = ResourceManager.getFace("96_96_FaceActor1")
				.getSubImage(startPosXSpriteFace, startPosYSpriteFace, 96, 96);
		;

		this.sheetCharacter = ResourceManager.getCharacter("Actor1");
		this.startColSprite = 3;
		this.startRowSprite = 4;
	}

	public void buildAssassinM() {
		this.startPosXSpriteFace = Util.SPRITE_FACE_SIZE * 2;
		this.startPosYSpriteFace = Util.SPRITE_FACE_SIZE;
		this.playerFace = ResourceManager.getFace("96_96_FaceActor1")
				.getSubImage(startPosXSpriteFace, startPosYSpriteFace, 96, 96);
		;

		this.sheetCharacter = ResourceManager.getCharacter("Actor1");
		this.startColSprite = 6;
		this.startRowSprite = 4;
	}

	public void buildMageM() {
		this.startPosXSpriteFace = Util.SPRITE_FACE_SIZE * 3;
		this.startPosYSpriteFace = Util.SPRITE_FACE_SIZE;
		this.playerFace = ResourceManager.getFace("96_96_FaceActor1")
				.getSubImage(startPosXSpriteFace, startPosYSpriteFace, 96, 96);
		;

		this.sheetCharacter = ResourceManager.getCharacter("Actor1");
		this.startColSprite = 9;
		this.startRowSprite = 4;
	}

	// *******************
	public Animation getPlayerDireita() {
		return playerDireita;
	}

	public Animation getPlayerEsquerda() {
		return playerEsquerda;
	}

	public Animation getPlayerCima() {
		return playerCima;
	}

	public Animation getPlayerBaixo() {
		return playerBaixo;
	}

	public Image getPlayerFace() {
		return playerFace;
	}

	public Animation playerAnimationMove(CharacterPosition position, boolean moving) {

		// Direita
		if (position == CharacterPosition.FACE_RIGHT) {
			this.playerDireita.stop();
			this.playerEsquerda.stop();
			this.playerCima.stop();
			this.playerBaixo.stop();
			this.playerCorrente = this.playerDireita;
		} else
		// Esquerda
		if (position == CharacterPosition.FACE_LEFT) {
			this.playerDireita.stop();
			this.playerEsquerda.stop();
			this.playerCima.stop();
			this.playerBaixo.stop();
			this.playerCorrente = this.playerEsquerda;
		} else // Descendo
		if (position == CharacterPosition.FACE_DOWN) {
			this.playerDireita.stop();
			this.playerEsquerda.stop();
			this.playerCima.stop();
			this.playerBaixo.stop();
			this.playerCorrente = this.playerBaixo;
		} else // Subindo
		if (position == CharacterPosition.FACE_UP) {
			this.playerDireita.stop();
			this.playerEsquerda.stop();
			this.playerCima.stop();
			this.playerBaixo.stop();
			this.playerCorrente = this.playerCima;
		} 
		
		if (moving==true){
			this.playerCorrente.start();
		} else if (moving==false)// Parado
		{
			this.playerCorrente.stop();
		}
		this.playerCorrente.setSpeed(Util.SPEED_ANIMATION_PLAYER);

		return this.playerCorrente;
	}

}
