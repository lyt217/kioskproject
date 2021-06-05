package _client.view;

import java.awt.*;

import javax.swing.*;

import java.awt.event.*;
import java.net.*;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.*;

//현재 사용중인 피시방 이용자의 이용시간과 이용요금창을 보여준다
public class ClientPc {// 클라이언트 클래스 시작

	static Login_Hud login_Fr_Hud;
	
	private String id; // 현재 사용중인 아이디 저장
	private String pc; // 현재 사용중인 피시 번호 저장
	private String kiosk;
	private JFrame clFrame;
	private JLabel userId;
	private JLabel userPc;
	private JLabel userTime;
	private JLabel userPrice;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ClientChat chat;
	private Menu menu;
	protected static boolean doClient=true;
	ScheduledExecutorService executor;

	ClientPc(String id, String pc, String kioskIp) {// 클라이언트 생성자시작

		this.id = id;
		this.pc = pc;
		String originPc = String.valueOf(Integer.parseInt(pc) + 1);
		this.kiosk = kioskIp;
		clFrame = new JFrame("이용중");
		clFrame.setLocationByPlatform( true );
		clFrame.addWindowListener(getWindowAdapter());
		// 현재 스크린사이즈를 받아온다
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;

		// 표시라벨
		userId = new JLabel(id);
		userPc = new JLabel(originPc);
		userTime = new JLabel("");
		userPrice = new JLabel("");

		JLabel pc_label = new JLabel("피씨번호:");
		JLabel id_label = new JLabel("고객아이디:");
		JLabel time_label = new JLabel("잔여시간:");
		JLabel price_label = new JLabel("이용요금:");

		// 버튼
		JButton chatBtn = new JButton("채팅");
		JButton menuBtn = new JButton("이용종료");

		// 컴포넌트가 붙을 패널 생성
		JPanel panel = new JPanel();

		// 컴포넌트 배치부
		pc_label.setBounds(30, 30, 95, 30);
		id_label.setBounds(30, 5, 95, 30);
		time_label.setBounds(30, 55, 95, 30);
		price_label.setBounds(30, 80, 95, 30);
		userId.setBounds(130, 5, 95, 30);
		userPc.setBounds(130, 30, 95, 30);
		userTime.setBounds(130, 55, 95, 30);
		userPrice.setBounds(130, 80, 95, 30);
		chatBtn.setBounds(30, 120, 95, 30);
		menuBtn.setBounds(150, 120, 95, 30);

		// 컴포넌트 결합부
		panel.add(pc_label);
		panel.add(id_label);
		panel.add(time_label);
		panel.add(userId);
		panel.add(userTime);
		panel.add(userPc);
		panel.add(userPrice);
		// panel.add(chatBtn);
		panel.add(menuBtn);
//		panel.add(price_label);
		panel.setLayout(null);
		clFrame.add(panel);

		// 버튼 이벤트 처리부
		chatBtn.addActionListener(new ChatEvent());
		menuBtn.addActionListener(new MenuEvent());

		// 현재 프레임 위치 및 크기
		clFrame.setBounds(width - 300, height / 5 - 100, 270, 200);
		clFrame.setResizable(false);

		// 유저가 창을 강제 종료시키면 안되므로
		clFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		clFrame.setAlwaysOnTop(true);
		clFrame.setVisible(true);
		// 소켓 쓰레드시작
		new Thread(new ClientConnector()).start();

		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(timerRunnerble, 0, 10, TimeUnit.SECONDS);
		

	}// 클라이언트 생성자종료

	// 챗이벤트클래스 시작(채팅창을 불러온다)
	private class ChatEvent implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {

			if (chat == null) {
				chat = new ClientChat(out, pc);
				return;
			}
			chat.chatFrameVisible();

		}

	}
	// 챗이벤트 클래스 종료
	// 메뉴표를 불러오기 위한 이벤트 클래스 시작
	private class MenuEvent implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
//			Menu menu = new Menu(out,Integer.parseInt(pc));
			try {	
				if(out == null) {
					out = new DataOutputStream(new BufferedOutputStream(
							socket.getOutputStream()));
				}
				int pcNum = Integer.parseInt(pc);

				System.out.println("로그아웃 시도 : "+String.valueOf(pcNum));
				
				String message = String.valueOf(pcNum)+" 번 PC 이용을 종료하시겠습니까?";
				int result = JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.CLOSED_OPTION) {
					
				}
				else if(result == JOptionPane.YES_OPTION) {
					out.writeInt(pcNum);
					out.writeUTF(id);
					out.writeUTF("로그아웃");
					out.flush();
				}
				else {
					
				}

				// socket.close();
				
//				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

	// 메뉴표를 불러오기 위한 이벤트 클래스 종료
	// 서버와 연결을 위한 클라이언트 커넥터
	private class ClientConnector implements Runnable {

		@Override
		public void run() {
			try {
				String serverIp = kiosk;	//"222.118.199.105";// "172.168.0.80";
				
				System.out.println("KIOSK IP : "+serverIp);
				socket = new Socket(InetAddress.getByName(serverIp), 7777);
				System.out.println("연결성공");
				in = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
				out = new DataOutputStream(new BufferedOutputStream(
						socket.getOutputStream()));

				int pcNum = Integer.parseInt(pc);
				out.writeInt(pcNum);
				out.writeUTF(id);
				out.writeUTF("로그인");
				out.flush();

				while (true) {
					String str = in.readUTF();
					// 이용요금 처리부
					if (str.equals("요금정보")) {
						Integer money = in.readInt();
//						userPrice.setText(money.toString());
						userTime.setText(in.readUTF());
					}
					// 채팅메시지 처리부
					if (str.equals("메시지")) {
						String msg = in.readUTF();
						System.out.println(msg);
						if (chat == null) {
							chat = new ClientChat(out, pc);
						}
						chat.chatFrameVisible();
						chat.addChat(msg);

					}
					// 로그아웃 처리부
					if (str.equals("로그아웃")) {
						Robot r = new Robot();
						r.setAutoDelay(250);
						r.keyPress(KeyEvent.VK_HOME);
						
						// doClient = false;
						executor.shutdown();
						
						try{
							TimeUnit.SECONDS.sleep(1);
						}	catch(Exception e) {
							
						}
						
						socket.close();
						
						
						try{
							TimeUnit.SECONDS.sleep(1);
						}	catch(Exception e) {
							
						}
						
						clFrame.dispose();
						// login_Fr_Hud = new Login_Hud();
						
					}
				}

			} catch (IOException e) {// 서버와 연결이 끊어질시 창이 변함

				if (chat != null) {
					chat.closeFrame();
				}
				// if(doClient == true){
				doClient=false;
				clFrame.dispose();
				// ClientLogin cl = new ClientLogin();
				login_Fr_Hud = new Login_Hud();
				// }

			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}
//	 public static final String getActiveWindowText() {
//        long /*int*/ handle = OS.GetForegroundWindow();
//        int length = OS.GetWindowTextLength(handle);
//        if(length == 0) return "";
//        /* Use the character encoding for the default locale */
//        TCHAR buffer = new TCHAR(0, length + 1);
//        OS.GetWindowText(handle, buffer, length + 1);
//        return buffer.toString(0, length);
//    }
	 
	 // 클라이언트 커넥터종료
    private WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {//overrode to show message
                super.windowClosing(we);

                JOptionPane.showMessageDialog(clFrame, "Cant Exit");
            }

            @Override
            public void windowIconified(WindowEvent we) {
            	clFrame.setState(JFrame.NORMAL);
//                JOptionPane.showMessageDialog(clFrame, "Cant Minimize");
            }
        };
    }
    
    Runnable timerRunnerble = new Runnable() {
		public void run() {
			try {
				out.writeUTF("요금확인");
				out.writeInt(Integer.parseInt(pc));
				out.flush();
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   System.out.println("Hello World!"); 
		}
    };
	
}// 클라이언트 클래스 종료
