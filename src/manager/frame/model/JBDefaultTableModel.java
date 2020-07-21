package manager.frame.model;

import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class JBDefaultTableModel extends DefaultTableModel {
	public JBDefaultTableModel(String[][] _columns, String[] _rows) {
		super(_columns, _rows);
	}
	
	@Override
    public boolean isCellEditable(int row, int column) {
       // 모든 셀 편집 불가로 설정
       return false;
    }
}
