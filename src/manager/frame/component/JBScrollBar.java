package manager.frame.component;

import javax.swing.JScrollBar;

/**
 * 스크롤 숨김 상태에서 스크롤을 할 수 있도록 처리하는 클래스
 */
@SuppressWarnings("serial")
public class JBScrollBar extends JScrollBar {
	
	public JBScrollBar(int _state) {
		super(_state);
	}
	
	@Override
	public boolean isVisible() {
		return true;
	}
}
