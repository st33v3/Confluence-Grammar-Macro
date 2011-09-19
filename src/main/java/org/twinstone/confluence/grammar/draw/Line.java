package org.twinstone.confluence.grammar.draw;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

public class Line {

	public static float OFFSET = 6;
	public static float DIAMETER = 12;

	public Box from;
	public Box to;
	public boolean direction;
	public int around;
	
	public void draw(Graphics2D dc) {
		dc.setStroke(new BasicStroke(1.5f));
		if (direction) {
		if (from.r()<to.x) {
				if (Math.abs(from.cy()-to.cy())<0.1) {
					if (around==0) dc.draw(new Line2D.Float(from.r(), from.cy(), to.x, to.cy()));
					else {
						float ly = from.b(); 
						int s = 1;
						int a = around;
						if (around<0) {s = -1; a = -a; ly = from.y;}
						ly += Box.SPACE/2*s;
						ly += from.h * (a-1)*s;
						Path2D p = new Path2D.Float();
						p.moveTo(from.r(), from.cy());
						p.lineTo(from.r()+OFFSET, from.cy());
						p.quadTo(from.r()+OFFSET+DIAMETER,from.cy(), from.r()+OFFSET+DIAMETER, from.cy() + s*DIAMETER);
						p.lineTo(from.r()+OFFSET+DIAMETER, ly-s*DIAMETER);
						p.quadTo(from.r()+OFFSET+DIAMETER,ly, from.r()+OFFSET+2*DIAMETER, ly);
						p.lineTo(to.x-OFFSET-2*DIAMETER, ly);
						p.quadTo(to.x-OFFSET-DIAMETER,ly, to.x-OFFSET-DIAMETER, ly-s*DIAMETER);
						p.lineTo(to.x-OFFSET-DIAMETER, to.cy()+s*DIAMETER);
						p.quadTo(to.x-OFFSET-DIAMETER,to.cy(), to.x-OFFSET, to.cy());
						p.lineTo(to.x, to.cy());
						dc.draw(p);
					}
				} else {
					Path2D p = new Path2D.Float();
					p.moveTo(from.r(), from.cy());
					p.lineTo(from.r()+OFFSET, from.cy());
					float s = Math.signum(to.cy()-from.cy());
					p.quadTo(from.r()+OFFSET+DIAMETER,from.cy(), from.r()+OFFSET+DIAMETER, from.cy() + s*DIAMETER);
					p.lineTo(from.r()+OFFSET+DIAMETER, to.cy()-s*DIAMETER);
					p.quadTo(from.r()+OFFSET+DIAMETER,to.cy(), from.r()+OFFSET+2*DIAMETER, to.cy());
					p.lineTo(to.x, to.cy());
					dc.draw(p);
				}
			} else {
				//full arc
				Path2D p = new Path2D.Float();
				p.moveTo(from.r(), from.cy());
				p.lineTo(from.r()+OFFSET, from.cy());
				float s = Math.signum(from.cy()-to.cy());
				p.quadTo(from.r()+OFFSET+DIAMETER, from.cy(), from.r()+OFFSET+DIAMETER, from.cy()-s*DIAMETER);
				p.lineTo(from.r()+OFFSET+DIAMETER, to.cy()+s*DIAMETER);
				p.quadTo(from.r()+OFFSET+DIAMETER, to.cy(), from.r()+OFFSET, to.cy());
				p.lineTo(to.r(), to.cy());
				dc.draw(p);
			}
		} else {
			if (from.x>to.r()) {
				if (Math.abs(from.cy()-to.cy())<0.1) {
					dc.draw(new Line2D.Float(from.x, from.cy(), to.r(), to.cy()));
				} else {
					Path2D p = new Path2D.Float();
					p.moveTo(from.x, from.cy());
					//p.lineTo(from.x-10, from.cy()-s2*10);
					p.lineTo(from.r()+10, to.cy());
					p.lineTo(to.x, to.cy());
					dc.draw(p);
				}
			} else {
				if (Math.abs(from.cy()-to.cy())<0.1) {
				//full arc
					float ly = from.b(); 
					int s = 1;
					int a = around;
					if (around<0) {s = -1; a = -a; ly = from.y;}
					ly += Box.SPACE/2*s;
					ly += from.h * (a-1)*s;
					Path2D p = new Path2D.Float();
					p.moveTo(from.x, from.cy());
					p.lineTo(from.x-OFFSET, from.cy());
					p.quadTo(from.x-OFFSET-DIAMETER, from.cy(), from.x-OFFSET-DIAMETER, from.cy()+s*DIAMETER);
					p.lineTo(from.x-OFFSET-DIAMETER, ly-s*DIAMETER);
					p.quadTo(from.x-OFFSET-DIAMETER, ly, from.x-OFFSET, ly);
					p.lineTo(to.r()+OFFSET, ly);
					p.quadTo(to.r()+OFFSET+DIAMETER, ly, to.r()+OFFSET+DIAMETER, ly-s*DIAMETER);
					p.lineTo(to.r()+OFFSET+DIAMETER, to.cy()+s*DIAMETER);
					p.quadTo(to.r()+OFFSET+DIAMETER, to.cy(), to.r()+OFFSET, to.cy());
					p.lineTo(to.r(), to.cy());
					dc.draw(p);
				} else {
					Path2D p = new Path2D.Float();
					p.moveTo(to.x, to.cy());
					float s = Math.signum(from.cy()-to.cy());
					p.lineTo(to.x-OFFSET, to.cy());
					p.quadTo(to.x-OFFSET-DIAMETER,to.cy(),to.x-OFFSET-DIAMETER, to.cy()+s*DIAMETER);
					p.lineTo(to.x-OFFSET-DIAMETER, from.cy()-s*DIAMETER);
					p.quadTo(to.x-OFFSET-DIAMETER,from.cy(),to.x-DIAMETER, from.cy());
					p.lineTo(from.x, from.cy());
					dc.draw(p);
				}
			}
			
		}
		
	}
}
