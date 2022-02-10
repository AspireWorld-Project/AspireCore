package org.bukkit.plugin.messaging;

import org.bukkit.plugin.Plugin;

/**
 * Contains information about a {@link Plugin}s registration to a plugin
 * channel.
 */
public final class PluginMessageListenerRegistration {
	private final Messenger messenger;
	private final Plugin plugin;
	private final String channel;
	private final PluginMessageListener listener;

	public PluginMessageListenerRegistration(Messenger messenger, Plugin plugin, String channel,
			PluginMessageListener listener) {
		if (messenger == null)
			throw new IllegalArgumentException("Messenger cannot be null!");
		if (plugin == null)
			throw new IllegalArgumentException("Plugin cannot be null!");
		if (channel == null)
			throw new IllegalArgumentException("Channel cannot be null!");
		if (listener == null)
			throw new IllegalArgumentException("Listener cannot be null!");

		this.messenger = messenger;
		this.plugin = plugin;
		this.channel = channel;
		this.listener = listener;
	}

	/**
	 * Gets the plugin channel that this registration is about.
	 *
	 * @return Plugin channel.
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * Gets the registered listener described by this registration.
	 *
	 * @return Registered listener.
	 */
	public PluginMessageListener getListener() {
		return listener;
	}

	/**
	 * Gets the plugin that this registration is for.
	 *
	 * @return Registered plugin.
	 */
	public Plugin getPlugin() {
		return plugin;
	}

	/**
	 * Checks if this registration is still valid.
	 *
	 * @return True if this registration is still valid, otherwise false.
	 */
	public boolean isValid() {
		return messenger.isRegistrationValid(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PluginMessageListenerRegistration other = (PluginMessageListenerRegistration) obj;
		if (messenger != other.messenger && (messenger == null || !messenger.equals(other.messenger)))
			return false;
		if (plugin != other.plugin && (plugin == null || !plugin.equals(other.plugin)))
			return false;
		if (channel == null ? other.channel != null : !channel.equals(other.channel))
			return false;
		if (listener != other.listener && (listener == null || !listener.equals(other.listener)))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + (messenger != null ? messenger.hashCode() : 0);
		hash = 53 * hash + (plugin != null ? plugin.hashCode() : 0);
		hash = 53 * hash + (channel != null ? channel.hashCode() : 0);
		hash = 53 * hash + (listener != null ? listener.hashCode() : 0);
		return hash;
	}
}