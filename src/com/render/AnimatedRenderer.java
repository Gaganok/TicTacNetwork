package com.render;

import java.util.ArrayList;
import java.util.List;

import com.service.Coordinate;
import com.service.Helper;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AnimatedRenderer extends Renderer{
	private int RECT_WIDTH = 1, RECT_HEIGHT = 1;
	private int speed;
	private boolean interrupted;

	public AnimatedRenderer(GraphicsContext gc, int WIDTH, int HEIGHT, int speed) {
		super(gc, WIDTH, HEIGHT);
		this.speed = speed;
		interrupted = false;
	}

	public AnimatedRenderer(GraphicsContext gc, int WIDTH, int HEIGHT) {
		this(gc, WIDTH, HEIGHT, 10); //Default Speed is here
	}

	@Override
	public void drawCrest(Coordinate cord, Color color) {
		drawAnimateLine(cord.x + OFFSET, cord.y + OFFSET, cord.x + CELL_WIDTH - OFFSET, cord.y + CELL_HEIGHT - OFFSET, color);
		drawAnimateLine(cord.x + OFFSET, cord.y + CELL_HEIGHT - OFFSET, cord.x + CELL_WIDTH - OFFSET, cord.y + OFFSET, color);
	}
	
	@Override
	public void clear() {
		init();
		interrupted = false;
		for(int i = 0; i < HEIGHT; i++) {
			if(!interrupted)
				drawAnimateLine(0, i, WIDTH, i, Color.WHITE);
			else
				break;
		}
	}

	private void drawAnimateLine(double x, double y, double x1, double y1, Color color) {
		final int LINE_DENCITY = 500; // Analogue of the speed, but can cause line tearing
		final double deltaX = (x1 - x) / LINE_DENCITY, deltaY = (y1 - y) / LINE_DENCITY;

		new AnimationTimer() {
			double currentX = x, currentY = y;
			int dencity = 0;
			@Override
			public void handle(long time) {
				if(dencity < LINE_DENCITY && !interrupted) {
					for(int i = 0; i < speed; i++) {
						gc.setStroke(color);
						gc.strokeOval(currentX, currentY, RECT_WIDTH, RECT_HEIGHT);
						currentX += deltaX;
						currentY += deltaY;
						dencity++;
					}
				}else
					stop();
			}
		}.start();
	}

	@Override
	public void drawCircle(Coordinate cord, Color color) {
		final int R = 117;
		
		cord.x += CELL_WIDTH / 2;
		cord.y += CELL_HEIGHT / 2;

		new AnimationTimer() {
			private double angle = 0;
			private double rad;
			@Override
			public void handle(long time) {
				if(angle <= 360 && !interrupted) {
					for(int i = 0; i < speed; i++) {
						rad = Math.toRadians(angle++);
						gc.setStroke(color);
						gc.strokeOval(Math.cos(rad) * R + cord.x, Math.sin(rad) * R + cord.y, RECT_WIDTH, RECT_HEIGHT);
					}
				}else
					stop();
			}
		}.start();
	}

	@Override
	public void drawLine(int[] pos) {
		Coordinate cord1 = convertToPixelCord(Helper.convertToCord(pos[0]));
		Coordinate cord2 = convertToPixelCord(Helper.convertToCord(pos[1]));

		gc.setLineWidth(15);

		double[] cords;
		if(cord1.x != cord2.x && cord1.y != cord2.y) 
			cords = cord1.x > 0 ? new double[] {WIDTH, 0, 0, HEIGHT}: new double[] {0, 0, WIDTH, HEIGHT};	
			else 
				cords = cord1.x != cord2.x ? 
						new double[] {cord1.x, cord1.y + CELL_HEIGHT / 2, WIDTH, cord1.y + CELL_HEIGHT / 2} :
							new double[] {cord1.x + CELL_WIDTH / 2, cord1.y , cord1.x + CELL_WIDTH / 2, HEIGHT};

						drawAnimateLine(cords[0], cords[1] , cords[2], cords[3], Color.web("purple", opacity));
	}

	@Override
	public void drawOval(Coordinate cord, Color color) {
		int ovalOffSet = 100 / 2; // 100 - default Oval Size
		gc.setFill(color);
		gc.fillOval(cord.x - ovalOffSet, cord.y - ovalOffSet, ovalOffSet, ovalOffSet);	
	}

	@Override
	public void drawBoard() {
		for(int i = 0; i <= SCALE; i++) {
			double	x = CELL_WIDTH * i;
			double	y = CELL_HEIGHT * i; 

			drawAnimateLine(x, 0, x , HEIGHT, Color.web(boardColor, opacity));
			drawAnimateLine(0, y, WIDTH , y, Color.web(boardColor, opacity));
		}
	}
	
	@Override
	public void setGraphicsContext(GraphicsContext gc) {
		interrupted = true;
		this.gc = gc;
	}
}
