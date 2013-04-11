package game.cliente.systems;

import game.cliente.components.Player;
import game.cliente.connections.HostConnect;
import game.cliente.gui.chat.ChatControlDialogController;
import game.cliente.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.opengl.SlickCallable;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyInputConsumer;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.StyleBuilder;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.chatcontrol.builder.ChatBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglKeyboardInputEventCreator;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.spi.input.InputSystem;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.TimeProvider;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class RenderGUISystem extends EntityProcessingSystem implements
		KeyListener, MouseListener {

	private GameContainer container;
	private List<MouseEvent> mouseEvents = new ArrayList<MouseEvent>();
	private List<KeyboardInputEvent> keyEvents = new ArrayList<KeyboardInputEvent>();
	private LwjglKeyboardInputEventCreator inputEventCreator = new LwjglKeyboardInputEventCreator();
	private Nifty nifty;
	private Screen mainScreen;
	private Chat chat;
	private Input input;
	private int mouseX;
	private int mouseY;
	protected boolean mouseDown;
	private CameraPlayerSystem cameraSystem;
	private ChatControlDialogController chatControlDialogController;
	private HostConnect hostConnect;
	private NiftyImage chatIconVoid;
	private ComponentMapper<Player> playerHeroMapper;

	public RenderGUISystem(GameContainer container, HostConnect hostConnect) {
		super(Player.class);

		// Remove os 999 logs do Nifty-Gui, mude para Warning para mostrar
		// alguns
		Logger.getLogger("de.lessvoid").setLevel(Level.SEVERE);

		this.setContainer(container);
		this.input = container.getInput();
		this.hostConnect = hostConnect;

		input.addKeyListener(this);
		input.addMouseListener(this);
		chatControlDialogController = new ChatControlDialogController();
	}

	@Override
	public void initialize() {
		cameraSystem = world.getSystemManager().getSystem(CameraPlayerSystem.class);
		playerHeroMapper = new ComponentMapper<Player>(Player.class,
				world.getEntityManager());

		nifty = new Nifty(new LwjglRenderDevice(), new NullSoundDevice(),
				new InputSystem() {
					public void forwardEvents(
							final NiftyInputConsumer inputEventConsumer) {
						for (MouseEvent event : mouseEvents) {
							event.processMouseEvents(inputEventConsumer);
						}
						mouseEvents.clear();

						for (KeyboardInputEvent event : keyEvents) {
							inputEventConsumer.processKeyboardEvent(event);
						}
						keyEvents.clear();
					}

					public void setMousePosition(int x, int y) {

					}

				}, new TimeProvider());

		addNiftyComponentsMainScreen();

	}

	public void update() {
		if (Util.CHAT_HERO != null && Util.CHAT_HERO != "") {
			Entity hero = world.getTagManager().getEntity("Hero");
			Player playerHero = playerHeroMapper.get(hero);
			this.hostConnect.sendChannel("chat/" + playerHero.getName() + ": "
			
					+ Util.CHAT_HERO + "/", Util.CHANNEL_MAP);
			Util.CHAT_HERO = null;
		}

		if (this.hostConnect.getReceivedChannelMessage() != null) {
			String[] msg = this.hostConnect.getReceivedChannelMessage().split(
					"/");
			if (msg[0].equals("chat")) {
				String[] y = msg[1].split(":");
				Entity hero = world.getTagManager().getEntity("Hero");
				Player playerHero = playerHeroMapper.get(hero);
				if (!y[0].equalsIgnoreCase(playerHero.getName())) {
					this.chat.receivedChatLine(msg[1], chatIconVoid);
					this.hostConnect.setReceivedChannelMessage(null);
				}
			}
		}

		nifty.getScreen("mainScreen").layoutLayers();
		nifty.update();
	}

	@Override
	protected void process(Entity player) {
		SlickCallable.enterSafeBlock();
		nifty.render(false);
		SlickCallable.leaveSafeBlock();
	}

	  private static void registerStyles(final Nifty nifty) {
		    new StyleBuilder() {{
		      id("base-font-link");
		      base("base-font");
		      color("#8fff");
		      interactOnRelease("$action");
		      onHoverEffect(new HoverEffectBuilder("changeMouseCursor") {{
		        effectParameter("id", "hand");
		      }});
		    }}.build(nifty);

		    new StyleBuilder() {{
		      id("creditsImage");
		      alignCenter();
		    }}.build(nifty);

		    new StyleBuilder() {{
		      id("creditsCaption");
		      font("verdana-48-regular.fnt");
		      width("100%");
		      textHAlignCenter();
		    }}.build(nifty);

		    new StyleBuilder() {{
		      id("creditsCenter");
		      base("base-font");
		      width("100%");
		      textHAlignCenter();
		    }}.build(nifty);
		  }
	  
	public void addNiftyComponentsMainScreen() {
		nifty.loadStyleFile("nifty-default-styles.xml");
		nifty.loadControlFile("nifty-default-controls.xml");
		nifty.registerMouseCursor("hand", "resources/images/mouse-cursor-hand.png", 5, 4);
		nifty.setDebugOptionPanelColors(false);
		registerStyles(nifty);
		mainScreen = new ScreenBuilder("main") {
			{
				controller(new ScreenControllerExample());
				layer(new LayerBuilder("layer") {
					{
						childLayoutAbsolute();
						panel(new PanelBuilder("panel-chat") {
							{
								height("250px");
								width("790px");
								// style("nifty-panel");
								align(Align.Center);
								valign(VAlign.Center);
								childLayoutVertical();
								control(new ChatBuilder("chat", 5) {
									{
										this.sendLabel("<<");
									}
								});
								controller(chatControlDialogController);
							}
						});
					}
				});
			}
		}.build(nifty);

		nifty.addScreen("mainScreen", mainScreen);
		nifty.gotoScreen("mainScreen");
		nifty.setDebugOptionPanelColors(false);
		this.chat = mainScreen.findNiftyControl("chat", Chat.class);
		this.chatIconVoid = nifty.createImage(
				"resources/images/chat-icon-user.png", false);

		Element element = nifty.getScreen("mainScreen").findElementByName(
				"panel-chat");
		element.setConstraintX(new SizeValue("5"));
		element.setConstraintY(new SizeValue("440"));
	}

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAcceptingInput() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setInput(Input arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(final int key, final char c) {
		// Não passo para o niftygui ocontrole das setas do teclado
		if (key != Input.KEY_UP && key != Input.KEY_DOWN
				&& key != Input.KEY_RIGHT && key != Input.KEY_LEFT)
			keyEvents.add(inputEventCreator.createEvent(key, c, true));
	}

	@Override
	public void keyReleased(final int key, final char c) {
		// Não passo para o niftygui ocontrole das setas do teclado
		if (key != Input.KEY_UP && key != Input.KEY_DOWN
				&& key != Input.KEY_RIGHT && key != Input.KEY_LEFT)
			keyEvents.add(inputEventCreator.createEvent(key, c, false));
	}

	private void forwardMouseEventToNifty(final int mouseX, final int mouseY,
			final boolean mouseDown) {
		cameraSystem = world.getSystemManager().getSystem(CameraPlayerSystem.class);
		mouseEvents.add(new MouseEvent(
				mouseX + (int) cameraSystem.getOffsetX(), mouseY
						+ (int) cameraSystem.getOffsetY(), mouseDown, 0));

	}

	@Override
	public void mouseMoved(final int oldx, final int oldy, final int newx,
			final int newy) {
		mouseX = newx;
		mouseY = newy;
		forwardMouseEventToNifty(mouseX, mouseY, mouseDown);
	}

	@Override
	public void mousePressed(final int button, final int x, final int y) {
		mouseX = x;
		mouseY = y;
		mouseDown = true;
		if (button==Util.MOUSE_BUTTON_LEFT){
			Util.MOUSE_CLICK_POSITION_XY.setLocation(mouseX, mouseY);
		}
		forwardMouseEventToNifty(mouseX, mouseY, mouseDown);
	}

	@Override
	public void mouseReleased(final int button, final int x, final int y) {
		mouseX = x;
		mouseY = y;
		mouseDown = false;
		forwardMouseEventToNifty(mouseX, mouseY, mouseDown);
	}

	@Override
	public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(int arg0) {
		// TODO Auto-generated method stub
	}

	public void setContainer(GameContainer container) {
		this.container = container;
	}

	public GameContainer getContainer() {
		return container;
	}

	class MouseEvent {
		private int mouseX;
		private int mouseY;
		private int mouseWheel;
		private int button;
		private boolean buttonDown;

		public MouseEvent(final int mouseX, final int mouseY,
				final boolean mouseDown, final int mouseButton) {
			this.mouseX = mouseX;
			this.mouseY = mouseY;
			this.buttonDown = mouseDown;
			this.button = mouseButton;
			this.mouseWheel = 0;
		}

		public void processMouseEvents(
				final NiftyInputConsumer inputEventConsumer) {
			inputEventConsumer.processMouseEvent(mouseX, mouseY, mouseWheel,
					button, buttonDown);
		}
	}

	class ScreenControllerExample implements ScreenController {
		public void bind(Nifty nifty, Screen screen) {
		}

		public void onEndScreen() {
		}

		public void onStartScreen() {
		}
	}

}
