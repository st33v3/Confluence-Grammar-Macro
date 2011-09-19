package org.twinstone.confluence.grammar.draw;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class DiamondBox extends IdentifiedBox {

	@Override
	public void measure(Graphics2D dc) {
		w = 8;
		h = defaultHeight(dc);
	}

	@Override
	public void draw(Graphics2D dc) {
		Path2D path = new Path2D.Float();
		path.moveTo(x, y+h/2);
		path.lineTo(x+w/2, y);
		dc.draw(path);
	}

}
