package com.github.pfichtner.core.mqtt;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.UnknownHostException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Rule;
import org.junit.Test;

public class MqttLinkTest {

	@Rule
	// newLnk tries to connection so we need a running broker
	public Broker broker = new Broker();

	@Test
	public void defaultHostIsLocalhostAndLinkHasCreatedWithoutConfiguring()
			throws UnknownHostException, IOException, MqttException {
		MqttLinkFactory factory = new MqttLinkFactory();
		MqttLinkConfig config = factory.newLinkConfig();
		String host = config.getHost();
		assertThat(host, is("localhost"));
		assertThat(factory.newLink(config), notNullValue());
	}

}
