// ViaVersionStatus - Logs players' client versions
// Copyright 2019 Bobcat00
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.

package com.bobcat00.viaversionstatus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;

public final class ViaVersionStatus extends JavaPlugin implements Listener {

	// Listener listeners;

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
	}

	@Override
	public void onDisable() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		ViaAPI api = Via.getAPI(); // Get the API
		int version = api.getPlayerVersion(player); // Get the protocol version
		ProtocolVersion key = ProtocolVersion.getProtocol(version);
		getLogger().info(player.getName() + " is using version " + key.toString() + ".");
		String message = ChatColor.translateAlternateColorCodes('&', this.getConfig().get("Message").toString());

		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.hasPermission("viaversionstatus.notify")) {
				p.sendMessage(message.replace("{user}", player.getName()).replace("{client_version}", version + ""));
			}
		}

	}

}
