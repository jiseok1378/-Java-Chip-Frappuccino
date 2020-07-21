package manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.SocketException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import manager.frame.BasicFrame;
import manager.frame.component.JBTabbedPaneUI;
import manager.frame.panel.JBItemListPanel;
import manager.frame.panel.JBOrderPanel;
import manager.frame.panel.JBOrderedDetailPanel;
import manager.frame.panel.JBOrderedItemPanel;
import manager.frame.panel.JBOrderedPanel;
import manager.frame.panel.JBOrderedTotalPanel;
import jdbc.JDBCManager;

@SuppressWarnings("serial")
public class Manager extends BasicFrame {
	
	public static String pIcon = "img/icon/java_bean.png";
	private static JTabbedPane tpMenu;

	/**
	 * 매니저 프로그램의 메인 화면 프레임
	 * @param _title
	 * @param _width
	 * @param _height
	 */
	public Manager(String _title, int _width, int _height) {
		super(_title, _width, _height);
		// TODO Auto-generated constructor stub
		
		// 프로그램 중복 실행 방지 처리
		try {
			Monitor.monitoring();
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "매니저 프로그램이 이미 실행 중입니다.", "JavaBean - 프로그램 종료", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(9);
			};
		}
		
		// 아이콘 설정
		setIconImage(Toolkit.getDefaultToolkit().getImage(pIcon));
		
		// 패널 등록
		setTabbedPanel();
		
		// 창 표기
		setVisible(true);
		
		// 프로그램 종료 전 메시지 띄우기
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(null,
				    	"프로그램을 종료하시겠습니까?",
				    	"JavaBean - 프로그램 종료",
				    	JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							
					try {
			      				
					    // 프로그램 종료시 JDBC 연결 해제
					    JDBCManager jdbc = JDBCManager.getJDBCManager();
						jdbc.setClose();
										
						// 프로그램 종료
						setDefaultCloseOperation(EXIT_ON_CLOSE);
										
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
										
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				else {			
					// 프로그램 종료 방지
				    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			}
		});
	}
	
	private void setTabbedPanel() {

		// Client Frame에 등록하기 위한 탭 패널 선언 및 카테고리 처리를 위한 lMenu 변수 선언
		tpMenu = new JTabbedPane();
		
		// 관리자가 선택 가능한 탭 리스트
		JPanel[] pTabList = {
				new JBOrderPanel(), 
				new JBOrderedPanel(), 
				new JBOrderedDetailPanel(), 
				new JBOrderedItemPanel(),
				new JBOrderedTotalPanel(),
				new JBItemListPanel()
		};
		
		// 탭 목록 변수 선언
		String[] nTabList = {
				"주문 조회", "주문 내역", "주문 검색", "판매 내역", "매출 내역", "물품 목록"
		};
		
		// 탭 목록 수만큼 탭 표기
		for (int i = 0; i < pTabList.length; i++) {
			
			// 탭에 표기할 라벨 선언
			JLabel _tab = new JLabel(nTabList[i]);
						
			// 라벨 크기 조절 및 디자인 조절
			_tab.setPreferredSize(new Dimension(75, 25));
			_tab.setForeground(Color.WHITE);
						
			// 라벨 중앙 정렬
			_tab.setHorizontalAlignment(JLabel.CENTER);
						
			// 카테고리 탭 팬에 탭 및 패널 추가
			tpMenu.add(pTabList[i]);
			tpMenu.setTabComponentAt(tpMenu.getTabCount() - 1, _tab);
			
			// 탭 폰트  설정
			tpMenu.getTabComponentAt(tpMenu.getTabCount() - 1).setFont(new Font("맑은 고딕", Font.BOLD, 12));
		}
		
		// tpCategory 디자인 설정
		tpMenu.setOpaque(true);
		tpMenu.setBackground(Color.DARK_GRAY);
		tpMenu.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		tpMenu.setUI(new JBTabbedPaneUI(tpMenu));
		
		// tpCategory 포커스 해제
		tpMenu.setFocusable(false);
		
		// 카테고리 탭 등록
		add(tpMenu, BorderLayout.CENTER);
	}
	
	/**
	 * tpMenu 반환
	 * @return JTabbedPane
	 */
	public static JTabbedPane getMenus() {
		return tpMenu;
	}
	
	/**
	 * 매니저 프로그램 실행
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 이곳에 메인 프레임을 선언해주세요.
		// 나머지 프레임 및 기타 작업들은 패키지에 맞게 클래스 선언해서 사용해주세요.
		// 이곳은 프로그램 실행을 위한 메인함수만 작성하도록 합니다.
		
		// 매니저용 프로그램 시작
		new Manager(" JavaBean 1.1.0v - 관리자 프로그램", 800, 500);
	}
}
