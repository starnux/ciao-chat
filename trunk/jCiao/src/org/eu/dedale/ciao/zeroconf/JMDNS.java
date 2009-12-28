/**
 * 
 */
package org.eu.dedale.ciao.zeroconf;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

/**
 * @author superna
 *
 */
public class JMDNS extends Zeroconf implements ServiceListener {
	
	private JmDNS jmdns;
	
	public JMDNS() {
		try {
			jmdns = new JmDNS();
		} catch (IOException e) {
			listener.onServiceError(e.getMessage());
		}
	}

	public void Browse(String type) {
		jmdns.addServiceListener(type+".local.", this);
	}

	public void RegisterService(String name, String type, int port) {
		ServiceInfo info = new ServiceInfo(type+".local.", name, port, "");
		try {
			jmdns.registerService(info);
		} catch (IOException e) {
			listener.onServiceError(e.getMessage());
		}
	}

	public void serviceAdded(ServiceEvent arg0) {
		System.out.println("ADD: "+arg0.getName());
		ServiceInfo info = jmdns.getServiceInfo(arg0.getType(), arg0.getName(), 3000);
		jmdns.requestServiceInfo(arg0.getType(), arg0.getName(), 3000);
	}

	public void serviceRemoved(ServiceEvent arg0) {
		ServiceInfo info = arg0.getInfo();
		if(info != null)
			listener.onServiceRemove(info.getName(), info.getQualifiedName(), info.getInetAddress().toString(), info.getPort());
	}

	public void serviceResolved(ServiceEvent arg0) {
		ServiceInfo info = arg0.getInfo();
		System.out.println("RESOLVED: "+arg0.getInfo());
		listener.onServiceAdd(info.getName(), info.getQualifiedName(), info.getInetAddress().toString(), info.getPort());
	}

}
