package customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.json.simple.JSONObject;

import customer.frame.component.JBTabbedPaneUI;
import customer.frame.panel.JBCategoryPanel;
import customer.frame.panel.JBOrderPanel;
import customer.frame.BasicFrame;

import jdbc.JDBCManager;
import jdbc.oracle.customer.Items;

@SuppressWarnings("serial")
public class Customer extends BasicFrame {
	
	public static String pIcon = "img/icon/java_bean.png";
	private JTabbedPane tpCategory;

	/**
	 * 커스토머 창 메인 화면
	 * @param _title
	 * @param _width
	 * @param _height
	 */
	public Customer(String _title, int _width, int _height) {
		super(_title, _width, _height);
		// TODO Auto-generated constructor stub
		
		// 아이콘 설정
		setIconImage(Toolkit.getDefaultToolkit().getImage(pIcon));
		
		// 패널 등록
		setNorthPanel();
		setCenterPanel();
		setSouthPanel();
		
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
	
	/**
	 * 카페 로고 및 이름을 표기
	 */
	private void setNorthPanel() {
		
		JPanel pNorth = new JPanel();
		
		// 로고 이미지 생성
		Image iLogo = new ImageIcon("img/logo/java_bean.png").getImage();
		
		// 원본 이미지를 부드럽게 200x100 사이즈로 조절한 후, 등록한다
		JLabel lLogo = new JLabel();
		lLogo.setIcon(new ImageIcon(iLogo.getScaledInstance(200, 100, Image.SCALE_SMOOTH)));
		
		// 등록 전, 패널 디자인 수정
		pNorth.setOpaque(true);
		pNorth.setBackground(Color.DARK_GRAY);
		
		// 판넬 등록
		pNorth.add(lLogo, BorderLayout.CENTER);
		add(pNorth, BorderLayout.NORTH);
	}
	
	/**
	 * 카테고리 탭 및 탭별 아이템 목록 패널 등록
	 */
	private void setCenterPanel() {
		
		// Client Frame에 등록하기 위한 탭 패널 선언 및 카테고리 처리를 위한 lMenu 변수 선언
		tpCategory = new JTabbedPane();
		Vector<JSONObject> lMenu;
		
		// 오류처리
		try {
					
			// 카테고리 정보를 받아오고, 개수만큼 버튼 배열 생성
			lMenu = Items.getCategoryList();
			
			// 카테고리 수만큼 반복하면서 tpCategory에 탭 & 패널 등록
			for (JSONObject _json : lMenu) {
				
				// 탭에 표기할 라벨 선언
				JLabel _tab = new JLabel(_json.get("카테고리").toString());
				
				// 라벨 크기 조절 및 디자인 조절
				_tab.setPreferredSize(new Dimension(75, 25));
				_tab.setForeground(Color.WHITE);
				
				// 라벨 중앙 정렬
				_tab.setHorizontalAlignment(JLabel.CENTER);
				
				// 카테고리 탭 팬에 탭 및 패널 추가
				tpCategory.add(new JBCategoryPanel(_json.get("카테고리").toString(), getWidth() / 5 * 4, getHeight() / 5));
				tpCategory.setTabComponentAt(tpCategory.getTabCount() - 1, _tab);
				
				// 카테고리 탭 폰트 설정
				tpCategory.getTabComponentAt(tpCategory.getTabCount() - 1).setFont(new Font("맑은 고딕", Font.BOLD, 12));
			}
			
			// 탭 변경 시 이벤트 처리 추가
			tpCategory.addChangeListener((e) -> {
				
				// 아이템 상태 갱신을 위해 카테고리 패널을 새롭게 다시 등록한다.
				JLabel _tOld = (JLabel) tpCategory.getTabComponentAt(tpCategory.getSelectedIndex());
				tpCategory.setComponentAt(tpCategory.getSelectedIndex(), new JBCategoryPanel(_tOld.getText(), getWidth() / 5 * 4, getHeight() / 5));
				
			});
				
		// 예외처리
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
					
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -1)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-1);
			};
		}
		
		// tpCategory 디자인 설정
		tpCategory.setOpaque(true);
		tpCategory.setBackground(Color.DARK_GRAY);
		tpCategory.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		tpCategory.setUI(new JBTabbedPaneUI(tpCategory));
		
		// tpCategory 포커스 해제
		tpCategory.setFocusable(false);
		
		// 카테고리 탭 등록
		add(tpCategory, BorderLayout.CENTER);
	}
	
	
	/**
	 * 주문 목록 및 주문 / 취소 패널 등록
	 */
	private void setSouthPanel() {
		
		JPanel pSouth = new JPanel(new BorderLayout());
		pSouth.setPreferredSize(new Dimension(getWidth(), getHeight() / 3));
		
		// 주문 목록 관련 패널 등록
		pSouth.add(new JBOrderPanel(getWidth(), getHeight()));
		add(pSouth, BorderLayout.SOUTH);
	}

	
	/**
	 * 커스토머 프로그램 실행
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 이곳에 메인 프레임을 선언해주세요.
		// 나머지 프레임 및 기타 작업들은 패키지에 맞게 클래스 선언해서 사용해주세요.
		// 이곳은 프로그램 실행을 위한 메인함수만 작성하도록 합니다.
		
		// 커스토머용 프로그램 시작
		new Customer(" JavaBean 1.1.0v - 카페에 오신걸 환영합니다.", 640, 800);
	}
}
