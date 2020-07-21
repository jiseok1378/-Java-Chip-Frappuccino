package manager.frame.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.simple.JSONObject;

import manager.frame.BasicFrame;
import manager.frame.component.JBMutableTable;
import jdbc.oracle.manager.Managers;

@SuppressWarnings("serial")
public class JBOrderedDetailPanel extends JPanel {
	
	private JPanel pSouth;
	private JBMutableTable tOrderedDetailList;

	public JBOrderedDetailPanel() {
		// 패널 레이아웃 설정
		setLayout(new BorderLayout());
				
		// 패널 등록
		setNorthPanel();
		setSouthPanel();
				
		// 디자인 설정
		setOpaque(true);
		setBackground(Color.DARK_GRAY);
	}
	
	// 헤더 패널 등록
	private void setNorthPanel() {

		// 레이아웃 설정
		JPanel pNorth = new JPanel(new BorderLayout());
		
		
		// 텍스트 필드 설정
		JTextField tfOrderNumber = new JTextField("승인 번호 입력");
		tfOrderNumber.setCaretPosition(tfOrderNumber.getText().length());
		tfOrderNumber.setPreferredSize(new Dimension(BasicFrame.width / 4 * 3 - 15, 0));
		
		// 텍스트 필드 디자인 설정
		tfOrderNumber.setOpaque(true);
		tfOrderNumber.setBackground(Color.DARK_GRAY);
		tfOrderNumber.setForeground(Color.WHITE);
		tfOrderNumber.setCaretColor(Color.WHITE);
		tfOrderNumber.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		tfOrderNumber.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		
		
		// 조회버튼 정의 및 리스너 익명 클래스 등록
		JButton btnShow = new JButton("조회");
		btnShow.setPreferredSize(new Dimension(BasicFrame.width / 4 - 5, 0));
		btnShow.addActionListener((e) -> {
			
			// 승인 번호 정규식 검사
			if (!tfOrderNumber.getText().matches("\\d{16}$")) {
				JOptionPane.showConfirmDialog(null, 
						"올바르지 않은 승인 번호입니다.\n승인 번호 확인 후 다시 시도해주세요.", 
						"JavaBean - 경고",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE);

				// 텍스트 필드 값 제거
				tfOrderNumber.setText("");
				return;
			}
			
			// 오류 처리
			try {
				
				// 승인번호에 대한 조회 결과 테이블 등록
				Vector<JSONObject> _data = Managers.getOrderDetailAtNumber(tfOrderNumber.getText());
				tOrderedDetailList = new JBMutableTable(_data);
				
				// 패널에 테이블 등록
				pSouth.removeAll();
				pSouth.add(tOrderedDetailList.getScrollTable(), BorderLayout.CENTER);
				
				// 화면 갱신
				revalidate();
				
				// 텍스트 필드 값 제거
				tfOrderNumber.setText("");
				
				// 조회결과가 존재하지 않는 경우, 메시지 표시
				if (_data.get(0).get("승인번호") == null) {
					JOptionPane.showConfirmDialog(null, 
							"해당 번호에 대한 상세 주문 내역이 존재하지 않습니다.", 
							"JavaBean - 조회 결과",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
				}
			
			// 에러 처리
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				
				// 오류 발생시 확인창을 띄우고, 프로그램 종료
				if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -9)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
					System.exit(-9);
				};
			}
			
		});
		
		// 디자인 설정
		btnShow.setOpaque(true);
		btnShow.setFocusable(false);
		btnShow.setBackground(Color.WHITE);
		btnShow.setForeground(Color.DARK_GRAY);
		btnShow.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		btnShow.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		
		
		// 레이블 및 버튼 등록
		pNorth.add(tfOrderNumber, BorderLayout.WEST);
		pNorth.add(btnShow, BorderLayout.EAST);
		
		// 디자인 설정
		pNorth.setOpaque(true);
		pNorth.setBackground(Color.DARK_GRAY);
		pNorth.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		pNorth.setPreferredSize(new Dimension(BasicFrame.width, BasicFrame.height / 16));
		
		add(pNorth, BorderLayout.NORTH);
	}
	
	// 테이블 패널 등록
	private void setSouthPanel() {
		
		pSouth = new JPanel(new BorderLayout());
		
		try {
			
			// 주문 상세 리스트 공백 테이블 생성
			tOrderedDetailList = new JBMutableTable(Managers.getOrderDetailAtNumber("0"));	
			pSouth.add(tOrderedDetailList.getScrollTable(), BorderLayout.CENTER);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -8)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-8);
			};
		}
		
		// 디자인 설정
		pSouth.setOpaque(true);
		pSouth.setBackground(Color.DARK_GRAY);
		pSouth.setPreferredSize(new Dimension(BasicFrame.width, BasicFrame.height / 16 * 12));
		
		// 패널 등록
		add(pSouth, BorderLayout.SOUTH);
		
	}
}
