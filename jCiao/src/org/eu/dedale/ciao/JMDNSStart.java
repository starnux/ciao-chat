package org.eu.dedale.ciao;

import org.eu.dedale.ciao.zeroconf.JMDNS;
import org.eu.dedale.ciao.zeroconf.Zeroconf;

public class JMDNSStart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Zeroconf zeroconf = new JMDNS();
		new CiaoMainFrame(zeroconf);
	}

}
