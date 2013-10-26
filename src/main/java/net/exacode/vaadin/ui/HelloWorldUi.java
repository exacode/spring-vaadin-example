package net.exacode.vaadin.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Title("Hello Window")
@Component
public class HelloWorldUi extends UI {

	@Autowired
	private MessageProvider messageProvider;

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		setContent(content);
		// Create the content root layout for the UI
		setContent(content);

		Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				String msg = messageProvider.getMessage();
				// String msg = "abc";
				content.addComponent(new Label(msg));
			}
		});
		content.addComponent(button);
	}
}
