package _client.view;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.Visibility;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import _client.dbprocess.GetComputer;
import control.DB_query;
import control.Vcontrol;
import control.manage_store.dbprocess.StoreInfoProcess;
import model.Computer;
import model.Store;

@SuppressWarnings("serial")
public class Login_Hud extends JFrame implements ActionListener {
	Vcontrol vc= Vcontrol.getInstance();
	// 이미지와 버튼은 전역변수설정
	BufferedImage img = null;
	JButton bt, btj;
	JTextField tf;
	JPasswordField tf2;
	Store thisStore;
	String internalAddress = "";
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ClientChat chat;
	protected static boolean doClient=true;
	// 메인
	public static void main(String[] args) {
		new Login_Hud();
	}

	// 생성자
	public Login_Hud() {
		// 프레임 기본 설정
		setTitle("회원 로그인");
		setSize(1440, 2560);
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationByPlatform( true );
		addWindowListener(getWindowAdapter());
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBackground(Color.black);
		setLayout(null);
		
		
		// 중앙사이즈조절
		int width = (Toolkit.getDefaultToolkit().getScreenSize().width - 1440) / 2;
		int height = (Toolkit.getDefaultToolkit().getScreenSize().height - 2560 ) / 2;
		setLocation(width, height);
		// 이미지 받아오기
		try {
			img = ImageIO.read(new File("img/member_login2.png"));

		} catch (IOException e) {
			System.out.println("이미지 불러오기 실패!");
			System.exit(0);
		}

		// 가장 큰 JLayer패널= 레이어를 순서대로 올려줌
		JLayeredPane lpane = new JLayeredPane();
		lpane.setBounds(0, 0, 1440, 2560);
		lpane.setLayout(null);

		// 첫번째 panel = 텍스트필드 tf, tf2 들어감.
		MyPanel panel = new MyPanel();
		panel.setBounds(0, 0, 1440, 2560);
		panel.setLayout(null);

		// 두번째 패널 panel2 = 버튼들어감 , 755 , 689
		JPanel panel2 = new JPanel();
		panel2.setLayout(null);
		panel2.setBounds(0, 0, 1440, 2560);
		panel2.setBackground(Color.black);
		panel2.setOpaque(false);

//		JLabel usernameLabel = new JLabel("아이디");
//		usernameLabel.setBounds(100, 1450, 280, 30);
//		usernameLabel.setForeground(Color.white);
//		panel2.add(usernameLabel);
		
		tf = new JTextField(15);
		tf.setOpaque(false);
		tf.setBounds(480, 1363, 280, 40);
//		tf.setBackground(Color.white);
		tf.setForeground(Color.black);
		tf.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
		panel2.add(tf);
//		
//		JLabel pwLabel = new JLabel("비밀번호");
//		pwLabel.setBounds(100, 1540, 280, 30);
//		pwLabel.setForeground(Color.white);
//		panel2.add(pwLabel);
		
		// 두번째 텍스트 731, 529  -> 891, 619
		tf2 = new JPasswordField(15);
		tf2.setOpaque(false);
		tf2.setBounds(480, 1450, 280, 40);
//		tf2.setBounds(100, 1900, 280, 30);
//		tf2.setBackground(Color.white);
		tf2.setForeground(Color.black);
		tf2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
		tf.setDocument(new JTextFieldLimit(10));
		tf2.setDocument(new JTextFieldLimit(10));
		panel2.add(tf2);

		bt = new JButton();//
		//new ImageIcon("img/btLogin_hud.png")); // 587 458 에
																// 위치해야한다.
//		bt.setBorderPainted(false);
//		bt.setFocusPainted(false);
//		bt.setContentAreaFilled(false);
		bt.setOpaque(false);
//		bt.setBackground(new Color(0x3EAF0E));
		bt.setBounds(566, 1526, 170, 60);
//		bt.setBounds(100, 2000, 104, 48);
		bt.addActionListener(this);
//		bt.setForeground(Color.white);
		bt.setBorderPainted( false );
		bt.setFocusPainted( false );
		bt.setContentAreaFilled( false );
		panel2.add(bt);
		
		

		btj = new JButton();//
		//new ImageIcon("img/btLogin_hud.png")); // 587 458 에
																// 위치해야한다.
//		btj.setBorderPainted(false);
//		btj.setFocusPainted(false);
//		btj.setContentAreaFilled(false);
		btj.setOpaque(false);
//		btj.setBackground(new Color(0xFEE500));
		btj.setBounds(300, 1526, 200, 60);
//		btj.setBounds(100, 2000, 104, 48);
		btj.addActionListener(this);
//		btj.setForeground(Color.black);
		btj.setBorderPainted( false );
		btj.setFocusPainted( false );
		btj.setContentAreaFilled( false );
		panel2.add(btj);
		

		// 패널들 프레임에 삽입
		lpane.add(panel, new Integer(0), 0);
		lpane.add(panel2, new Integer(1), 0);
		getContentPane().add(lpane);
		
		try{
			TimeUnit.SECONDS.sleep(1);
		} catch(Exception e){

		}
		
		// if(this.getExtendedState() == Frame.ICONIFIED) {
		setVisible(true);
		// }
		
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("google.com", 80));
			InetAddress ip = socket.getLocalAddress();
			String externalAddress = getIp();
			
			socket.close();
			
			internalAddress = ip.toString().replace("/", "");
			thisStore = StoreInfoProcess.checkStoreOnlyEx(externalAddress);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		new Thread(new ClientConnector()).start();
	}

	private class JTextFieldLimit extends PlainDocument // 텍스트 필드 글자수 제한을 위한 이너
	// 클래스 시작
	{
		private int limit; // 제한할 길이

		private JTextFieldLimit(int limit) // 생성자 : 제한할 길이를 인자로 받음
		{
			super();
			this.limit = limit;
		}

		// 텍스트 필드를 채우는 메써드 : 오버라이드
		public void insertString(int offset, String str, AttributeSet attr)
				throws BadLocationException {
			if (str == null)
				return;

			if (getLength() + str.length() <= limit)
				super.insertString(offset, str, attr);
		}
	}// 텍스트 필드 글자수 제한을 위한 이너 클래스 종료

	// 그림그리기 패널 (panel1)들어감
	class MyPanel extends JPanel {
		public void paint(Graphics g) {
			g.drawImage(img, 0, 0, null);
		}
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
	
	// 액션
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) { // 로그인 처리 부

		// 종태씨.. 아이디랑 비밀번호 안적혀있을때 매번 로그인 쿼리날리면 시간걸리니까
		// else 에 로그인 쿼리 넣어둘게요~

		if(e.getSource() == bt) {
			if (tf.getText().equals("") || String.valueOf(tf2.getPassword()).equals(""))// 아이디 비번
			// 입력했는
			// 지 검사
			{
				if (tf.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "아이디를 입력하세요", "아이디 입력",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (String.valueOf(tf2.getPassword()).equals("")) {
					JOptionPane.showMessageDialog(null, "비밀번호를 입력하세요", "비번 입력",
							JOptionPane.INFORMATION_MESSAGE);
				}
	
			} else {
				// 로그인 쿼리
				Computer computer = null;
				int existId = DB_query.loginMember(tf.getText(), String.valueOf(tf2.getPassword()), thisStore.getStoreName());
	
				if (existId == 1) // 로그인 가능 판별
					
				{
					computer = GetComputer.getComputer(internalAddress, thisStore.getStoreId());
					
					if(computer == null || computer.getId() == 0) {
						System.out.println("NO PC for IP : "+internalAddress);
						JOptionPane.showMessageDialog(null, "사용이 허가되지 않은 PC입니다.", "관리자에게 문의하세요.",
								JOptionPane.ERROR_MESSAGE);
					}
					else {
						System.out.println(String.valueOf(computer.getId())+" : "+computer.getInternalAddress()+" ("+String.valueOf(computer.getSeatNumber())+" 번 컴퓨터)");
						//로그인액션
						JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다.", "로그인 성공",
								JOptionPane.INFORMATION_MESSAGE);
						ClientPc.doClient=true;
						ClientPc cl = new ClientPc("비회원", String.valueOf((computer.getSeatNumber() - 1)), thisStore.getInternalAddress());
					}
					dispose();
					
				} else if(existId == 2){
					JOptionPane.showMessageDialog(null,"잔여시간이 5분 미만입니다.", "요금 충전 후 이용하세요.",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else {
				
					JOptionPane.showMessageDialog(null, "로그인에 실패하였습니다.", "로그인 실패",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		else if(e.getSource() == btj) {
			JoinMember join = new JoinMember();
		}
	}// 액션 끝

	private class ClientConnector implements Runnable {

		@Override
		public void run() {
			try {
				String serverIp = "192.168.0.201";// "172.168.0.80";
				socket = new Socket(InetAddress.getByName(serverIp), 7777);
				System.out.println("연결성공");
				in = new DataInputStream(new BufferedInputStream(
						socket.getInputStream()));
				out = new DataOutputStream(new BufferedOutputStream(
						socket.getOutputStream()));

				while (true) {
					String str = in.readUTF();
					// 이용요금 처리부
					if (str.equals("턴온")) {
						Computer computer = null;
						computer = GetComputer.getComputer(internalAddress, thisStore.getStoreId());
							
						if(computer == null || computer.getId() == 0) {
							System.out.println("NO PC for IP : "+internalAddress);
							JOptionPane.showMessageDialog(null, "사용이 허가되지 않은 PC입니다.", "관리자에게 문의하세요.",
							JOptionPane.ERROR_MESSAGE);
						}
						else {
							// System.out.println(String.valueOf(computer.getId())+" : "+computer.getInternalAddress()+" ("+String.valueOf(computer.getSeatNumber())+" 번 컴퓨터)");
							// //로그인액션
							// JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다.", "로그인 성공",
							// 		JOptionPane.INFORMATION_MESSAGE);
							ClientPc.doClient = true;
							ClientPc cl = new ClientPc(tf.getText(), String.valueOf((computer.getSeatNumber() - 1)), thisStore.getInternalAddress());
						}
						dispose();
					}
				}

			} catch (IOException e) {// 서버와 연결이 끊어질시 창이 변함

				if (chat != null) {
					chat.closeFrame();
				}
				doClient=false;
				dispose();
				ClientLogin cl = new ClientLogin();

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

	}// 클라이언트 커넥터종료
    private WindowAdapter getWindowAdapter() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {//overrode to show message
                super.windowClosing(we);

                // JOptionPane.showMessageDialog(clFrame, "Cant Exit");
            }

            @Override
            public void windowIconified(WindowEvent we) {
            	setState(JFrame.NORMAL);
            	
//                JOptionPane.showMessageDialog(clFrame, "Cant Minimize");
            }
        };
    }


}
