package com.java;

import java.io.IOException;
import java.util.Optional;

import com.controller.Controller;
import com.controller.GameController;
import com.controller.MenuController;
import com.render.AnimatedRenderer;
import com.render.Renderer;
import com.render.StaticRenderer;
import com.service.Client;
import com.service.Coordinate;
import com.service.Helper;
import com.view.Animator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class TicGame extends Application{
	private long test = System.currentTimeMillis();
	private final int WIDTH = 1280, HEIGHT = 800, SCALE = 3;
	private Stage stage;
	private Controller controller;
	private Renderer renderer;
	private Client client;
	private FXMLLoader loader;
	private Animator animator;
	private boolean animated = true;

	public static void main(String[] args) {
		launch(args);	
	}

	@Override
	public void start(Stage primaryStage){
		client = new Client();
		client.setAddress("127.0.0.1");
		
		stage = primaryStage;
		setControllerScene("MenuFrame.fxml");
		
		renderer = animated ? new AnimatedRenderer(controller.getCanvas().getGraphicsContext2D(), WIDTH, HEIGHT): 
			new StaticRenderer(controller.getCanvas().getGraphicsContext2D(), WIDTH, HEIGHT);

		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.show();

		if(animated) 
			animator = new Animator(renderer);
		
		System.out.println(System.currentTimeMillis() - test);
	}
	private void setControllerScene(String fxml) {
		try {
			loader = new FXMLLoader(getClass().getResource(fxml));
			Pane pane = loader.load();
			controller = loader.getController();
			controller.setClient(client);
			controller.setGame(this);
			stage.setScene(new Scene(pane, WIDTH, HEIGHT));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
	}

	public void dragStage(double x, double y) {
		stage.setX(stage.getX() + x);
		stage.setY(stage.getY() + y);
	}

	public void exit() {
		stage.close();
	}

	public int getPos(Coordinate cord) {
		return renderer.getPos(cord);
	}

	public void turn(int playerID, Coordinate cord) {
		renderer.turn(playerID, renderer.getBoardCord(cord));
	}
	
	public void turn(int playerID, int pos) {
		renderer.turn(playerID, Helper.convertToCord(pos));
	}

	public void endGame(int[] pos) {
		renderer.drawLine(pos);
	}

	public void startGame() {
		setControllerScene("GameFrame.fxml");
		renderer.setGraphicsContext(controller.getCanvas().getGraphicsContext2D());
		if(animated)
			animator.stop();
		
	}
}