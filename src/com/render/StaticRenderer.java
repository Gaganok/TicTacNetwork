package com.render;

import com.service.Coordinate;
import com.service.Helper;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StaticRenderer extends Renderer{
	
	public StaticRenderer(GraphicsContext gc, int width, int height) {
		super(gc, width, height);
	}
	
	@Override
	public void drawBoard() {
		for(int i = 0; i <= SCALE; i++) {
			double x = CELL_WIDTH * i;
			double y = CELL_HEIGHT * i; 

			gc.strokeLine(x, 0, x , HEIGHT);
			gc.strokeLine(0, y, WIDTH , y);
		}	
	}
	
	@Override
	public void drawCrest(Coordinate cord, Color color) {
		gc.strokeLine(cord.x + OFFSET, cord.y + OFFSET, cord.x + CELL_WIDTH - OFFSET, cord.y + CELL_HEIGHT - OFFSET);
		gc.strokeLine(cord.x + OFFSET, cord.y + CELL_HEIGHT - OFFSET, cord.x + CELL_WIDTH - OFFSET, cord.y + OFFSET);
	}

	@Override
	public void drawCircle(Coordinate cord, Color color) {
		gc.strokeOval(cord.x + OFFSET, cord.y + OFFSET, CELL_WIDTH - OFFSET * 2, CELL_HEIGHT - OFFSET * 2);
	}

	@Override
	public void drawLine(int[] pos) {
		Coordinate cord1 = convertToPixelCord(Helper.convertToCord(pos[0]));
		Coordinate cord2 = convertToPixelCord(Helper.convertToCord(pos[1]));
		
		gc.setStroke(Color.BLUEVIOLET);

		if(cord1.x != cord2.x && cord1.y != cord2.y) 
			if(cord1.x > 0) 
				gc.strokeLine(WIDTH, 0, 0, HEIGHT);
			else 
				gc.strokeLine(0, 0, WIDTH, HEIGHT);

		else if (cord1.x != cord2.x) {
			double y = cord1.y + CELL_HEIGHT / 2;
			gc.strokeLine(cord1.x, y, WIDTH, y);
		} else {
			double x = cord1.x + (CELL_WIDTH / 2);
			gc.strokeLine(x, cord1.y , x, HEIGHT);
		}
	}
	
	@Override
	public void drawOval(Coordinate cord, Color color) {
		int ovalOffSet = 100 / 2; // 100 - default Oval Size
		gc.setFill(color);
		gc.fillOval(cord.x - ovalOffSet, cord.y - ovalOffSet, ovalOffSet, ovalOffSet);	
	}

/*	@Override
	public void drawBoard() {
		for(int i = 1; i < SCALE; i++) {
			int	x = CELL_WIDTH * i + (i - 1) * LINE_WIDTH;
			int	y = CELL_HEIGHT * i + (i - 1) * LINE_WIDTH; 

			gc.strokeLine(x, 0, x , HEIGHT);
			gc.strokeLine(0, y, WIDTH , y);
		}	

		drawBorders();
	}*/
}
