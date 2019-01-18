package com.videosambo.sankochat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author videosambo
 * 
 *         onnikontiokorpi@gmail.com
 **/
public class ChatListener implements Listener {

	Main plugin = Main.getPlugin(Main.class);
	Messages messages = new Messages();
	Matcher matcher = new Matcher();
	String noPermMessage = messages.getMessage("no-permission");
	WarningSystem warning = new WarningSystem();

	private HashMap<UUID, String> messagesMap = new HashMap<UUID, String>();
	private HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
	private int cooldownTime = plugin.getConfig().getInt("cooldown-time");
	private ArrayList<String> blockedWords = (ArrayList<String>) plugin.getConfig().getStringList("blocked-words");

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e) {

		Player player = (Player) e.getPlayer();
		UUID playerID = player.getUniqueId();

		
		//Blocked words
		if (plugin.getConfig().getBoolean("blocked-word-detection")) {
			if (!player.hasPermission("sankochat.bypass.blockedwords")) {
				for (String blockedWord : blockedWords) {
					if (e.getMessage().toLowerCase().contains(blockedWord.toLowerCase())) {
						e.setCancelled(true);
						player.sendMessage(messages.getMessage("blocked-word-message"));
						
						if (plugin.getConfig().getBoolean("use-warning-system")) {
							if (plugin.getConfig().getBoolean("warn-blocked-words")) {
								if (plugin.getConfig().getBoolean("use-own-warning-system")) {
									warning.runOwnWarningCommand(playerID);
								} else {
									String from = messages.getMessage("reason-blocked-words");
									warning.addWarning(playerID, from);
								}
							}
						}
						
						if (plugin.getConfig().getBoolean("resend-null-message")) {
							e.setMessage(null);
						} else {
							e.setMessage(messages.getMessage("resend-message").replace("{0}", e.getMessage()));
						}
					}
				}
			}
		}

		//Link Domain name and IP filter
		if (plugin.getConfig().getBoolean("link-filter")) {
			if (!player.hasPermission("sankochat.bypass.filter")) {
				if (matcher.isLink(e.getMessage())) {
					e.setCancelled(true);
					player.sendMessage(messages.getMessage("filter-message"));
					
					if (plugin.getConfig().getBoolean("use-warning-system")) {
						if (plugin.getConfig().getBoolean("warn-filter")) {
							if (plugin.getConfig().getBoolean("use-own-warning-system")) {
								warning.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-filter");
								warning.addWarning(playerID, from);
							}
						}
					}
					
					if (plugin.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						e.setMessage(messages.getMessage("resend-message").replace("{0}", e.getMessage()));
					}
				}
			}
		}
		
		//CAPS
		if (plugin.getConfig().getBoolean("stop-caps")) {
			if (!player.hasPermission("sankochat.bypass.caps")) {
				if (testUpperCase(e.getMessage())) {
					e.setMessage(e.getMessage().toLowerCase());
					player.sendMessage(messages.getMessage("caps-message"));
					
					if (plugin.getConfig().getBoolean("use-warning-system")) {
						if (plugin.getConfig().getBoolean("warn-caps")) {
							if (plugin.getConfig().getBoolean("use-own-warning-system")) {
								warning.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-caps");
								warning.addWarning(playerID, from);
							}
						}
					}
					
				}
			}
		}

		if (plugin.getConfig().getBoolean("use-cooldown-time")) {
			if (!player.hasPermission("sankochat.bypass.cooldown")) {
				if (cooldown.containsKey(playerID)) {
					long secondsLeft = ((cooldown.get(playerID) / 1000) + cooldownTime)
							- (System.currentTimeMillis() / 1000);
					if (secondsLeft > 0) {
						e.setCancelled(true);
						player.sendMessage(messages.getMessage("cooldown-message").replace("{0}",
								Integer.toString((int) secondsLeft)));
						if (plugin.getConfig().getBoolean("resend-null-message")) {
							e.setMessage(null);
						} else {
							e.setMessage(messages.getMessage("resend-message").replace("{0}", e.getMessage()));
						}
						return;
					} else if (secondsLeft <= 0) {
						cooldown.remove(playerID);
					}
				} else {
					cooldown.put(playerID, System.currentTimeMillis());
				}
			}
		}

		if (getMessage(playerID) == null) {
			addMessage(playerID, e.getMessage());
			return;
		}

		//Message repeat
		if (plugin.getConfig().getBoolean("stop-message-repeat")) {
			if (!player.hasPermission("sankochat.bypass.repeat")) {
				if (getMessage(playerID).toLowerCase().equals(e.getMessage().toLowerCase())) {
					e.setCancelled(true);
					player.sendMessage(messages.getMessage("message-repeat"));
					
					if (plugin.getConfig().getBoolean("use-warning-system")) {
						if (plugin.getConfig().getBoolean("warn-repeat")) {
							if (plugin.getConfig().getBoolean("use-own-warning-system")) {
								warning.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-repeat");
								warning.addWarning(playerID, from);
							}
						}
					}
					
					if (plugin.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						e.setMessage(messages.getMessage("resend-message").replace("{0}", e.getMessage()));
					}
					return;
				}
			}
		}

		//Similar messages
		if (plugin.getConfig().getBoolean("stop-message-similaries")) {
			Double similarityPrecent = plugin.getConfig().getDouble("similiar-message-precent");
			if (!player.hasPermission("sankochat.bypass.similar")) {
				if (similarity(e.getMessage(), messagesMap.get(playerID)) > similarityPrecent) {
					e.setCancelled(true);
					player.sendMessage(messages.getMessage("similiar-message"));
					
					if (plugin.getConfig().getBoolean("use-warning-system")) {
						if (plugin.getConfig().getBoolean("warn-similar")) {
							if (plugin.getConfig().getBoolean("use-own-warning-system")) {
								warning.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-similar");
								warning.addWarning(playerID, from);
							}
						}
					}
					
					if (plugin.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						e.setMessage(messages.getMessage("resend-message").replace("{0}", e.getMessage()));
					}
					return;
				}
			}
		}

		addMessage(playerID, e.getMessage());
		return;

	}

	public static boolean testUpperCase(String message) {
		if (message.length() < 4) {
			return false;
		}
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			while (!Character.isLetter(c)) {
				i++;
				c = message.charAt(i);
			}
			if (c >= 97 && c <= 122) {
				return false;
			}

		}
		return true;
	}

	public double similarity(String firstMessage, String latestMessage) {

		String message = firstMessage;
		int messageLenght = message.length();

		String lastMessage = latestMessage;
		int lastMessageLenght = lastMessage.length();

		String shorterMessage, longerMessage;
		int shorterMessageLenght, longerMessageLenght, messageChars = 0;

		if (messageLenght < lastMessageLenght) {
			shorterMessage = message;
			longerMessage = lastMessage;
		} else {
			shorterMessage = lastMessage;
			longerMessage = message;
		}

		shorterMessageLenght = shorterMessage.length();
		longerMessageLenght = longerMessage.length();

		for (int i = 0; i < shorterMessage.length(); i++) {
			if (shorterMessage.charAt(i) == longerMessage.charAt(i)) {
				messageChars += 1;
			}
		}

		if (messageChars > shorterMessageLenght) {
			throw new ArithmeticException("Probability percent is higher than 100%");
		}

		Double probability = (double) (messageChars / shorterMessageLenght);

		return probability;
	}

	public void clearMessages() {
		messagesMap.clear();
	}

	public String getMessage(UUID playerID) {
		return messagesMap.get(playerID);
	}

	public void addMessage(UUID playerID, String message) {
		messagesMap.put(playerID, message);
	}

	public HashMap<UUID, String> getMessageMap() {
		return messagesMap;
	}

}
