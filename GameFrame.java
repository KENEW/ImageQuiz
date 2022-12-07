import java.awt.*; 
import javax.swing.*;
import java.awt.event.*;

class MyDialog extends JDialog
{
	public MyDialog(JFrame frame, String title)
	{
		super(frame, title);
		
		setLayout(new FlowLayout());
		JLabel ad = new JLabel("들어가려는 방 IP 주소");
		ad.setFont(new Font("함초롱바탕", Font.BOLD, 25));
		JTextField tf = new JTextField(28);
		JButton okButton = new JButton("확인");
		
		add(ad);
		add(tf);
		add(okButton);
		setSize(350,150);
		
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setVisible(false);
				GameManager.InGameConnect(tf.getText());
			}
		});
	}
}

public class GameFrame extends JFrame
 {
	private String userName;
	boolean roomJoin = false;
	public GameFrame()
	{
		setTitle("사진 퀴즈");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(null);
		JLabel textLabel = new JLabel("사진 퀴즈");
		textLabel.setLocation(130,-90);
		textLabel.setSize(300,300);
		textLabel.setFont(new Font("고딕", Font.BOLD, 65));
		c.add(textLabel);
		
		JLabel textLabel2 = new JLabel("닉네임 입력");
		textLabel2.setLocation(75,20);
		textLabel2.setSize(200,200);
		textLabel2.setFont(new Font("함초롱바탕", Font.BOLD, 20));
		c.add(textLabel2);
		
		JTextField name = new JTextField(30);
		name.setLocation(75,140);
		name.setSize(360,50);
		c.add(name);
		
		JButton btn1 = new JButton("방만들기");
		btn1.setFont(new Font("함초롱바탕", Font.BOLD, 30));
		btn1.setLocation(80,300);
		btn1.setSize(170,50);
		c.add(btn1);

		btn1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GameManager.InGameConnect(name.getText());
				setVisible(false);
			}
		});
		
		JButton btn2 = new JButton("방입장");
		btn2.setFont(new Font("함초롱바탕", Font.BOLD, 30));
		btn2.setLocation(270,300);
		btn2.setSize(170,50);
		c.add(btn2);
		
		MyDialog dialog = new MyDialog(this, "방 입장");
		
		btn2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.setVisible(true);
				setVisible(false);
				//InGameConnect(name.getText());
			}
		});

		// if (roomJoin) {
		// 	String ipInputBox = (String) JOptionPane.showInputDialog(this, "들어가려는 IP 주소를 입력하세요.",
		// 			"방 입장", JOptionPane.PLAIN_MESSAGE, null, null, null);

		// 	if (ipInputBox == null || ipInputBox.isEmpty()) {
		// 		System.out.println("아무 것도 입력하지 않았습니다.");
		// 	} else {
		// 		System.out.println(ipInputBox);
		// 	}
		// }

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(550,450);
		setVisible(true);
	}
}

