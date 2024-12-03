package com.example.demo;

import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LevelView {
	
	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final int WIN_IMAGE_X_POSITION = 355;
	private static final int WIN_IMAGE_Y_POSITION = 175;
	private static final int LOSS_SCREEN_X_POSITION = -160;
	private static final int LOSS_SCREEN_Y_POSISITION = -375;
	private final Group root;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private final HeartDisplay heartDisplay;
	private static final double LEVEL_DISPLAY_X_POSITION = 575; // Adjust for the top-right corner
	private static final double LEVEL_DISPLAY_Y_POSITION = 50;
	private final Text levelText;

	public LevelView(Group root, int heartsToDisplay, int levelNumber) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSISITION);

		// Initialize level text
		this.levelText = new Text("Level: " + levelNumber);
		// this.levelText.setFont(new Font(20));
		// this.levelText.setFill(javafx.scene.paint.Color.WHITE); // Set the text color
		// this.levelText.setX(LEVEL_DISPLAY_X_POSITION);
		// this.levelText.setY(LEVEL_DISPLAY_Y_POSITION);
		root.getChildren().add(this.levelText);
	}
	
	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	public void showWinImage() {
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}
	
	public void showGameOverImage() {
		root.getChildren().add(gameOverImage);
	}
	
	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}

	// Method to update level text dynamically
	public void updateLevelText(int levelNumber) {
		levelText.setText("Level: " + levelNumber);
	}

}
