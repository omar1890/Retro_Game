package com.example.demo;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpg";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final BigBoss boss;
    private LevelView levelView;

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH);
        boss = new BigBoss();
        System.out.println("New Boss initialized: " + boss);
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    @Override
    protected void checkIfGameOver() {
        if (isLevelCompleted()) {
            return; // Prevent repeated logic
        }

        if (userIsDestroyed()) {
            loseGame();
            setLevelCompleted(true);
            System.out.println("Game Over!");
        } else if (boss.isDestroyed()) {
            winGame();
            setLevelCompleted(true);
            System.out.println("You defeated the new boss! Well done!");
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
        return 3; // Level 3
    }

    @Override
    protected LevelView instantiateLevelView() {
        levelView = new LevelView(getRoot(), PLAYER_INITIAL_HEALTH, getLevelNumber());
        return levelView;
    }
}
