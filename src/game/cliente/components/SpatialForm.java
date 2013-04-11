package game.cliente.components;

import com.artemis.Component;

/**
 * 
 * @author Michel Montenegro
 * 
 */
public class SpatialForm extends Component {
	private String spatialFormFile;

	public SpatialForm(String spatialFormFile) {
		this.spatialFormFile = spatialFormFile;
	}

	public String getSpatialFormFile() {
		return spatialFormFile;
	}

}
