package com.example.demo;

public class LevelOne extends LevelParent {
	
	private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
	private static final String NEXT_LEVEL = "com.example.demo.LevelTwo";
	private static final int TOTAL_ENEMIES = 5;
	private static final int KILLS_TO_ADVANCE = 1;
	private static final double ENEMY_SPAWN_PROBABILITY = .20;
	private static final int PLAYER_INITIAL_HEALTH = 5;

	public LevelOne(double screenHeight, double screenWidth) {
		super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
	}

	// @Override
	// protected void checkIfGameOver() {
	// 	if (userIsDestroyed()) {
	// 		loseGame();
	// 		System.out.println("Game Over!");
	// 	}
	// 	else if (userHasReachedKillTarget()){
	// 		goToNextLevel(NEXT_LEVEL);
	// 		System.out.println("Next level: " + NEXT_LEVEL);
	// 	}
	// }
	@Override
	protected void checkIfGameOver() {
		if (isLevelCompleted()) {
			return; // If the level is already marked as completed, do nothing
		}

		if (userIsDestroyed()) {
			loseGame();
			System.out.println("Game Over!");
			setLevelCompleted(true); // Mark level as completed to prevent further updates
		} else if (userHasReachedKillTarget()) {
			goToNextLevel(NEXT_LEVEL);
			System.out.println("Next level: " + NEXT_LEVEL);
			setLevelCompleted(true); // Mark level as completed to prevent further updates
		}
	}


	@Override
	protected void initializeFriendlyUnits() {
		getRoot().getChildren().add(getUser());
	}

	@Override
	protected void spawnEnemyUnits() {
		int currentNumberOfEnemies = getCurrentNumberOfEnemies();
		for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
			if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
				double newEnemyInitialYPosition = Math.random() * getEnemyMaximumYPosition();
				ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
				addEnemyUnit(newEnemy);
			}
		}
	}

	@Override
	protected int getLevelNumber() {
		return 1; // Level 1
	}

	@Override
	protected LevelView instantiateLevelView() {
		return new LevelView(getRoot(), PLAYER_INITIAL_HEALTH, getLevelNumber());
	}

	private boolean userHasReachedKillTarget() {
		return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
	}

}
