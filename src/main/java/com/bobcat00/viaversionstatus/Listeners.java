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
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package com.bobcat00.viaversionstatus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;

public final class Listeners implements Listener
{
    private ViaVersionStatus plugin;
    @SuppressWarnings("rawtypes")
    private ViaAPI api;
    
    // Constructor
    
    public Listeners(ViaVersionStatus plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        api = Via.getAPI();
    }
    
    // Player join event
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        final Player player = e.getPlayer();
        
        // Protocol consists of a Name and Id (toString returns both as a combined string)
        @SuppressWarnings("unchecked")
        final ProtocolVersion clientProtocol = ProtocolVersion.getProtocol(api.getPlayerVersion(player));
        final String clientVersion = clientProtocol.getName();

        final ProtocolVersion serverProtocol = ProtocolVersion.getProtocol(ProtocolRegistry.SERVER_PROTOCOL);
        final String serverVersion = serverProtocol.getName(); // may be UNKNOWN
        
        // 1. Write to log file
        
        plugin.getLogger().info(player.getName() + " is using version " + clientProtocol.toString() + ".");
        
        // 2. Notify ops
      
        for (Player p: Bukkit.getServer().getOnlinePlayers())
        {
            if (p.hasPermission("viaversionstatus.notify"))
            {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.config.getNotifyString().replace("%player%",  player.getName()).
                                                    replace("%version%", clientVersion).
                                                    replace("%server%",  serverVersion)));
            }
        }
        
        // 3. Warn player
        
        if (plugin.config.getWarnPlayers() &&
            !serverVersion.equals("UNKNOWN") &&
            (clientProtocol.getId() != serverProtocol.getId()))
        {
            // Delay by 250 msec to make sure the player sees the message
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    if (player.isOnline())
                    {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.config.getWarnString().replace("%player%",  player.getName()).
                                                          replace("%version%", clientVersion).
                                                          replace("%server%",  serverVersion)));
                    }
                }
            }, 5L);
        }

    }
    
}
