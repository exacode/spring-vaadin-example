package net.exacode.vaadin.ui;

import org.springframework.stereotype.Component;

@Component
public class MessageProvider {

	public String getMessage() {
		return "Message from message provider";
	}

}
