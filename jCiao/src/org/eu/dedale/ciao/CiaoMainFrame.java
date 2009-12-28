/**
 * 
 */
package org.eu.dedale.ciao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.eu.dedale.ciao.network.SocketThread;
import org.eu.dedale.ciao.utils.ComponentFactory;
import org.eu.dedale.ciao.utils.Console;
import org.eu.dedale.ciao.zeroconf.Zeroconf;
import org.eu.dedale.ciao.zeroconf.ZeroconfListener;

/**
 * @author superna
 *
 */
public class CiaoMainFrame extends JFrame implements ZeroconfListener, ActionListener, KeyListener {

	private String chatName;
	private Zeroconf zeroconf;
	private int port;
	private JButton start, stop;
	private JTextField text;
	private SocketThread server;
	public static final String CIAO_REGTYPE = "_ciao._tcp"; 
	
	public CiaoMainFrame(Zeroconf zeroconf) {
		super("Ciao!");
		this.zeroconf = zeroconf;
		
		//Frame
		this.setLayout(new BorderLayout());
		Console c = new Console("Ciao!");
		c.setBounds(0,0,700,440);
		this.add(c, BorderLayout.CENTER);
		this.setSize(new Dimension(600,500));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panLeft = new JPanel();
		panLeft.setLayout(new FlowLayout());
		this.add(panLeft, BorderLayout.WEST);
		
		this.setVisible(true);
		
		//Ask for name
		chatName = JOptionPane.showInputDialog(null,
				  "Enter chat name",
				  "Enter chat name",
				  JOptionPane.QUESTION_MESSAGE);
		Console.getConsoleInstance("Ciao!").addLine("Chat name : '"+chatName+"'\n");
		
		server = new SocketThread(chatName);
		
		startZeroconf();
	}
	
	public void startZeroconf()
	{
		Console.getConsoleInstance("Ciao!").addLine("Starting server\n");
		port = server.StartServer();
		Console.getConsoleInstance("Ciao!").addLine("Browsing Zeroconf network for peers\n");
		zeroconf.AddListener(this);
		zeroconf.Browse(CIAO_REGTYPE);
		Console.getConsoleInstance("Ciao!").addLine("Registering Zeroconf network service on port " + port + "\n");
		zeroconf.RegisterService(chatName, CIAO_REGTYPE, port);
	}
	
	public void onServiceAdd(String name, String fullName, String adress,
			int port) {
		Console.getConsoleInstance("Ciao!").addLine("Added service "+name+" at "+adress+":"+port+"\n");
	}

	public void onServiceError(String error) {
		Console.getConsoleInstance("Ciao!").addLine("Zeroconf error "+ error + "\n");
		}

	public void onServiceRemove(String name, String fullName, String address,
			int port) {
		Console.getConsoleInstance("Ciao!").addLine("Removed service "+name+" at "+address+":"+port+"\n");
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
