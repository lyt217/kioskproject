package view_HUD;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
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
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.DocumentFilter.FilterBypass;

import view.Manage;
import view.MenuSet;
import view.SalesToday;
import view.manage_member.ManageMember;
import view_HUD.Control_Fr_Hud.MyIntFilter;
import control.HostPcServer;
import control.Vcontrol;
import control.manage_member.dbprocess.DepositMemberProcess;
import control.manage_member.dbprocess.JoinMemberProcess;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class Payment_Hud extends JFrame implements ActionListener {
	Vcontrol vcm;
	private byte[] readBuffer = new byte[400];
    JButton btn10000, btn20000, btn30000, btn50000, btn100000, btnc, btnc2, btn_confirm, btn_cancel;
    JPanel panel, panel2, panel3;
    JLabel label, label2, label3, hourTF;
    public JTextField label4;
    OutputStream outStream;
    InputStream inStream;
    SerialPort serialPort;
    JLayeredPane lpane;
    Keyboard keyboard = null;
    int incomed = 0;
    int goal = 0;
    JButton findButton;
    String resultString = "";
    int krw_per_hour;
	CommPort commPort;
    public Payment_Hud() {
    	

		
    	setTitle("이용요금 충전");
    	setSize(1920, 1680);
    	setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setUndecorated(true);

		// 프레임 화면 중앙 배열
		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(0, 30);
		
		vcm =  Vcontrol.getInstance("매니지프레임HUD");
		
		krw_per_hour = vcm.getStore().getKrwPerHour();

		// 가장 큰 JLayer패널= 레이어를 순서대로 올려줌
		JLayeredPane lpane = new JLayeredPane();
		lpane.setBounds(0, 0, 1920, 1080);
		lpane.setLayout(null);

		// 배경패널
		panel = new MyPanel("img/charge_back.png");
		panel.setLayout(null);
		panel.setBounds(0, 0, 1920, 1080);
		
		lpane = new JLayeredPane();
		lpane.setBounds(0, 0, 1920, 1680);
		lpane.setLayout(null);
		lpane.setOpaque(false);

		Font font1 = new Font("SansSerif", Font.BOLD, 40);
		Font font2 = new Font("SansSerif", Font.BOLD, 60);
		hourTF = new JLabel("0시간 0분");
		hourTF.setBounds(700, 395, 350, 70); 
		hourTF.setFont(font1);
		hourTF.setForeground(Color.white);
		panel.add(hourTF);
		
		label = new JLabel("0", SwingConstants.RIGHT);
		label.setBounds(700, 720, 480, 80); 
		label.setFont(font2);
		label.setForeground(Color.white);
		panel.add(label);

		
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("COM1");
	        if ( portIdentifier.isCurrentlyOwned() )
	        {
	            System.out.println("Error: Port is currently in use");
	        }
	        else
	        {
	            commPort = portIdentifier.open("BILL", 6000);
	            
	            if ( commPort instanceof SerialPort )
	            {
	                serialPort = (SerialPort) commPort;
	                serialPort.setSerialPortParams(19200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
//	                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
	                
	                inStream = serialPort.getInputStream();
	                outStream = serialPort.getOutputStream();
	                               
	        
	                (new Thread(new SerialWriter(outStream))).start();
//	                (new Thread(new SerialReader(in))).start();
	                serialPort.addEventListener(new SerialEventHandler());
	                //(SerialPortEventListener) new SerialReader(inStream));
	                serialPort.notifyOnDataAvailable(true);
	                
	                

	            }
	            else
	            {
	                System.out.println("Error: Only serial ports are handled by this example.");
	            }
	        }     
		} catch (Exception e2) {
			e2.printStackTrace();
		}
    	

		Image cc = (new ImageIcon("img/cancel1.png")).getImage();
		cc = cc.getScaledInstance(32, 32, Image.SCALE_SMOOTH);

		btnc = new JButton(new ImageIcon(cc));
		btnc.setBorderPainted(false);
        btnc.setFocusPainted(false);
        btnc.setContentAreaFilled(false);
        btnc.setBounds(928, 0, 32, 32);//132, 132);
        btnc.addActionListener(this);

        panel.add(btnc);

		btn_confirm = new JButton(new ImageIcon("img/btn_confirm_on.png"));
		btn_confirm.setBorderPainted(false);
		btn_confirm.setFocusPainted(false);
		btn_confirm.setContentAreaFilled(false);
		btn_confirm.setBounds(1100, 580, 102, 60);//132, 132);
		btn_confirm.addActionListener(this);
		panel.add(btn_confirm);
//		btn_confirm.setVisible(false);

		panel3 = new JPanel();
		panel3.setBounds(480, 337, 960, 332);
		panel3.setLayout(null);
		panel3.setBackground(Color.black);
		panel3.setOpaque(true);

		
		label4 = new JTextField();
		label4.setSize(340, 60);
		label4.setLocation(710, 580);
		label4.setFont(font1);
		label4.setOpaque(false);
		label4.setForeground(Color.white);
		label4.setBackground(new Color(60, 60, 60));
		
		label4.addFocusListener(new FocusListener() {
		      public void focusGained(FocusEvent e) {
//		          displayMessage("Focus gained", e);
		          if(keyboard == null) {
		        	  keyboard = new Keyboard();
			          keyboard.setPH(Payment_Hud.this);
			          keyboard.launch();
		          }
		          else {
		        	  keyboard.toFront();
		          }
		        }

		        public void focusLost(FocusEvent e) {
//		        	keyboard.dispose();
//		        	keyboard = null;
//		        	displayMessage("Focus lost", e);
		        }

		        void displayMessage(String prefix, FocusEvent e) {
//		          System.out.println(prefix
//		              + (e.isTemporary() ? " (temporary):" : ":")
//		              + e.getComponent().getClass().getName()
//		              + "; Opposite component: "
//		              + (e.getOppositeComponent() != null ? e.getOppositeComponent().getClass().getName()
//		                  : "null"));
		        }

		      });

		panel.add(label4);
		

		btn_cancel = new JButton(new ImageIcon("img/btn_cancel.png"));
		btn_cancel.setBorderPainted(false);
		btn_cancel.setFocusPainted(false);
		btn_cancel.setContentAreaFilled(false);
		btn_cancel.addActionListener(this);
		btn_cancel.setBounds(1545, 35, 300, 85);		
		panel.add(btn_cancel);
//		
//		label3.setFont(new Font(labelFont.getName(), Font.BOLD, 20));
//		label4.setFont(new Font(labelFont.getName(), Font.PLAIN, 15));

//		findButton = new JButton("검색");
//		findButton.setLocation(620,170);
//		findButton.setSize(100, 50);
//		findButton.addActionListener(this);
//		panel3.add(label3);
//		panel3.add(label4);
//		panel3.add(findButton);
//		panel3.setVisible(false);

		
        lpane.add(panel, new Integer(0), 0);
//        lpane.add(panel2, new Integer(1), 0);
//        lpane.add(panel3, new Integer(2), 0);
        
        getContentPane().add(lpane);
        setVisible(true);
        
    
			 
		
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    	if(e.getSource() == btn_confirm) {
    		String username = label4.getText();
    		if(JoinMemberProcess.findMember(username) == true) {
    			if(incomed > 0) {
	    			String message = username+" 회원에게 "+incomed+"원을 충전하시겠습니까?";
	    			int result = JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION);
	    			if(result == JOptionPane.CLOSED_OPTION) {
	    				
	    			}
	    			else if(result == JOptionPane.YES_OPTION) {
	    				System.out.println(message);
	    				if(DepositMemberProcess.insertMoney(username, incomed, vcm.getStore().getStoreId()) == 1){
	    					JOptionPane.showMessageDialog(null, "충전이 완료되었습니다.", "", JOptionPane.INFORMATION_MESSAGE);
					
							if(commPort != null){
								commPort.close();
								inStream = null;
								outStream = null;
								if(keyboard != null) {
									keyboard.dispose();
								}
								dispose();
							}
	    				}
	    			}
	    			else {
	    				
	    			}
    			}
    			else {
    				JOptionPane.showMessageDialog(null, "금액을 먼저 투입해주세요.", "",
    						JOptionPane.ERROR_MESSAGE);
    			}
    		}
    		else {
    			JOptionPane.showMessageDialog(null, "없는 아이디입니다.", "아이디를 입력해주세요",
						JOptionPane.ERROR_MESSAGE);
    		}
    	}
    	else if(e.getSource() == btn_cancel) {
    		int result;
    	
    		if(incomed == 0) {
    			result = JOptionPane.YES_OPTION;
    		}
    		else {
    			String message = "나가시면 투입하신 금액을 되돌려받을 수 없습니다. 그래도 나가시겠습니까?";
				result = JOptionPane.showConfirmDialog(null, message, "나가기", JOptionPane.YES_NO_OPTION);
    		}
			if(result == JOptionPane.CLOSED_OPTION) {
				
			}
			else if(result == JOptionPane.YES_OPTION) {
				incomed = 0;

				if(commPort != null){
					commPort.close();
					inStream = null;
					outStream = null;
					if(keyboard != null) {
						keyboard.dispose();
					}
					dispose();
				}
			}
    	}
    	else if(e.getSource() == btnc || e.getSource() == btnc2) {
    		if(keyboard != null) {
    			keyboard.dispose();
    		}
    		dispose();
    	}
    	else {
	    	if(e.getSource() == btn10000) {
	    		goal = 10000;
	    	}
	    	else if(e.getSource() == btn20000) {
	    		goal = 20000;
	    	}
	    	else if(e.getSource() == btn30000) {
	    		goal = 30000;
	    	}
	    	else if(e.getSource() == btn50000) {
	    		goal = 50000;
	    	}
	    	else if(e.getSource() == btn100000) {
	    		goal = 100000;
	    	}
	    	else if(e.getSource() == btn_confirm) {
	    		
	    	}
	    	
	    	
	    	panel.setVisible(false); 
	    	panel2.setVisible(true);
    	}
    }
    private void readSerial() {
    	try {
    		int availableBytes = inStream.available();
    		if(availableBytes > 0) {
    			inStream.read(readBuffer, 0, availableBytes);
    			
    			String newString = new String(readBuffer, 0, availableBytes);
            	newString = newString.replaceAll("[^a-zA-Z0-9]", "");
            	
            	if(resultString.startsWith("s")) {
            		resultString = resultString + newString;
            	}
            	else {
            		resultString = newString;
            	}
            	
                System.out.println("RESULT : " + resultString);
                
            	if( resultString.startsWith("s") &&  resultString.endsWith("e")) {

                	int money = 0;
                	if(resultString.equals("sC1e")){
                		money = 1000;
                	}
                	else if(resultString.equals("sC2e")){
                    	money = 5000;
                	}
                	else if(resultString.equals("sC3e")){
                    	money = 10000;
                	}
                	else if(resultString.equals("sC4e")){
                    	money = 50000;
                	}

                	System.out.println("MONEY : "+String.valueOf(money)+" | KRWPERHOUR : "+String.valueOf(krw_per_hour));
//                	
            		outStream.write("sBle".getBytes());
            		outStream.flush();
                		
            		incomed = incomed + money;
                	System.out.println("incomed : "+String.valueOf(incomed));
                	
                	
            		int minute = (incomed * 60 / krw_per_hour) % 60;
            		int hour = incomed / krw_per_hour; // / 60;
            		
            		System.out.println(String.valueOf(incomed)+" inserted / "+String.valueOf(hour)+":"+String.valueOf(minute));
            		hourTF.setText(String.valueOf(hour)+"시간 "+String.valueOf(minute)+"분");
            		btn_confirm.setVisible(true);

                	resultString = "";
            	}
            	
    		}
    	} catch(IOException e) {
    		
    	}
    }
    
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

    private void inputUsername() {
    	panel.setVisible(false);
    	panel2.setVisible(false);
    	panel3.setVisible(true);
    	
    }
    private class SerialEventHandler implements SerialPortEventListener {
    	public void serialEvent(SerialPortEvent event) {
    		switch (event.getEventType()) {
	    		case SerialPortEvent.DATA_AVAILABLE:
	    			readSerial();
	    			break;
    		}
    	}
    }


    /** */
    public class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
       
                	String newString = new String(buffer,0,len);
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
    private String checkMoney(int inputMoney) {
    	if(incomed + inputMoney > goal) {
    		return "EXCEED";
    	}
    	else {
    		return "ACCEPTABLE";
    	}
    }
    
    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {   
            	out.write("sREe".getBytes());
            	
//            	
//                int c = 0;
//                while ( ( c = System.in.read()) > -1 )
//                {
//                    this.out.write(c);
//                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }            
        }
    }
}
