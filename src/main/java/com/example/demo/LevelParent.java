package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

public abstract class LevelParent extends Observable {

	private static final double SCREEN_HEIGHT_ADJUSTMENT = 150;
	private static final int MILLISECOND_DELAY = 50;
	private final double screenHeight;
	private final double screenWidth;
	private final double enemyMaximumYPosition;

	private final Group root;
	private final Timeline timeline;
	private final UserPlane user;
	private final Scene scene;
	private final ImageView background;

	private final List<ActiveActorDestructible> friendlyUnits;
	private final List<ActiveActorDestructible> enemyUnits;
	private final List<ActiveActorDestructible> userProjectiles;
	private final List<ActiveActorDestructible> enemyProjectiles;
	
	private int currentNumberOfEnemies;
	private LevelView levelView;
	private boolean levelCompleted = false;

	public LevelParent(String backgroundImageName, double screenHeight, double screenWidth, int playerInitialHealth) {
		this.root = new Group();
		this.scene = new Scene(root, screenWidth, screenHeight);
		this.timeline = new Timeline();
		this.user = new UserPlane(playerInitialHealth);
		this.friendlyUnits = new ArrayList<>();
		this.enemyUnits = new ArrayList<>();
		this.userProjectiles = new ArrayList<>();
		this.enemyProjectiles = new ArrayList<>();

		this.background = new ImageView(new Image(getClass().getResource(backgroundImageName).toExternalForm()));
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.enemyMaximumYPosition = screenHeight - SCREEN_HEIGHT_ADJUSTMENT;
		this.levelView = instantiateLevelView();
		this.currentNumberOfEnemies = 0;
		initializeTimeline();
		friendlyUnits.add(user);
	}

	protected abstract void initializeFriendlyUnits();

	protected abstract void checkIfGameOver();

	protected abstract void spawnEnemyUnits();

	protected abstract LevelView instantiateLevelView();

	protected boolean isLevelCompleted() {
		return levelCompleted;
	}
	
	protected abstract int getLevelNumber() ;

	protected void setLevelCompleted(boolean completed) {
		this.levelCompleted = completed;
	}

	public Scene initializeScene() {
		root.getChildren().clear(); // Clear all existing elements
		initializeBackground();
		initializeFriendlyUnits();
		levelView.showHeartDisplay();
		return scene;
	}

	public void startGame() {
		background.requestFocus();
		timeline.play();
	}

	// public void goToNextLevel(String levelName) {
	// 	setChanged();
	// 	notifyObservers(levelName);
	// }

	public void goToNextLevel(String levelName) {
		if (!isLevelCompleted()) {
			setChanged();
			notifyObservers(levelName);
		}
	}
	/* 
		is called on every iteration of the game loop
		which typically runs at a fixed interval, such as every 16 milliseconds (approximately 60 frames per second).
	*/
	private void updateScene() {
		spawnEnemyUnits();
		updateActors();
		generateEnemyFire();
		updateNumberOfEnemies();
		handleEnemyPenetration();
		handleUserProjectileCollisions();
		handleEnemyProjectileCollisions();
		handlePlaneCollisions();
		removeAllDestroyedActors();
		updateKillCount();
		updateLevelView();
		checkIfGameOver();
	}

	private void initializeTimeline() {
		timeline.setCycleCount(Timeline.INDEFINITE);
		KeyFrame gameLoop = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> updateScene());
		timeline.getKeyFrames().add(gameLoop);
	}

	private void initializeBackground() {
		background.setFocusTraversable(true);
		background.setFitHeight(screenHeight);
		background.setFitWidth(screenWidth);
		background.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP) user.moveUp();
				if (kc == KeyCode.DOWN) user.moveDown();
				if (kc == KeyCode.LEFT) user.moveLeft();
				if (kc == KeyCode.RIGHT) user.moveRight();
				if (kc == KeyCode.SPACE) fireProjectile();
			}
		});
		background.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode kc = e.getCode();
				if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stopVerticalMovement();
				if (kc == KeyCode.LEFT || kc == KeyCode.RIGHT) user.stopHorizontalMovement();
			}
		});
		root.getChildren().add(background);
	
		// Add the level text after the background
		Text levelText = new Text("Level: " + getLevelNumber());
		levelText.setFont(new Font(50));
		levelText.setFill(javafx.scene.paint.Color.WHITE); // Set a visible color
		levelText.setX(575); // Position in the top-right corner
		levelText.setY(50);
		root.getChildren().add(levelText); // Add the text after the background
	}
	

	private void fireProjectile() {
		ActiveActorDestructible projectile = user.fireProjectile();
		root.getChildren().add(projectile);
		userProjectiles.add(projectile);
	}

	private void generateEnemyFire() {
		enemyUnits.forEach(enemy -> spawnEnemyProjectile(((FighterPlane) enemy).fireProjectile()));
	}

	private void spawnEnemyProjectile(ActiveActorDestructible projectile) {
		if (projectile != null) {
			root.getChildren().add(projectile);
			enemyProjectiles.add(projectile);
		}
	}

	private void updateActors() {
		friendlyUnits.forEach(plane -> plane.updateActor());
		enemyUnits.forEach(enemy -> enemy.updateActor());
		userProjectiles.forEach(projectile -> projectile.updateActor());
		enemyProjectiles.forEach(projectile -> projectile.updateActor());
	}

	private void removeAllDestroyedActors() {
		removeDestroyedActors(friendlyUnits);
		removeDestroyedActors(enemyUnits);
		removeDestroyedActors(userProjectiles);
		removeDestroyedActors(enemyProjectiles);
	}

	private void removeDestroyedActors(List<ActiveActorDestructible> actors) {
		List<ActiveActorDestructible> destroyedActors = actors.stream().filter(actor -> actor.isDestroyed())
				.collect(Collectors.toList());
		root.getChildren().removeAll(destroyedActors);
		actors.removeAll(destroyedActors);
	}

	private void handlePlaneCollisions() {
		handleCollisions(friendlyUnits, enemyUnits);
	}

	private void handleUserProjectileCollisions() {
		handleCollisions(userProjectiles, enemyUnits);
	}

	private void handleEnemyProjectileCollisions() {
		handleCollisions(enemyProjectiles, friendlyUnits);
	}

	private void handleCollisions(List<ActiveActorDestructible> actors1,
			List<ActiveActorDestructible> actors2) {
		for (ActiveActorDestructible actor : actors2) {
			for (ActiveActorDestructible otherActor : actors1) {
				if (actor.getBoundsInParent().intersects(otherActor.getBoundsInParent())) {
					actor.takeDamage();
					otherActor.takeDamage();
				}
			}
		}
	}

	private void handleEnemyPenetration() {
		for (ActiveActorDestructible enemy : enemyUnits) {
			if (enemyHasPenetratedDefenses(enemy)) {
				user.takeDamage();
				enemy.destroy();
			}
		}
	}

	private void updateLevelView() {
		levelView.removeHearts(user.getHealth());
	}

	private void updateKillCount() {
		for (int i = 0; i < currentNumberOfEnemies - enemyUnits.size(); i++) {
			user.incrementKillCount();
		}
	}

	private boolean enemyHasPenetratedDefenses(ActiveActorDestructible enemy) {
		return Math.abs(enemy.getTranslateX()) > screenWidth;
	}

	protected void winGame() {
		timeline.stop();
		levelView.showWinImage();
		SoundPlayer.playWinnerSound();
		showRestartButton();

	}

	protected void loseGame() {
		timeline.stop();
		levelView.showGameOverImage();
		SoundPlayer.playGameOverSound(); // Play the game over sound
		showRestartButton();
	}

	protected UserPlane getUser() {
		return user;
	}

	protected Group getRoot() {
		return root;
	}

	protected int getCurrentNumberOfEnemies() {
		return enemyUnits.size();
	}

	protected void addEnemyUnit(ActiveActorDestructible enemy) {
		enemyUnits.add(enemy);
		root.getChildren().add(enemy);
	}

	protected double getEnemyMaximumYPosition() {
		return enemyMaximumYPosition;
	}

	protected double getScreenWidth() {
		return screenWidth;
	}

	protected boolean userIsDestroyed() {
		return user.isDestroyed();
	}

	private void updateNumberOfEnemies() {
		currentNumberOfEnemies = enemyUnits.size();
	}
	private void showRestartButton() {
		// Restart Level 1 Button
		Button restartLevel1Button = new Button("Restart Game - Level 1");
		restartLevel1Button.setOnAction(e -> restartGame("com.example.demo.LevelOne"));

		// Restart Level 2 Button
		Button restartLevel2Button = new Button("Restart Game - Level 2");
		restartLevel2Button.setOnAction(e -> restartGame("com.example.demo.LevelTwo"));

		Button restartLevel3Button = new Button("Restart Game - Level 3");
		restartLevel3Button.setOnAction(e -> restartGame("com.example.demo.LevelThree"));

		// Stop Game Button
		Button stopGameButton = new Button("Stop Game");
		stopGameButton.setOnAction(e -> stopGame());

		// Create an HBox for the buttons
		HBox buttonMenu = new HBox(20); // 20 is the spacing between buttons
		buttonMenu.setAlignment(Pos.CENTER); // Center-align the buttons
		buttonMenu.getChildren().addAll(restartLevel1Button, restartLevel2Button, restartLevel3Button, stopGameButton);

		// Position the HBox in the scene
		buttonMenu.setLayoutX((screenWidth - 300) / 2); // Adjust 300 based on button sizes
		buttonMenu.setLayoutY(screenHeight / 2 + 100);

		// Add the HBox to the root node only if it's not already added
		if (!root.getChildren().contains(buttonMenu)) {
			root.getChildren().add(buttonMenu);
		}
	}


	
	
	
	private void restartGame(String levelName) {
		root.getChildren().clear(); // Clear current game elements
		timeline.stop(); // Stop the current timeline
		setChanged();
		notifyObservers(levelName); // Notify observers with the selected level
	}
	
	private void stopGame() {
		// Stop the timeline (game loop)
		if (timeline != null) {
			timeline.stop();
		}

		// Clear all game elements
		root.getChildren().clear();

		// Optional: Show a message
		Text stopMessage = new Text("We have hope that you have a fun time.");
		stopMessage.setFont(new Font(40));
		stopMessage.setFill(javafx.scene.paint.Color.RED);
		stopMessage.setX(screenWidth / 2 - 150);
		stopMessage.setY(screenHeight / 2);
		root.getChildren().add(stopMessage);

		// Optionally, close the application after a short delay
		PauseTransition delay = new PauseTransition(Duration.seconds(3));
		delay.setOnFinished(e -> Platform.exit()); // Close the app
		delay.play();
	}

	
}
