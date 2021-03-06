package org.jboss.tools.forge.runtime.ext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.command.CommandMetadata;
import org.jboss.forge.shell.command.PluginMetadata;
import org.jboss.forge.shell.command.PluginRegistry;
import org.jboss.forge.shell.spi.TriggeredAction;

public class MetaCommandAction implements TriggeredAction {

	private static final String ESCAPE = new String(new char[] { 27, '%' });

	@Inject Shell shell;
	
	@Inject PluginRegistry registry;
	
	@Inject EventHandler eventHandler;

	@Override
	public ActionListener getListener() {
		return new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					shell.print(ESCAPE);
					String text = shell.readLine();
					shell.print(ESCAPE);
					handleHiddenCommand(text);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
	}
	
	private void handleHiddenCommand(String text) {	
		try {
			if ("plugin-candidates-query".equals(text)) {
				shell.print(ESCAPE + "RESULT: plugin-candidates-answer: " + getPluginCandidates() + ESCAPE);
			} else {
				executeCommand(text);
			}
		} catch (Exception e) {
			// ignored
		}
	}
	
	private void executeCommand(String text) throws Exception {
		try {
			eventHandler.setEnabled(false);
			shell.print(ESCAPE + "RESULT: ");
			shell.execute(text);
			shell.print(ESCAPE);
		} finally {
			eventHandler.setEnabled(true);
		}
	}
	
	private String getPluginCandidates() {
		try {
			StringBuffer resultBuffer = new StringBuffer();
			Map<String, List<PluginMetadata>> plugins = registry.getPlugins();
			for (Entry<String, List<PluginMetadata>> entry : plugins.entrySet()) {
				for (PluginMetadata pluginMeta : entry.getValue()) {
					if (pluginMeta.constrantsSatisfied(shell)) {
						List<CommandMetadata> commands = pluginMeta.getAllCommands();
						if (!commands.isEmpty()) {
							resultBuffer.append("p:").append(pluginMeta.getName()).append(' ');
							for (CommandMetadata commandMeta : commands) {
								resultBuffer.append("c:").append(commandMeta.getName()).append(' ');
							}
						}
					}
				}
			}
			return resultBuffer.toString();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public char getTrigger() {
		return (char)31;
	}

}
