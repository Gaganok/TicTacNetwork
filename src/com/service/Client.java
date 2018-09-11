package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;

import com.controller.Controller;
import com.controller.GameController;
import com.controller.MenuController;
import com.render.Renderer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Client implements Runnable{
	private GameController gameController;
	private MenuController menuController;
	private Socket connection;
	private PrintWriter printWriter;
	private String address;
	private int port;
	private int wait = 10; // 10 ms sleep between input requests
	public static final Object lock = new Object();

	public Client(){
		address = "127.0.0.1";
		port = 6565; 
	}

	private int getInput(Scanner scan) { 
		while(!scan.hasNext()) {
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		return scan.nextInt();
	}

	@Override
	public void run() {
		try {
			while(connection == null) {
				try {
					connection = new Socket(InetAddress.getByName(address), port);	
				} catch (IOException e) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				} 
			}
			printWriter = new PrintWriter(connection.getOutputStream(), true);
			Scanner scan = new Scanner(connection.getInputStream());

			menuController.setPlayerID(getInput(scan));
			getInput(scan); //skipping Input;
			menuController.setPlayable();
			
			
			
			synchronized (lock) {
			    try {
					lock.wait();
				} catch (InterruptedException e) {
				
				}
			}
			
			int gameState = 0;
			while(gameState == 0) {
				int pos = getInput(scan);
				gameState = getInput(scan);

				if(pos >= 0) 
					gameController.setEnemyTurn(pos);
				if (gameState == 0) 
					gameController.setPlayable();
				else if(gameState == 1) 
					gameController.endGame(new int[]{getInput(scan), getInput(scan)});


			}
		} catch (IOException e) {
			
		} finally {
			try {
				if(connection != null)
					connection.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	public void makeTurn(int pos) {
		printWriter.println(pos);
	}
	
	public void setController(GameController gameController) {
		this.gameController = gameController;
	}
	
	public void setController(MenuController menuController) {
		this.menuController = menuController;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void close() {
		if(connection != null)
			try {
				connection.close();
			} catch (IOException e) {
				System.out.println(e);
			}
	}

}