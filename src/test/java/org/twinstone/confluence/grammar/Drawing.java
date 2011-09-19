package org.twinstone.confluence.grammar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.twinstone.confluence.grammar.draw.Box;
import org.twinstone.confluence.grammar.draw.IdentifiedBox;
import org.twinstone.confluence.grammar.draw.Line;
import org.twinstone.confluence.grammar.draw.Parser;

public class Drawing extends JFrame {

	private JPanel panel;
	private JTextArea text;
	private JButton button;
	private Box root;
	private boolean measured;
	private List<Line> lines;
	private Map<String, IdentifiedBox> boxes;
	
	public Drawing() {
		setLayout(new BorderLayout());
		panel = new JPanel(true) {
			@Override public void paintComponent(Graphics g) {
				if (root==null) return;
				Graphics2D g2 = (Graphics2D) g;
				g2.setFont(getFont().deriveFont((float) (getFont().getSize2D()*Math.sqrt(2))));
				Map rh = new HashMap();
				rh.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				rh.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.addRenderingHints(rh);
				g2.setFont(Font.decode("SansSerif-PLAIN-14"));
				if (!measured) {
					root.measure(g2);
					root.adjust(30, 30);
					root.align(boxes, root.x, root.y, root.w, root.h);
					measured = true;
				}
				root.draw(g2);
				if (lines!=null) {
					for (Line l : lines) l.draw(g2);
				}
				g2.drawString("Zdar", 20f, 20f);
			}
		};
		panel.setBackground(Color.WHITE);
		text = new JTextArea();
		button = new JButton();
		button.setText("Parse");
		//panel.setMinimumSize(new Dimension(100,100));
		//text.setMinimumSize(new Dimension(100,100));
		panel.setPreferredSize(new Dimension(300,300));
		text.setPreferredSize(new Dimension(300,100));
		add(panel, BorderLayout.NORTH);
		add(text, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parse();
			}
		});
	}
	protected void parse() {
		measured = false;
		String t = text.getText();
		Parser p = new Parser();
		try {
			p.parse(t);
		} catch (ParseException e) {
			root = null;
			lines = null;
			JOptionPane.showMessageDialog(this,e.getErrorOffset()+": "+e.getMessage());
		}
		root = p.getRoot();
		lines = p.getLines();
		boxes = p.getBoxes(); 
		panel.repaint();
	}
	public static void main(String[] args) {
		JFrame window = new Drawing();
		window.pack();
        window.setVisible(true);
		Graphics2D g = (Graphics2D) window.getGraphics();
		Font font = window.getFont();
		System.out.println(font);
		System.out.println(font.getSize2D()+"  ->  "+font.getStringBounds("W", g.getFontRenderContext()));
		System.out.println(g.getFontMetrics(font));
		System.out.println(g.getFontMetrics(font).getMaxCharBounds(g));
		//for (Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) System.out.println(f);;
	}
}
