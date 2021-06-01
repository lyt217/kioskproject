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

import view.Manage;
import view.MenuSet;
import view.SalesToday;
import view.manage_member.ManageMember;
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
	private byte[] readBuffer = new byte[400];
    JButton btn10000, btn20000, btn30000, btn50000, btn100000, btnc, btnc2;
    JPanel panel, panel2, panel3;
    JLabel label, label2, label3;
    JTextField label4;
    OutputStream outStream;
    InputStream inStream;
    SerialPort serialPort;
    JLayeredPane lpane;
    int incomed = 0;
    int goal = 0;
    JButton findButton;
    String resultString = "";
    public Payment_Hud() {
    	setTitle("이용요금 충전");
    	setSize(1920, 1680);
    	setResizable(false);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setUndecorated(true);
		setLocation(0, 0);
		setOpacity((float) 0.9);
		
		lpane = new JLayeredPane();
		lpane.setBounds(0, 0, 1920, 1680);
		lpane.setLayout(null);
		lpane.setOpaque(false);

		panel = new JPanel();
		panel.setBounds(480, 337, 960, 332);
		panel.setLayout(null);
		panel.setBackground(Color.gray);
		panel.setOpaque(true);
	
		


		Image img1 = (new ImageIcon("img/btn_10000.png")).getImage();
		Image img2 = (new ImageIcon("img/btn_20000.png")).getImage();
		Image img3 = (new ImageIcon("img/btn_30000.png")).getImage();
		Image img5 = (new ImageIcon("img/btn_50000.png")).getImage();
		Image img10 = (new ImageIcon("img/btn_100000.png")).getImage();
		Image cc = (new ImageIcon("img/cancel1.png")).getImage();

		img1 = img1.getScaledInstance(132, 132, Image.SCALE_SMOOTH);
		img2 = img2.getScaledInstance(132, 132, Image.SCALE_SMOOTH);
		img3 = img3.getScaledInstance(132, 132, Image.SCALE_SMOOTH);
		img5 = img5.getScaledInstance(132, 132, Image.SCALE_SMOOTH);
		img10 = img10.getScaledInstance(132, 132, Image.SCALE_SMOOTH);
		cc = cc.getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        btn10000 = new JButton(new ImageIcon(img1));
        btn20000 = new JButton(new ImageIcon(img2));
        btn30000 = new JButton(new ImageIcon(img3));
        btn50000 = new JButton(new ImageIcon(img5));
        btn100000 = new JButton(new ImageIcon(img10));
        btnc = new JButton(new ImageIcon(cc));
        btnc2 = new JButton(new ImageIcon(cc));

        btn10000.setBorderPainted(false);
        btn10000.setFocusPainted(false);
        btn10000.setContentAreaFilled(false);
        btn10000.setBounds(50, 100, 132, 132);

        btn20000.setBorderPainted(false);
        btn20000.setFocusPainted(false);
        btn20000.setContentAreaFilled(false);
        btn20000.setBounds(232, 100, 132, 132);

        btn30000.setBorderPainted(false);
        btn30000.setFocusPainted(false);
        btn30000.setContentAreaFilled(false);
        btn30000.setBounds(414, 100, 132, 132);

        btn50000.setBorderPainted(false);
        btn50000.setFocusPainted(false);
        btn50000.setContentAreaFilled(false);
        btn50000.setBounds(596, 100, 132, 132);

        btn100000.setBorderPainted(false);
        btn100000.setFocusPainted(false);
        btn100000.setContentAreaFilled(false);
        btn100000.setBounds(778, 100, 132, 132);

        btnc.setBorderPainted(false);
        btnc.setFocusPainted(false);
        btnc.setContentAreaFilled(false);
        btnc.setBounds(928, 0, 32, 32);//132, 132);


        btnc2.setBorderPainted(false);
        btnc2.setFocusPainted(false);
        btnc2.setContentAreaFilled(false);
        btnc2.setBounds(928, 0, 32, 32);//132, 132);


        btn10000.addActionListener(this);
        btn20000.addActionListener(this);
        btn30000.addActionListener(this);
        btn50000.addActionListener(this);
        btn100000.addActionListener(this);
        btnc.addActionListener(this);
        btnc2.addActionListener(this);
        
        panel.add(btn10000);
        panel.add(btn20000);
        panel.add(btn30000);
        panel.add(btn50000);
        panel.add(btn100000);
        panel.add(btnc);

		panel2 = new JPanel();
		panel2.setBounds(480, 337, 960, 332);
		panel2.setLayout(null);
		panel2.setBackground(Color.black);
		panel2.setOpaque(true);
		
		label = new JLabel("지폐를 투입해주세요");
		label2 = new JLabel("현재 투입 금액 : 0원");
		label.setSize(300, 30);
		label.setLocation(100, 100);
		label.setForeground(Color.white);
		label2.setSize(500, 30);
		label2.setLocation(100, 200);
		label2.setForeground(Color.white);
		
		Font labelFont = label.getFont();
		label.setFont(new Font(labelFont.getName(), Font.BOLD, 30));
		label2.setFont(new Font(labelFont.getName(), Font.BOLD, 30));
		panel2.add(label);
		panel2.add(label2);
		panel2.add(btnc2);
		panel2.setVisible(false);

		

		panel3 = new JPanel();
		panel3.setBounds(480, 337, 960, 332);
		panel3.setLayout(null);
		panel3.setBackground(Color.black);
		panel3.setOpaque(true);

		label3 = new JLabel("사용시간을 충전할 아이디를 검색하세요");
		label4 = new JTextField();
		//("현재 투입 금액 : 0원");
		label3.setSize(500, 30);
		label3.setLocation(100, 100);
		label3.setForeground(Color.white);
		label4.setSize(500, 50);
		label4.setLocation(100, 170);
		
		label3.setFont(new Font(labelFont.getName(), Font.BOLD, 20));
		label4.setFont(new Font(labelFont.getName(), Font.PLAIN, 15));

		findButton = new JButton("검색");
		findButton.setLocation(620,170);
		findButton.setSize(100, 50);
		findButton.addActionListener(this);
		panel3.add(label3);
		panel3.add(label4);
		panel3.add(findButton);
		panel3.setVisible(false);

		
        lpane.add(panel, new Integer(0), 0);
        lpane.add(panel2, new Integer(1), 0);
        lpane.add(panel3, new Integer(2), 0);
        
        getContentPane().add(lpane);
        setVisible(true);
        
        
//        try {
//			Enumeration e = CommPortIdentifier.getPortIdentifiers();
//			 
//			System.out.println("Enumeration get()............... "+ e.hasMoreElements());
//			 
//			while (e.hasMoreElements()) {
//				CommPortIdentifier first = (CommPortIdentifier) e.nextElement();
//			    System.out.println("COM name : " + first.getName());
//			}
//			    
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
			 
		
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    	if(e.getSource() == findButton) {
    		String username = label4.getText();
    		if(JoinMemberProcess.findMember(username) == true) {
    			String message = username+" 회원에게 "+goal+"원을 충전하시겠습니까?";
    			int result = JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION);
    			if(result == JOptionPane.CLOSED_OPTION) {
    				
    			}
    			else if(result == JOptionPane.YES_OPTION) {
    				System.out.println(message);
    				if(DepositMemberProcess.insertMoney(username, incomed) == 1){
    					JOptionPane.showMessageDialog(null, "충전이 완료되었습니다.", "",
    							JOptionPane.INFORMATION_MESSAGE);
    					
    					dispose();
    				}
    			}
    			else {
    				
    			}
    		}
    		else {
    			JOptionPane.showMessageDialog(null, "없는 아이디입니다.", "아이디를 입력해주세요",
						JOptionPane.ERROR_MESSAGE);
    		}
    	}
    	else if(e.getSource() == btnc || e.getSource() == btnc2) {
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
	    	
	    	try {
				CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("COM1");
		        if ( portIdentifier.isCurrentlyOwned() )
		        {
		            System.out.println("Error: Port is currently in use");
		        }
		        else
		        {
		            CommPort commPort = portIdentifier.open("BILL", 6000);
		            
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
            	
                System.out.println(resultString);
                
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
                	if(checkMoney(money).equals("ACCEPTABLE")) {

                		btnc2.setVisible(false);
                		outStream.write("sBle".getBytes());
                		outStream.flush();
                		
                		incomed = incomed + money;
                		String newString2 = "현재 투입 금액 : "+String.valueOf(incomed)+"원";
                		label2.setText(newString2);
                		
                		if(incomed == goal) {
                			JOptionPane.showMessageDialog(null, "투입 완료!", "아이디를 입력해주세요",
            						JOptionPane.INFORMATION_MESSAGE);
                			inputUsername();
                		}
                				
                	}
                	else {

        				JOptionPane.showMessageDialog(null, "투입금액 오류", "",
        						JOptionPane.INFORMATION_MESSAGE);
                		outStream.write("sBOe".getBytes());
                		outStream.flush();
                	}
                	resultString = "";
            	}
            	
    		}
    	} catch(IOException e) {
    		
    	}
    }
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
