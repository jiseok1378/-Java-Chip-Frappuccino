package customer.frame.model;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class JBOrderDefaultTableModel extends DefaultTableModel {
	
	// 부모 클래스 생성자 초기화
	public JBOrderDefaultTableModel(String[][] _columns, String[] _rows) {
		super(_columns, _rows);
	}
	
	@Override
    public boolean isCellEditable(int row, int column) {
       // 모든 셀 편집 불가로 설정
       return false;
    }
}
