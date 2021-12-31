package view_HUD;

import javax.swing.*;   // JFrame, JPanel, JLabel, JButton

import com.mysql.jdbc.StringUtils;

import java.awt.*;      // GridBagLayout, GridBagConstraints, Insets, Font
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Keyboard {
	private Payment_Hud pay;
    private final JFrame f = new JFrame("충전 아이디 입력");

    private final JPanel keyboard = new JPanel();

    private static final String[][] key = {
        {"`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "Backspace"},
        {"Tab", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"},
        {"Caps", "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'", "Enter"},
        {"Shift", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/", "\u2191"},
        {" ", "<", "\u2193", ">"}
    };
    private static final int[][] keyEvent = {
    		{KeyEvent.VK_BACK_QUOTE, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_0, KeyEvent.VK_MINUS, KeyEvent.VK_EQUALS, KeyEvent.VK_BACK_SPACE},
            {KeyEvent.VK_TAB, KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U, KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_BRACELEFT, KeyEvent.VK_BRACERIGHT, KeyEvent.VK_BACK_SLASH},
            {KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_SEMICOLON, KeyEvent.VK_QUOTE, KeyEvent.VK_ENTER},
            {KeyEvent.VK_SHIFT, KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M, KeyEvent.VK_COMMA, KeyEvent.VK_COMMA, KeyEvent.VK_SLASH, KeyEvent.VK_SLASH},
            {KeyEvent.VK_SPACE, 0, 0, 0}
    };
    
    public void toFront() {
    	f.toFront();
    	f.repaint();
    }

    public void dispose() {
    	f.dispose();
    }
    public Keyboard() {
    	keyboard.setLayout(new GridBagLayout());

        Insets zeroInset = new Insets(0, 0, 0, 0);
        Font monospace = new Font(Font.MONOSPACED, Font.PLAIN, 12);

        JPanel pRow;
        JButton b;
        try {
	        final Robot robot = new Robot();
	        
	        GridBagConstraints cRow = new GridBagConstraints(),
	                           cButton = new GridBagConstraints();
	        cRow.anchor = GridBagConstraints.WEST;
	        cButton.ipady = 21;
	
	        // first dimension of the key array
	        // representing a row on the keyboard
	        for (int row = 0, i = 0; row < key.length; ++row) {
	            pRow = new JPanel(new GridBagLayout());
	
	            cRow.gridy = row;
	
	            // second dimension representing each key
	            for (int col = 0; col < key[row].length; ++col, ++i) {
	
	                // specify padding and insets for the buttons
	                switch (key[row][col]) {
	                    case "Backspace":   cButton.ipadx = 0; break;
	                    case "Tab":         cButton.ipadx = 17; break;
	                    case "Caps":        cButton.ipadx = 10; break;
	                    case "Enter":       cButton.ipadx = 27; break;
	                    case "Shift":       cButton.ipadx = 27; break;
	                    case "/":
	                        cButton.insets = new Insets(0, 0, 0, 24);
	                        break;
	                    case " ":
	                        cButton.ipadx = 247;
	                        cButton.insets = new Insets(0, 192, 0, 72);
	                        break;
	                    default:
	                        cButton.ipadx = 7;
	                        cButton.insets = zeroInset;
	                }
	
	                b = new JButton(key[row][col]);
	                b.setFont(monospace);
	                b.setFocusable(false);
	                final int value = keyEvent[row][col];
	                final String valueS = key[row][col];
	                b.addActionListener(new ActionListener() {
	                    
	                    @Override
	                    public void actionPerformed(ActionEvent arg0) {
	                        // TODO Auto-generated method stub
	                    	
	                    	if(value != 0) {
	                    		
	                    		JTextField ff = pay.label4;
	                    		String now = ff.getText().toString();
	                    		if(valueS.equals("Backspace")) {
	                    			if(now.length() > 0) {
	                    				now = now.substring(0, now.length() - 1);
	                    			}
	                    		}
	                    		else {
		                    		now = now + valueS;
	                    		}
	                    		now = now.toLowerCase();
	                    		ff.setText(now);
	                    	}
	                        
	                    }
	                });
	                pRow.add(b, cButton);
	            }
	
	            keyboard.add(pRow, cRow);
	        }
	
	        f.add(keyboard);
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }

    public void setPH(Payment_Hud ph) {
    	pay = ph;
    }
    public void launch() {
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.dispose();
        f.setUndecorated(true);
    	f.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setLocation(600, 800);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        Keyboard ui = new Keyboard();
        ui.launch();
    }
}