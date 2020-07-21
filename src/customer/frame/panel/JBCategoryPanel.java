package customer.frame.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.simple.JSONObject;

import jdbc.oracle.customer.Items;

@SuppressWarnings("serial")
public class JBCategoryPanel extends JScrollPane {
	
	private Vector<JSONObject> lItem;
	private JPanel pItemList;
	
	public JBCategoryPanel(String _name, int _width, int _height) {
		
        // 오류 처리
        try {
        	
        	// 아이템 리스트를 가져온다.
			lItem = Items.getCategoryItemList(_name);
			pItemList = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
			pItemList.setPreferredSize(new Dimension(_width, (int) Math.round(_height * lItem.size() * 1.25)));
			
			// 아이템별 패널을 생성한 후 해당 패널을 리스트 패널에 담고 
			for (JSONObject _json : lItem) {
				pItemList.add(
					new JBItemPanel(
						_name,
						_json.get("품명").toString(), _json.get("단가").toString(),_json.get("상태").toString().equals("0"),
						_width,
						_height
					)
				);
			}
			
			// pItemList 디자인 설정
			pItemList.setOpaque(true);
			pItemList.setBackground(Color.DARK_GRAY);
			
			// ScrollPane 디자인 설정
	        setOpaque(true);
	        setBackground(Color.DARK_GRAY);
	        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        
	        // JScrollPane 스크롤 가능, 스크롤바 숨김 처리
	        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

	        // JScrollPane에 pItemList 컴포넌트를 등록
			setViewportView(pItemList);
		
		// 예외처리
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -2)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-2);
			};
		}
	}
}
