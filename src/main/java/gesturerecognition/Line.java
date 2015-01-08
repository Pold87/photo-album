package main.java.gesturerecognition;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Line extends Shape {

	public Line() {
		super();
		//System.out.println("Line created");
	}

	public Line(int x1, int y1, int x2, int y2) {
		super(x1, y1, x2, y2, null);
		//System.out.println("Line created");
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		sprite = new Line2D.Double(x1, y1, x2, y2);
		g.draw(sprite);
	}

}