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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.eu.dedale.ciao.network.ClientMessageListener;
import org.eu.dedale.ciao.network.ClientThread;
import org.eu.dedale.ciao.utils.ComponentFactory;
import org.eu.dedale.ciao.utils.Console;
import org.eu.dedale.ciao.zeroconf.Zeroconf;
import org.eu.dedale.ciao.zeroconf.ZeroconfListener;

/**
 * @author superna
 *
 */
public class CiaoChatFrame extends JFrame implements ClientMessageListener, ActionListener, KeyListener, WindowListener {

	private String chatName;
	private int port;
	private JTextField text;
	private ClientThread socket;
	private Console console;
	
	public CiaoChatFrame(String name, ClientThread socket) {
		super("Chat Window");
		this.socket = socket;
		this.chatName = name;
		socket.setMessageListener(this);
		addWindowListener(this);
		
		//Frame
		this.setLayout(new BorderLayout());
		console = new Console("ChatWindow"+socket.getId());
		console.setBounds(0,0,700,440);
		this.add(console, BorderLayout.CENTER);
		this.setSize(new Dimension(700,500));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel panSouth = new JPanel();
		panSouth.setLayout(new FlowLayout());
		text = new JTextField();
		text.setPreferredSize(new Dimension(400,30));
		panSouth.add(text);
		text.addKeyListener(this);
		panSouth.add(ComponentFactory.makeButtonString("Send", 10, 10, 120, 30, this, this));
		this.add(panSouth, BorderLayout.SOUTH);
		this.setVisible(true);
		
		console.addLine("Initiating Conversation\n");
	}

	public void actionPerformed(ActionEvent e) {
		if(text.isEnabled())
		{
			socket.sendMessage(chatName, text.getText());
		}
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

	public void MessageFrom(String name, String message) {
		console.addLine(name + ":" + message + "\n");
	}

	public void SocketClosed() {
		console.addLine("Connection closed by peer\n");
		text.setEnabled(false);
	}

	public void windowActivated(WindowEvent e) {
		//UNUSED
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent e) {
		//UNUSED
	}

	public void windowDeactivated(WindowEvent e) {
		//UNUSED
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
