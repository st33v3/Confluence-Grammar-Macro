package org.twinstone.confluence.grammar.draw;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class TextBox extends IdentifiedBox {
	public String name;
	public String type;
	public boolean rounded;
	boolean bold;
	
	@Override
	public void measure(Graphics2D dc) {
		h = defaultHeight(dc);
		Font f = dc.getFont();
		if (bold) f = f.deriveFont(Font.BOLD);
		Rectangle2D bounds = f.getStringBounds(name, dc.getFontRenderContext());
		w = (float) bounds.getWidth();
		if (!bold & type!=null) {
			float size = (float) (dc.getFont().getSize2D() / RATIO);
			f = f.deriveFont(size);
			bounds = f.getStringBounds(type, dc.getFontRenderContext());
			w = (float) Math.max(w, bounds.getWidth());
		}
		w += 2*WSPACE;
		if (rounded && type!=null) w+=10;
		w = Math.max(w, SPACE);
	}

	@Override
	public void draw(Graphics2D dc) {
		dc.setStroke(new BasicStroke(2));
		Shape shape;
		if (rounded) shape =  new RoundRectangle2D.Float(x, y, w, h,40,30);
		else shape =  new Rectangle2D.Float(x, y, w, h);
		drawWithShadow(dc, shape);
		Font old = dc.getFont();
		Font f = old;
		if (bold) f = f.deriveFont(Font.BOLD);
		dc.setFont(f);
		Rectangle2D bounds = f.getStringBounds(name, dc.getFontRenderContext());
		if (type==null) {
			float ty = (float) (cy() - bounds.getHeight() / 2 - bounds.getY());
			float tx = (float) (cx() - bounds.getWidth() / 2);
			dc.drawString(name, tx, ty);
		} else {
			float tx = (float) (cx() - bounds.getWidth() / 2);
			float ty = y + (float) (HSPACE - bounds.getY());
			dc.drawString(name, tx, ty);
			dc.setStroke(new BasicStroke(1));
			ty = y+(float)bounds.getHeight()+FRLINE/2 + HSPACE;
			dc.draw(new Line2D.Float(x+WSPACE,ty,r()-WSPACE,ty));
			f = f.deriveFont(f.getSize2D() / RATIO);
			dc.setFont(f);
			bounds = f.getStringBounds(type, dc.getFontRenderContext());
			tx = (float) (cx() - bounds.getWidth() / 2);
			ty = ty + FRLINE/2;
			ty = (float) (ty - bounds.getY());
			dc.drawString(type, tx, ty);
		}
		dc.setFont(old);
	}
}
