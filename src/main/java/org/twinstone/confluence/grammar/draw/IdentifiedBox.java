package org.twinstone.confluence.grammar.draw;

import java.util.Map;

public abstract class IdentifiedBox extends Box {

	public String alias;
	
	@Override
	public void collect(Map<String, IdentifiedBox> boxes) {
		if (boxes.containsKey(alias)) throw new IllegalStateException("Duplicated box alias: "+alias);
		boxes.put(alias, this);
	}

}
