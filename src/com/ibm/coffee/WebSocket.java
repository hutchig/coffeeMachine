package com.ibm.coffee;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/wsep")
public class WebSocket {

	private static final String customer = "Gordon";
	private static Map<Customer, Session> sessions = new HashMap<Customer, Session>();

	@OnOpen
	public void onOpen(Session session) {

		sessions.put(Customer.getCustomer(customer), session);
		
		try {
			sendText(Customer.getCustomer(customer), "The coffee machine is on.");
		} catch (IOException ex) {
			System.out.println(ex);
		}
		System.out.println("ON OPEN WS End point class ID -- " + this.hashCode());
	}

	@OnMessage
	public void onMsg(String msg) throws IOException {
		CoffeeMachine coffeeMachine = new CoffeeMachine();
		if (msg.contentEquals("customer=Gordon&drink=tea")) {
			msg = coffeeMachine.buy(customer, "tea");
			sendText(Customer.getCustomer(customer), msg );
			if( msg.startsWith("Charge account")) {
				pauseVending(Customer.getCustomer(customer));
			}
		}
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println(sessions.get(Customer.getCustomer(customer)).getId() + " disconnected ");
	}

	public void sendText(String customer, String txt) throws IOException {
		getBasicRemote(Customer.getCustomer(customer)).sendText(txt);
	}

	public static Basic getBasicRemote(Customer customer) {
		return sessions.get(customer).getBasicRemote();
	}

	public static void sendText(Customer c , String txt) throws IOException {
		getBasicRemote(c).sendText(txt);
	}
	
	static void pauseVending(Customer customer) throws IOException {
		sendText(customer, "pause");
	}

	static void resumeVending(Customer customer) throws IOException {
		sendText(customer,  "resume");
	}

}
