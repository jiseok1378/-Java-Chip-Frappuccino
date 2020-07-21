package customer.frame.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;


/**
 * JTabbedPane UI 설정을 위한 클래스
 */
public class JBTabbedPaneUI extends BasicTabbedPaneUI {
	
	private JTabbedPane pTabbed;
	
	public JBTabbedPaneUI(JTabbedPane _pTabbed) {
		pTabbed = _pTabbed;
	}

	/*
	 * 탭 디자인 & 탭 선택 디자인 관련 설정 
	 */
	@Override 
	protected void paintTabBorder(
			Graphics g, int tabPlacement, int tabIndex,
			int x, int y, int w, int h, boolean isSelected) 
	{
		// Do not paint anything
			    	  
		g.setColor(Color.white);
		g.drawRoundRect(x, y, w, h, 5, 5);
		
		// 탭 선택 안됐을 때 디자인 처리
		pTabbed.setBackgroundAt(tabIndex, Color.DARK_GRAY);
		pTabbed.getTabComponentAt(tabIndex).setForeground(Color.WHITE);
		
		// 탭 선택 됐을 때 디자인
		if (isSelected)
		{
			g.setColor(Color.white);
			g.fillRect(x, y, w, h);				
			pTabbed.getTabComponentAt(tabIndex).setForeground(Color.DARK_GRAY);
		}
	}
	
	/*
	 * 탭 및 콘텐츠 Insets 관련 설정 
	 */
	@Override
	protected Insets getSelectedTabPadInsets (int tabPlacement) { return new Insets(0, 0, 0, 0); }
	
	@Override
	protected Insets getTabAreaInsets (int tabPlacement) { return new Insets(0, 0, 0, 0); }
	
	@Override
	protected Insets getTabInsets (int tabPlacement, int tabIndex) { return new Insets(0, 0, 0, 0); }
	
	@Override
    protected Insets getContentBorderInsets(int tabPlacement) { return new Insets(0, 0, 0, 0); }
}
