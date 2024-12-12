package com.example.demo;

import java.util.*;

public class BigBoss extends FighterPlane {

    private static final String IMAGE_NAME = "bossplane.png";
    private static final double INITIAL_X_POSITION = 1000.0;
    private static final double INITIAL_Y_POSITION = 400;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 75.0;
    private static final double BOSS_FIRE_RATE = .04;
    private static final double BOSS_SHIELD_PROBABILITY = .002;
    private static final int IMAGE_HEIGHT = 300;
    private static final int VELOCITY = 8;
    private static final int HEALTH = 25;
    private static final int MAX_FRAMES_WITH_SAME_MOVE = 10;
    private static final double X_POSITION_LEFT_BOUND = 200.0;
    private static final double X_POSITION_RIGHT_BOUND = 1000.0;
    private static final double Y_POSITION_UPPER_BOUND = -100.0;
    private static final double Y_POSITION_LOWER_BOUND = 475.0;
    private static final int MAX_FRAMES_WITH_SHIELD = 500;

    private int directionX;
    private int directionY;
    private int framesInCurrentMove;
    private boolean isShielded;
    private int framesWithShieldActivated;

    public BigBoss() {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, HEALTH);
        directionX = 1; // Initially moving right
        directionY = 1; // Initially moving down
        framesInCurrentMove = 0;
        framesWithShieldActivated = 0;
        isShielded = false;
        randomizeDirection();
    }

    @Override
    public void updatePosition() {
        framesInCurrentMove++;

        // Change direction randomly after a certain number of frames
        if (framesInCurrentMove >= MAX_FRAMES_WITH_SAME_MOVE) {
            randomizeDirection();
            framesInCurrentMove = 0;
        }

        // Move horizontally
        double nextX = getLayoutX() + getTranslateX() + directionX * VELOCITY;
        if (nextX < X_POSITION_LEFT_BOUND || nextX > X_POSITION_RIGHT_BOUND) {
            directionX *= -1; // Reverse direction if hitting horizontal boundaries
        }
        moveHorizontally(directionX * VELOCITY);

        // Move vertically
        double nextY = getLayoutY() + getTranslateY() + directionY * VELOCITY;
        if (nextY < Y_POSITION_UPPER_BOUND || nextY > Y_POSITION_LOWER_BOUND) {
            directionY *= -1; // Reverse direction if hitting vertical boundaries
        }
        moveVertically(directionY * VELOCITY);
    }

    @Override
    public void updateActor() {
        updatePosition();
        updateShield();
    }

    @Override
    public ActiveActorDestructible fireProjectile() {
        return bossFiresInCurrentFrame() ? new BigBossProjectile(getProjectileInitialPosition()) : null;
    }

    @Override
    public void takeDamage() {
        if (!isShielded) {
            super.takeDamage();
        }
    }

    private void randomizeDirection() {
        directionX = Math.random() < 0.5 ? -1 : 1;
        directionY = Math.random() < 0.5 ? -1 : 1;
    }

    private void updateShield() {
        if (isShielded) {
            framesWithShieldActivated++;
        } else if (shieldShouldBeActivated()) {
            activateShield();
        }

        if (shieldExhausted()) {
            deactivateShield();
        }
    }

    private boolean bossFiresInCurrentFrame() {
        return Math.random() < BOSS_FIRE_RATE;
    }

    private double getProjectileInitialPosition() {
        return getLayoutY() + getTranslateY() + PROJECTILE_Y_POSITION_OFFSET;
    }

    private boolean shieldShouldBeActivated() {
        return Math.random() < BOSS_SHIELD_PROBABILITY;
    }

    private boolean shieldExhausted() {
        return framesWithShieldActivated >= MAX_FRAMES_WITH_SHIELD;
    }

    private void activateShield() {
        isShielded = true;
    }

    private void deactivateShield() {
        isShielded = false;
        framesWithShieldActivated = 0;
    }
}
