package ardulink.ardumailng.camel;

import static org.zu.ardulink.util.Preconditions.checkNotNull;
import static org.zu.ardulink.util.Preconditions.checkState;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.zu.ardulink.util.ListMultiMap;

import ardulink.ardumailng.Command;

import com.github.pfichtner.ardulink.core.Link;
import com.github.pfichtner.ardulink.core.convenience.Links;

public class ArdulinkProducer extends DefaultProducer {

	private final Link link;

	public ArdulinkProducer(Endpoint endpoint, String type, String typeParams) {
		super(endpoint);
		try {
			String str = "ardulink://"
					+ checkNotNull(type, "type must not be null");
			if (typeParams != null && !typeParams.isEmpty()) {
				str += "?" + typeParams;
			}
			this.link = Links.getLink(new URI(str));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		String out = process(exchange.getIn());
		if (out != null) {
			getMessageTarget(exchange).setBody(out, String.class);
		}
	}

	private Message getMessageTarget(Exchange exchange) {
		Message in = exchange.getIn();
		if (exchange.getPattern().isOutCapable()) {
			Message out = exchange.getOut();
			out.setHeaders(in.getHeaders());
			out.setAttachments(in.getAttachments());
			return out;
		}
		return in;
	}

	@Override
	public void stop() throws Exception {
		this.link.close();
		super.stop();
	}

	// --------------------------------------------------------------------------------------------------------

	private final List<String> validFroms = new ArrayList<String>();
	private final ListMultiMap<String, Command> commands = new ListMultiMap<String, Command>();

	private String process(Message message) {
		String from = message.getHeader("From", String.class);
		checkState(from != null && !from.isEmpty(), "No from set in message");
		checkState(validFroms.contains(from),
				"From user %s not a valid from address", from);

		checkState(message.getBody() instanceof String, "Body not a String");
		String commandName = (String) message.getBody();
		checkState(!commandName.isEmpty(), "Body not set");

		for (Entry<String, List<Command>> entry : commands.asMap().entrySet()) {
			String key = entry.getKey();
			if (commandName.equals(key)) {
				List<Command> values = entry.getValue();
				StringBuilder sb = new StringBuilder();
				for (Command command : values) {
					sb = sb.append(command);
					try {
						command.execute(link);
						sb = sb.append("=OK");
					} catch (Exception e) {
						sb = sb.append("=KO");
					}
				}
				return sb.toString();
			}
		}
		throw new IllegalStateException("Command " + commandName + " not known");
	}

	public void setValidFroms(List<String> validFroms) {
		this.validFroms.addAll(validFroms);
	}

	public void setCommands(String onValue, Command... commands) {
		setCommands(onValue, Arrays.asList(commands));
	}

	public void setCommands(String onValue, List<Command> commands) {
		for (Command command : commands) {
			this.commands.put(onValue, command);
		}
	}

}
