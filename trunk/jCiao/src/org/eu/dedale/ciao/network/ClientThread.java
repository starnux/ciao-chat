package org.eu.dedale.ciao.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.eu.dedale.ciao.CiaoChatFrame;

public class ClientThread extends Thread{

	Socket client;
	ClientMessageListener listener;
	int last;
	String name;
	
	public ClientThread(Socket client, String name)
	{
		this.client = client;
		this.name = name;
		start();
	}
	
	public void run()
	{
		String line = "";
		System.out.println("Client Connected");
		BufferedReader in;
		try
		{
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		while(!client.isClosed() && client.isConnected())
		{
			try
			{
				line = in.readLine();
			} catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
			if(line == null)
				break;
			
			listener.MessageFrom("him", line);
			System.out.println("Received from client : " + line);
		}
		try
		{
			System.out.println("Closed");
			listener.SocketClosed();
			client.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void sendMessage(String chatName, String text) {
		PrintWriter out = null;
		try
		{
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e)
		{
			listener.SocketClosed();
			e.printStackTrace();
			return;
		}
		System.out.println("Sending to client : " + chatName+":"+text);
		String msg = "_ciao_:"+chatName+":"+text+"\n";
		byte[] utf8;
		try {
			utf8 = msg.getBytes("UTF-8");
			//out.println(utf8);
			//out.flush();
			client.getOutputStream().write(utf8);
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMessageListener(CiaoChatFrame ciaoChatFrame) {
		this.listener = ciaoChatFrame;
	}

}
