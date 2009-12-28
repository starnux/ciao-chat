package org.eu.dedale.ciao.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.eu.dedale.ciao.CiaoChatFrame;


public class SocketThread extends Thread {
	
	private ServerSocket sock;
	private int port;
	private String name;
	
	public SocketThread(String name)
	{
		this.name = name;
	}
	
	public int StartServer()
	{
		try
		{
			sock = new ServerSocket();
			sock.bind(null);
			port = sock.getLocalPort();	
		} catch (IOException e)
		{
			e.printStackTrace();
			return -1;
		}
		
		start();
		
		return port;
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				Socket client = sock.accept();
				ClientThread thread = new ClientThread(client, name);
				new CiaoChatFrame(name, thread);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}

}
