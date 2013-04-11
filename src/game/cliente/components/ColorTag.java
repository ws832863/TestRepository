package game.cliente.components;

import org.newdawn.slick.Color;

import com.artemis.Component;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class ColorTag extends Component {
	private Color color;

	public ColorTag(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

}
