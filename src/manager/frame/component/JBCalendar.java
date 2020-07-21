package manager.frame.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import manager.Manager;
import manager.frame.model.JBDefaultTableModel;

@SuppressWarnings("serial")
public class JBCalendar extends JFrame implements ActionListener, MouseListener {
	
	private Calendar cal = new GregorianCalendar();
	private JButton[] bSelect;
	protected JLabel lTitle;
	
	private JBDefaultTableModel mTable;
	protected JTable tCal;
	
	/**
	 * 캘린더 프레임 생성자
	 * @param _title
	 * @param _p
	 * @param _width
	 * @param _height
	 */
	public JBCalendar(String _title, Point _p, int _width, int _height) 
	{
		setLayout(new BorderLayout());
		
		// 아이콘 설정
		setIconImage(Toolkit.getDefaultToolkit().getImage(Manager.pIcon));
		
		// 기본사항 설정
		setTitle(_title);
		setLocation(_p);
		setSize(_width, _height);
		
		// 사이즈 조절 불가 & 창 닫기시 숨김처리
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		// 헤더 패널 등록
		setNorthPanel();
		
		// 테이블 패널 등록
		setSouthPanel();
		
		// 화면 표시
		setVisible(true);
	}
	
	// 달력 헤더 패널 등록
	private void setNorthPanel() {
		
		JPanel pNorth = new JPanel(new BorderLayout());
		
		// 내부 달력 타이틀 라벨
		lTitle = new JLabel("");
		lTitle.setHorizontalAlignment(JLabel.CENTER);
		
		lTitle.setOpaque(true);
		lTitle.setBackground(Color.DARK_GRAY);
		lTitle.setForeground(Color.WHITE);
		
		// 이전 달, 다음 달 선택 버튼
		bSelect = new JButton[2];
		for (int i = 0; i < bSelect.length; i++) {
			bSelect[i] = new JButton((i == 0) ? "  ◀   " : "   ▶  ");
			bSelect[i].addActionListener(this);
							
			bSelect[i].setOpaque(true);
			bSelect[i].setFocusable(false);
			bSelect[i].setBackground(Color.WHITE);
			bSelect[i].setForeground(Color.DARK_GRAY);
			bSelect[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		}
		
		// 컴포넌트 등록
		pNorth.add(bSelect[0], BorderLayout.WEST);
		pNorth.add(lTitle, BorderLayout.CENTER);
		pNorth.add(bSelect[1], BorderLayout.EAST);
		
		// 디자인 설정
		pNorth.setOpaque(true);
		pNorth.setBackground(Color.DARK_GRAY);
		pNorth.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		pNorth.setPreferredSize(new Dimension(getWidth(), getHeight() / 9 - 1));
				
		add(pNorth, BorderLayout.NORTH);
	}

	// 달력 패널 등록
	private void setSouthPanel() {
		
		JPanel pSouth = new JPanel(new BorderLayout());
		
		// 테이블 설정
		String [] header = { "일" ,"월", "화", "수", "목", "금", "토" };
		mTable = new JBDefaultTableModel(null, header);
		tCal = new JTable(mTable);
		
		// 달력 정보 업데이트
		updateMonth();
		
		// 테이블 헤더 순서 및 사이즈 편집 불가 설정
		tCal.getTableHeader().setResizingAllowed(false);
		tCal.getTableHeader().setReorderingAllowed(false);
		
		// 테이블 헤더 디자인 설정
		tCal.getTableHeader().setDefaultRenderer(new JBTableCellRenderer());
		
		// 컬럼 선택 불가 설정
		tCal.setColumnSelectionAllowed(true);
						
		// 테이블 셀 정렬 설정
		for (int _h = 0; _h < header.length; _h++)
			tCal.getColumn(header[_h]).setCellRenderer(new JBTableCellRenderer(JBTableCellRenderer.CENTER));
				
		// 테이블 컬럼 사이즈 자동 조절
		TableColumnModel mColumn = tCal.getColumnModel(); 
		for (int c = 0; c < tCal.getColumnCount(); c++) {
			mColumn.getColumn(c).setPreferredWidth(JLabel.CENTER); 
		}
				
		// 테이블 컬럼 디자인 설정
		tCal.setOpaque(true);
		tCal.setBackground(Color.DARK_GRAY);
		tCal.setForeground(Color.WHITE);
		tCal.setShowGrid(false);
				
		// 테이블 선택 모드 설정 & 마우스 이벤트 리스너 등록
		tCal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tCal.addMouseListener(this);
		
		
		// 스크롤 패널 정의
		JScrollPane spCal = new JScrollPane(
			tCal, 
			JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		
		// 스크롤 디자인 설정
		spCal.setOpaque(true);
		spCal.setBackground(Color.WHITE);
		spCal.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
				
		// 스크롤 숨김 상태에서 스크롤 가능하도록 처리
		spCal.setVerticalScrollBar(new JBScrollBar(JScrollBar.VERTICAL));
				
		// 스크롤 배경 설정
		spCal.getViewport().setBackground(Color.DARK_GRAY);
		
		// 컴포넌트 등록
		pSouth.add(spCal, BorderLayout.CENTER);
		
		// 디자인 설정
		pSouth.setOpaque(true);
		pSouth.setBackground(Color.DARK_GRAY);
		pSouth.setPreferredSize(new Dimension(getWidth(), getHeight() / 9 * 7));
				
		// 패널 등록
		add(pSouth, BorderLayout.SOUTH);
	}
	
	// 달력 업데이트 함수
	private void updateMonth() {
		
		// 달력 및 헤더 설정
		cal.set(Calendar.DAY_OF_MONTH, 1);
		lTitle.setText(cal.get(Calendar.YEAR) + "년 " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.KOREA));
		
		// 행 초기화 후 설정
		mTable.setRowCount(0);
		mTable.setRowCount(cal.getActualMaximum(Calendar.WEEK_OF_MONTH));

		// 셀 정보 설정
		int _w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		for(int _d = 1; _d <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); _d++){
			mTable.setValueAt(_d, _w / 7 , _w % 7);
			_w++;
		}

	}

	/**
	 * 액션 리스너 이벤트 영역
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		// 이전 달 버튼 클릭
		if (e.getSource().equals(bSelect[0])) {
			cal.add(Calendar.MONTH, -1);
			updateMonth();
		}
		
		// 다음 달 버튼 클릭
		if (e.getSource().equals(bSelect[1])) {
			cal.add(Calendar.MONTH, 1);
			updateMonth();
		}
		
	}

	/**
	 * 마우스 리스너 이벤트 영역
	 */
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
}
