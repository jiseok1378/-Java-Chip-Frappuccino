package manager.frame;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class BasicFrame extends JFrame {
	
	public static int width;
	public static int height;
	
	public BasicFrame(String _title, int _width, int _height) {
		
		setTitle(_title);
		
		width = _width;
		height = _height;
		
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
}
