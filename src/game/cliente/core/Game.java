package game.cliente.core;

import game.cliente.connections.HostConnect;
import game.cliente.states.BattleState;
import game.cliente.states.IDeferredResourceState;
import game.cliente.states.InGameState;
import game.cliente.states.LoginState;
import game.cliente.states.ResourcesState;
import game.cliente.utils.ResourceManager;
import game.cliente.utils.Util;

import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class Game extends StateBasedGame {

	// CONFIGURATION

	private ArrayList<BasicGameState> states;
	private HostConnect hostConnect;

	public Game(String resourcesLocation, HostConnect hostConnect) {
		super("JMMORPG Project");
		states = new ArrayList<BasicGameState>();
		this.hostConnect = hostConnect;

		try {
			ResourceManager.init(resourcesLocation);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {

		// 0
		ResourcesState rs = new ResourcesState();
		states.add(rs);
		addState(rs);

		// 1
		LoginState ls = new LoginState();
		states.add(ls);
		addState(ls);

		// 2
		InGameState igs = new InGameState(this.hostConnect);
		states.add(igs);
		addState(igs);

		// 3
		BattleState bs = new BattleState();
		states.add(bs);
		addState(bs);

	}

	public void initAllStateResources() {
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i) instanceof IDeferredResourceState) {
				((IDeferredResourceState) states.get(i))
						.initDeferredResources();
			}
		}
	}

	public void launch() throws SlickException {
		AppGameContainer container = new AppGameContainer(this);
		container.setMinimumLogicUpdateInterval(1); // default
		container.setMaximumLogicUpdateInterval(0); // default
		container.setUpdateOnlyWhenVisible(true);
		container.setAlwaysRender(true);

		// Apply Configuration
		container.setDisplayMode(Util.FRAME_WIDTH, Util.FRAME_HEIGHT,
				Util.FULLSCREEN);
		container.setTargetFrameRate(Util.TARGET_FRAME_RATE);
		container.setVSync(Util.VSYNC);
		container.setShowFPS(Util.DEBUG);
		container.setVerbose(Util.DEBUG);

		// Start the game
		container.start();
	}

}
