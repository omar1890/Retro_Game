package com.example.demo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShieldImage extends ImageView {
	
	// private static final String IMAGE_NAME = "/images/shield.png"; wrong path 
	private static final String IMAGE_NAME = "/com/example/demo/images/shield.png";
	private static final int SHIELD_SIZE = 200;
	
	public ShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		//this.setImage(new Image(IMAGE_NAME));
		// this.setImage(new Image(getClass().getResource("/com/example/demo/images/shield.jpg").toExternalForm())); it's png image not jpg image
		this.setImage(new Image(getClass().getResource("/com/example/demo/images/shield.png").toExternalForm()));
		this.setVisible(false);
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
	}

	public void showShield() {
		this.setVisible(true);
	}
	
	public void updatePosition(double xPosition, double yPosition) {
		this.setLayoutX(xPosition + 50);
		this.setLayoutY(yPosition);
	}
	public void hideShield() {
		this.setVisible(false);
	}

}
