package com.example.demo;

public class LevelTwo extends LevelParent {

	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final int PLAYER_INITIAL_HEALTH = 5;
	private static final String NEXT_LEVEL = "com.example.demo.LevelThree";
	private final Boss boss;
	private LevelViewLevelTwo levelView;

	public LevelTwo(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
		boss = new Boss();
		System.out.println("Boss initialized: " + boss);
	}

	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	// @Override
	// protected void checkIfGameOver() {
	// 	if (userIsDestroyed()) {
	// 		loseGame();
	// 	}
	// 	else if (boss.isDestroyed()) {
	// 		winGame();
	// 	}
	// }


	@Override
	protected void checkIfGameOver() {
		// If the level is already marked as completed, exit early to prevent repeated logic
		if (isLevelCompleted()) {
			return;
		}

		if (userIsDestroyed()) {
			loseGame();
			setLevelCompleted(true); // Mark level as completed to prevent further updates
			System.out.println("Game Over!");
		} else if (boss.isDestroyed()) {
			goToNextLevel(NEXT_LEVEL); // Transition to LevelThree
			setLevelCompleted(true);
			System.out.println("You defeated the boss! Congratulations!");
			// winGame();
			// setLevelCompleted(true); // Mark level as completed to prevent further updates
			// System.out.println("You defeated the boss! Congratulations!");
		}
	}

	@Override
	protected void spawnEnemyUnits() {
		if (getCurrentNumberOfEnemies() == 0) {
			addEnemyUnit(boss);
		}
	}

	@Override
	protected int getLevelNumber() {
		return 2; // Level 2
	}
	@Override
	protected LevelView instantiateLevelView() {
		levelView = new LevelViewLevelTwo(getRoot(), PLAYER_INITIAL_HEALTH, getLevelNumber());
		return levelView;
	}

}
