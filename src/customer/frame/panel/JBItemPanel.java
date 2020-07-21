package customer.frame.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import jdbc.oracle.customer.Items;

@SuppressWarnings("serial")
public class JBItemPanel extends JPanel implements ActionListener {
	
	// 옵션 관련 컴포넌트
	private JComboBox<String> cbTemp, cbSize;
	private JLabel lQuantity;
	private JButton bOrder;
	
	// 아이템 패널 사이즈 (가로, 세로)
	private int width;
	private int height;
	
	// 아이템 관련 정보
	private String category;
	private String name;
	private String price;
	private boolean isSell;
	
	private JSONObject option;

	// 초기화 완료 여부
	private boolean isInit;
	
	// 수량 추가, 감소 버
	private JButton bQuantityMinus;
	private JButton bQuantityPlus;
	
	/**
	 * 물품 패널 등록
	 * @param _category
	 * @param _name
	 * @param _price
	 * @param _width
	 * @param _height
	 */
	@SuppressWarnings("unchecked")
	public JBItemPanel(String _category, String _name, String _price, boolean _isSell, int _width, int _height) {
		
		// 기본 레이아웃 설정
		setLayout(new BorderLayout());
		
		// 기본 옵션 설정
		category = _category;
		name = _name;
		price = _price;
		isSell = _isSell;
		
		width = _width;
		height = _height;
		
		// 오류 처리
		try {
			
			// 메뉴이름에 해당하는 옵션 정보를 Vector<JSONObject>로 가져온다.
			// 전역 처리를 위해 Client 클래스의 static타입 Client.json 변수에 JSONObject 객체를 할당한다.
			Vector<JSONObject> iDetail = Items.getItemDetailList(_name);
			option = new JSONObject();
			
			// ICE / TALL
			// ICE / GRANDE
			// ICE / VENTI
			
			// 옵션 개수만큼 반복하며 JSON 정보를 가져온다
			for (JSONObject _json : iDetail) {
				
				// '옵션'이라는 키에 해당하는 값을 가져와 " & "로 파싱한다.
				String[] split = _json.get("옵션").toString().split(" & ");
				
				// 파싱한 문자열의 좌측에 해당하는 키값이 option에 없을 경우, 해당 키값에 JSONObject 객체를 값으로 할당한다.
				if (option.get(split[0]) == null) {
					option.put(split[0], new JSONObject()); 
				}
				
				/*
				 * 파싱한 문자열의 0번째 값을 '키'로 하고, 해당 '키'에 해당하는 값인 JSONObject 객체에 파싱한 문자열의 1번째 값을  키로, 해당 키에 대한 가격을 값으로 넣어준다.
				 * 처리 완료된 option은 아래 구조를 가진다.
				 * {
				 * 		"ICE": {
				 * 					"TALL": "0",
				 * 					"GRANDE": "500",
				 * 					"VENTI": "1000"
				 * 		},
				 * 		"HOT": {
				 * 					"TALL": "0",
				 * 					"GRANDE": "500",
				 * 					"VENTI": "1000"
				 * 		}
				 * }
				 */
				((JSONObject) option.get(split[0])).put(split[1], _json.get("옵션단가"));
			}
			
		// 예외 처리
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			// 오류 발생시 확인창을 띄우고, 프로그램 종료
			if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -3)", "JavaBean - 오류 안내", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				System.exit(-3);
			};
		}
		
		
		// 패널 구성
		setWestPanel();
		setCenterPanel();
		setEastPanel();
		
		
		// 디자인 설정
		setOpaque(true);
		setPreferredSize(new Dimension(_width, _height));
		setBorder(BorderFactory.createLineBorder(Color.WHITE));
		setBackground(Color.DARK_GRAY);
	}
	
	
	/**
	 * 물품 이미지 등록
	 */
	private void setWestPanel() {
		
		JPanel pWest = new JPanel();
		
		// 이미지 파일 검사를 위한 객체 생성
		File fIcon = new File((isSell) ? 
				("img/category/" + category + "/" + name + ".png") :
				("img/category/" + category + "/" + name + "_sold_out.png")
		);
		
		// 로고 이미지 생성
		Image iLogo = new ImageIcon((!fIcon.exists()) ? 
				((isSell) ? ("img/category/" + category + "/logo.png") :
				("img/category/" + category + "/logo_sold_out.png")) :
				((isSell) ? ("img/category/" + category + "/" + name + ".png") :
				("img/category/" + category + "/" + name + "_sold_out.png"))
		).getImage();
		
		// 이미지 사이즈 조절 후 등록
		JLabel lLogo = new JLabel();
		lLogo.setIcon(new ImageIcon(iLogo.getScaledInstance(95, 140, Image.SCALE_SMOOTH)));
		
		// 등록 전, 패널 디자인 수정
		pWest.setOpaque(true);
		pWest.setBackground(Color.DARK_GRAY);
		pWest.setPreferredSize(new Dimension(width / 3, height));
		pWest.setBorder(BorderFactory.createEmptyBorder(3, 0, 5, 20));
		
		// 판넬 등록
		pWest.add(lLogo, BorderLayout.CENTER);
		add(pWest, BorderLayout.WEST);
	}
	
	
	/**
	 * 물품 정보 등록
	 */
	private void setCenterPanel() {
		
		JPanel pCenter = new JPanel(null);
		
		/*
		 * 정보 표기 영역 
		 */
		
		// 이름 표기를 위한 컴포넌트 선언
		JLabel lName = new JLabel(name + "  /  " + price + " 원");
		
		// 디자인 설정
		lName.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		lName.setBounds(0, 20, width / 2, 20);
		lName.setForeground(Color.WHITE);
		
		
		
		/*
		 * 옵션 표기 영역 
		 */
		
		// 핫 & 아이스 선택을 위한 관련 컴포넌트 선언
		JLabel lTemp = new JLabel("핫 / 아이스");
		
		// 디자인 설정
		lTemp.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lTemp.setBounds(0, 50, 70, 20);
		lTemp.setForeground(Color.WHITE);
					
		// 옵션 콤보박스 선언 및 가운데 정렬 설정
		cbTemp = new JComboBox<String>();
		
		// 디자인 설정
		cbTemp.setBounds(80, 50, 85, 20);
		cbTemp.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		((JLabel) cbTemp.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		// cbTemp에 아이템 등록 
		// Ex) [ "ICE", "HOT" ]
		if (isSell) {
			for (Object _temp : option.keySet().toArray()) {
				cbTemp.addItem(_temp.toString());
			}
			
			// cbTemp에 이벤트 처리기 등록
			cbTemp.addActionListener(this);
		}
		else { cbTemp.setEditable(false); }
					
					
		// 사이즈 선택을 위한 관련 컴포넌트 선언
		JLabel lSize = new JLabel("사이즈");
		
		// 디자인 설정
		lSize.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lSize.setBounds(0, 80, 60, 20);
		lSize.setForeground(Color.WHITE);
		
		// 사이즈 콤보박스 선언 및 가운데 정렬 설정
		cbSize = new JComboBox<String>();
		
		// 디자인 설정
		cbSize.setBounds(80, 80, 85, 20);
		cbSize.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		((JLabel) cbSize.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		
		
		// 사이즈 선택 시 가격 재표기 이벤트 등록
		if (isSell) {
			cbSize.addActionListener(this);
		}
		else { cbSize.setEditable(false); }
		
		
		// 기본 사이즈로 가장 작은 사이즈를 선택
		setInitDefaultSize();
					
					
		// 수량 선택 관련 컴포넌트 추가 
		JLabel lQuantityName = new JLabel("수량");
		
		// 디자인 설정
		lQuantityName.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lQuantityName.setBounds(0, 110, 50, 20);
		lQuantityName.setForeground(Color.WHITE);
		
		// 수량 감소 버튼
		bQuantityMinus = new JButton("-");
		bQuantityMinus.setBounds(72, 110, 20, 20);
		
		// 디자인 설정
		bQuantityMinus.setBorder(null);
		bQuantityMinus.setFocusable(false);
		bQuantityMinus.setBackground(Color.DARK_GRAY);
		bQuantityMinus.setForeground(Color.WHITE);
		bQuantityMinus.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		
		// 버튼 클릭시 수량 감소
		if (isSell) {
			bQuantityMinus.addActionListener(this);
		}
		else { bQuantityMinus.setEnabled(false); }
		
		
		// 수량 표기 레이블
		lQuantity = new JLabel("0");
		
		// 디자인 설정
		lQuantity.setBounds(118, 110, 20, 20);
		lQuantity.setForeground(Color.WHITE);
		lQuantity.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		
		// 수량 증가 버튼
		bQuantityPlus = new JButton("+");
		bQuantityPlus.setBounds(148, 110, 20, 20);
		
		// 디자인 설정
		bQuantityPlus.setBorder(null);
		bQuantityPlus.setFocusable(false);
		bQuantityPlus.setBackground(Color.DARK_GRAY);
		bQuantityPlus.setForeground(Color.WHITE);
		bQuantityPlus.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		
		// 버튼 클릭시 수량 증가
		if (isSell) {
			bQuantityPlus.addActionListener(this);
		}
		else { bQuantityPlus.setEnabled(false); }
					
		
		/*
		 * pCenter 패널에 상기 모든 컴포넌트 등록
		 */
		
		// 품명 & 가격
		pCenter.add(lName);
		
		// 핫 & 아이스
		pCenter.add(lTemp);
		pCenter.add(cbTemp);
					
		// 사이즈
		pCenter.add(lSize);
		pCenter.add(cbSize);
					
		// 수량
		pCenter.add(lQuantityName);
		pCenter.add(bQuantityMinus);
		pCenter.add(lQuantity);
		pCenter.add(bQuantityPlus);
		
		
		// ItemPanel에 등록하기 전, 디자인 설정
		pCenter.setOpaque(true);
		pCenter.setBackground(Color.DARK_GRAY);
		pCenter.setPreferredSize(new Dimension(width / 2, height));
		
		// ItemPanel에 pCenter 패널 등록
		add(pCenter, BorderLayout.CENTER);
	}
	
	
	/**
	 * 물품 담기 등록
	 */
	private void setEastPanel() {
		
		JPanel pEast = new JPanel(new BorderLayout());
		
		// 장바구니에 담는 버튼 만들기
		bOrder = new JButton("담기");
		
		// 디자인 설정
		bOrder.setBorder(null);
		bOrder.setFocusable(false);
		bOrder.setBackground(Color.WHITE);
		bOrder.setForeground(Color.DARK_GRAY);
		bOrder.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		
		// 버튼 클릭시 이벤트 처리
		if (isSell) {
			bOrder.addActionListener(this);
		}
		else { bOrder.setEnabled(false); }
		
		
		// 버튼 등록
		pEast.add(bOrder, BorderLayout.CENTER);
		
		
		// ItemPanel에 등록하기 전, 디자인 설정
		pEast.setOpaque(true);
		pEast.setBackground(Color.WHITE);
		pEast.setPreferredSize(new Dimension(width / 7, height));
				
		// ItemPanel에 pCenter 패널 등록
		add(pEast, BorderLayout.EAST);
	}
	
	/**
	 * 사이즈 콤보박스를 초기화하고, 기본 사이즈로 설정하는 함수
	 */
	private void setInitDefaultSize() {
		
		// 사이즈 관련 콤보 박스의 목록을 지운다.
		cbSize.removeAllItems();
					
		// 핫 & 아이스 관련 콤보 박스에서 선택된 아이템이 아무것도 없다면 이벤트를 종료한다.
		if (cbTemp.getSelectedItem() == null) { return; }
					
		// 핫 & 아이스 관련 콤보 박스의 정보를 기준으로, 선택 가능한 사이즈 종류를 사이즈 관련 콤보 박스에 등록한다.
		for (Object _value : ((JSONObject) option.get(cbTemp.getSelectedItem().toString())).keySet().toArray()) {
			cbSize.addItem(_value.toString());
		}
		
		// 가장 작은 사이즈를 기본 값으로 선택하도록 한다.
		for (Object _value : ((JSONObject) option.get(cbTemp.getSelectedItem().toString())).keySet().toArray()) {
						
			String size = _value.toString();
			if (size.equals("SOLO") || size.equals("TALL")) {
				cbSize.setSelectedItem(size);
				break;
			}
		}
		
		// 초기화 완료 설정
		isInit = true;
	}
	
	
	/**
	 * cbTemp & cbSize & bOrder 액션 리스너 이벤트 처리
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		// cbTemp 이벤트 처리
		if (e.getSource().equals(cbTemp)) {
			setInitDefaultSize();
		}
		
		// cbSize 이벤트 처리
		if (e.getSource().equals(cbSize)) {

			// 초기화가 완료된 이후부터 동작하도록 설정
			if (!isInit) { return; }
			
			// 초기 화면에서 핫 / 아이스 재선택시 발생하는 오류 해결
			if (cbSize.getItemCount() == 0) { 
				isInit = false;
				return; 
			}
			
			// 사이즈 및 옵션 가격 설정
			String size = cbSize.getSelectedItem().toString();
			String optionPrice = ((JSONObject) option.get(cbTemp.getSelectedItem().toString())).get(cbSize.getSelectedItem().toString()).toString();
			
			// 기본 사이즈인 경우 주의사항 무시
			if (size.equals("SOLO") || size.equals("TALL")) { return; }
			
			// 주의사항 표기
			JOptionPane.showMessageDialog(
					null, 
					cbSize.getSelectedItem().toString() + " 사이즈를 선택하셨습니다.\n" +
					"음료 한잔당 " + optionPrice + " 원이 부과됩니다.\n" +
					"\n※ 해당 옵션은 선택한 수량에 모두 동일하게 적용됩니다.", "JavaBean - 사이즈 선택", JOptionPane.WARNING_MESSAGE);

		}
		
		// bQuantityPlus 이벤트 처리
		if (e.getSource().equals(bQuantityPlus)) {
			int quantity = Integer.parseInt(lQuantity.getText());
			
			if (quantity + 1 >= 100) { return; }
			lQuantity.setText(String.valueOf(++quantity));
		}

		// bQuantityMinus 이벤트 처리
		if (e.getSource().equals(bQuantityMinus)) {
			int quantity = Integer.parseInt(lQuantity.getText());
			
			if (quantity - 1 < 0) { return; }
			lQuantity.setText(String.valueOf(--quantity));
		}
		
		// bOrder 이벤트 처리
		if (e.getSource().equals(bOrder)) {
			
			// 수량 미선택시 안내 메시지 출력
			if (lQuantity.getText().equals("0")) {
				
				// 주의사항 표기
				JOptionPane.showMessageDialog(
						null, 
						"수량이 올바르지 않습니다.",
						"JavaBean - 경고", 
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// 초기화 완료 변수를 false로 설정
			isInit = false;
			
			// 장바구니에 담기 위한 배열 변수 선언
			String[] item = new String[7];
			
			// 정보 입력
			// 품명, 핫 or 아이스, 사이즈, 가격, 옵션단가, 수량, 합계 ((가격 + 옵션단가) * 수량)
			item[0] = name;
			item[1] = cbTemp.getSelectedItem().toString();
			item[2] = cbSize.getSelectedItem().toString();
			item[3] = price;
			item[4] = ((JSONObject) option.get(cbTemp.getSelectedItem().toString())).get(item[2]).toString();
			item[5] = lQuantity.getText();
			item[6] = String.valueOf((Integer.parseInt(item[3].toString()) + Integer.parseInt(item[4].toString())) * Integer.parseInt(item[5].toString()));

			// 주문 클래스에 작업 설정
			JBOrderPanel pOrder = new JBOrderPanel();
			
			// 주문 추가
			pOrder.setRow(item);
			
			// 오토 스크롤 기능 ON	
			pOrder.setAutoScroll(true);
			
			// 불필요한 정보 리셋
			setInitDefaultSize();
			lQuantity.setText("0");
			
			// 화면 갱신
			revalidate();
		}
	}
}
