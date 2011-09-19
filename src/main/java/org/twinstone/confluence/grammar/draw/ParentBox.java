package org.twinstone.confluence.grammar.draw;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParentBox extends Box {

	public boolean orientation; //true = vertical
	public List<Box> children = new ArrayList<Box>();
	public String align;
	
	@Override

	public void collect(Map<String, IdentifiedBox> boxes) {
		for (Box c : children) c.collect(boxes);
	}
	
	@Override
	public void measure(Graphics2D dc) {
		for (Box c : children) c.measure(dc);
		boolean first = true; 
		for (Box c : children) {
			add(c, first ? 0 : SPACE);
			max(c, 0);
			first = false;
		}
		for (Box c : children) {
			center(c, orientation ? w/2 : h/2);
		}
		float l = 0;
		for (Box c : children) {
			l = distrib(c, l, SPACE);
		}
	}
	
	public void adjust(float x, float y) {
		super.adjust(x, y);
		for (Box c : children) c.adjust(this.x,this.y);
	}

	@Override
	public void move(int dx, float dy) {
		super.move(dx, dy);
		for (Box c : children) c.move(dx,dy);
	}

	private float distrib(Box b, float l, float space) {
		if (!orientation) {
			b.x = l;
			return l+b.w+space;
		}
		b.y = l;
		return l+b.h+space;
	}

	private void center(Box b, float c) {
		if (orientation) b.x = c - b.w/2;
		else b.y = c - b.h/2;
	}

	void add(Box b, float space) {
		if (orientation) h+=b.h + space;
		else w+=b.w+space;
		
	}
	
	void max(Box b, float space) {
		if (orientation) w = Math.max(w, b.w+space);
		else h = Math.max(h, b.h+space);
	}

	
	@Override
	public void align(Map<String, IdentifiedBox> boxes, float px, float py, float pw, float ph) {
		for (Box c : children) c.align(boxes, x, y, w, h);
		if (align==null) return;
		if (orientation) return;  //TODO
		Box box = boxes.get(align);
		if (box==null) throw new IllegalStateException("Invalid align box: "+align);
		float ny = box.cy() - h/2;
		move(0,ny-y);
	}


	@Override
	public void draw(Graphics2D dc) {
		dc.setStroke(new BasicStroke(0));
		Rectangle2D r = new Rectangle2D.Float();
		r.setFrame(x, y, w, h);
		dc.draw(r);
		for (Box c : children) c.draw(dc);
	}

}
