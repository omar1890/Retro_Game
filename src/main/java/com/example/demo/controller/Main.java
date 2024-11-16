package com.example.demo.controller;

import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application { 
	/* 
		entry point for JavaFX applications
		It is an abstract class that needs to be extended in order to create a JavaFX application. 
		It provides the lifecycle methods necessary to launch a JavaFX GUI application. 
	*/
	private static final int SCREEN_WIDTH = 1300;
	private static final int SCREEN_HEIGHT = 750;
	private static final String TITLE = "Sky Battle";
	private Controller myController;

	// represents the top-level container or window in a JavaFX application.
	@Override
	public void start(Stage stage) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		stage.setTitle(TITLE);
		stage.setResizable(false);
		stage.setHeight(SCREEN_HEIGHT);
		stage.setWidth(SCREEN_WIDTH);
		myController = new Controller(stage);
		myController.launchGame();
	}

	public static void main(String[] args) {
		launch(); 
		/* This method is a static method provided by the Application class to start the JavaFX application. 
		It takes care of setting up the JavaFX runtime environment and then calls the start() method. */
	}
}