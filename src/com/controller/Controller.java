package com.controller;

import com.java.TicGame;
import com.service.Client;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public abstract class Controller {
	
	@FXML protected Canvas canvas;
	protected TicGame ticGame;
	protected int playerID;
	protected boolean playable;
	private double deltaX, deltaY;
	
	public Controller(){
		playable = false;
	}
	
	public abstract void setClient(Client client);
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	@FXML
	private void handlePress(MouseEvent e) {
		setDelta(e.getScreenX(), e.getScreenY());
	}
	
	@FXML
	private void handleDrag(MouseEvent e) {
		dragStage(e.getScreenX(), e.getScreenY());
	}
	
	private void dragStage(double x, double y) {
		ticGame.dragStage(x - deltaX, y - deltaY);
		setDelta(x, y);
	}
	
	private void setDelta(double x, double y) {
		deltaX = x;
		deltaY = y;
	}
	
	public void setGame(TicGame ticGame) {
		this.ticGame = ticGame;
	}
	
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	public void setPlayable() {
		playable = true;
	}
}
