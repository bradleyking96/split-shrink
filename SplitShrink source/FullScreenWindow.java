package edu.ncssm.splitshrink;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class FullScreenWindow extends JFrame implements KeyListener, MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4812132950709577213L;
	GraphicsEnvironment env;
	GraphicsDevice device;
	
	public GraphicsDevice getDevice(){
		return device;
	}
	
	public void renderingLoop(Level level){
	    do {
	    	do {
    	    	Graphics2D g = (Graphics2D) getBufferStrategy().getDrawGraphics();
    	    	g.setRenderingHint(
    	    			RenderingHints.KEY_ANTIALIASING,
    	    			RenderingHints.VALUE_ANTIALIAS_ON);
    	    	level.render(g);
    	        g.dispose();
	        } while (getBufferStrategy().contentsRestored());
	    	getBufferStrategy().show();
	    } while (getBufferStrategy().contentsLost());
	}
	
	public void renderingLoop(TextScreen textScreen){
	    do {
	    	do {
    	    	Graphics2D g = (Graphics2D) getBufferStrategy().getDrawGraphics();
    	    	textScreen.draw(g);
    	        g.dispose();
	        } while (getBufferStrategy().contentsRestored());
	    	getBufferStrategy().show();
	    } while (getBufferStrategy().contentsLost());
	}
	
	public FullScreenWindow(){
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();
		if(GraphicsEnvironment.isHeadless() || !device.isFullScreenSupported()){
			System.exit(0);
		}
		this.addKeyListener(this);
		this.addMouseListener(this);
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
        setResizable(false);
        setIgnoreRepaint(true);
        device.setFullScreenWindow(this);
		// Creating blank cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new java.awt.Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);
        setVisible(true);
		
		createBufferStrategy(2);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code < 256){
			if(Resource.getKeyState(code) == KeyState.UP){
				Resource.setKeyState(code, KeyState.ONCE);
			}else if(Resource.getKeyState(code) == KeyState.ONCE){
				Resource.setKeyState(code, KeyState.DOWN);
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code < 256){
			Resource.setKeyState(code, KeyState.UP);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//do nothing
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		int num = e.getButton() - 1;
		if((num == 2 && Resource.getMouseState(0) == KeyState.ONCE)
				||(num == 0 && Resource.getMouseState(2) == KeyState.ONCE)){
			Resource.setMouseState(num, KeyState.DOWN);
		}else{
			Resource.setMouseState(num, KeyState.ONCE);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int num = e.getButton() - 1;
		if(Resource.getMouseState(2 - num) == KeyState.DOWN){
			Resource.setMouseState(2 - num, KeyState.ONCE);
		}
		Resource.setMouseState(num, KeyState.UP);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// nothing to do with this
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// This might be useful
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// This might be useful
		
	}
}
