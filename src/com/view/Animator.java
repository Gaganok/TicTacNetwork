package com.view;

import java.util.ArrayList;
import java.util.List;
import com.model.Board;
import com.render.Renderer;
import com.service.Helper;

public class Animator implements Runnable{
	private final int speed = 1500;
	private List<Integer> boardImage;
	private Board board;
	private Thread thread;
	private Renderer renderer;
	private boolean interrupted;

	public Animator(Renderer renderer){
		this.renderer = renderer;
		boardImage = new ArrayList<Integer>(9);
		board = new Board();
		thread = new Thread(this);
		thread.setDaemon(true);
		start();
	}

	@Override
	public void run() {
		try {
			refresh();
		} catch (InterruptedException e2) {
			interrupted = true;
		}
		while(!interrupted) {
			try {
				sleep();
			} catch (InterruptedException e1) {
				break;
			}
			int gameState = board.getGameState();
			if(gameState == 0) {
				int turn = boardImage.remove((int)(Math.random() * boardImage.size()));
				board.turn(turn);
				board.nextPlayer();
				
				if(Thread.interrupted())
					break;
				else
					renderer.turn(board.getPlayer(), Helper.convertToCord(turn));
				
			} else {
				if(gameState == 1) {
					renderer.drawLine(board.getWinPos());
					try {
						sleep();
					} catch (InterruptedException e) {
						break;
					}
				}
				try {
					refresh();
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		try {
			refreshCanvas();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	private void sleep() throws InterruptedException {
		Thread.sleep(speed);
	}

	private void refresh() throws InterruptedException {
		refreshCanvas();
		refreshBoard();
	}

	private void refreshBoard() {
		board.refresh();
		boardImage.clear();
		for(int i = 0; i < 9; i++)
			boardImage.add(i);
	}

	private void refreshCanvas() throws InterruptedException {
		renderer.clear();
		sleep();
		renderer.drawBoard();
	}

	public void start() {
		thread.start();
	}

	public void stop() {
		thread.interrupt();
	}
}
