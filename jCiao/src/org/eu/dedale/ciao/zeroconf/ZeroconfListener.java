/**
 * 
 */
package org.eu.dedale.ciao.zeroconf;

/**
 * @author superna
 *
 */
public interface ZeroconfListener {
	void onServiceAdd(String name, String fullName, String adress, int port);
	void onServiceRemove(String name, String fullName, String address, int port);
	void onServiceError(String error);
}
