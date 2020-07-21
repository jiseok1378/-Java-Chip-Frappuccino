import customer.Customer;
import manager.Manager;

public class Main {

	/**
	 * 사용자 프로그램 및 매니저 프로그램 실행
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 이곳에 메인 프레임을 선언해주세요.
		// 나머지 프레임 및 기타 작업들은 패키지에 맞게 클래스 선언해서 사용해주세요.
		// 이곳은 프로그램 실행을 위한 메인함수만 작성하도록 합니다.
		
		// 커스토머용 프로그램 시작
		new Customer(" JavaBean 1.1.0v - 카페에 오신걸 환영합니다.", 640, 800);
		
		// 매니저용 프로그램 시작
		new Manager(" JavaBean 1.1.0v - 관리자 프로그램", 800, 500);
		
		// 둘 중 한 프로그램을 종료하면 나머지 프로그램이 동시에 종료됩니다.
		// 따라서 개별적으로 실행하기 위해서는 customer.Customer 클래스 
		// manager.Manager 클래스 파일에서 개별적으로 실행시켜주시기 바랍니다.
	}
}
