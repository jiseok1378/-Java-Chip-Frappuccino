package manager.frame.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import manager.frame.BasicFrame;
import manager.frame.component.JBCalendar;
import manager.frame.component.JBMutableTable;
import jdbc.oracle.manager.Managers;

@SuppressWarnings("serial")
public class JBOrderedPanel extends JPanel implements ActionListener {
	
	// 달력 및 날짜 처리를 위한 변수
	private SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
	private GregorianCalendar today = new GregorianCalendar();
	
	private JBCalendar[] fCalendar = new JBCalendar[2];
	
	private JBMutableTable tOrderedList;
	
	private JLabel[] lblDate;
	private JButton[] btnDate;
	private JButton btnShow;
	
	private JPanel pSouth;
	
	public JBOrderedPanel() {
		
		// 패널 레이아웃 설정
		setLayout(new BorderLayout());
		
		// 패널 등록
		setNorthPanel();
		setSouthPanel();
		
		// 디자인 설정
		setOpaque(true);
		setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * 주문 내역을 보기 위한 시작 날짜 및 종료 날짜 선택 패널
	 */
	public void setNorthPanel() {
		
		// 레이아웃 설정
		JPanel pNorth = new JPanel(new GridLayout(1, 5));
		
		// 날짜 표기 레이블 정의
		lblDate = new JLabel[2];
		for (int i = 0; i < lblDate.length; i++) {
			lblDate[i] = new JLabel(format.format(today.getTime()));
			lblDate[i].setHorizontalAlignment(JLabel.CENTER);
			
			lblDate[i].setOpaque(true);
			lblDate[i].setBackground(Color.DARK_GRAY);
			lblDate[i].setForeground(Color.WHITE);
			lblDate[i].setFont(new Font("맑은 고딕", Font.BOLD, 12));
		}
		
		// 캘린더 열기 버튼 정의
		btnDate = new JButton[2];
		for (int i = 0; i < btnDate.length; i++) {
			btnDate[i] = new JButton((i == 0) ? "시작 날짜 선택" : "종료 날짜 선택");
			btnDate[i].addActionListener(this);
					
			btnDate[i].setOpaque(true);
			btnDate[i].setFocusable(false);
			btnDate[i].setBackground(Color.WHITE);
			btnDate[i].setForeground(Color.DARK_GRAY);
			btnDate[i].setFont(new Font("맑은 고딕", Font.BOLD, 12));
			btnDate[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		}
		
		// 조회버튼 정의
		btnShow = new JButton("조회");
		btnShow.addActionListener(this);
		
		btnShow.setOpaque(true);
		btnShow.setFocusable(false);
		btnShow.setBackground(Color.DARK_GRAY);
		btnShow.setForeground(Color.WHITE);
		btnShow.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		btnShow.setBorder(BorderFactory.createEmptyBorder());
		
		// 레이블 및 버튼 등록
		pNorth.add(lblDate[0]);
		pNorth.add(btnDate[0]);
		pNorth.add(lblDate[1]);
		pNorth.add(btnDate[1]);
		pNorth.add(btnShow);

		
		// 디자인 설정
		pNorth.setOpaque(true);
		pNorth.setBackground(Color.DARK_GRAY);
		pNorth.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		pNorth.setPreferredSize(new Dimension(BasicFrame.width, BasicFrame.height / 16));
		
		add(pNorth, BorderLayout.NORTH);
	}
	
	/**
	 * 특정 기간 동안의 주문 내역을 보여주는 테이블 등록
	 */
	public void setSouthPanel() {
		
		pSouth = new JPanel(new BorderLayout());
		
		// 오류 처리
		try {
			
			String[] _date = getToday();
			tOrderedList = new JBMutableTable(Managers.getOrderAtPeriod(_date[0], _date[1]));
			
			pSouth.add(tOrderedList.getScrollTable(), BorderLayout.CENTER);
		
		// 에러 처리
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -5)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-5);
			};
		}
		
		// 디자인 설정
		pSouth.setOpaque(true);
		pSouth.setBackground(Color.DARK_GRAY);
		pSouth.setPreferredSize(new Dimension(BasicFrame.width, BasicFrame.height / 16 * 12));
		
		// 패널 등록
		add(pSouth, BorderLayout.SOUTH);
	}
	
	/**
	 * 오늘 날짜 반환하는 함수
	 * @return String[]
	 */
	private String[] getToday() {
		SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar _cal = Calendar.getInstance();
		String[] _day = new String[2];
		
		_cal.setTime(today.getTime());
		_day[0] = _format.format(_cal.getTime());
		
		_cal.add(Calendar.DATE, 1);
		_day[1] = _format.format(_cal.getTime());
		
		return _day;
	}

	/**
	 * 액션 리스너 이벤트 핸들러
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		// 시작 날짜 선택 버튼
		if (e.getSource().equals(btnDate[0])) {
			fCalendar[0] = new JBOrderedCalendar("JavaBean - 시작 날짜 선택", MouseInfo.getPointerInfo().getLocation(), 400, 250, true);
		}
		
		// 종료 날짜 선택 버튼
		if (e.getSource().equals(btnDate[1])) {
			fCalendar[1] = new JBOrderedCalendar("JavaBean - 종료 날짜 선택", MouseInfo.getPointerInfo().getLocation(), 400, 250, false);	
		}
		
		// 조회 버튼
		if (e.getSource().equals(btnShow)) {
			SimpleDateFormat fDB = new SimpleDateFormat("yyyy-MM-dd");
			
			// 오류 처리
			try {
				
				Date _sDate = format.parse(lblDate[0].getText());
				Date _eDate = format.parse(lblDate[1].getText());
				
				
				// DB조회를 위해 마지막 날짜를 하루 더 증가시켜준다.
				Calendar _cal = Calendar.getInstance();
					
				_cal.setTime(_eDate);
				_cal.add(Calendar.DATE, 1);
					
				_eDate = _cal.getTime();
				
				
				// 테이블 새로고침
				Vector<JSONObject> _data = Managers.getOrderAtPeriod(
					fDB.format(_sDate), 
					fDB.format(_eDate)
				);
				tOrderedList = new JBMutableTable(_data);
				
				// 패널 초기화 후, 새로운 테이블 등록
				pSouth.removeAll();
				pSouth.add(tOrderedList.getScrollTable(), BorderLayout.CENTER);
				
				// 화면 갱신
				revalidate();
				
				
				// 조회결과가 존재하지 않는 경우, 메시지 표시
				if (_data.get(0).get("승인번호") == null) {
					JOptionPane.showConfirmDialog(null, 
							"해당 기간동안의 내역이 존재하지 않습니다.", 
							"JavaBean - 조회 결과",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				
				// 오류 발생시 확인창을 띄우고, 프로그램 종료
				if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -6)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
					System.exit(-6);
				};
			}
		}
	}
	
	// 용이한 처리를 위해 캘린더를 상속받아 재정의한다
	private class JBOrderedCalendar extends JBCalendar {
		
		boolean isStartDate;

		// 부모 클래스 초기화를 위한 생성자 구현
		public JBOrderedCalendar(String _title, Point _p, int _width, int _height, boolean _isStartDate) {
			super(_title, _p, _width, _height);
			// TODO Auto-generated constructor stub
			
			// 이벤트 처리 구분을 위한 설정
			isStartDate = _isStartDate;
		}
		
		/**
		 * 마우스 리스너 이벤트  영역
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
			// 셀 더블클릭 시 처리
			if (e.getClickCount() == 2) {
				
				// 오류 처리
				try {
					
					// 빈칸 클릭 시 아무 반응 없도록 처리
					if (tCal.getValueAt(tCal.getSelectedRow(), tCal.getSelectedColumn()) == null) { return; }
					
					// 선택한 날짜 및 비교할 날짜 설정
					Date _sDate = format.parse(lTitle.getText() + " " + tCal.getValueAt(tCal.getSelectedRow(), tCal.getSelectedColumn()) + "일");
					Date _cDate = format.parse(lblDate[(isStartDate) ? 1 : 0].getText());
					
					// 선택한 시작 날짜가 종료 날짜보다 이후인 경우 알람 메시지 표시
					if (((isStartDate) && (_sDate.compareTo(_cDate) > 0)) || ((!isStartDate) && (_sDate.compareTo(_cDate) < 0))) {
						JOptionPane.showConfirmDialog(null, 
								(isStartDate) ? "시작 날짜는 종료 날짜보다 이후일 수 없습니다." : "종료 날짜는 시작 날짜보다 이전일 수 없습니다.", 
								"JavaBean - 경고",
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.WARNING_MESSAGE);
						
						return;
					}
					
					// 부모 패널의 텍스트 라벨 설정
					lblDate[(isStartDate) ? 0 : 1].setText(format.format(_sDate));
					
					// 해당 프레임 종료
					dispose();
					
				// 에러 처리
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
					// 오류 발생시 확인창을 띄우고, 프로그램 종료
					if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -7)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
						System.exit(-7);
					};
				}
			}
		}
	}
}
