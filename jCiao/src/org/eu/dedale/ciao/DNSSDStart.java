package org.eu.dedale.ciao;

import org.eu.dedale.ciao.zeroconf.DNSSD;
import org.eu.dedale.ciao.zeroconf.Zeroconf;

public class DNSSDStart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Zeroconf zeroconf = new DNSSD();
		new CiaoMainFrame(zeroconf);
	}

}
