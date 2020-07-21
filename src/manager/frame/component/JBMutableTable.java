package manager.frame.component;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.json.simple.JSONObject;

@SuppressWarnings("serial")
public class JBMutableTable extends JTable {
	
	private JBMutableDefaultTableModel mTable;
	private JScrollPane spTable;
	
	private JTable table;
	private String[] header;
	private String[][] contents;
	
	/**
	 * 릴레이션의 내포를 기반으로 테이블을 만드는 클래스
	 * @param Vector<JSONObject> _intension
	 * @param ListSelectionListener _listSelectionListener
	 * _intension 테이블로 표시할 릴레이션의 내포 정보
	 * _listSelectionListener 테이블의 행이 선택되었을 때 처리할 이벤트 처리 담당 클래스
	 */
	public JBMutableTable(Vector<JSONObject> _intension) throws Exception {
		
		// 헤더 배열 크기 설정
		header = new String[_intension.get(0).keySet().size() - 1];
		
		// 외연을 String[]로 변환
		for (int i = 0; i < header.length; i++) {
			header[i] = ((String[]) _intension.get(0).get("order"))[i];
		}

		// 내포를 String[][]로 변환
		contents = new String[_intension.size()][];
				
		if (!_intension.isEmpty()) {
			for (JSONObject _json : _intension) {
				String[] _tuple = new String[header.length];
								
				for (int i = 0; i < _tuple.length; i++) {
					_tuple[i] = (_json.get(header[i]) == null) ? null : _json.get(header[i]).toString();
				}
								
			contents[_intension.indexOf(_json)] = _tuple;
			}
		}
				
		// 모델 설정 및 모델이 적용된 테이블 생성
		mTable = new JBMutableDefaultTableModel(contents, header);
				
		// model이 적용된 테이블 생성
		table = new JTable(mTable);
		 			
		// 테이블 헤더 순서 편집 불가 설정
		table.getTableHeader().setReorderingAllowed(false);
		
		// 테이블 셀 정렬 설정
		for (String _h : header) {
			// 테이블 셀 설정
			table.getColumn(_h).setCellRenderer(
					new JBTableCellRenderer(DefaultTableCellRenderer.CENTER)
			);
		}
		
		// 테이블 헤더 디자인 설정
		table.getTableHeader().setDefaultRenderer(new JBTableCellRenderer());
				
		// 컬럼 선택 불가 설정
		table.setColumnSelectionAllowed(false);
		
		// 열 선택 불가 설정
		table.setRowSelectionAllowed((contents[0][0] != null));
				
		// 테이블 컬럼 디자인 설정
		table.setOpaque(true);
		table.setBackground(Color.DARK_GRAY);
		table.setForeground(Color.WHITE);
		table.setShowGrid(false);
				
		// 테이블 컬럼 사이즈 자동 조절
		resizeColumnWidth(table);
				
				
		/**
		 * 테이블 컬럼 이벤트 추가
		 */
		mTable.addTableModelListener(this);
				
		// 스크롤바 숨김처리
		spTable = new JScrollPane(
				table, 
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
				
		// 스크롤 디자인 설정
		spTable.setOpaque(true);
		spTable.setBackground(Color.WHITE);
		spTable.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
				
		// 스크롤 숨김 상태에서 스크롤 가능하도록 처리
		spTable.setVerticalScrollBar(new JBScrollBar(JScrollBar.VERTICAL));
				
		// 스크롤 배경 설정
		spTable.getViewport().setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * 이벤트 처리기를 등록하기 위한 함수
	 * @param _listSelectionListener
	 */
	public void addListSelectionListener(ListSelectionListener _listSelectionListener) {
		table.getSelectionModel().addListSelectionListener(_listSelectionListener);
	}
	
	/**
	 * 테이블이 붙여진 스크롤 테이블 반환
	 * @return JScrollPane
	 */
	public JScrollPane getScrollTable() {
		return spTable;
	}
	
	/**
	 * 테이블의 외연 반환
	 * @return String[]
	 */
	public String[] getHeader() {
		return header;
	}
	
	/**
	 * 테이블의 내포 반환
	 * @return String[][]
	 */
	public String[][] getContents() {
		return contents;
	}
	
	/**
	 * Column 내용이 모두 보여지도록 가로 사이즈를 자동 조절해주는 함수
	 * @param table
	 */
	private void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel(); 
		for (int column = 0; column < table.getColumnCount(); column++) {
			
			int width = 50;
			for (int row = 0; row < table.getRowCount(); row++) { 
				TableCellRenderer renderer = table.getCellRenderer(row, column); 
				Component comp = table.prepareRenderer(renderer, row, column); 
				width = Math.max(comp.getPreferredSize().width + 1 , width); 
			}
			
			columnModel.getColumn(column).setPreferredWidth(width); 
		} 
	}
	
	/**
	 * 테이블의 행을 갱신시키기 위하여 제작된 함수
	 * JBOrderPanel의 Thread로 자동갱신이 되는 테이블을 위하여 특별히 제작되었다.
	 * 해당 테이블을 제한 나머지 테이블에서는 해당 함수를 사용하지 말것.
	 * @param _intension
	 * @throws Exception
	 */
	public void addRowsAtNew(Vector<JSONObject> _intension) throws Exception {
		// TODO Auto-generated method stub
		
		// 릴레이션 차수 불일치시 오류
		if (_intension.get(0).keySet().size() - 1 != header.length) { throw new Exception("내포의 형식이 올바르지 않습니다."); }
				
		// 내포를 String[]로 변환
		for (int i = 0; i < header.length; i++) {
			if (!header[i].equals(((String[]) _intension.get(0).get("order"))[i]))
				throw new Exception("내포가 일치하지 않습니다.");
		}
		
		// 내포를 String[][]로 변환
		String[][] _c = new String[_intension.size()][];

		// 내포의 값을 비교		
		if (!_intension.isEmpty()) {
			
			for (JSONObject _json : _intension) {
				String[] _tuple = new String[header.length];
										
				for (int i = 0; i < _tuple.length; i++) {
					_tuple[i] = (_json.get(header[i]) == null) ? null : _json.get(header[i]).toString();
				}
										
				_c[_intension.indexOf(_json)] = _tuple;
			}
		}
		
		// 새로 받은 정보가 기존 정보보다 많거나, 해당 주문이 첫번째 주문인 경우에 정보를 넣어준다.
		if (_c.length > contents.length) {
			
			// 로우 값을 추가한다.
			for (int _r = _c.length - contents.length; _r > 0; _r--)
				mTable.insertRow(0, _c[_r - 1]);
			
			// 기존의 내포 값을 새로운 내포 값으로 대체한다.
			contents = _c;
			
			// 행 선택 설정
			table.setRowSelectionAllowed(true);
		}
	}
}
