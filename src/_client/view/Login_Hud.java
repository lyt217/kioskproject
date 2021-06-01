package _client.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

import control.DB_query;
import control.Vcontrol;

@SuppressWarnings("serial")
public class Login_Hud extends JFrame implements ActionListener {
	Vcontrol vc= Vcontrol.getInstance();
	// 이미지와 버튼은 전역변수설정
	BufferedImage img = null;
	JButton bt, btj;
	JTextField tf;
	JPasswordField tf2;
	

	// 메인
	public static void main(String[] args) {
		new Login_Hud();
	}

	// 생성자
	public Login_Hud() {
		// 프레임 기본 설정
		setTitle("회원 로그인");
		setSize(1440, 2560);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBackground(Color.black);
		setLayout(null);
		// 중앙사이즈조절
		int width = (Toolkit.getDefaultToolkit().getScreenSize().width - 1440) / 2;
		int height = (Toolkit.getDefaultToolkit().getScreenSize().height - 2560 ) / 2;
		setLocation(width, height);
		// 이미지 받아오기
		try {
			img = ImageIO.read(new File("img/member_login.jpg"));

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

		JLabel usernameLabel = new JLabel("아이디");
		usernameLabel.setBounds(100, 1450, 280, 30);
		usernameLabel.setForeground(Color.white);
		panel2.add(usernameLabel);
		
		tf = new JTextField(15);
		tf.setBounds(100, 1480, 280, 50);
//		tf.setBounds(100, 1800, 280, 50);
		tf.setBackground(Color.white);
		tf.setForeground(Color.black);
		tf.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
		panel2.add(tf);
		
		JLabel pwLabel = new JLabel("비밀번호");
		pwLabel.setBounds(100, 1540, 280, 30);
		pwLabel.setForeground(Color.white);
		panel2.add(pwLabel);
		
		// 두번째 텍스트 731, 529  -> 891, 619
		tf2 = new JPasswordField(15);
		tf2.setBounds(100, 1570, 280, 50);
//		tf2.setBounds(100, 1900, 280, 30);
		tf2.setBackground(Color.white);
		tf2.setForeground(Color.black);
		tf2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5,5,5,5));
		tf.setDocument(new JTextFieldLimit(10));
		tf2.setDocument(new JTextFieldLimit(10));
		panel2.add(tf2);

		bt = new JButton("로그인");//
		//new ImageIcon("img/btLogin_hud.png")); // 587 458 에
																// 위치해야한다.
//		bt.setBorderPainted(false);
//		bt.setFocusPainted(false);
//		bt.setContentAreaFilled(false);
		bt.setOpaque(true);
		bt.setBackground(new Color(0x3EAF0E));
		bt.setBounds(100, 1640, 135, 48);
//		bt.setBounds(100, 2000, 104, 48);
		bt.addActionListener(this);
		bt.setForeground(Color.white);
		panel2.add(bt);
		
		

		btj = new JButton("회원가입");//
		//new ImageIcon("img/btLogin_hud.png")); // 587 458 에
																// 위치해야한다.
//		btj.setBorderPainted(false);
//		btj.setFocusPainted(false);
//		btj.setContentAreaFilled(false);
		btj.setOpaque(true);
		btj.setBackground(new Color(0xFEE500));
		btj.setBounds(245, 1640, 135, 48);
//		btj.setBounds(100, 2000, 104, 48);
		btj.addActionListener(this);
		btj.setForeground(Color.black);
		panel2.add(btj);
		

		// 패널들 프레임에 삽입
		lpane.add(panel, new Integer(0), 0);
		lpane.add(panel2, new Integer(1), 0);
		getContentPane().add(lpane);
		setVisible(true);
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
				boolean existId = DB_query.loginMember(tf.getText(), String.valueOf(tf2.getPassword()));
	
				if (existId == true) // 로그인 가능 판별
					
				{
					//로그인액션
					JOptionPane.showMessageDialog(null, "로그인에 성공하였습니다.", "로그인 성공",
							JOptionPane.INFORMATION_MESSAGE);
					ClientPc.doClient=true;
					ClientPc cl = new ClientPc(tf.getText(), "49");
					
					dispose();
					
				} else {
					JOptionPane.showMessageDialog(null, "로그인에 실패하였습니다.", "로그인 실패",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		else if(e.getSource() == btj) {
			JoinMember join = new JoinMember();
		}
	}// 액션 끝
}
