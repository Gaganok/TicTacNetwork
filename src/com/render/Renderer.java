package com.render;

import com.service.Coordinate;
import com.service.Helper;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Renderer {
	protected final String[] playersColor = {"00FFFF", "DC143C"};
	protected final String boardColor = "#181947";
	protected final double WIDTH, HEIGHT;
	protected final double LINE_WIDTH = 10, SCALE = 3, OFFSET = 15;
	protected final double CELL_WIDTH, CELL_HEIGHT;
	
	protected double opacity;

	protected GraphicsContext gc;

	public Renderer(GraphicsContext graphicsContext, double width, double height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		CELL_WIDTH = WIDTH / SCALE;
		CELL_HEIGHT = HEIGHT / SCALE;
		
		opacity = 1;

		gc = graphicsContext;
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
	}

	public abstract void drawOval(Coordinate cord, Color color);
	public abstract void drawCircle(Coordinate cord, Color color);
	public abstract void drawCrest(Coordinate cord, Color color);
	public abstract void drawLine(int[] pos);
	public abstract void drawBoard();
	//public abstract void clearDraw();
	
	public void turn(int player, Coordinate cord) {
		cord = convertToPixelCord(cord);
		
		Color color = Color.web(playersColor[player], opacity);
		
		if(player == 0)
			drawCrest(cord, color);
		else
			drawCircle(cord, color);
	}
	
	//Unused
	public void redrawBoard(int[] board) {
		clear();
		drawBoard();
		
		if(board != null)
			for(int i = 0; i < board.length; i++) 
				if(board[i] == 0) 
					drawCrest(Helper.convertToCord(i), Color.web(playersColor[0], opacity));
				else if(board[i] == 1) 
					drawCircle(Helper.convertToCord(i), Color.web(playersColor[1], opacity));
	}

	public void clear() {
		init();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
	}

	protected void init() {
		gc.setFill(Color.rgb(0, 0, 0, opacity));
		gc.setStroke(Color.rgb(0, 0, 0, opacity));
		gc.setLineWidth(LINE_WIDTH);
	}

	public Coordinate convertToPixelCord(Coordinate cord) {
		cord.x = (int)cord.x * CELL_WIDTH;
		cord.y = (int)cord.y * CELL_HEIGHT;
		return cord;
	}
	
	public Coordinate getBoardCord(Coordinate cord) {
		cord.x /= CELL_WIDTH;
		cord.y /= CELL_HEIGHT;
		return cord;
	}
	
	public int getPos(Coordinate cord) {
		int x = (int)(cord.x / CELL_WIDTH);
		int y = (int)(cord.y / CELL_HEIGHT);
		return x + y * (int)SCALE;
	}
	
	public void setOpacity(double opacity) {
		this.opacity = opacity;
	}
	
	public void setGraphicsContext(GraphicsContext gc) {
		this.gc = gc;
	}
}
