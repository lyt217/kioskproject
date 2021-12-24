package view_HUD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import assets.RoundedBorder;
import view.Join;
import view.Manage;
import view.MenuSet;
import view.Payment;
import view.SalesToday;
import view.manage_member.ManageMember;
import control.HostPcServer;
import control.Vcontrol;
import control.manage_store.dbprocess.StoreInfoProcess;

public class Control_Fr_Hud extends Manage implements ActionListener {

	private static final long serialVersionUID = 1L;
	Vcontrol vcm;

	JLabel title, changepwtitle, timetitle, calculatetitle, bonustitle;
	JTextField tf2, tf3, tf4;
	String inputPassword = "";
	JPanel panel, pan_navi, pan_clock, adminPWPanel, adminTimePanel;
	public JButton bt[] = new JButton[10]; // 네비게이션 버튼 4개(화면, 회원, 재고, 매상)
	public JPanel seat50, adminPanel; // 50개 패널을 담기 위한 그릇
	int pX, pY;
	int x = 0, y = 0; // 좌표 계속 움직이게 해주는 x, y
	int sx = 92, sy = 0;
	int manageCount = 0;
	long millisec = 0;
	JPopupMenu popup;
	JMenuItem allOnSeat, allOffSeat, turnOnSeat, turnOffSeat, calculSeat;
	JPanel pan_imgClock;
	Image image, image2, image3;
	Image img;

	public Control_Fr_Hud control_fr_hud;
	static Payment_Hud paymentHud;
	
	public Control_Fr_Hud() {
		vcm =  Vcontrol.getInstance("매니지프레임HUD");
		// 프레임 초기 설정
		setSize(1920, 1080);
		setTitle("MainFrame_HUD");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setBackground(Color.BLACK);

		// 프레임 화면 중앙 배열
		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		// 가장 큰 JLayer패널= 레이어를 순서대로 올려줌
		JLayeredPane lpane = new JLayeredPane();
		lpane.setBounds(0, 0, 1920, 1080);
		lpane.setLayout(null);

		// 배경패널
		panel = new MyPanel("img/mainHud_back.png");
		panel.setLayout(null);
		panel.setBounds(0, 0, 1920, 1080);

		Font f = new Font("Serif", 1, 35);
		Font font1 = new Font("SansSerif", Font.BOLD, 60);
		
		title = new JLabel("관리자페이지");
		title.setBounds(127, 41, 400, 70);
		title.setFont(f);
		title.setForeground(Color.white);
		panel.add(title);
		

		changepwtitle = new JLabel("비밀번호 변경");
		changepwtitle.setBounds(127, 221, 400, 70);
		changepwtitle.setFont(f);
		changepwtitle.setForeground(Color.white);
		panel.add(changepwtitle);

		timetitle = new JLabel("시간당 이용금액");
		timetitle.setBounds(127, 460, 400, 70);
		timetitle.setFont(f);
		timetitle.setForeground(Color.white);
		panel.add(timetitle);

		bonustitle = new JLabel("시간관리");
		bonustitle.setBounds(127, 730, 400, 70);
		bonustitle.setFont(f);
		bonustitle.setForeground(Color.white);
		panel.add(bonustitle);

		calculatetitle = new JLabel("정산");
		calculatetitle.setBounds(1100, 460, 400, 70);
		calculatetitle.setFont(f);
		calculatetitle.setForeground(Color.white);
		panel.add(calculatetitle);
		
		bt[0] = new JButton("설정");
		bt[1] = new JButton("취소");
		bt[2] = new JButton("설정");
		bt[3] = new JButton("취소");
		bt[4] = new JButton("내역확인");
		bt[5] = new JButton("정산초기화");
		bt[6] = new JButton("지우기");
		bt[7] = new JButton("추가시간");
		bt[8] = new JButton("시간회수");
		

		bt[0].setBorderPainted(false);
		bt[0].setFocusPainted(false);
		bt[0].addActionListener(this);
		bt[0].setForeground(Color.white);
		bt[0].setBounds(1190, 310, 150, 80);
		bt[0].setFont(f);
		bt[0].setOpaque(true);
		bt[0].setBackground(new Color(51, 202, 253));
//		bt[0].setBorder(new RoundedBorder(10));
		panel.add(bt[0]);

		bt[1].setBorderPainted(false);
		bt[1].setFocusPainted(false);
		bt[1].setContentAreaFilled(false);
		bt[1].addActionListener(this);
		bt[1].setForeground(Color.white);
		bt[1].setBounds(1350, 310, 150, 80);
		bt[1].setFont(f);
		bt[1].setOpaque(true);
		bt[1].setBackground(new Color(22, 86, 107));
		panel.add(bt[1]);


		bt[2].setBorderPainted(false);
		bt[2].setFocusPainted(false);
		bt[2].addActionListener(this);
		bt[2].setForeground(Color.white);
		bt[2].setBounds(615, 550, 150, 80);
		bt[2].setFont(f);
		bt[2].setOpaque(true);
		bt[2].setBackground(new Color(51, 202, 253));
//		bt[0].setBorder(new RoundedBorder(10));
		panel.add(bt[2]);

		bt[3].setBorderPainted(false);
		bt[3].setFocusPainted(false);
		bt[3].setContentAreaFilled(false);
		bt[3].addActionListener(this);
		bt[3].setForeground(Color.white);
		bt[3].setBounds(775, 550, 150, 80);
		bt[3].setFont(f);
		bt[3].setOpaque(true);
		bt[3].setBackground(new Color(22, 86, 107));
		panel.add(bt[3]);
		
		
		bt[4].setBorderPainted(false);
		bt[4].setFocusPainted(false);
		bt[4].addActionListener(this);
		bt[4].setForeground(Color.white);
		bt[4].setBounds(1175, 550, 220, 80);
		bt[4].setFont(f);
		bt[4].setOpaque(true);
		bt[4].setBackground(new Color(51, 202, 253));
//		bt[0].setBorder(new RoundedBorder(10));
		panel.add(bt[4]);

		bt[5].setBorderPainted(false);
		bt[5].setFocusPainted(false);
		bt[5].setContentAreaFilled(false);
		bt[5].addActionListener(this);
		bt[5].setForeground(Color.white);
		bt[5].setBounds(1405, 550, 285, 80);
		bt[5].setFont(f);
		bt[5].setOpaque(true);
		bt[5].setAlignmentX(CENTER_ALIGNMENT);
		bt[5].setBackground(new Color(22, 86, 107));
		panel.add(bt[5]);

		
		bt[6].setBorderPainted(false);
		bt[6].setFocusPainted(false);
		bt[6].addActionListener(this);
		bt[6].setForeground(Color.white);
		bt[6].setBounds(1465, 730, 220, 80);
		bt[6].setFont(f);
		bt[6].setOpaque(true);
		bt[6].setBackground(new Color(51, 202, 253));
//		bt[0].setBorder(new RoundedBorder(10));
		panel.add(bt[6]);

		
		bt[7].setBorderPainted(false);
		bt[7].setFocusPainted(false);
		bt[7].addActionListener(this);
		bt[7].setForeground(Color.white);
		bt[7].setBounds(1000, 845, 220, 80);
		bt[7].setFont(f);
		bt[7].setOpaque(true);
		bt[7].setBackground(new Color(51, 202, 253));
//		bt[0].setBorder(new RoundedBorder(10));
		panel.add(bt[7]);

		bt[8].setBorderPainted(false);
		bt[8].setFocusPainted(false);
		bt[8].addActionListener(this);
		bt[8].setForeground(Color.white);
		bt[8].setBounds(1230, 845, 220, 80);
		bt[8].setFont(f);
		bt[8].setOpaque(true);
		bt[8].setBackground(new Color(51, 202, 253));
//		bt[0].setBorder(new RoundedBorder(10));
		panel.add(bt[8]);

		tf2 = new RoundJTextField(4);
		tf2.setBounds(545, 310, 604, 80); 
		tf2.setFont(font1);
		tf2.setForeground(Color.black);
		tf2.setBackground(new Color(193, 239, 254));
		tf2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		tf2.setDocument(new JTextFieldLimit(4));
		panel.add(tf2);


		tf3 = new RoundJTextField(4);
		tf3.setBounds(200, 550, 375, 80); 
		tf3.setFont(font1);
		tf3.setForeground(Color.black);
		tf3.setBackground(new Color(193, 239, 254));
		tf3.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		tf3.setDocument(new JTextFieldLimit(4));
		PlainDocument doc = (PlainDocument) tf3.getDocument();
		doc.setDocumentFilter(new MyIntFilter());
		panel.add(tf3);
		
		tf3.setText(String.valueOf(vcm.getStore().getKrwPerHour()));


		tf4 = new RoundJTextField(4);
		tf4.setBounds(545, 845, 440, 80); 
		tf4.setFont(font1);
		tf4.setForeground(Color.black);
		tf4.setBackground(new Color(193, 239, 254));
		tf4.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		tf4.setDocument(new JTextFieldLimit(4));
		panel.add(tf4);
		

		//비밀번호패널
		adminPWPanel = new MyPanel("img/btn_password.png");
		adminPWPanel.setLayout(null);
		adminPWPanel.setBounds(545, 210, 909, 73);
		adminPWPanel.setOpaque(false);
		adminPWPanel.addMouseListener(new MouseListener() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	int xPos = e.getLocationOnScreen().x;
		    	xPos = xPos - 40;
		    	if(xPos >= 508 && xPos <= 572) {
		    		// 0
		    		inputPassword = inputPassword + "0";
		    	}
		    	else if(xPos >= 605 && xPos <= 670) {
		    		// 1
		    		inputPassword = inputPassword + "1";
		    	}
		    	else if(xPos >= 697 && xPos <= 759) {
		    		// 2
		    		inputPassword = inputPassword + "2";
		    	}
		    	else if(xPos >= 787 && xPos <= 848) {
		    		// 3
		    		inputPassword = inputPassword + "3";
		    	}
		    	else if(xPos >= 884 && xPos <= 944) {
		    		// 4
		    		inputPassword = inputPassword + "4";
		    	}
		    	else if(xPos >= 977 && xPos <= 1038) {
		    		// 5
		    		inputPassword = inputPassword + "5";
		    	}
		    	else if(xPos >= 1071 && xPos <= 1132) {
		    		// 6
		    		inputPassword = inputPassword + "6";
		    	}
		    	else if(xPos >= 1163 && xPos <= 1223) {
		    		// 7
		    		inputPassword = inputPassword + "7";
		    	}
		    	else if(xPos >= 1257 && xPos <= 1317) {
		    		// 8
		    		inputPassword = inputPassword + "8";
		    	}
		    	else if(xPos >= 1349 && xPos <= 1408) {
		    		// 9
		    		inputPassword = inputPassword + "9";
		    	}
		    	if(inputPassword.length() > 4) {
		    		inputPassword = inputPassword.substring(0, 4);
		    	}
		    	tf2.setText(inputPassword);
//		    	if(inputPassword.length() == 4) {
//			    	boolean passwordChk = vcm.checkPassword(inputPassword);
//			    	if(passwordChk == false) {
//			    		JOptionPane.showMessageDialog(null, "비밀번호 오류", "",
//								JOptionPane.ERROR_MESSAGE);
//			    		turnOffAdmin();
//			    	}
//			    	else {
//			    		showAManageFrame();
//			    	}
//		    	}
		    }	

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	    });
		

		//비밀번호패널
		adminTimePanel = new MyPanel("img/btn_password.png");
		adminTimePanel.setLayout(null);
		adminTimePanel.setBounds(545, 730, 909, 73);
		adminTimePanel.setOpaque(false);
		adminTimePanel.addMouseListener(new MouseListener() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	int xPos = e.getLocationOnScreen().x;
		    	xPos = xPos - 40;
		    	String value = tf4.getText().toString();
		    	
		    	if(xPos >= 508 && xPos <= 572) {
		    		// 0
		    		value = value + "0";
		    		
		    	}
		    	else if(xPos >= 605 && xPos <= 670) {
		    		// 1
		    		value = value + "1";
		    	}
		    	else if(xPos >= 697 && xPos <= 759) {
		    		// 2
		    		value = value + "2";
		    	}
		    	else if(xPos >= 787 && xPos <= 848) {
		    		// 3
		    		value = value + "3";
		    	}
		    	else if(xPos >= 884 && xPos <= 944) {
		    		// 4
		    		value = value + "4";
		    	}
		    	else if(xPos >= 977 && xPos <= 1038) {
		    		// 5
		    		value = value + "5";
		    	}
		    	else if(xPos >= 1071 && xPos <= 1132) {
		    		// 6
		    		value = value + "6";
		    	}
		    	else if(xPos >= 1163 && xPos <= 1223) {
		    		// 7
		    		value = value + "7";
		    	}
		    	else if(xPos >= 1257 && xPos <= 1317) {
		    		// 8
		    		value = value + "8";
		    	}
		    	else if(xPos >= 1349 && xPos <= 1408) {
		    		// 9
		    		value = value + "9";
		    	}
		    	tf4.setText(value);
		    }	

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	    });
		bt[9] = new JButton(new ImageIcon("img/btn_cancel.png"));
		bt[9].setBorderPainted(false);
		bt[9].setFocusPainted(false);
		bt[9].setContentAreaFilled(false);
		bt[9].addActionListener(this);
		bt[9].setBounds(1545, 35, 300, 85);		
		panel.add(bt[9]);
		
		// 마지막 붙이기
		lpane.add(panel, new Integer(0), 0);
		lpane.add(adminPWPanel, new Integer(5), 0);
		lpane.add(adminTimePanel, new Integer(6), 0);
//		adminPWPanel.setVisible(false);
		
//		lpane.add(pan_imgClock, new Integer(4), 0);
//		lpane.add(pan_clock, new Integer(5), 0); // 시계패널은 최상단
//		lpane.add(star, new Integer(3), 0);
//		lpane.add(pan_navi, new Integer(2), 0);
//
//		lpane.add(seat50, new Integer(2), 0);
//		lpane.add(sPanel, new Integer(0), 0);

		getContentPane().add(lpane);
		setVisible(true);

		// 화면 버튼 구현하기
		/** 여기서부터는 오른쪽 버튼 구현~ */
		popup = new JPopupMenu();
		allOnSeat = new JMenuItem("전체켜기");
		allOffSeat = new JMenuItem("전체끄기");
		turnOnSeat = new JMenuItem("단체켜기");
		turnOffSeat = new JMenuItem("단체끄기");
		calculSeat = new JMenuItem("단체계산");
		allOnSeat.addActionListener(this);
		allOffSeat.addActionListener(this);
		turnOnSeat.addActionListener(this);
		turnOffSeat.addActionListener(this);
		calculSeat.addActionListener(this);
		popup.add(allOnSeat);
		popup.add(allOffSeat);
		popup.add(turnOnSeat);
		popup.add(turnOffSeat);
		popup.add(calculSeat);

	}
	public static void test(){
		HostPcServer host = new HostPcServer();
		host.startFromFrame(1);
	}
	public static void main(String[] args) {
		
		test();
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

	// 이미지 그리기 위한 마이패널
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
		Image image;

		MyPanel(String img) {
			image = Toolkit.getDefaultToolkit().createImage(img);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (image != null) {
				g.drawImage(image, 0, 0, this);
			}
		}

		public void update(Graphics g) {
			paintComponent(g);
		}

	}// 마이패널 종료

	public void reimg() {
		repaint();
	}

	public class RoundJTextField extends JTextField {
	    private Shape shape;
	    public RoundJTextField(int size) {
	        super(size);
	        setOpaque(false); // As suggested by @AVD in comment.
	    }
	    protected void paintComponent(Graphics g) {
	         g.setColor(getBackground());
	         g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
	         super.paintComponent(g);
	    }
	    protected void paintBorder(Graphics g) {
	         g.setColor(getForeground());
	         g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
	    }
	    public boolean contains(int x, int y) {
	         if (shape == null || !shape.getBounds().equals(getBounds())) {
	             shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 15, 15);
	         }
	         return shape.contains(x, y);
	    }
	}
//	
//	@SuppressWarnings("serial")
//	class MyPanel2 extends JPanel {
//
//		int i=2;
//		//시계이미지쓰레드
//		MyPanel2() {
//			image = Toolkit.getDefaultToolkit().createImage("img/cl1.png");
//			image2 = Toolkit.getDefaultToolkit().createImage("img/cl2.png");
//			image3 = Toolkit.getDefaultToolkit().createImage("img/cl3.png");
//			img = image;
//			
//			Thread thread = new ClockRoThread();
//			thread.start();
//
//		}
//
//		public void paintComponent(Graphics g) {
//			super.paintComponent(g);
//			if (img != null) {
//				g.drawImage(img, 0, 0, this);
//			}
//		}
//
//		class ClockRoThread extends Thread {
//			public void run() {
//
//				try {
//					while (true) {
//						Thread.sleep(10000);
//						switch (i) {
//						case 1:
//							img = image;
//							i = 2;
//							pan_imgClock.repaint();
//							break;
//						case 2:
//							img = image2;
//							i = 3;
//							pan_imgClock.repaint();
//							break;
//						case 3:
//							img = image3;
//							i = 1;
//							pan_imgClock.repaint();
//							break;
//						}
//
//					}
//
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//			}
//		}
//	}
//
//	// 별똥별 패널
//	@SuppressWarnings("serial")
//	class MyStarPanel extends JPanel {
//		Image img;
//
//		MyStarPanel(String img) {
//			this.img = Toolkit.getDefaultToolkit().createImage(img);
//			Thread thread = new MyMoveThread();
//			thread.start();
//		}
//
//		public void paintComponent(Graphics g) {
//			super.paintComponent(g);
//			if (img != null) {
//				g.drawImage(img, sx, sy, this);
//			}
//		}
//
//		class MyMoveThread extends Thread {
//			int i = 1;
//			int sleep = 25; // 슬립 초기값 25
//
//			public void run() {
//				try {
//					while (true) {
//						while (i == 1) {
//							Thread.sleep(sleep);
//							sy += 2;
//							repaint();
//							if (sy >= 945)
//								i = 2;
//						}
//						while (i == 2) {
//							Thread.sleep(sleep);
//							sx += 2;
//							repaint();
//							if (sx >= 1809)
//								i = 3;
//						}
//						while (i == 3) {
//							Thread.sleep(sleep);
//							sy -= 2;
//							repaint();
//							if (sy <= 64)
//								i = 4;
//						}
//						while (i == 4) {
//							Thread.sleep(sleep);
//							sx -= 2;
//							repaint();
//							if (sx <= 94)
//								i = 1;
//						}
//					}
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//
//			}
//		}
//
//	}
//	
//	
	class MyIntFilter extends DocumentFilter {
	   @Override
	   public void insertString(FilterBypass fb, int offset, String string,
	         AttributeSet attr) throws BadLocationException {
	
	      Document doc = fb.getDocument();
	      StringBuilder sb = new StringBuilder();
	      sb.append(doc.getText(0, doc.getLength()));
	      sb.insert(offset, string);
	
	      if (test(sb.toString())) {
	         super.insertString(fb, offset, string, attr);
	      } else {
	         // warn the user and don't allow the insert
	      }
	   }
	
	   private boolean test(String text) {
	      try {
	         Integer.parseInt(text);
	         return true;
	      } catch (NumberFormatException e) {
	         return false;
	      }
	   }
	
	   @Override
	   public void replace(FilterBypass fb, int offset, int length, String text,
	         AttributeSet attrs) throws BadLocationException {
	
	      Document doc = fb.getDocument();
	      StringBuilder sb = new StringBuilder();
	      sb.append(doc.getText(0, doc.getLength()));
	      sb.replace(offset, offset + length, text);
	
	      if (test(sb.toString())) {
	         super.replace(fb, offset, length, text, attrs);
	      } else {
	         // warn the user and don't allow the insert
	      }
	
	   }
	
	   @Override
	   public void remove(FilterBypass fb, int offset, int length)
	         throws BadLocationException {
	      Document doc = fb.getDocument();
	      StringBuilder sb = new StringBuilder();
	      sb.append(doc.getText(0, doc.getLength()));
	      sb.delete(offset, offset + length);
	
	      if (test(sb.toString())) {
	         super.remove(fb, offset, length);
	      } else {
	         // warn the user and don't allow the insert
	      }
	
	   }
	}

	// 선택영역 그리기 위한 패널
	@SuppressWarnings("serial")
	class SelectPanel extends JPanel implements MouseMotionListener,MouseListener {
		int x, y, pX, pY, iX, iY;

		SelectPanel() {
			addMouseListener(this);
			addMouseMotionListener(this);
			setFocusable(true);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.draw3DRect(x, y, pX - x, pY - y, false);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
			pX = e.getX();
			pY = e.getY();
			repaint();
		}

		public void mousePressed(MouseEvent e) {
			x = e.getX();
			iX = e.getX();
			y = e.getY();
			iY = e.getY();
			pX = e.getX();
			pY = e.getY();
			// System.out.println("x:" + x + " y:" + y);
		}

		public void mouseReleased(MouseEvent e) {
			// System.out.println("x:" + x + " y:" + y);
			// System.out.println("px:" + pX + " py:" + pY);
			for (int i = 0; i < 50; i++) {
				if (x < pan[i].x && pan[i].x < pX && y < pan[i].y
						&& pan[i].y < pY)
					pan[i].checkOn();
			}

			x = 0;
			y = 0;
			pX = 0;
			pY = 0;
			repaint();
		}

		public void mouseMoved(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	public void turnOnAdmin() {
		adminPWPanel.setVisible(true);
		bt[0].setVisible(false);
		inputPassword = "";
	}

	public void turnOffAdmin() {
		adminPWPanel.setVisible(false);
		bt[0].setVisible(true);
		inputPassword = "";
		manageCount = 0;
	}
	
	private void showAManageFrame() {

		 control_fr_hud = new Control_Fr_Hud();
		 control_fr_hud.dispose();
		 control_fr_hud.setUndecorated(true);
		 control_fr_hud.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	@SuppressWarnings("serial")
	class MyClock extends JPanel {
		Calendar ctoday = Calendar.getInstance();
		int i = ctoday.get(Calendar.AM_PM);
		String[] ampm = { "AM", "PM" };
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String time = sdf.format(today);
		JLabel timeLabel;
		JLabel ampmLabel;

		public MyClock() {
			this.setLayout(null);

			timeLabel = new JLabel(time);
			timeLabel.setBounds(0, 0, 100, 20);
			timeLabel.setForeground(new Color(36, 205, 198));
			timeLabel.setFont(new Font("배달의민족 한나", Font.BOLD, 12));
			ampmLabel = new JLabel(ampm[i]);
			ampmLabel.setBounds(15, 20, 100, 30);
			ampmLabel.setForeground(new Color(36, 205, 198));
			ampmLabel.setFont(new Font("배달의민족 한나", Font.BOLD, 12));

			add(timeLabel, BorderLayout.NORTH);
			add(ampmLabel, BorderLayout.CENTER);
			Thread thread = new MyClockThread();
			thread.start();
		}

		class MyClockThread extends Thread {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					today = new Date();
					time = sdf.format(today);
					timeLabel.setText(time);
				}

			}
		}
	}


	// 좌석 쓰레드! 1일 경우 좌석, 2일경우 꺼짐켜짐으로 사용된다.
	class MyThread extends Thread {
		int i;

		MyThread(int i) {
			this.i = i;
		}

		public void run() {
			Set<Integer> hs = null;
			if (i == 1) {
				hs = new LinkedHashSet<Integer>();
				for (; hs.size() < 50;) {
					int x = (int) ((Math.random() * 50));
					hs.add(x);
				}
			} else {
				hs = new HashSet<Integer>();
				for (int a = 0; a < 50; a++)
					hs.add(a);
			}
			try {
				int tmp=0;
				for (Integer s : hs) {

					if (i == 1)
						Thread.sleep(50);
					else {
						Thread.sleep(25);
					}

					switch (i) {
					case 2:
						int hour = Integer.parseInt(tf4.getText().toString());
						vcm.turnOn(s, hour);
						break;
					case 3:
						vcm.turnOff(s);
						break;
					}
					repaint();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	@Override
	// 액션퍼폼
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bt[0]) {
			//kiosk_password 변경
			System.out.println("LET's CHANGE PASSWORD => "+inputPassword);
			boolean flag = StoreInfoProcess.updatePassword(vcm.getStore().getStoreId(), inputPassword);
			if(flag == true) {
				vcm.getStore().setKioskPassword(inputPassword);

				JOptionPane.showMessageDialog(null, "비밀번호 변경 성공!", "",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(null, "비밀번호 변경 실패!", "",
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == bt[1]) {
			inputPassword = "";
			tf2.setText(inputPassword);
		} else if(e.getSource() == bt[2]) {
			//시간당 금액 변경 
			int newPrice = Integer.parseInt(tf3.getText());
			boolean flag = StoreInfoProcess.updatePrice(vcm.getStore().getStoreId(), newPrice);
			if(flag == true) {
				vcm.getStore().setKrwPerHour(newPrice);

				JOptionPane.showMessageDialog(null, "시간당 금액 변경 성공!", "",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(null, "시간당 금액 변경 실패!", "",
						JOptionPane.ERROR_MESSAGE);
			}
//			popup.show(Manage_Fr_Hud.this, 1504, 901);
			// 올온!
//		} else if (e.getSource() == allOnSeat) {
//			System.out.println("올온시트!");
//			Thread seatThread = new MyThread(2);
//			seatThread.start();
//			// 올오프!
//		} else if (e.getSource() == allOffSeat) {
//			Thread seatThread = new MyThread(3);
//			seatThread.start();
//			// 턴온시트
		} else if (e.getSource() == bt[3]) {
			tf3.setText(String.valueOf(vcm.getStore().getKrwPerHour()));
		} else if (e.getSource() == bt[4]) {
			new SalesToday();
		} else if (e.getSource() == bt[5]) {
			//clear todaysale
			
		} else if (e.getSource() == bt[6]) {
			tf4.setText("");
		} else if (e.getSource() == bt[7]) {
//			tf4.setText("");
			System.out.println("올온시트!");
			try {
				int addtime = Integer.parseInt(tf4.getText().toString());
				URL url = new URL("http://52.78.238.247/addtime.php?store="+String.valueOf(vcm.getStore().getStoreId())+"&time="+String.valueOf(addtime * 60 * 60));
				System.out.println(url.toString());
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String inputLine;
	
				while ((inputLine = bufferedReader.readLine()) != null)  {
					stringBuffer.append(inputLine);
				}
				bufferedReader.close();
			} catch(Exception e3) {
				e3.printStackTrace();
			}
			// Thread seatThread = new MyThread(2);
			// seatThread.start();
		} else if (e.getSource() == bt[8]) {
			try {
				URL url = new URL("http://52.78.238.247/minus.php?store="+String.valueOf(vcm.getStore().getStoreId()));
				System.out.println(url.toString());
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer();
				String inputLine;
	
				while ((inputLine = bufferedReader.readLine()) != null)  {
					stringBuffer.append(inputLine);
				}
				bufferedReader.close();
			} catch(Exception e3) {
				e3.printStackTrace();
			} finally {
				vcm.allOff();
			}
//			Thread seatThread = new MyThread(3);
//			
//			seatThread.start();
			
		} else if (e.getSource() == turnOffSeat) {
			for (int i = 0; i < 50; i++) {
				if (pan[i].isChecked == true) {
					vcm.turnOff(i);
					pan[i].checkOff();
				}
			}
			// 단체계산
		} else if (e.getSource() == calculSeat) {
			vcm.groupPayOff(2, 0);

			// 회원메뉴
		} else if (e.getSource() == bt[9]) {
			dispose();
		}

	}
}// 클래스 종료
