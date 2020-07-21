package manager;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 매니저 프로그램 중복 실행 방지를 위한 클래스
 */
public class Monitor {
 
    private static DatagramSocket isRun;
    
    /**
     * 소켓 점유 모니터링
     * @throws SocketException
     */
    public static void monitoring() throws SocketException {
            isRun = new DatagramSocket(1103);
    }
 
    /**
     * 모니터링 종료
     */
    public static void close() {
        if(isRun != null) {
            isRun.close();
        }
    }
}