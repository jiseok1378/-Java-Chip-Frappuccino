package manager.frame.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * 테이블 헤더 및 셀 디자인 설정 클래스 선언
 */
@SuppressWarnings("serial")
public class JBTableCellRenderer extends DefaultTableCellRenderer {

	private int cAlign = -99;
	
	public JBTableCellRenderer() {}
	public JBTableCellRenderer(int _cAlign) {
		
		// 정렬 정보 설정
		cAlign = _cAlign;
		
	}
	
	// 테이블 셀 설정
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
        
		// 셀 클릭시 테두리 해제
     	setBorder(noFocusBorder);
		
        if (cAlign != -99) {
        	
        	// 중앙 정렬 & 우측 정렬 설정
        	setHorizontalAlignment(cAlign);
        	setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        	
        	return this;
        }
        
        // 헤더 컴포넌트 설정
        JComponent component = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
		((JLabel) component).setHorizontalAlignment(JLabel.CENTER);
        
        // 헤더 디자인 설정
     	component.setPreferredSize(new Dimension(component.getSize().width, 30));
     	component.setBorder(BorderFactory.createEmptyBorder());
     			
     	// 헤더 배경 색상 설정
     	component.setBackground(Color.WHITE);
     			
     	// 헤더 폰트 및 폰트 색상 설정
     	component.setFont(new Font("맑은 고딕", Font.BOLD, 12));
     	component.setForeground(Color.DARK_GRAY);
     	
     	return component;
	}
}
