package org.eu.dedale.ciao.network;

public interface ClientMessageListener {
	void MessageFrom(String name, String message);
	void SocketClosed();
}
