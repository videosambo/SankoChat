package com.videosambo.sankochat;

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

	private final Main pl;
	private final Messages messages;
	private final Matcher matcher;
	private final WarningSystem warningSystem;

	private final HashMap<UUID, String> messagesMap = new HashMap<>();
	private final HashMap<UUID, Long> cooldownMap = new HashMap<>();

	public ChatListener(Main pl) {
		this.pl = pl;
		this.messages = new Messages();
		this.matcher = new Matcher();
		this.warningSystem = new WarningSystem();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent e) {

		Player player = e.getPlayer();
		UUID playerID = player.getUniqueId();

		// Blocked words
		if (pl.getConfig().getBoolean("blocked-word-detection")) {
			if (!player.hasPermission("sankochat.bypass.blockedwords")) {
				for (String blockedWord : pl.getConfig().getStringList("blocked-words")) {
					if (e.getMessage().toLowerCase().contains(blockedWord.toLowerCase())) {
						e.setCancelled(true);
						player.sendMessage(messages.getMessage("blocked-word-message"));

						if (pl.getConfig().getBoolean("use-warning-system")) {
							if (pl.getConfig().getBoolean("warn-blocked-words")) {
								if (pl.getConfig().getBoolean("use-own-warning-system")) {
									warningSystem.runOwnWarningCommand(playerID);
								} else {
									String from = messages.getMessage("reason-blocked-words");
									warningSystem.addWarning(playerID, from);
								}
							}
						}

						if (pl.getConfig().getBoolean("resend-null-message")) {
							e.setMessage(null);
						} else {
							e.setMessage(messages.getMessage("resend-message").replace("{0}", e
									.getMessage()));
						}
					}
				}
			}
		}

		// Link Domain name and IP filter
		if (pl.getConfig().getBoolean("link-filter")) {
			if (!player.hasPermission("sankochat.bypass.filter")) {
				if (matcher.isLink(e.getMessage())) {
					e.setCancelled(true);
					player.sendMessage(messages.getMessage("filter-message"));

					if (pl.getConfig().getBoolean("use-warning-system")) {
						if (pl.getConfig().getBoolean("warn-filter")) {
							if (pl.getConfig().getBoolean("use-own-warning-system")) {
								warningSystem.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-filter");
								warningSystem.addWarning(playerID, from);
							}
						}
					}

					if (pl.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						e.setMessage(messages.getMessage("resend-message").replace("{0}", e
								.getMessage()));
					}
				}
			}
		}

		// CAPS
		if (pl.getConfig().getBoolean("stop-caps")) {
			if (!player.hasPermission("sankochat.bypass.caps")) {
				if (testUpperCase(e.getMessage())) {
					e.setMessage(e.getMessage().toLowerCase());
					player.sendMessage(messages.getMessage("caps-message"));

					if (pl.getConfig().getBoolean("use-warning-system")) {
						if (pl.getConfig().getBoolean("warn-caps")) {
							String reason = messages.getMessage("reason-caps");
							warn(playerID, reason);
						}
					}

				}
			}
		}

		if (pl.getConfig().getBoolean("use-cooldown-time")) {
			if (!player.hasPermission("sankochat.bypass.cooldown")) {
				if (cooldownMap.containsKey(playerID)) {
					long secondsLeft = ((cooldownMap.get(playerID) / 1000) + pl.getConfig().getInt(
							"cooldown-time")) - (System.currentTimeMillis() / 1000);
					if (secondsLeft > 0) {
						e.setCancelled(true);
						player.sendMessage(messages.getMessage("cooldown-message").replace("{0}",
								Integer.toString((int) secondsLeft)));
						if (pl.getConfig().getBoolean("resend-null-message")) {
							e.setMessage(null);
						} else {
							e.setMessage(messages.getMessage("resend-message").replace("{0}", e
									.getMessage()));
						}
						return;
					} else if (secondsLeft <= 0) {
						cooldownMap.remove(playerID);
					}
				} else {
					cooldownMap.put(playerID, System.currentTimeMillis());
				}
			}
		}

		if (getMessage(playerID) == null) {
			addMessage(playerID, e.getMessage());
			return;
		}

		// Message repeat
		if (pl.getConfig().getBoolean("stop-message-repeat")) {
			if (!player.hasPermission("sankochat.bypass.repeat")) {
				if (getMessage(playerID).toLowerCase().equals(e.getMessage().toLowerCase())) {
					e.setCancelled(true);
					player.sendMessage(messages.getMessage("message-repeat"));

					if (pl.getConfig().getBoolean("use-warning-system")) {
						if (pl.getConfig().getBoolean("warn-repeat")) {
							if (pl.getConfig().getBoolean("use-own-warning-system")) {
								warningSystem.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-repeat");
								warningSystem.addWarning(playerID, from);
							}
						}
					}

					if (pl.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						e.setMessage(messages.getMessage("resend-message").replace("{0}", e
								.getMessage()));
					}
					return;
				}
			}
		}

		// Similar messages
		if (pl.getConfig().getBoolean("stop-message-similaries")) {
			Double similarityPrecent = pl.getConfig().getDouble("similiar-message-precent");
			if (!player.hasPermission("sankochat.bypass.similar")) {
				if (similarity(e.getMessage(), messagesMap.get(playerID)) > similarityPrecent) {
					e.setCancelled(true);
					player.sendMessage(messages.getMessage("similiar-message"));

					if (pl.getConfig().getBoolean("use-warning-system")) {
						if (pl.getConfig().getBoolean("warn-similar")) {
							if (pl.getConfig().getBoolean("use-own-warning-system")) {
								warningSystem.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-similar");
								warningSystem.addWarning(playerID, from);
							}
						}
					}

					if (pl.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						e.setMessage(messages.getMessage("resend-message").replace("{0}", e
								.getMessage()));
					}
					return;
				}
			}
		}

		addMessage(playerID, e.getMessage());
		return;

	}

	private void warn(UUID playerID, String reason) {
		if (pl.getConfig().getBoolean("use-own-warning-system")) {
			warningSystem.runOwnWarningCommand(playerID);
		} else {
			warningSystem.addWarning(playerID, reason);
		}
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

	public WarningSystem getWarningSystem() {
		return warningSystem;
	}

}
