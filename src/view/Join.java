package view;
import javax.swing.*;

import control.manage_member.dbprocess.JoinMemberProcess;
import assets.BCrypt;

import java.awt.*;
import java.awt.event.*;
public class Join extends JFrame implements ActionListener {
// Components of the Form
	private Container c;
	private JLabel title;
	
	private JLabel name;
	private JTextField tname;
	
	private JLabel username;
	private JTextField tusername;
	
	private JLabel password;
	private JPasswordField tpassword;
	
	private JLabel mno;
	private JTextField tmno;
	
	private JLabel add;
	private JPasswordField tadd;
	
	private JButton sub;
	private JButton reset;
	private JTextArea tout;
	private JLabel res;
	private JTextArea resadd;
	private String dates[] = { "1", "2", "3", "4", "5",
	"6", "7", "8", "9", "10",
	"11", "12", "13", "14", "15",
	"16", "17", "18", "19", "20",
	"21", "22", "23", "24", "25",
	"26", "27", "28", "29", "30",
	"31" };
	private String months[] = { "Jan", "feb", "Mar", "Apr",
	"May", "Jun", "July", "Aug",
	"Sup", "Oct", "Nov", "Dec" };
	private String years[] = { "1995", "1996", "1997", "1998",
	"1999", "2000", "2001", "2002",
	"2003", "2004", "2005", "2006",
	"2007", "2008", "2009", "2010",
	"2011", "2012", "2013", "2014",
	"2015", "2016", "2017", "2018",
	"2019" };
// constructor, to initialize the components
// with default values.
	public Join()
	{
		setTitle("회원가입");
		setBounds(300, 90, 450, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		c = getContentPane();
		c.setLayout(null);
		title = new JLabel("회원가입");
//		title.setFont(new Font("Arial", Font.PLAIN, 30));
		title.setSize(300, 30);
		title.setLocation(150, 30);
		c.add(title);
		name = new JLabel("이름");
//		name.setFont(new Font("Arial", Font.PLAIN, 20));
		name.setSize(100, 30);
		name.setLocation(100, 100);
		c.add(name);
		tname = new JTextField();
//		tname.setFont(new Font("Arial", Font.PLAIN, 15));
		tname.setSize(150, 30);
		tname.setLocation(200, 100);
		c.add(tname);
		mno = new JLabel("휴대폰번호");
//		mno.setFont(new Font("Arial", Font.PLAIN, 20));
		mno.setSize(100, 30);
		mno.setLocation(100, 150);
		c.add(mno);
		tmno = new JTextField();
//		tmno.setFont(new Font("Arial", Font.PLAIN, 15));
		tmno.setSize(150, 30);
		tmno.setLocation(200, 150);
		c.add(tmno);
		username = new JLabel("아이디");
//		username.setFont(new Font("Arial", Font.PLAIN, 20));
		username.setSize(100, 30);
		username.setLocation(100, 200);
		c.add(username);
		tusername = new JTextField();
//		tusername.setFont(new Font("Arial", Font.PLAIN, 15));
		tusername.setSize(150, 30);
		tusername.setLocation(200, 200);
		c.add(tusername);
		
		password = new JLabel("비밀번호");
//		password.setFont(new Font("Arial", Font.PLAIN, 20));
		password.setSize(100, 30);
		password.setLocation(100, 250);
		c.add(password);
		
		tpassword = new JPasswordField();
//		tpassword.setFont(new Font("Arial", Font.PLAIN, 15));
		tpassword.setSize(150, 30);
		tpassword.setLocation(200, 250);
		c.add(tpassword);
		
		add = new JLabel("비밀번호확인");
//		add.setFont(new Font("Arial", Font.PLAIN, 20));
		add.setSize(100, 30);
		add.setLocation(100, 300);
		c.add(add);
		
		tadd = new JPasswordField();
//		tadd.setFont(new Font("Arial", Font.PLAIN, 15));
		tadd.setSize(150, 30);
		tadd.setLocation(200, 300);
		c.add(tadd);
		
		sub = new JButton("가입");
//		sub.setFont(new Font("Arial", Font.PLAIN, 15));
		sub.setSize(100, 30);
		sub.setLocation(150, 450);
		sub.addActionListener(new JoinProcess());
		c.add(sub);
		
		reset = new JButton("취소");
//		reset.setFont(new Font("Arial", Font.PLAIN, 15));
		reset.setSize(100, 30);
		reset.setLocation(270, 450);
		reset.addActionListener(this);
		c.add(reset);
		
		res = new JLabel("");
//		res.setFont(new Font("Arial", Font.PLAIN, 20));
		res.setSize(500, 25);
		res.setLocation(100, 500);
		c.add(res);
		
		// resadd = new JTextArea();
		// resadd.setFont(new Font("Arial", Font.PLAIN, 15));
		// resadd.setSize(200, 75);
		// resadd.setLocation(580, 175);
		// resadd.setLineWrap(true);
		// c.add(resadd);
		setVisible(true);
	}
// method actionPerformed()
// to get the action performed
// by the user and act accordingly
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == sub) {
			String data1;
			String data
			= "Name : "
			+ tname.getText() + "\n"
			+ "Mobile : "
			+ tmno.getText() + "\n";
			
			
//			
//			String data3 = "Username : " + tusername.getText();
//			String data4 = "Password : " + tpassword.getText();
//			tout.setText(data + data3);
//			tout.setEditable(false);
//			res.setText("회원가입에 성공했습니다.");
		// }
		// else {
		// tout.setText("");
		// resadd.setText("");
		// res.setText("Please accept the"
		// + " terms & conditions..");
		// }
		}
		else if (e.getSource() == reset) {
			dispose();
		// String def = "";
		// tname.setText(def);
		// tadd.setText(def);
		// tmno.setText(def);
		// res.setText(def);
		// tout.setText(def);
		// term.setSelected(false);
		// date.setSelectedIndex(0);
		// month.setSelectedIndex(0);
		// year.setSelectedIndex(0);
		// resadd.setText(def);
		}
	}
	
	// JoinProcess 클래스 시작
		private class JoinProcess implements ActionListener {
			public void actionPerformed(ActionEvent arg0) {

				if (tusername.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "아이디를 입력해주세요");

				} else {
					String encrypt_Pass = BCrypt.hashpw(String.valueOf(tpassword.getPassword()), BCrypt.gensalt(12));
					JoinMemberProcess.insertMember(tusername.getText(), encrypt_Pass, tmno.getText(), tname.getText());
					JOptionPane.showMessageDialog(null, "회원가입에 성공하셨습니다.");
					dispose();
				}

			}

		}

		// JoinProcess 클래스 종료

		// CloseProcess 클래스 시작
		private class CloseProcess implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				dispose();

			}

		}
}
