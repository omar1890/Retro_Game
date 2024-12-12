package com.example.demo;

public class UserPlane extends FighterPlane {

	private static final String IMAGE_NAME = "userplane.png";
	private static final double Y_UPPER_BOUND = -40;
	private static final double Y_LOWER_BOUND = 600.0;

	private static final double X_LEFT_BOUND = 0; // Left boundary
    private static final double X_RIGHT_BOUND = 800.0; // Right boundary

	private static final double INITIAL_X_POSITION = 5.0;
	private static final double INITIAL_Y_POSITION = 300.0;
	private static final int IMAGE_HEIGHT = 100;
	private static final int VERTICAL_VELOCITY = 8;
	private static final int PROJECTILE_X_POSITION = 110;
	private static final int PROJECTILE_Y_POSITION_OFFSET = 20;
	private static final int VELOCITY = 8;
	private int velocityMultiplier;
	private int horizontalVelocityMultiplier;

	private int numberOfKills;

	public UserPlane(int initialHealth) {
		super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, INITIAL_Y_POSITION, initialHealth);
		velocityMultiplier = 0;
		horizontalVelocityMultiplier = 0;
	}
	
	// @Override
	// public void updatePosition() {
	// 	if (isMoving()) {
	// 		double initialTranslateY = getTranslateY();
	// 		this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
	// 		double newPosition = getLayoutY() + getTranslateY();
	// 		if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
	// 			this.setTranslateY(initialTranslateY);
	// 		}
	// 	}
	// }
	@Override
    public void updatePosition() {
        if (isMoving()) {
            double initialTranslateY = getTranslateY();
			this.moveVertically(VERTICAL_VELOCITY * velocityMultiplier);
			double newPosition = getLayoutY() + getTranslateY();
			if (newPosition < Y_UPPER_BOUND || newPosition > Y_LOWER_BOUND) {
				this.setTranslateY(initialTranslateY);
			}
        }

        if (isMovingHorizontally()) {
            double initialTranslateX = getTranslateX();
            this.moveHorizontally(VELOCITY * horizontalVelocityMultiplier);
            double newXPosition = getLayoutX() + getTranslateX();
            if (newXPosition < X_LEFT_BOUND || newXPosition > X_RIGHT_BOUND) {
                this.setTranslateX(initialTranslateX);
            }
        }
    }
	
	@Override
	public void updateActor() {
		updatePosition();
	}
	
	@Override
	public ActiveActorDestructible fireProjectile() {
		return new UserProjectile(getProjectileXPosition(PROJECTILE_X_POSITION), getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET));
	}

	private boolean isMoving() {
		return velocityMultiplier != 0;
	}

	private boolean isMovingHorizontally() {
        return horizontalVelocityMultiplier != 0;
    }

	public void moveUp() {
		velocityMultiplier = -1;
	}

	public void moveDown() {
		velocityMultiplier = 1;
	}

	public void moveLeft() {
        horizontalVelocityMultiplier = -1;
    }

    public void moveRight() {
        horizontalVelocityMultiplier = 1;
    }

	public void stopVerticalMovement() {
		velocityMultiplier = 0;
	}

	public void stopHorizontalMovement() {
        horizontalVelocityMultiplier = 0;
    }

	public int getNumberOfKills() {
		return numberOfKills;
	}

	public void incrementKillCount() {
		numberOfKills++;
	}

}
