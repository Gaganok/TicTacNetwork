package com.controller;

import com.java.TicGame;
import com.service.Client;
import com.service.Coordinate;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameController extends Controller{
	private Client client;
	private int[] board;

	public GameController() {
		super();
		init();
	}
	
	private void init() {
		board = new int[9];
		for(int i = 0; i < board.length; i++)
			board[i] = -1;
	}
	
	@FXML
	private void handleClick(MouseEvent e) {
		if(e.getButton().name().equals("SECONDARY")) 
			ticGame.exit();
		else if(playable){
			int pos = ticGame.getPos(new Coordinate((int)e.getSceneX(), (int)e.getSceneY()));
			if(board[pos] == -1) {
				board[pos] = 1;
				ticGame.turn(playerID, new Coordinate(e.getSceneX(), e.getSceneY()));
				client.makeTurn(pos);
				playable = false;
			}		
		}
	}
	
	@Override
	public void setClient(Client client) {
		client.setController(this);
		this.client = client;
		
		synchronized(client.lock) {
			client.lock.notifyAll();
		}
	}

	public void endGame(int[] pos) {
		ticGame.endGame(pos);
	}
	
	public void setEnemyTurn(int pos) {
		int enemy = (playerID + 1) % 2;
		board[pos] = enemy;
		ticGame.turn(enemy, pos);
	}
}
