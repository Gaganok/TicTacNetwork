package com.controller;

import com.service.Client;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuController extends Controller{
	@FXML private Label connectionLabel;
	boolean playable;
	
	public MenuController(){
		super();
		init();
	}
	
	@FXML
	private void init() {
		playable = false;
	}
	
	@FXML
	private void handlePlay() {
		if(playable)
			ticGame.startGame();
	}
	
	@FXML
	private void handleSettings() {
		//Coming Soon
	}
	
	@FXML
	private void handleExit() {
		ticGame.exit();
	}
	
	
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
		Platform.runLater(() -> connectionLabel.setText("Waiting For Opponent.."));
	}
	
	@Override
	@FXML
	public void setPlayable() {
		playable = true;
		Platform.runLater(() -> connectionLabel.setText("Ready To Play"));
	}

	@Override
	public void setClient(Client client) {
		client.setController(this);
		Thread clientThread = new Thread(client);
		clientThread.setDaemon(true);
		clientThread.start();
	}
}
