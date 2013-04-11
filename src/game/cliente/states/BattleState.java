package game.cliente.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
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
public class BattleState extends BasicGameState implements
		IDeferredResourceState {

	public static final int ID = 3;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer container, StateBasedGame sbGame, int arg2)
			throws SlickException {
		if (container.getInput().isKeyDown(Input.KEY_K)) {
			sbGame.enterState(InGameState.ID, new FadeOutTransition(),
					new FadeInTransition());
		}

	}

	@Override
	public void initDeferredResources() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
