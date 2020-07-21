package manager.frame.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.simple.JSONObject;

import manager.Manager;
import manager.frame.BasicFrame;
import manager.frame.component.JBMutableTable;
import jdbc.oracle.manager.Managers;

@SuppressWarnings("serial")
public class JBOrderPanel extends JPanel implements ActionListener {
	
	private static DefaultListSelectionModel[] dlsm = new DefaultListSelectionModel[2];
	private static JBMutableTable tOrder, tDetail, tSelection;
	
	private static String nCustomer;
	private String[] nItem = new String[1];
	private String[] nDetail = new String[1];
	
	private JScrollPane spDetail;
	
	private JPanel pWest;
	private JPanel pEastNorth;
	
	private JButton bCancel;
	private JButton bComplete;
	
	/**
	 * 현재 접수된 주문 내역을 보여주는 패널
	 */
	public JBOrderPanel() {
		
		// 패널 레이아웃 설정
		setLayout(new BorderLayout());
		
		// 패널 등록
		setWestPanel();
		setEastPanel();
		
		// 디자인 설정
		setOpaque(true);
		setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * 금일 미수령 주문 정보 테이블 패널 등록
	 */
	private void setWestPanel() {
		
		// 레이아웃 설정
		pWest = new JPanel(new BorderLayout());
		
		// 디자인 설정
		pWest.setOpaque(true);
		pWest.setBackground(Color.DARK_GRAY);
		pWest.setPreferredSize(new Dimension(BasicFrame.width / 4, BasicFrame.height));
		
		try {
			
			// 테이블 & 이벤트 처리기 등록
			tOrder = new JBMutableTable(Managers.getOrderNotReceivedAtToday());
			tOrder.addListSelectionListener(new JBListSelectionListener(tOrder));
			
			// 테이블이 붙여진 JScrollPane을 가져와서 pWest에 등록한다.
			pWest.add(tOrder.getScrollTable(), BorderLayout.CENTER);
			
			// 익명 쓰레드 실행
			// 5초마다 반복하며 새로 주문된 내용을 업데이트
			new Thread() {
	            public void run() {
	                while (true) {
	                    try {
	                    	
	                    	// 5초동안 쓰레드 휴지
	                        Thread.sleep(5000);
	                        
	                        // 목록이 아에 없는 경우는 테이블을 새로 만든다.
	                        if (tOrder.getContents()[0][0] == null) {
	                        	
	                        	// 테이블 등록
	                        	tOrder = new JBMutableTable(Managers.getOrderNotReceivedAtToday());
	                        	tOrder.addListSelectionListener(new JBListSelectionListener(tOrder));
	                        	
	                        	// 패널에 테이블 등록
	                        	pWest.removeAll();
	                        	pWest.add(tOrder.getScrollTable(), BorderLayout.CENTER);
	                        	
	                        	// 화면 갱신
	                        	revalidate();
	                     
	                        }
	                        else {
	                        	
	                        	// 새로운 내용과 기존 테이블 내용을 비교하여 다른 부분만 테이블에 추가한다.
	                        	// 테이블을 새로 만들면 사용자가 선택했던 셀렉션이 사라지는 문제 발생
	                        	// 따라서 기존 테이블과 다른 내용의 행들만을 추려서 테이블에 추가시킨다.
		                        tOrder.addRowsAtNew(Managers.getOrderNotReceivedAtToday());
	                        }
	                        
	                    // 오류 처리
	                    } catch (Exception e) {
	                    	e.printStackTrace();
	                    	
	                    	// 쓰레드 및 일반 이벤트 처리에 의한 SQL Connection 문제가 해결되지 않음
	                    	// Thread-Safe하게 처리하기 위해서는 다음의 방법을 사용해야함
	                    	// 1. 쓰레드와 일반 메소드 처리용 JDBCManager를 분리하거나 Connection을 별도로 설정 해야함
	                    	// -> 현실성 부족, 별도의 처리를 위해 모든 메소드 수정이 불가피
	                    	// 2. 세마포어 방식
	                    	// -> 구현 난이도가 있으며, 현재 설계된 프로그램의 방식에서는 사용하기가 난해함.
	                    	// 3. 기타 방식 (별도 플래그 혹은 큐(Queue)를 통한 처리)
	                    	// -> 플래그 방식은 도입이 어려우며, 큐(Queue) 방식은 처리 방법을 좀 더 고민해볼 필요가 있음
	                    	
	                    	// 상기 이유로 해당 오류 발생시 프로그램을 종료하지 않도록 한다.
	                    	// 단순히 갱신만 안 될뿐, 특별한 이상은 없기 때문이다.
	                    }
	                }
	            }
	        }.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		add(pWest, BorderLayout.WEST);
	}
	
	/**
	 * 금일 미수령 주문 번호에 대한 상세 정보 테이블 및 일괄 취소 / 취소 / 수령 버튼 패널 등록
	 */
	private void setEastPanel() {
		
		int _width = BasicFrame.width / 4 * 3 - 15;
		int _height = BasicFrame.height / 4 * 3 - 15;
		
		JPanel pEast = new JPanel(new BorderLayout());
		
		// 디자인 설정
		pEast.setOpaque(true);
		pEast.setBackground(Color.DARK_GRAY);
		pEast.setPreferredSize(new Dimension(_width, _height));
		
		// 오류 처리
		try {
			
			pEastNorth = new JPanel(new BorderLayout());
			
			// 테이블 등록
			tDetail = new JBMutableTable(Managers.getOrderDetailNotReceivedAtNumber("-1"));
			
			// 스크롤 테이블 사이즈 조절
			spDetail = tDetail.getScrollTable();
			spDetail.setPreferredSize(new Dimension(_width, _height));
			
			// 스크롤 테이블 추가
			pEastNorth.add(spDetail, BorderLayout.CENTER);
			pEast.add(pEastNorth, BorderLayout.NORTH);
		
		// 에러 처리
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -1)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-1);
			};
		}
		
		// 주문 취소, 주문 승인을 위한 패널 정의
		JPanel pEastSouth = new JPanel(new BorderLayout());
		
		// 패널 색상 및 사이즈 조절
		pEastSouth.setOpaque(true);
		pEastSouth.setBackground(Color.DARK_GRAY);
		pEastSouth.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		pEastSouth.setPreferredSize(new Dimension(_width, _height / 8));
		
		// 주문 취소 & 승인 코드 및 이벤트 등록
		bCancel = new JButton("주문 취소");
		bCancel.addActionListener(this);
		
		bComplete = new JButton("수령 완료");
		bComplete.addActionListener(this);
		
		// 취소 버튼 색상 및 사이즈 조절
		bCancel.setOpaque(true);
		bCancel.setFocusable(false);
		bCancel.setBackground(Color.WHITE);
		bCancel.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		bCancel.setBorder(BorderFactory.createEmptyBorder());
		bCancel.setPreferredSize(new Dimension(_width / 2 - 1, _height / 8));
		
		// 승인 버튼 색상 및 사이즈 조절
		bComplete.setOpaque(true);
		bComplete.setFocusable(false);
		bComplete.setBackground(Color.WHITE);
		bComplete.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		bComplete.setBorder(BorderFactory.createEmptyBorder());
		bComplete.setPreferredSize(new Dimension(_width / 2 - 1, _height / 8));
		
		// 패널에 버튼 등록
		pEastSouth.add(bCancel, BorderLayout.WEST);
		pEastSouth.add(bComplete, BorderLayout.EAST);
		
		// 패널 등록
		pEast.add(pEastSouth, BorderLayout.SOUTH);
		add(pEast, BorderLayout.EAST);
	}
	
	/**
	 * 정보를 받아 주문 조회 테이블을 갱신하도록 하는 함수
	 * @param _intension
	 */
	private void setOrderTable(Vector<JSONObject> _intension) {
		
		// 오류 처리
		try {
		
			// 새로운 정보를 지닌 테이블을 다시 등록
			tOrder = new JBMutableTable(_intension);
			tOrder.addListSelectionListener(new JBOrderPanel.JBListSelectionListener(tOrder));
		
			pWest.removeAll();
			pWest.add(tOrder.getScrollTable(), BorderLayout.CENTER);
			
			// 화면 갱신
			revalidate();
			
		// 에러 처리
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
						
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -3)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-3);
			};
		}
	}
	
	/**
	 * 정보를 받아 상세 주문 조회 테이블을 갱신하도록 하는 함수
	 * @param _intension
	 */
	private void setOrderDetailTable(Vector<JSONObject> _intension) {
		
		// 오류 처리
		try {
			
			// 테이블 등록
			tDetail = new JBMutableTable(_intension);
			tDetail.addListSelectionListener(new JBOrderPanel.JBListSelectionListener(tDetail));
			
			// 스크롤 테이블 사이즈 변경
			spDetail = tDetail.getScrollTable();
			spDetail.setPreferredSize(new Dimension(BasicFrame.width / 4 * 3 - 15, BasicFrame.height / 4 * 3 - 15));
			
			// 새로운 테이블 등록
			pEastNorth.removeAll();
			pEastNorth.add(spDetail, BorderLayout.CENTER);
			
			// 화면 갱신
            revalidate();
			
		// 에러 처리
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -4)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-4);
			};
		}
	}
	
	/**
	 * 리스트 셀렉션 이너 클래스
	 */
	private static class JBListSelectionListener implements ListSelectionListener {
		
		private JBMutableTable _tSelection;
		
		/**
		 * 리스트 셀렉션 리스너 생성자
		 * @param _table
		 */
		public JBListSelectionListener(JBMutableTable _table) {
			_tSelection = _table;
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			// TODO Auto-generated method stub	
			
			// 튜플이 클릭되었을 시에만 동작하도록 처리
			if(!e.getValueIsAdjusting()) {
				
				// 선택된 테이블 등록
				tSelection = _tSelection;
				
				// tOrder의 행이 클릭되었을 시
				if (_tSelection.equals(tOrder)) {
					
					dlsm[0] = (DefaultListSelectionModel) e.getSource();
					
					// 오류 처리
					try {
						
						// 가장 최근에 선택된 주문번호를 저장하도록 한다.
						nCustomer = tOrder.getContents()[dlsm[0].getAnchorSelectionIndex()][0];
						
						// 주문 처리전 nCustomer가 null인 경우 처리를 중단한다.
						if (nCustomer == null) { return; }
						
						// 선택된 주문에 따른 상세 주문 내역 테이블을 보이도록 한다.
						((JBOrderPanel) Manager.getMenus().getComponent(0)).setOrderDetailTable(Managers.getOrderDetailNotReceivedAtNumber(nCustomer));
					
					// 에러 처리
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						
						// 오류 발생시 확인창을 띄우고, 프로그램 종료
						if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -2)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
							System.exit(-2);
						};
					}
				}
				
				// tDetail의 행이 클릭되었을 시
				if (_tSelection.equals(tDetail)) {
					dlsm[1] = (DefaultListSelectionModel) e.getSource();
				}
			}
		}
	}
	
	/**
	 * 주문 취소 및 승인 버튼의 이벤트 처리
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		// 오류 처리
		try {

			// 아무것도 선택되지 않았을 때 처리
			if ((dlsm[0] == null) || (tOrder.getContents()[dlsm[0].getAnchorSelectionIndex()][0] == null)) {
				
				JOptionPane.showConfirmDialog(null, 
						"주문 조회 테이블의 주문 번호를 선택한 후 다시 시도해주세요.", 
						"JavaBean - 경고",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE);
				
				return;
			}
			
			// 주문 일괄 취소 및 일괄 승인 동작
			if (tSelection.equals(tOrder)) {
				if (JOptionPane.showConfirmDialog(null,
						(e.getSource().equals(bCancel)) ? "해당 주문에 대한 모든 내역을 취소처리 하시겠습니까?" : "해당 주문에 대한 모든 내역을 수령처리 하시겠습니까?",
						(e.getSource().equals(bCancel)) ? "JavaBean - 주문 일괄 취소" : "JavaBean - 수령 일괄 처리",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
					// 선택 항목 및 세부 주문 사항에 대해 일괄 취소 처리
					Managers.updateOrderStatus(
							tOrder.getContents()[dlsm[0].getAnchorSelectionIndex()][0], 
								(e.getSource().equals(bCancel)) ? "주문 취소" : "수령 완료");
						
					// 주문 목록 테이블 업데이트
					setOrderTable(Managers.getOrderNotReceivedAtToday());
					
					// 세부 주문 목록 테이블 업데이트
					setOrderDetailTable(Managers.getOrderDetailNotReceivedAtNumber("-1"));
					
					// 처리 완료 메시지 출력
					JOptionPane.showConfirmDialog(null, 
						"정상적으로 처리 완료되었습니다.", 
						"JavaBean - 처리 완료",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE);

				}
			}
			else {
				
				// 주문 취소 및 승인 동작
				if (JOptionPane.showConfirmDialog(null,
						(e.getSource().equals(bCancel)) ? "해당 주문을 취소 하시겠습니까?" : "해당 주문을 수령처리 하시겠습니까?",
						(e.getSource().equals(bCancel)) ? "JavaBean - 주문 취소" : "JavaBean - 수령 완료",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
					nItem[0] = tDetail.getContents()[dlsm[1].getAnchorSelectionIndex()][0];
					nDetail[0] = tDetail.getContents()[dlsm[1].getAnchorSelectionIndex()][1];
					
					// 선택한 세부 주문 사항 취소 처리
					Managers.updateOrderDetailStatus(nCustomer, nItem, nDetail, 
							(e.getSource().equals(bCancel)) ? "주문 취소" : "수령 완료");
					
					// 테이블 내용에서 해당 열을 삭제처리한다.
					setOrderDetailTable(Managers.getOrderDetailNotReceivedAtNumber(nCustomer));
					
					// 만일 세부 주문 목록이 더이상 존재하지 않는 경우 주문 테이블을 새롭게 갱신한다.
					if(tDetail.getRowCount() == 0)
						setOrderTable(Managers.getOrderNotReceivedAtToday());
					
					// 화면 갱신
					revalidate();
					
					// 처리 완료 메시지 출력
					JOptionPane.showConfirmDialog(null, 
						"정상적으로 처리 완료되었습니다.", 
						"JavaBean - 처리 완료",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE);

	    		}
			}
			
		// 에러 처리
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			// 오류 메시지 출력
			JOptionPane.showConfirmDialog(null, 
					"주문 조회 테이블의 주문 번호를 선택한 후 다시 시도해주세요.", 
					"JavaBean - 경고",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE);
		}
	}
}
