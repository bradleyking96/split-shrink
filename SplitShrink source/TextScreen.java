package edu.ncssm.splitshrink;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

class TextScreen {

	private String text;
	private Font font;
	private Color textColor, bgColor;
	
	public TextScreen(String text, float textSize, Font font, Color textColor, Color bgColor){
		this.text = text;
		this.font = font.deriveFont(textSize);
		this.textColor = textColor;
		this.bgColor = bgColor;
	}
	
	public void draw(Graphics2D g){
		g.setBackground(bgColor);
		g.clearRect(0, 0, Resource.getScreenWidth(), Resource.getScreenHeight());
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();
		g.setColor(textColor);
		g.drawString(text,
				(Resource.getScreenWidth() - metrics.stringWidth(text))/2,
				(Resource.getScreenHeight() - metrics.getAscent())/2);
		Font smallFont = font.deriveFont(24f);
		g.setFont(smallFont);
		FontMetrics smallMetrics = g.getFontMetrics();
		String continueText = "click to continue";
		g.drawString(continueText,
				Resource.getScreenWidth() - smallMetrics.stringWidth(continueText) - 4,
				Resource.getScreenHeight() - 4);
	}

	public void go(FullScreenWindow fullscreen) {
		Resource.setMouseState(0, KeyState.UP);
		Resource.setMouseState(2, KeyState.UP);
		while(true){
			fullscreen.renderingLoop(this);
			if(Resource.getMouseState(0) == KeyState.ONCE){
				break;
			}
		}
		Resource.setMouseState(0, KeyState.UP);
		Resource.setMouseState(2, KeyState.UP);
	}
}
