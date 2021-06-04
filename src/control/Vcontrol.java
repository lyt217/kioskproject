package control;

/**
 * GUI에서 하는 대부분의 일들을 처리해주는 중앙집권클래스이다.
 * 하는 일 순서도 
 * 
 //00. 바깥의 프레임에서 메인프레임을 보이게 한다.
 //01. 컨트롤타워 싱글톤만들기
 //02. 새로운 자리받는 메소드
 //03. 컴퓨터 켜짐 from HostPcServer
 //04. 컴퓨터 꺼짐 from HostPcServer
 //05. 로그인 처리 from HostPcServer
 //06. 로그아웃처리 from HostPcServer
 //07. 나머지 따옴표 처리 from HostPcServer
 //08. 계속 좌석 계산해주고 바로 밑에 센드 from Seat
 //09. 계산메소드 from HostPcServer
 //10. 클라이언트로 부터 받은 메시지
 //11. 단체계산
 * 유지보수 일지
 * 여기서 모든 Login HashMap을 가지고 있기로 하였다.
 * 번호 파라미터 하나만 받아서 모두 처리하도록.. 
 */
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import assets.DBConnectionMgr;
import control.manage_member.dbprocess.JoinMemberProcess;
import control.manage_member.dbprocess.ReadMemberProcess;
import control.manage_store.dbprocess.StoreInfoProcess;
import model.People;
import model.Seat;
import model.Store;
import view.Login_Fr;
import view.Manage;
import view.Manage_Fr;
import view.CalculatorFrame;
import view.MsgToCustomer;
import view_HUD.Login_Fr_Hud;
import view_HUD.Manage_Fr_Hud;

import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;


import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Vcontrol {
	Socket socket;
	DataOutputStream out;

	// 핵심 필드 : 좌석 객체모델을 저장함과 동시에 소켓을 저장한다.
	public HashMap<Seat, Socket> clients 
		= new HashMap<Seat, Socket>();
	public Seat[] pcseat = new Seat[50];
	static Manage mf;
	
	public static MsgToCustomer[] chatClient = new MsgToCustomer[50];
	static Login_Fr login_Fr;
	static Login_Fr_Hud login_Fr_Hud;

	static Manage_Fr_Hud manage_Fr_Hud;
	static String storeName = "";
	public boolean inManagementMode = false;
	private static Store thisStore;
	public static void main(String[] args) {
		InetAddress ip = null;
		try{
			
//				final DatagramSocket socket = new DatagramSocket()){
			
			//FOR WINDOWS//
//		  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
//		  ip = socket.getLocalAddress().getHostAddress();
		  
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress("google.com", 80));
			ip = socket.getLocalAddress();
			String externalAddress = getIp();
			
			socket.close();
			
			String internalAddress = ip.toString().replace("/", "");
			
			thisStore = StoreInfoProcess.checkStore(externalAddress, internalAddress);
			storeName = thisStore.getStoreName();
			
			
			System.out.println(storeName+" FROM IP ADDRESS : "+internalAddress+" / "+externalAddress);
		} catch(Exception e) {
			
		}
		
		if(storeName.equals("")) {
			JOptionPane.showMessageDialog(null, "등록되지 않은 PC방입니다.", "키오스크를 실행할 수 없습니다.",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		else {
	
			mainFrameHUD();
		}
	
		// login_Fr_Hud = new Login_Fr_Hud();
		// login_Fr_Hud.dispose();
		// login_Fr_Hud.setUndecorated(true);
		// login_Fr_Hud.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		}
//		scanner.close();
		

//		try {
//			TimeUnit.SECONDS.sleep(1);
//			login_Fr_Hud.setVisible(true);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
	}

	// 00.바깥의 프레임에서 메인프레임을 보이게 한다.
	public void mainFrame() {
		mf = new Manage_Fr();
		login_Fr.dispose();
		Thread host = new HostPcServer();
		host.start();
	}

	public static void mainFrameHUD() {
		mf = new Manage_Fr_Hud();
		

		
//		login_Fr_Hud.dispose();
		Thread host = new HostPcServer();
		host.start();
	}

	// 메인 관리 프레임선택 (삽질한 것)
	public void selectFrame(int i) {
		if (i == 1)
			mf = new Manage_Fr_Hud();
		else
			mf = new Manage_Fr();
	}

	// 01.컨트롤타워 싱글톤만들기
	private static Vcontrol instance = new Vcontrol();

	public static Vcontrol getInstance() {
		System.out.println("브이컨트롤호출");
		return instance;
	}

	public static Vcontrol getInstance(String s) {
		System.out.println(s + "에서 브이컨트롤호출");
		return instance;
	}

	private Vcontrol() {
		System.out.println("브이컨트롤 : 브이컨트롤 가동 ");
		// 동기화시켜서 쓰레드간 비동기화발생하지않도록
		Collections.synchronizedMap(clients);
	}

	// 02.새로운 자리받는 메소드
	public void newSeat(int num, String name, Socket socket) {
		pcseat[num] = new Seat(num, name);
		clients.put(pcseat[num], socket);
	}

	// 03.컴퓨터켜짐 from HostPcServer
	public void turnOn(int num, int hour) {
		System.out.println("브이컨트롤  : 턴온시작!");
		mf.pan[num].setBackground(Color.white);
		// mf.pan[num].label[1].setText("자리 켜짐");
		mf.pan[num].turnOn();
		ddaom(num);

	}

	// 04.컴퓨터 꺼짐 from HostPcServer
	public void turnOff(int num) {
		System.out.println("자리꺼짐입장");
		mf.pan[num].setBackground(Color.gray);
		mf.pan[num].label[0].setText("");
		ddaom(num);
		mf.pan[num].turnOff();
	}

	// 05.로그인 처리 from HostPcServer
	public void login(int num, String name) {
		int money = pcseat[num].getMoney();
		mf.pan[num].setBackground(Color.blue);
		// mf.pan[num].label[0].setForeground(Color.red);
		// mf.pan[num].label[0].setText((num + 1) + ". 로그인");
		// mf.pan[num].label[1].setText(name + "님");
		// mf.pan[num].label[2].setText("");
		// mf.pan[num].label[3].setText(money + ""+ "원");
		mf.pan[num].isLogined = true;
		mf.pan[num].nickname = name;
		pcseat[num].setUsername(name);
		pcseat[num].setLogin(true);
		if (!pcseat[num].isFirst()) {
			System.out.println("실험: 첫번째 실행이 된 적이 없으므로 새로 스타트");
			pcseat[num].setFirst(true);
			pcseat[num].start();
		}

	}

	// 06.로그아웃처리 from HostPcServer
	public void logout(int num) {
		String userId = pcseat[num].getUserame();
		System.out.println("Vcontrol : " + num + "번째 자리를 로그아웃시킬려합니다.");
		pcseat[num].interrupt();
		mf.pan[num].setBackground(Color.white);
		mf.pan[num].label[0].setForeground(new Color(36, 205, 198));
		mf.pan[num].label[0].setText("");
		ddaom(num);
		mf.pan[num].isLogined = false;
		// 인터럽트시켜서 요금 올리기 중지
		pcseat[num].interrupt();
		pcseat[num].setLogin(false);

		try {
			
			socket = clients.get(pcseat[num]);
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("로그아웃");
			// 계산메소드
			this.payOff(pcseat[num]);
			// 클라이언트에서 없애기
			clients.remove(pcseat[num]);
			
			boolean logout = JoinMemberProcess.lastLogout(userId);
			if(logout == false) {
				System.out.println("db 로그아웃 처리 실패 ");
			}
		} catch (IOException e) {
			System.out.println("브이컨트롤 : 로그아웃 메시지 보내는 데 실패함");
		}

	}

	// 07.나머지 따옴표 처리 from HostPcServer
	public void ddaom(int num) {
		// mf.pan[num].label[1].setText("");
		// mf.pan[num].label[2].setText("");
		// mf.pan[num].label[3].setText("");

	}

	// 08. 계속 좌석 계산해주고 바로 밑에 센드 from Seat
	public void continueMoney(int num, Calendar date) {
		System.out.println("CONTINUEMONEY (SEAT : "+String.valueOf(num)+") ");
		int money = pcseat[num].getMoney();
		// mf.pan[num].label[3].setText(money + "원");

		Calendar dateAfter = Calendar.getInstance();
		dateAfter.setTimeInMillis(System.currentTimeMillis());

		long differ = (dateAfter.getTimeInMillis() - date.getTimeInMillis()) / (1000);
		long differ_hour = differ / 60;

		String gametime = differ_hour + ":" + (differ % 60) + "초";
//		mf.pan[num].label[2].setText(gametime);

		// 바로 밑에 클라이언트로 사용시간 보내주는 메소드 실행
		sendTime(pcseat[num], money, gametime);
	}

	public void sendTime(Seat pcseat, int money, String gametime) {
		try {
			int remainsecond = ReadMemberProcess.remainSecond(pcseat.getUserame());
			
			if(remainsecond < 0) {
				logout(pcseat.getNum_seat());
			}
			else {
				long hour = (long) remainsecond / 3600;
				long minute = (remainsecond % 3600) / 60;
				int second = remainsecond % 60;
				
				if(hour > 0) {
					gametime = hour + "시간 "+minute+"분 ";
				}
				else {
					gametime = minute+"분 ";
				}
				out = new DataOutputStream(clients.get(pcseat).getOutputStream());
				out.writeUTF("요금정보");
				out.writeInt(money);
				out.writeUTF(gametime);

				int num = pcseat.getNum_seat();
				mf.pan[num].label[0].setText("남은시간 : "+gametime);

			}
		} catch (IOException e) {
			System.out.println("브이컨트롤 : 요금정보 메시지 보내는데 애로사항이 꽃핀다. ");
			logout(pcseat.getNum_seat());
		}
	}

	// 09. 계산메소드 from HostPcServer
	public void payOff(Seat pcseat) {
		int mileage = (int) (pcseat.getMoney() * 0.1);
		String nick = pcseat.getUserame();

		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		DBConnectionMgr pool = null;

		try {
			pool = DBConnectionMgr.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			con = pool.getConnection();
			sql = "UPDATE member SET mileage = ? WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, mileage);
			pstmt.setString(2, nick);
			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			pool.freeConnection(con, pstmt);
		}
	}

	// 10. 클라이언트로 부터 받은 메시지
	public void messageFromPC(int num, String message) {
		if (chatClient[num] == null)
			chatClient[num] = new MsgToCustomer(num);
		chatClient[num].setVisible(true);
		chatClient[num].ta.append(message);
	}

	// 11. 단체계산
	public void groupPayOff(int x, int num) {
		ArrayList<People> peoples = new ArrayList<People>();

		if (x == 1) {
			peoples.add(new People(num, pcseat[num].getUserame(),
					"", pcseat[num].getMoney()));
		} else {
			for (int a = 0; a < 50; a++) {
				if (mf.pan[a].isChecked && mf.pan[a].isLogined) {
					peoples.add(new People(a, pcseat[a].getUserame(),
							"", pcseat[a].getMoney()));
					mf.pan[a].checkOff();
				}
			}

		}
		new CalculatorFrame(peoples);
	}

	public boolean checkPassword(String password) {
		boolean flag = false;
		flag = StoreInfoProcess.checkPassword(password, storeName);
		
		return flag;
	}
	public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	public void test(int num) {
		System.out.println("브이컨트롤 : " + num);
	}
	
	public Store getStore() {
		return thisStore;
	}
}
