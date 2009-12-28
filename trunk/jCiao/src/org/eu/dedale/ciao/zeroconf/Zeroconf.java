package org.eu.dedale.ciao.zeroconf;

public abstract class Zeroconf implements ZeroconfListener {
	protected ZeroconfListener listener;

	public static final boolean ENABLE_IPV6 = true;
	
	public Zeroconf() {
		listener = this;
	}
	
	public void AddListener(ZeroconfListener listener)
	{
		this.listener = listener;
	}
	
	public abstract void Browse(String type);
	public abstract void RegisterService(String name, String type, int port);

	public void onServiceAdd(String name, String fullName, String adresss, int port) {
		System.out.println("Zeroconf: Unregistered Listener");
	}

	public void onServiceError(String error) {
		System.out.println("Zeroconf: Unregistered Listener");
	}

	public void onServiceRemove(String name, String fullName, String adress, int port) {
		System.out.println("Zeroconf: Unregistered Listener");
	}
	
	

}
