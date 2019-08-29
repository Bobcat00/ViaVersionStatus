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

import com.bobcat00.viaversionstatus.connections.ProtocolVersion;
import com.bobcat00.viaversionstatus.connections.PSConnection;
import com.bobcat00.viaversionstatus.connections.ViaConnection;

public final class Listeners implements Listener
{
    private ViaVersionStatus plugin;
    
    private ViaConnection via;
    private PSConnection ps;
    
    private enum UseConnection
    {
        USE_VIA,
        USE_PS,
        USE_BOTH,
        USE_NONE
    }
    
    private UseConnection useConnection = UseConnection.USE_NONE;
    
    // Constructor
    
    public Listeners(ViaVersionStatus plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        via = new ViaConnection();
        ps = new PSConnection();
        
        if (via.isValid() && ps.isValid())
        {
            useConnection = UseConnection.USE_BOTH;
            plugin.getLogger().info("Using both ViaVersion and ProtocolSupport to determine versions.");
        }
        else if (via.isValid())
        {
            useConnection = UseConnection.USE_VIA;
            plugin.getLogger().info("Using ViaVersion to determine versions.");
        }
        else if (ps.isValid())
        {
            useConnection = UseConnection.USE_PS;
            plugin.getLogger().info("Using ProtocolSupport to determine versions.");
        }
        else
        {
            plugin.getLogger().severe("This plugin requires either ViaVersion or ProtocolSupport or both.");
            plugin.shutdown();
            throw new RuntimeException("ViaVersion or ProtocolSupport required."); // Get the user's attention
        }
    }
    
    // Player join event
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        final Player player = e.getPlayer();
        
        // Protocol consists of a Name and Id (toString returns both as a combined string)
        ProtocolVersion serverProtocol = null;
        ProtocolVersion clientProtocol = null;
        
        switch (useConnection)
        {
        case USE_VIA:
            serverProtocol = via.getServerProtocol();
            clientProtocol = via.getProtocol(player);
            break;
            
        case USE_PS:
            serverProtocol = ps.getServerProtocol();
            clientProtocol = ps.getProtocol(player);
            break;
            
        case USE_BOTH:
            serverProtocol = via.getServerProtocol(); // Get server info from ViaVersion

            // If PS ID < server ID, use PS; else use Via
            if (ps.getProtocol(player).getId() < serverProtocol.getId())
            {
                // Use PS
                clientProtocol = ps.getProtocol(player);
            }
            else
            {
                // Use Via
                clientProtocol = via.getProtocol(player);
            }
            break;
            
        case USE_NONE:
            // Should never get here
            return;
            
        default:
            // Should never get here
            return;
        }
        
        final String clientVersion = clientProtocol.getName();
        final String serverVersion = serverProtocol.getName();
        
//        // ***** DEBUG *****
//        plugin.getLogger().info("serverProtocol = " + serverProtocol.toString());
//        plugin.getLogger().info("clientProtocol = " + clientProtocol.toString());
//
//        if (via.isValid())
//            plugin.getLogger().info("viaProtocol    = " + via.getProtocol(player).toString());
//        else
//            plugin.getLogger().info("viaProtocol    = not valid");
//
//        if (ps.isValid())
//            plugin.getLogger().info("psProtocol     = " + ps.getProtocol(player).toString());
//        else
//            plugin.getLogger().info("psProtocol     = not valid");
//        // ***** DEBUG *****
        
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
            }, 5L); // time delay (ticks)
        }

    }
    
}
