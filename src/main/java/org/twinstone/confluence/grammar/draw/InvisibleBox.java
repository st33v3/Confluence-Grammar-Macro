package org.twinstone.confluence.grammar.draw;

import java.awt.Graphics2D;

public class InvisibleBox extends IdentifiedBox {

	@Override
	public void measure(Graphics2D dc) {
		w = 0;
		h = defaultHeight(dc);
	}

	@Override
	public void draw(Graphics2D dc) {
		//
	}

}
