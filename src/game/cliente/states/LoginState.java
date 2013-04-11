package game.cliente.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class LoginState extends BasicGameState {

	public static final int ID = 1;

	public LoginState() {
	}

	@Override
	public void init(GameContainer container, StateBasedGame sbGame)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer container, StateBasedGame sbGame,
			Graphics graphics) throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer container, StateBasedGame sbGame, int delta)
			throws SlickException {
		sbGame.enterState(InGameState.ID, new FadeOutTransition(),
				new FadeInTransition());

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
