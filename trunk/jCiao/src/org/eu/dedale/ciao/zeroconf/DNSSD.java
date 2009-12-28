/**
 * 
 */
package org.eu.dedale.ciao.zeroconf;

import com.apple.dnssd.BrowseListener;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDRegistration;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.RegisterListener;
import com.apple.dnssd.ResolveListener;
import com.apple.dnssd.TXTRecord;

/**
 * @author superna
 *
 */
public class DNSSD extends Zeroconf implements BrowseListener,RegisterListener {
	private DNSSDService bonjour;
	
	public DNSSD() {
	}
	
	public void Browse(String type) {
		try {
			bonjour = com.apple.dnssd.DNSSD.browse(type, this);
		} catch (DNSSDException e) {
			listener.onServiceError(e.getMessage());
		}
	}

	public void RegisterService(String name, String type, int port) {
		try {
			com.apple.dnssd.DNSSD.register(name, type, port, this);
		} catch (DNSSDException e) {
			listener.onServiceError(e.getMessage());
		}
	}

	public void serviceFound(DNSSDService browser, int arg1, int arg2,
			String name, String type, String domain) {
		DNSSDResolver resolver = new DNSSDResolver(listener, name, type, domain, true);
		resolver.launchResolve();
	}

	public void serviceLost(DNSSDService browser, int arg1, int arg2, String name,
			String type, String domain) {
		DNSSDResolver resolver = new DNSSDResolver(listener, name, type, domain, false);
		resolver.launchResolve();
	}

	public void operationFailed(DNSSDService browser, int arg1) {
		listener.onServiceError("DNSSD Error " + arg1);
	}

	public void serviceRegistered(DNSSDRegistration arg0, int arg1,
			String arg2, String arg3, String arg4) {
		// TODO Do Something
	}

}
