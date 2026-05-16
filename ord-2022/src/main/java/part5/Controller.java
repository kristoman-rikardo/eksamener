package part5;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Controller {

	@FXML Text innput;
	@FXML TextField output;

	@FXML
	void handleKvadrer() {
		final String s = innput.getText();
		try {
			output.setText(s + " * " + s + " = " + Math.exp(Integer.parseInt(s)));
		} catch (Exception e) {
			output.setText('"' + s + "\" er ikke et tall");
		}
	}
}
