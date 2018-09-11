package com.java;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.model.Board;
import com.render.AnimatedRenderer;
import com.render.Renderer;
import com.render.StaticRenderer;
import com.service.Coordinate;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Server extends Application implements Runnable{
	private final int WIDTH = 640, HEIGHT = 480;
	private Renderer renderer;
	private Coordinate[] oval;
	private Thread serverThread;
	private ServerSocket server;
	private int port = 6565;
	private int wait = 10;

	private Board board;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void run() {
		int hostCount = 0, hostCap = 2; // Default values

		Socket[] connection = new Socket[hostCap];
		Scanner[] scan = new Scanner[hostCap];
		PrintWriter[] printWriter = new PrintWriter[hostCap];

		try {
			server = new ServerSocket(port, hostCap);

			for(; hostCount < hostCap; hostCount++) {
				Socket connect = server.accept();
				connection[hostCount] = connect;

				printWriter[hostCount] = new PrintWriter(connect.getOutputStream(), true); 
				scan[hostCount] = new Scanner(connect.getInputStream());

				printWriter[hostCount].println(hostCount);

				renderer.drawOval(oval[hostCount], Color.GREEN);
			}
			//Acknowledgment
			printWriter[0].println("0");
			printWriter[1].println("0");

			int pos = -1;
			while(hostCount > 0) {
				int player = board.getPlayer();
				int gameState = board.getGameState();

				printWriter[player].println(pos + " " + gameState);

				if(gameState > 0) {
					if(gameState == 1) {
						int[] winPos = board.getWinPos();
						printWriter[player].println(winPos[0] + " " + winPos[1]);
					} 				
					hostCount--;
					pos = -1;
				} else {
					pos = getInput(scan[player]);
					board.turn(pos);
				}	
				board.nextPlayer();
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				server.close();
				for(int i = 0; i < hostCap; i++) 
					if(connection[i] != null)
						connection[i].close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
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
	public void start(Stage primaryStage) throws Exception {
		Canvas canvas = new Canvas(640, 480);
		Pane root = new Pane(canvas);
		
		renderer = new AnimatedRenderer(canvas.getGraphicsContext2D(), WIDTH, HEIGHT);
		board = new Board();

		initOval();
		
		serverThread = new Thread(this);
		serverThread.setDaemon(true);
		serverThread.start();
		
		primaryStage.setOnCloseRequest(e -> close());
		
		primaryStage.setScene(new Scene(root, 640, 480));
		primaryStage.show();
	}
	
	private void close() {
		try {
			if(server != null)
				server.close();
		} catch (IOException e1) {
			System.out.println(e1);
		}
	}
	
	private void initOval() {
		oval = new Coordinate[]{new Coordinate(320, 180), new Coordinate(320, 300)};
		for(int i = 0; i < oval.length; i++)
			renderer.drawOval(oval[i], Color.RED);
	}

}
