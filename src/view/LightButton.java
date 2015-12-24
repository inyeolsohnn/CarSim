package view;

public class LightButton extends CustomJButton {

	int lightId;

	public LightButton(String text, int id) {
		super(text);
		// TODO Auto-generated constructor stub
		this.lightId = id;
	}

	public int getId() {
		return this.lightId;
	}
}
