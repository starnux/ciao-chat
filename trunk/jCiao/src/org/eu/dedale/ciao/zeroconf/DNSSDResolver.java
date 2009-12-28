/**
 * 
 */
package org.eu.dedale.ciao.zeroconf;

import java.io.IOException;

import sun.net.util.IPAddressUtil;
import sun.security.x509.IPAddressName;

import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.QueryListener;
import com.apple.dnssd.ResolveListener;
import com.apple.dnssd.TXTRecord;

/**
 * @author superna
 *
 */
public class DNSSDResolver implements ResolveListener, QueryListener {
	private String name;
	private String fullName;
	private String hostName;
	private String regType;
	private String domain;
	private int port;
	private TXTRecord TXT;
	private ZeroconfListener listener;
	private boolean add;
	
	public DNSSDResolver(ZeroconfListener listener, String name, String regType, String domain, boolean add) {
		this.name = name;
		this.regType = regType;
		this.domain = domain;
		this.listener = listener;
		this.add = add;
	}
	
	public void launchResolve()
	{
		try {
			com.apple.dnssd.DNSSD.resolve(0, 0, name, regType, domain, this);
		} catch (DNSSDException e) {
			listener.onServiceError(e.getMessage());
		}
	}
	
	public void serviceResolved(DNSSDService arg0, int arg1, int arg2,
			String fullName, String hostName, int port, TXTRecord TXT) {
		this.fullName = fullName;
		this.hostName = hostName;
		this.port = port;
		this.TXT = TXT;
		arg0.stop();
		try {
			com.apple.dnssd.DNSSD.queryRecord(0, 0, hostName, 1, 1, this); //IPv4
			if(Zeroconf.ENABLE_IPV6)
				com.apple.dnssd.DNSSD.queryRecord(0, 0, hostName, 28, 1, this); //IPv6
		} catch (DNSSDException e) {
			listener.onServiceError(e.getMessage());
		}
	}

	public void operationFailed(DNSSDService arg0, int arg1) {
		listener.onServiceError("DNSSD Error " + arg1);
	}

	public void queryAnswered(DNSSDService arg0, int arg1, int arg2,
			String arg3, int arg4, int arg5, byte[] data, int arg7) {
		IPAddressName a;
		try {
			a = new IPAddressName(data);
			
			if(add)
				listener.onServiceAdd(name, fullName, a.getName(), port);
			else
				listener.onServiceRemove(name, fullName, a.getName(), port);
		} catch (IOException e) {
			listener.onServiceError(e.getMessage());
		}
	}

}
