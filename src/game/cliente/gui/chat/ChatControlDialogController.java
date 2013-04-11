package game.cliente.gui.chat;

import game.cliente.utils.Util;

import java.util.Properties;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * The ChatControlDialogController registers a new control with Nifty that
 * represents the whole Dialog. This gives us later an appropriate
 * ControlBuilder to actual construct the Dialog (as a control).
 * 
 * @author void
 */
public class ChatControlDialogController implements Controller {
	private Chat chat;
	private NiftyImage chatIconNiftyUser;
	private NiftyImage chatIconVoid;

	@Override
	public void bind(final Nifty nifty, final Screen screen,
			final Element element, final Properties parameter,
			final Attributes controlDefinitionAttributes) {
		this.chat = screen.findNiftyControl("chat", Chat.class);
		this.chatIconVoid = nifty.createImage(
				"resources/images/chat-icon-ninja.png", false);
		this.chatIconNiftyUser = nifty.createImage(
				"resources/images/chat-icon-user.png", false);
		chat.addPlayer("Warrior", chatIconNiftyUser);
		chat.addPlayer("Mage", chatIconVoid);
		chat.addPlayer("Archer", chatIconVoid);
		chat.addPlayer("Healer", chatIconVoid);
	}

	public void addPlayerChat(String Player) {
		chat.addPlayer(Player, null);
	}

	@Override
	public void init(final Properties parameter,
			final Attributes controlDefinitionAttributes) {
	}

	@Override
	public void onStartScreen() {
	}

	@Override
	public void onFocus(final boolean getFocus) {
	}

	@Override
	public boolean inputEvent(final NiftyInputEvent inputEvent) {
		return true;
	}

	@NiftyEventSubscriber(id = "chat")
	public void onChatTextSendEvent(final String id,
			final ChatTextSendEvent event) {
		String msg = event.getText();
		// Mantem o limite maximo de 25 Caracteres
		if (msg.length() >= 56) {
			msg = msg.substring(0, 55);
		}
		if (!msg.isEmpty()) {
			// here we simply post it to the chat window
			chat.receivedChatLine(msg, chatIconVoid);
			// You should post that text to the server ...

			// Precisei fazer isso ao invez de usar o
			// "this.hostConnect.sendChannel(...)"
			// Porque ele toda hora deixava o "this.hostConnect" como null (Não
			// indentifiquei a causa)
			Util.CHAT_HERO = msg;
		}
	}

}
