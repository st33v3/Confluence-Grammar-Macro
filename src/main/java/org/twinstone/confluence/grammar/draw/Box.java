
package org.twinstone.confluence.grammar.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Map;

public abstract class Box {
	public static float SPACE = 30;
	public static float RATIO = 12f/10f;
	public static float WSPACE = 4;
	public static float HSPACE = 4;
	public static float FRLINE = 5;
	
	public float x;
	public float y;
	public float w;
	public float h;
	public String alias;
	
	public abstract void collect(Map<String, IdentifiedBox> boxes);
	public abstract void measure(Graphics2D dc);
	public abstract void draw(Graphics2D dc);

	public void align(Map<String, IdentifiedBox> boxes, float px, float py, float pw, float ph) {
		/**/
	}
	
	public float defaultHeight(Graphics2D dc) {
		Font f = dc.getFont();
		Rectangle2D bounds = dc.getFontMetrics(f).getMaxCharBounds(dc);
		f = f.deriveFont(f.getSize()/RATIO);
		Rectangle2D sb = dc.getFontMetrics(f).getMaxCharBounds(dc);
		return (float) (sb.getHeight() + bounds.getHeight() + 2*HSPACE + FRLINE);
	}
	
	public void drawWithShadow(Graphics2D dc, Shape shape) {
		AffineTransform t = dc.getTransform();
		AffineTransform shift = new AffineTransform(t);
		shift.translate(5, 5);
		Color color = dc.getColor();
		dc.setColor(Color.LIGHT_GRAY);
		dc.setTransform(shift);
		dc.fill(shape);
		shift.translate(-2, -2);
		dc.setTransform(shift);
		dc.setColor(Color.GRAY);
		dc.fill(shape);
		dc.setTransform(t);
		dc.setColor(Color.WHITE);
		dc.fill(shape);
		dc.setColor(color);
		dc.draw(shape);
	}
	
	public void move(int dx, float dy) {
		x+=dx;
		y+=dy;
	}

	public void adjust(float x, float y) {
		this.x += x;
		this.y += y;
	}

	public float cx() {
		return x+w/2;
	}

	public float cy() {
		return y+h/2;
	}
	
	public float r() {
		return x+w;
	}

	public float b() {
		return y+h;
	}

}
