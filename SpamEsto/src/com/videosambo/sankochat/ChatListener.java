package com.videosambo.sankochat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author videosambo
 * 
 *         onnikontiokorpi@gmail.com
 **/
public class ChatListener implements Listener {

	Main plugin = null;
	Messages messages = null;
	Matcher matcher = new Matcher();
	WarningSystem warning = null;

	private String noPermMessage;
	private HashMap<UUID, String> messagesMap = new HashMap<UUID, String>();
	private ArrayList<UUID> cooldown = new ArrayList<UUID>();
	private HashMap<UUID, Integer> cooldownMap = new HashMap<UUID, Integer>();
	private int cooldownTime;
	private ArrayList<String> blockedWords;
	private ArrayList<String> replaceword;

	public ChatListener(Main instance) {
		this.plugin = instance;
		messages = new Messages(plugin);
		warning = new WarningSystem(plugin);
		
		noPermMessage = messages.getMessage("no-permission", true);
		cooldownTime = plugin.getConfig().getInt("cooldown-time");
		blockedWords = (ArrayList<String>) plugin.getConfig().getStringList("blocked-words");
		replaceword = (ArrayList<String>) plugin.getConfig().getStringList("replace-word");
	}

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
						if(plugin.getConfig().getBoolean("enable-blocked-word-message")) player.sendMessage(messages.getMessage("blocked-word-message", true));
						
						if (plugin.getConfig().getBoolean("use-warning-system")) {
							if (plugin.getConfig().getBoolean("warn-blocked-words")) {
								if (plugin.getConfig().getBoolean("use-own-warning-system")) {
									warning.runOwnWarningCommand(playerID);
								} else {
									String from = messages.getMessage("reason-blocked-words", false);
									warning.addWarning(playerID, from);
								}
							}
						}
						
						if (plugin.getConfig().getBoolean("resend-null-message")) {
							e.setMessage(null);
						} else {
							if(plugin.getConfig().getBoolean("enable-resend-message")) e.setMessage(messages.getMessage("resend-message", true).replace("{0}", e.getMessage()));
						}
					}
				}
			}
		}
		
		//Replace word
		if (plugin.getConfig().getBoolean("use-replace-word")) {
			if (!player.hasPermission("sankochat.bypass.replaceword")) {
				for (String s : replaceword) {
					String[] words = s.split(":");
					e.setMessage(e.getMessage().replaceAll(words[0], words[1]));
				}
			}
		}

		//Link Domain name and IP filter
		if (plugin.getConfig().getBoolean("link-filter")) {
			if (!player.hasPermission("sankochat.bypass.filter")) {
				if (matcher.isLink(e.getMessage())) {
					e.setCancelled(true);
					player.sendMessage(messages.getMessage("filter-message", true));
					
					if (plugin.getConfig().getBoolean("use-warning-system")) {
						if (plugin.getConfig().getBoolean("warn-filter")) {
							if (plugin.getConfig().getBoolean("use-own-warning-system")) {
								warning.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-filter", false);
								warning.addWarning(playerID, from);
							}
						}
					}
					
					if (plugin.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						e.setMessage(messages.getMessage("resend-message", true).replace("{0}", e.getMessage()));
					}
				}
			}
		}
		
		//CAPS
		if (plugin.getConfig().getBoolean("stop-caps")) {
			if (!player.hasPermission("sankochat.bypass.caps")) {
				if (testUpperCase(e.getMessage())) {
					e.setMessage(e.getMessage().toLowerCase());
					if(plugin.getConfig().getBoolean("enable-caps-message")) player.sendMessage(messages.getMessage("caps-message", true));
					
					if (plugin.getConfig().getBoolean("use-warning-system")) {
						if (plugin.getConfig().getBoolean("warn-caps")) {
							if (plugin.getConfig().getBoolean("use-own-warning-system")) {
								warning.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-caps", false);
								warning.addWarning(playerID, from);
							}
						}
					}
					
				}
			}
		}
		
		//Cooldown
		if (plugin.getConfig().getBoolean("use-cooldown-time")) {
			if (!player.hasPermission("sankochat.bypass.cooldown.chat")) {
				if (cooldown.contains(playerID)) {
						e.setCancelled(true);
						player.sendMessage(messages.getMessage("cooldown-message", true).replace("{0}",
								Integer.toString(cooldownMap.get(playerID))));
						if (plugin.getConfig().getBoolean("resend-null-message")) {
							e.setMessage(null);
						} else {
							if(plugin.getConfig().getBoolean("enable-resend-message")) e.setMessage(messages.getMessage("resend-message", false).replace("{0}", e.getMessage()));
						}
						return;
				} else {
					cooldownMap.put(playerID, plugin.getConfig().getInt("cooldown-time"));
					cooldown.add(playerID);
					new BukkitRunnable() {
						@Override
						public void run() {
							if (cooldownMap.get(playerID) <= 0) {
								cooldown.remove(playerID);
								this.cancel();
								return;
							}
							int time = cooldownMap.get(playerID);
							time--;
							cooldownMap.put(playerID, time);
						}
					}.runTaskTimer(plugin, 0, 20);
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
					if(plugin.getConfig().getBoolean("enable-message-repeat")) player.sendMessage(messages.getMessage("message-repeat", true));
					
					if (plugin.getConfig().getBoolean("use-warning-system")) {
						if (plugin.getConfig().getBoolean("warn-repeat")) {
							if (plugin.getConfig().getBoolean("use-own-warning-system")) {
								warning.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-repeat", false);
								warning.addWarning(playerID, from);
							}
						}
					}
					
					if (plugin.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						if(plugin.getConfig().getBoolean("enable-resend-message")) e.setMessage(messages.getMessage("resend-message", false).replace("{0}", e.getMessage()));
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
					if(plugin.getConfig().getBoolean("enable-similiar-message")) player.sendMessage(messages.getMessage("similiar-message", true));
					
					if (plugin.getConfig().getBoolean("use-warning-system")) {
						if (plugin.getConfig().getBoolean("warn-similar")) {
							if (plugin.getConfig().getBoolean("use-own-warning-system")) {
								warning.runOwnWarningCommand(playerID);
							} else {
								String from = messages.getMessage("reason-similar", false);
								warning.addWarning(playerID, from);
							}
						}
					}
					
					if (plugin.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						if(plugin.getConfig().getBoolean("enable-resend-message")) e.setMessage(messages.getMessage("resend-message", false).replace("{0}", e.getMessage()));
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
