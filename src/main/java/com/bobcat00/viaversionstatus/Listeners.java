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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitRunnable;

import com.bobcat00.viaversionstatus.connections.ProtocolVersion;
import com.bobcat00.viaversionstatus.connections.PSConnection;
import com.bobcat00.viaversionstatus.connections.ViaConnection;

public final class Listeners implements Listener
{
    private ViaVersionStatus plugin;
    
    private ViaConnection via;
    private PSConnection ps;
    
    public enum UseConnection
    {
        USE_VIA,
        USE_PS,
        USE_BOTH,
        USE_NONE
    }
    
    private UseConnection useConnection = UseConnection.USE_NONE;
    
    // List of pre-1.7 protocol IDs which may be returned by ProtocolSupport
    private final List<Integer> oldProtocolIds = new ArrayList<>(Arrays.asList(51, 60, 61, 73, 74, 78));
    
    // Variables for outputting supported protocols at startup
    private boolean outputVia = false;
    private boolean outputPs = false;
    private int protocolListCounter = 0;
    
    // Constructor
    
    public Listeners(ViaVersionStatus plugin)
    {
        this.plugin = plugin;
        
        // Register listener
        
        EventPriority priority = EventPriority.NORMAL;
        if (plugin.config.getHighPriority())
        {
            // Use MONITOR if true
            priority = EventPriority.MONITOR;
            plugin.getLogger().info("Using listener priority " + priority.toString() + ".");
        }
        
        plugin.getServer().getPluginManager().registerEvent(PlayerJoinEvent.class, this, priority,
            new EventExecutor() { public void execute(Listener l, Event e) { onPlayerJoin((PlayerJoinEvent)e); }},
            plugin);
        
        // Determine which connection(s) to use
        
        via = new ViaConnection();
        ps = new PSConnection();
        
        if (via.isValid() && ps.isValid())
        {
            useConnection = UseConnection.USE_BOTH;
            outputVia = true;
            outputPs = true;
            plugin.getLogger().info("Using both ViaVersion and ProtocolSupport to determine versions.");
        }
        else if (via.isValid())
        {
            useConnection = UseConnection.USE_VIA;
            outputVia = true;
            plugin.getLogger().info("Using ViaVersion to determine versions.");
        }
        else if (ps.isValid())
        {
            useConnection = UseConnection.USE_PS;
            outputPs = true;
            plugin.getLogger().info("Using ProtocolSupport to determine versions.");
        }
        else
        {
            plugin.getLogger().severe("This plugin requires either ViaVersion or ProtocolSupport or both.");
            plugin.shutdown();
            throw new RuntimeException("ViaVersion or ProtocolSupport required."); // Get the user's attention
        }
        
        // Output supported protocols after giving ViaVersion/ProtocolSupport time to populate them
        
        if (plugin.config.getListSupportedProtocols())
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (outputVia)
                    {
                        List<ProtocolVersion> protocols = via.getSupportedProtocols();

                        if ((protocols != null) && (!protocols.isEmpty()))
                        {
                            plugin.getLogger().info("ViaVersion supported protocols:");
                            for(ProtocolVersion protocol : protocols)
                            {
                                plugin.getLogger().info(protocol.toString());
                            }
                            // Indicate done
                            outputVia = false;
                        }
                    }
                    
                    if (outputPs)
                    {
                        List<ProtocolVersion> protocols = ps.getSupportedProtocols();

                        if ((protocols != null) && (!protocols.isEmpty()))
                        {
                            plugin.getLogger().info("ProtocolSupport supported protocols:");
                            for(ProtocolVersion protocol : protocols)
                            {
                                plugin.getLogger().info(protocol.toString());
                            }
                            // Indicate done
                            outputPs = false;
                        }
                    }

                    ++protocolListCounter;
                    // Cancel if nothing more is to be done or if we tried 10 times
                    if ((!outputVia && !outputPs) || protocolListCounter >= 10)
                    {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin,
                           100L,  // delay 5 sec
                           100L); // period 5 sec
        }
    }
    
    // Get connection used
    
    public UseConnection getConnectionUsed()
    {
        return useConnection;
    }
    
    // Player join event
    
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        final Player player = e.getPlayer();
        
        if (player.hasPermission("viaversionstatus.exempt"))
        {
            return;
        }
        
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
            clientProtocol = ps.getProtocol(player); // Start with PS client protocol

            // If PS ID <= 0 or PS ID >= server ID and not in the old protocol ID list, use Via instead
            if ((clientProtocol.getId() <= 0) ||
                ((clientProtocol.getId() >= serverProtocol.getId()) && (!oldProtocolIds.contains(clientProtocol.getId()))))
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
        
        // 1. Write to log file
        
        if (!player.hasPermission("viaversionstatus.exempt.log"))
        {
            plugin.getLogger().info(player.getName() + " is using version " + clientProtocol.toString() + ".");
        }

        // 2. Notify any player with the viaversionstatus.notify permission (ops by default),
        //    unless the player logging in has an exempt permission

        if (plugin.config.getNotifyOps() &&
            !player.hasPermission("viaversionstatus.exempt.notify"))
        {
            if (!player.hasPermission("viaversionstatus.exempt.notify.message"))
            {
                String notifyMessage = plugin.config.getNotifyString();
                if (!notifyMessage.isEmpty())
                {
                    for (Player p : Bukkit.getServer().getOnlinePlayers())
                    {
                        if (p.hasPermission("viaversionstatus.notify") &&
                            (!p.hasPermission("viaversionstatus.notify.ignoresame") || (clientProtocol.getId() != serverProtocol.getId())))
                        {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                notifyMessage.replace("%player%",      player.getName()).
                                              replace("%displayname%", player.getDisplayName()).
                                              replace("%version%",     clientVersion).
                                              replace("%server%",      serverVersion)));
                        }
                    }
                }
            }

            if (!player.hasPermission("viaversionstatus.exempt.notify.command"))
            {
                String notifyCommand = plugin.config.getNotifyCommand();
                if (!notifyCommand.isEmpty())
                {
                    notifyCommand = notifyCommand.replace("%player%",      player.getName()).
                                                  replace("%displayname%", player.getDisplayName()).
                                                  replace("%version%",     clientVersion).
                                                  replace("%server%",      serverVersion);
                    plugin.getLogger().info("Executing command " + notifyCommand);
                    try
                    {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), notifyCommand);
                    }
                    catch (CommandException exc)
                    {
                        plugin.getLogger().warning("Command returned exception: " + exc.getMessage());
                    }
                }
            }
        }
        
        // 3. Warn player if they are connecting using an older version
        
        if (plugin.config.getOlderVersionWarnPlayers() &&
            !serverVersion.equals("UNKNOWN") &&
            (clientProtocol.getId() < serverProtocol.getId()) &&
            !player.hasPermission("viaversionstatus.exempt.warn"))
        {
            handleMismatchedClientServerVersionsForTargetPlayer(player,
                                                                "viaversionstatus.exempt.warn.message",
                                                                "viaversionstatus.exempt.warn.command",
                                                                plugin.config.getOlderVersionWarnString(),
                                                                plugin.config.getOlderVersionWarnCommand(),
                                                                clientVersion,
                                                                serverVersion);
        }

        // 4. Warn player if they are connecting using a newer version

        if (plugin.config.getNewerVersionWarnPlayers() &&
            !serverVersion.equals("UNKNOWN") &&
            (clientProtocol.getId() > serverProtocol.getId()) &&
            !player.hasPermission("viaversionstatus.exempt.warn.newer"))
        {
            handleMismatchedClientServerVersionsForTargetPlayer(player,
                                                                "viaversionstatus.exempt.warn.newer.message",
                                                                "viaversionstatus.exempt.warn.newer.command",
                                                                plugin.config.getNewerVersionWarnString(),
                                                                plugin.config.getNewerVersionWarnCommand(),
                                                                clientVersion,
                                                                serverVersion);
        }
        
        // 5. Send to Prism
        
        if (plugin.prismHooked)
        {
            plugin.prismEvent.callPrismEvent(plugin, "vvs-client-connect", player, clientProtocol.toString());
        }

    }
    
    // Send warning message and execute warning command
    
    private void handleMismatchedClientServerVersionsForTargetPlayer(Player player,
                                                                     String messageExemptPermission,
                                                                     String commandExemptPermission,
                                                                     String warnMessage,
                                                                     String warnCommand,
                                                                     String clientVersion,
                                                                     String serverVersion)
    {
        if (!player.hasPermission(messageExemptPermission) &&
            !warnMessage.isEmpty())
        {
            // Delay by 250 msec (5 ticks) to make sure the player sees the message
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    if (player.isOnline())
                    {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            warnMessage.replace("%player%",      player.getName()).
                                        replace("%displayname%", player.getDisplayName()).
                                        replace("%version%",     clientVersion).
                                        replace("%server%",      serverVersion)));
                    }
                }
            }, 5L); // time delay (ticks)
        }

        if (!player.hasPermission(commandExemptPermission) &&
            !warnCommand.isEmpty())
        {
            warnCommand = warnCommand.replace("%player%",      player.getName()).
                                      replace("%displayname%", player.getDisplayName()).
                                      replace("%version%",     clientVersion).
                                      replace("%server%",      serverVersion);
            plugin.getLogger().info("Executing command " + warnCommand);
            try
            {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), warnCommand);
            }
            catch (CommandException exc)
            {
                plugin.getLogger().warning("Command returned exception: " + exc.getMessage());
            }
        }
    }
}
