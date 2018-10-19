package com.ibm.coffee;

import java.io.IOException;
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

	public Session webSocketSession;

	@OnOpen
	public void onOpen(Session session) {

		this.webSocketSession = session;

		try {
			sendText("The coffee machine is on.");
		} catch (IOException ex) {
			System.out.println(ex);
		}
		System.out.println("ON OPEN WS End point class ID -- " + this.hashCode());
	}

	@OnMessage
	public void onMsg(String msg) throws IOException {
		CoffeeMachine cf = new CoffeeMachine();
		if (msg.contentEquals("tea")) {
			sendText(cf.buy("Gordon", "tea"));
		}
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println(webSocketSession.getId() + " disconnected ");
	}

	public Basic getBasicRemote() {
		return this.webSocketSession.getBasicRemote();
	}

	public void sendText(String txt) throws IOException {
		getBasicRemote().sendText(txt);
	}
}
