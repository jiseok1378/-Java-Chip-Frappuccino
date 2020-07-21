package customer.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import customer.Customer;
import customer.frame.component.JBScrollBar;
import customer.frame.model.JBOrderDefaultTableModel;

@SuppressWarnings("serial")
public class JBReceiptFrame extends BasicFrame {
	
	// 주문 정보 저장 변수
	private String[] iOrder;
	private JBOrderDefaultTableModel lOrder;
	
	// 전역 처리를 위한 변수
	private JScrollPane spBody;
	private JTextArea taReceipt;
	private JPanel pLogo;
	private JPanel pBody;

	/**
	 * 영수증 창 생성자
	 * @param _title
	 * @param _width
	 * @param _height
	 * @param _iOrder
	 * @param _lOrder
	 */
	public JBReceiptFrame(String _title, int _width, int _height, String[] _iOrder, JBOrderDefaultTableModel _lOrder) {
		super(_title, _width, _height);
		// TODO Auto-generated constructor stub
		
		// 변수 초기화
		iOrder = _iOrder;
		lOrder = _lOrder;
		
		// 아이콘 설정
		setIconImage(Toolkit.getDefaultToolkit().getImage(Customer.pIcon));
		
		// 창 닫을 시, 해당 창만 종료되도록 변경
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// 레이아웃 설정 및 배경 설정
		setLayout(new BorderLayout());
		setBackground(Color.DARK_GRAY);
		
		// 패널 등록
		setReceiptPanel();
		setSavePanel();
		
		// 창 표기
		setVisible(true);
	}
	
	/**
	 * 영수증 내역 출력
	 */
	private void setReceiptPanel() {
		
		// 로고 + 영수증 내역 등록을 위한 패널
		pBody = new JPanel(new BorderLayout());
		
		// 로고 등록을 위한 패널
		pLogo = new JPanel();
		
		// 로고 이미지 생성
		Image iLogo = new ImageIcon("img/logo/java_bean.png").getImage();
		
		// 원본 이미지를 부드럽게 200x100 사이즈로 조절한 후, 등록한다
		JLabel lLogo = new JLabel();
		lLogo.setIcon(new ImageIcon(iLogo.getScaledInstance(200, 100, Image.SCALE_SMOOTH)));
		
		// 영수증 내용 등록을 위한 패널
		taReceipt = new JTextArea();
		spBody = new JScrollPane(
				taReceipt,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		
		// 스크롤 패널 사이즈 설정
		spBody.setPreferredSize(new Dimension(getWidth(), getHeight() / 15 * 11 - 5));
		
		// 세로 스크롤바 감춤 상태에서 스크롤 가능하게끔 처리
		spBody.setVerticalScrollBar(new JBScrollBar(JScrollBar.VERTICAL));
		
		// 문자열 출력을 위한 포맷 설정
		DecimalFormat dFormat = new DecimalFormat("000000");
		SimpleDateFormat[] sFormat = {
				new SimpleDateFormat("yyyyMMdd"),
				new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss")
		};
		
		// 폰트 설정
		taReceipt.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		
		// 수정 불가하게 설정
		taReceipt.setEditable(false);
		
		// 영수증 상단 부분 출력
		taReceipt.append("#001 자바빈 인하대점\n");
		taReceipt.append("\n");
		taReceipt.append("대 표 자: 자바빈 프라푸치노\n");
		taReceipt.append("주소: 인천 남구 인하로\n");
		taReceipt.append("\n");
		taReceipt.append("\n");
		taReceipt.append(
				"영수증: " + 
				sFormat[0].format(new Date(Long.parseLong(iOrder[1]) / 1000)) + 
				"-" + 
				dFormat.format(Integer.parseInt(iOrder[0])) + 
				"\n"
		);
		taReceipt.append("테이블: 테이크아웃\n");
		taReceipt.append("\n");
		taReceipt.append("주문번호: " + iOrder[0] + "\n");
		taReceipt.append("\n");
		taReceipt.append("───────────────────────────────\n");
		taReceipt.append(String.format("%-24s\t%6s%15s%13s\n", "품명", "단가", "수량", "금액"));
		taReceipt.append("───────────────────────────────\n");
		
		// 영수증 물품 내역 출력
		int _pTotal = 0;
		for (int _r = 0; _r < lOrder.getRowCount(); _r++) {
			taReceipt.append(String.format("%-24s" + ((lOrder.getValueAt(_r, 0).toString().length() > 10) ? "" : "\t") + "%,8d\t%,4d%,16d\n", 
					lOrder.getValueAt(_r, 0).toString(), 
					Integer.parseInt(lOrder.getValueAt(_r, 3).toString()),
					Integer.parseInt(lOrder.getValueAt(_r, 5).toString()),
					Integer.parseInt(lOrder.getValueAt(_r, 5).toString()) * Integer.parseInt(lOrder.getValueAt(_r, 3).toString())));
			taReceipt.append(String.format("%24s\t%,8d\t%,4d%,16d\n", 
					lOrder.getValueAt(_r, 1).toString() + " & " + lOrder.getValueAt(_r, 2).toString(), 
					Integer.parseInt(lOrder.getValueAt(_r, 4).toString()),
					Integer.parseInt(lOrder.getValueAt(_r, 5).toString()),
					Integer.parseInt(lOrder.getValueAt(_r, 5).toString()) * Integer.parseInt(lOrder.getValueAt(_r, 4).toString())));
			
			// 합산 가격 계산
			_pTotal += Integer.parseInt(lOrder.getValueAt(_r, 6).toString());
		}
		
		// 영수증 나머지 부분 출력
		taReceipt.append("───────────────────────────────\n");
		taReceipt.append(String.format("%-40s\t%,21d\n", "주 문 합 계:", _pTotal));
		taReceipt.append(String.format("%-40s\t%,21d\n", "공급가금액:", (_pTotal / 11 * 10)));
		taReceipt.append(String.format("%-40s\t%,21d\n", "부  가  세:", (_pTotal - (_pTotal / 11 * 10))));
		taReceipt.append("───────────────────────────────\n");
		taReceipt.append(String.format("%-40s\t%,21d\n", "합 계 금 액:", _pTotal));
		taReceipt.append("───────────────────────────────\n");
		taReceipt.append(String.format("%-48s%23s\n", "승 인 번 호:", iOrder[1]));
		taReceipt.append(String.format("%-40s\t%,21d\n", "승 인 금 액:", _pTotal));
		taReceipt.append(String.format("%-24s%44s\n", "결 제 일 시:", sFormat[1].format(new Date(Long.parseLong(iOrder[1]) / 1000))));
		taReceipt.append("───────────────────────────────\n");
		taReceipt.append("자바빈을 이용해주셔서 감사합니다.\n\n\n");
		
		
		// TestArea 디자인 설정
		taReceipt.setOpaque(true);
		taReceipt.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		taReceipt.setBackground(Color.DARK_GRAY);
		taReceipt.setForeground(Color.WHITE);
				
		// 스크롤 팬 디자인 설정
		spBody.setOpaque(true);
		spBody.setBorder(BorderFactory.createEmptyBorder());
		spBody.setBackground(Color.DARK_GRAY);
		
		
		// 등록 전, 패널 디자인 수정
		pLogo.setOpaque(true);
		pLogo.setBackground(Color.DARK_GRAY);
		
		pBody.setOpaque(true);
		pBody.setBorder(BorderFactory.createEmptyBorder());
		pBody.setBackground(Color.DARK_GRAY);
		
		// 패널 등록
		pLogo.add(lLogo, BorderLayout.CENTER);
		pBody.add(pLogo, BorderLayout.NORTH);
		pBody.add(spBody, BorderLayout.CENTER);
		add(pBody, BorderLayout.NORTH);
	}
	
	/**
	 * 영수증 저장
	 */
	private void setSavePanel() {
		
		JPanel pSave = new JPanel(new BorderLayout());
		
		// 저장 버튼 및 사이즈 조절
		JButton bSave = new JButton("저장");
		
		bSave.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		bSave.setPreferredSize(new Dimension(getWidth(), getHeight() / 15 + 3));
		
		
		// 버튼 이벤트 등록
		bSave.addActionListener((e)->{
			
			// 스크린샷을 찍기 위해 가상 프레임을 생성한다.
			BasicFrame fVirtual = new BasicFrame(
					"JavaBean - Virtual Screen",
					taReceipt.getPreferredSize().width + 20,
					pLogo.getPreferredSize().height + 
					taReceipt.getPreferredSize().height);
			
			// 레이아웃 및 배경 설정
			fVirtual.setLayout(new BorderLayout());
			fVirtual.setBackground(Color.DARK_GRAY);
			fVirtual.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			// 컴포넌트 등록
			fVirtual.add(pLogo, BorderLayout.NORTH);
			fVirtual.add(spBody, BorderLayout.CENTER);
			
			// 이미지 저장을 위한 객체 생성
			BufferedImage image = new BufferedImage(
					fVirtual.getWidth() - 21,
					fVirtual.getHeight() - 38,
				    BufferedImage.TYPE_INT_RGB
			);
			
			// 스크린샷을 위해 프레임을 표시한 후, 다시 숨김처리한다.
			fVirtual.setVisible(true);
			fVirtual.getContentPane().paint(image.getGraphics());
			fVirtual.dispose();
			
			// 오류 처리
            try {
            	
            	// 파일 저장을 위한 경로 설정 창
    			JFileChooser fcPath = new JFileChooser();
            	
            	// 다중 설정 불가 및 저장 확장자 설정
            	fcPath.setFileFilter(new FileNameExtensionFilter("png", ".png"));
    			fcPath.setMultiSelectionEnabled(false);
            	
            	// 경로 설정 창
            	if(fcPath.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    
            		// 영수증을 선택한 경로에 이미지로 저장한다.
            		ImageIO.write(
          				  image,
          				  "png",
          				  new File(fcPath.getSelectedFile().toString() + "." + fcPath.getFileFilter().getDescription()));
            	
            		// 처리 완료 메시지 출력
					JOptionPane.showConfirmDialog(null, 
						"영수증이 저장되었습니다.", 
						"JavaBean - 저장 완료",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
					
					// 해당 창 닫기
					dispose();
            	}
            	else {
            		
            		// 화면 갱신을 위한 처리
            		// 화면 갱신을 하지 않을 경우, 스크롤이 먹히지 않는다.
            		
            		// 패널 제거
            		remove(pBody);
            		
            		// 패널에 컴포넌트 재등록
        			pBody.add(pLogo, BorderLayout.NORTH);
        			pBody.add(spBody, BorderLayout.CENTER);
        			add(pBody, BorderLayout.NORTH);
        			
        			// 화면 갱신
        			revalidate();
            	}
            
            // 에러 처리
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				
				// 오류 발생시 확인창을 띄운다.
				JOptionPane.showConfirmDialog(
						null,
						"예기치 않은 오류로 영수증 저장에 실패했습니다.\n잠시후 다시 시도해주세요.\n\n(ErrorCode: -5)", 
						"JavaBean - 저장 실패", 
						JOptionPane.DEFAULT_OPTION, 
						JOptionPane.ERROR_MESSAGE
				);
			}
		});
		
		// 버튼 디자인 설정
		bSave.setOpaque(true);
		bSave.setFocusable(false);
		bSave.setBackground(Color.WHITE);
		bSave.setForeground(Color.DARK_GRAY);
		bSave.setBorder(BorderFactory.createEmptyBorder());
		
		// 패널 디자인 설정
		pSave.setOpaque(true);
		pSave.setBorder(BorderFactory.createEmptyBorder());
		pSave.setBackground(Color.DARK_GRAY);
				
		// 패널 등록
		pSave.add(bSave, BorderLayout.CENTER);
		add(pSave, BorderLayout.SOUTH);
	}
}
