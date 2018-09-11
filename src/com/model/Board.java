package com.model;

import com.service.Helper;

public class Board {
	private final int SCALE = 3;
	private int[] board, winPos;
	private int gameState; //GameStates: 0 = OnGoing Session, 1 = SomeOne Win, 2 = Tie
	private int currentPlayer, turns;
	
	
	public Board(){
		board = new int[9];
		refresh();
	}
	
	public void refresh() {
		gameState = 0;
		turns = 0;
		winPos = null;
		currentPlayer = (int) (Math.random() * 2);
		for(int i = 0; i < board.length; i++)
			board[i] = -1;	
	}
	
	private int[] getWinner() {
		for(int i = 0 ; i < SCALE; i++) {
			int x = i * SCALE, y = i;
			if(board[x] == board[x + 1] && board[x] == board[x + 2] && board[x] != -1) 
				return new int[]{Helper.convertToPos(0, y), Helper.convertToPos(2, y)};
			else if(board[y] == board[y + SCALE] && board[y] == board[y + SCALE * 2] && board[y] != -1) 
				return new int[]{Helper.convertToPos(i, 0), Helper.convertToPos(i, 2)};
		}
		
		if(board[0] == board[4] && board[0] == board[8] && board[0] != -1)
			return new int[]{0, 8};
		else if(board[2] == board[4] && board[2] == board[6] && board[2] != -1)
			return new int[]{2, 6};
		
		return null;
	}
	
	public void turn(int pos) {
		board[pos] = currentPlayer;
		winPos = getWinner();

		if(winPos != null) 
			gameState = 1;
		else if(++turns == 9)
			gameState = 2;
	}
	
	public void nextPlayer() {
		currentPlayer = ++currentPlayer % 2;
	}
	
	public int getLength() {
		return board.length;
	}

	public int getPlayer() {
		return currentPlayer;
	}

	public int getGameState() {
		return gameState;
	}

	public int[] getWinPos() {
		return winPos;
	}
	
	
}
