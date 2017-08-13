package org.ardulink.camel;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.ardulink.core.events.AnalogPinValueChangedEvent;
import org.ardulink.core.events.DigitalPinValueChangedEvent;
import org.ardulink.core.events.EventListener;

public class ArdulinkConsumer extends DefaultConsumer {

	private final EventListener listener = listener();

	public ArdulinkConsumer(ArdulinkEndpoint endpoint, Processor processor)
			throws IOException {
		super(endpoint, processor);
	}

	@Override
	public void start() throws Exception {
		getEndpoint().getLink().addListener(listener);
		super.start();
	}

	@Override
	public void stop() throws Exception {
		getEndpoint().getLink().removeListener(listener);
		super.stop();
	}

	public ArdulinkEndpoint getEndpoint() {
		return (ArdulinkEndpoint) super.getEndpoint();
	}

	private EventListener listener() {
		return new EventListener() {

			@Override
			public void stateChanged(DigitalPinValueChangedEvent event) {
				Exchange exchange = getEndpoint().createExchange();
				Message message = exchange.getIn();
				message.setBody("D" + event.getPin().pinNum() + "="
						+ event.getValue());
				process(exchange);

			}

			@Override
			public void stateChanged(AnalogPinValueChangedEvent event) {
				Exchange exchange = getEndpoint().createExchange();
				Message message = exchange.getIn();
				message.setBody("A" + event.getPin().pinNum() + "="
						+ event.getValue());
				process(exchange);
			}

			private void process(Exchange exchange) {
				try {
					getProcessor().process(exchange);
				} catch (Exception ex) {
					ex.printStackTrace();
					getExceptionHandler().handleException(
							"Failed to process notification", ex);
				}
			}

		};

	}

}
