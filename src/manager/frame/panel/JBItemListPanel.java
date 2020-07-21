package manager.frame.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jdbc.oracle.manager.Items;
import manager.frame.BasicFrame;
import manager.frame.component.JBMutableTable;

/**
 * 물품 목록 탭 패널
 */
@SuppressWarnings("serial")
public class JBItemListPanel extends JPanel implements ActionListener, ListSelectionListener {

	// 내부 이벤트 처리를 위한 변수들을 멤버 변수로 설정
	private static DefaultListSelectionModel dlsm = new DefaultListSelectionModel();
	private static JBMutableTable tItem;
	private static JButton[] btnStatus;
	private static JPanel pNorth;

	/**
	 * 패널 생성자
	 */
	public JBItemListPanel() {

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
	 * North Panel 설정
	 */
	public void setNorthPanel() {
		pNorth = new JPanel(new BorderLayout());
		pNorth.setPreferredSize(new Dimension(BasicFrame.width, BasicFrame.height / 11 * 8));
		
		// 물품 목록 테이블 추가
		setRefresh();
		
		pNorth.setOpaque(true);
		add(pNorth, BorderLayout.NORTH);
	}

	/**
	 * South Panel 설정
	 */
	public void setSouthPanel() {
		
		JPanel pSouth = new JPanel(new GridLayout(1, 3));
		pSouth.setPreferredSize(new Dimension(BasicFrame.width, BasicFrame.height / 11));
		
		// 상태 변경 버튼 관련 변수 초기화
		String[] _nStatus = { "판매 가능", "일시 품절", "판매 중지" };
		btnStatus = new JButton[3];
		
		/*
		 * 상태 변경 버튼 설정 
		 */
		for (int i = 0; i < btnStatus.length; i++) {
			btnStatus[i] = new JButton(_nStatus[i]);
			
			btnStatus[i].addActionListener(this);
			
			// 디자인 설정
			btnStatus[i].setOpaque(true);
			btnStatus[i].setFocusable(false);
			btnStatus[i].setBackground(Color.WHITE);
			btnStatus[i].setForeground(Color.DARK_GRAY);
			btnStatus[i].setFont(new Font("맑은 고딕", Font.BOLD, 12));
			btnStatus[i].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
			
			pSouth.add(btnStatus[i]);
		}
		
		// 디자인 설정 및 프레임 컨테이너에 패널 등록
		pSouth.setOpaque(true);
		pSouth.setBorder(BorderFactory.createEmptyBorder());
		add(pSouth, BorderLayout.SOUTH);
	}

	/**
	 * 물품 목록 테이블 갱신
	 */
	private void setRefresh() {
		// 오류 처리
		try {
			
			// 물품 목록 테이블 생성 및 이벤트 리스너 등록
			tItem = new JBMutableTable(Items.getAllItemsWithStatus());
			tItem.addListSelectionListener(this);
			
			// pNorth 자식 컴포넌트 삭제 및 tItem 등록
			pNorth.removeAll();
			pNorth.add(tItem.getScrollTable(), BorderLayout.CENTER);
			
			// 화면 갱신
			revalidate();
			
		// 예외 처리
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -17)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-17);
			};
		}
	}

	/**
	 * 셀 클릭 시 동작하는 이벤트 핸들러
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
		// 행 클릭 시 dlsm 변수에 이벤트 정보를 등록한다.
		if (!e.getValueIsAdjusting()) {
			dlsm = (DefaultListSelectionModel) e.getSource();
			
			// 선택한 물품의 상태와 일치하는 버튼을 비활성화 처리
			for (int i = 0; i < btnStatus.length; i++)
				btnStatus[i].setEnabled(!btnStatus[i].getText().equals((tItem.getContents()[dlsm.getAnchorSelectionIndex()][3].toString())));
		}
	}
	
	/**
	 * 버튼 클릭시 동작하는 이벤트 핸들러
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		// 클릭된 버튼을 새로운 변수에 할당한다.
		JButton btnEvent = ((e.getSource().equals(btnStatus[0])) ? btnStatus[0] :
			((e.getSource().equals(btnStatus[1])) ? btnStatus[1] : btnStatus[2]));
		
		// 오류 처리
		try {
			
			// 상태 변경 안내 메시지를 띄운다
			if (JOptionPane.showConfirmDialog(null,
					"해당 품목에 대한 상태를 '" +
					btnEvent.getText()	
					+ "'(으)로 변경하시겠습니까?",
					"JavaBean - 물품 상태 변경",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				
				// 선택 항목에 대한 상태 변경 함수 호출
				Items.updateItemStatus(tItem.getContents()[dlsm.getAnchorSelectionIndex()][1].toString(), btnEvent.getText());
					
				// 물품 목록 테이블 갱신
				setRefresh();
				
				// 처리 완료 메시지 출력
				JOptionPane.showConfirmDialog(null, 
					"정상적으로 처리 완료되었습니다.", 
					"JavaBean - 처리 완료",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			}
			
		// 에러 처리
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -18)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-18);
			};
		}
	}
}
