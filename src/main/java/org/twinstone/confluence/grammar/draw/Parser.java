package org.twinstone.confluence.grammar.draw;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

	private String data;
	private int pos;
	private List<Line> lines;
	private Map<String, IdentifiedBox> boxes;
	private ParentBox root;
	
	public void parse(String str) throws ParseException {
		boxes = new HashMap<String, IdentifiedBox>();
		lines = new ArrayList<Line>();
		pos = 0;
		data = str;
		ch();
		root = parseBox(a());
		ch();
		root.collect(boxes);
		while (pos<data.length()) {
			parseLine();
			ch();
		}
	}
	
	private void parseLine() throws ParseException {
		String f = ident();
		Box b = boxes.get(f);
		if (b==null) throw new ParseException("No such box: "+f, pos); 
		ch();
		boolean dir;
		char d;
		switch (d=a()) {
			case '>': dir = true;break;
			case '<': dir = false;break;
			default: throw new ParseException("Invalid line direction: "+d, pos);
		}
		ch();
		String t = ident();
		Box c = boxes.get(t);
		if (c==null) throw new ParseException("No such box: "+t, pos);
		ch();
		int around = 0;
		while (data.length()>pos && "+-".indexOf(data.charAt(pos))>=0) {
			if (a()=='-') around--;
			else around++;
		}
		Line l = new Line();
		l.direction = dir;
		l.from = b;
		l.to = c;
		l.around = around;
		lines.add(l);
	}

	private ParentBox parseBox(char type) throws ParseException {
		boolean or;
		switch (type) {
			case 'v': or = true; break;
			case 'h': or = false; break;
			default: throw new ParseException("Box type required", pos);
		}
		ch();
		String al = null;
		if (data.charAt(pos)=='~') {
			a();
			ch();
			al = ident();
			ch();
		}
		match("{");
		ch();
		ParentBox ret = new ParentBox();
		ret.align = al;
		ret.orientation = or;
		while (data.charAt(pos)!='}') {
			Box box;
			char a = a();
			switch (a) {
				case 'd': box = parseDiamondOrEmpty(true); break;
				case 't': box = parseSymbol(false, false); break;
				case 'k': box = parseSymbol(false, true); break;
				case 'n': box = parseSymbol(true, false); break;
				case 'e': box = parseDiamondOrEmpty(false); break;
				case 'v': box = parseBox('v'); break;
				case 'h': box = parseBox('h'); break;
				default: throw new ParseException("Box type required: "+a, pos);
			}
			ret.children.add(box);
			ch();
		}
		a();
		return ret;
	}
	
	private Box parseSymbol(boolean nonterm, boolean keyword) throws ParseException {
		match(":");
		ch();
		String name = identOrString();
		String alias = name;
		ch();
		if (data.charAt(pos)=='=') {
			pos++;
			ch();
			alias = ident();
		}
		TextBox box = new TextBox();
		box.alias = alias;
		box.name = name;
		box.bold = keyword;
		box.rounded = !nonterm;
		ch();
		if (data.charAt(pos)=='/') {
			a();
			ch();
			box.type = identOrString();
		}
		return box;
	}

	private Box parseDiamondOrEmpty(boolean diamond) throws ParseException {
		match(":");
		ch();
		IdentifiedBox box = diamond ? new DiamondBox() : new InvisibleBox();
		String alias = ident();
		box.alias = alias;
		return box;
	}

	private String identOrString() throws ParseException {
		ch();
		char a = a();
		if (a=='"') {
			StringBuilder bld = new StringBuilder();
			a = a();
			do {
				bld.append(a);
				a = a();
				if (a=='\\') a = a(); 
			} while (a!='"');
			ch();
			return bld.toString();
		}
		return identTail(a);
	}

	private String ident() throws ParseException {
		ch();
		return identTail(a());
	}
	
	private String identTail(char a) throws ParseException {
		StringBuilder bld = new StringBuilder();
		if (!Character.isJavaIdentifierStart(a)) throw new ParseException("Invalid identifier", pos);
		bld.append(a);
		while (pos<data.length() && Character.isJavaIdentifierPart((a=data.charAt(pos)))) {
			bld.append(a);
			pos++;
		}
		return bld.toString();
	}

	private char a() {
		return data.charAt(pos++);
	}
	
	void match(String allowed) throws ParseException {
		if (allowed.indexOf(a())<0) throw new ParseException("Any of ["+allowed+"] is allowe here", pos);
	}
	private void ch() {
		while (pos<data.length() && Character.isWhitespace(data.charAt(pos))) pos++;
	}

	public Box getRoot() {
		return root;
	}
	
	public List<Line> getLines() {
		return lines;
	}

	public Map<String, IdentifiedBox> getBoxes() {
		return boxes;
	}
	
}
