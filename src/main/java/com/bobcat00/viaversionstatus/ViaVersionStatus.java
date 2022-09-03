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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import network.darkhelmet.prism.Prism;
import network.darkhelmet.prism.actionlibs.ActionTypeImpl;
import network.darkhelmet.prism.exceptions.InvalidActionException;

public final class ViaVersionStatus extends JavaPlugin
{
    ViaVersionStatus plugin = this;
    Config config;
    Listeners listeners;
    Prism prism;
    String prismVersion = "unknown";
    PrismEvent prismEvent; // Used to send event to Prism
    boolean prismHooked = false;
    int prismCounter = 0;
    
    @Override
    public void onEnable()
    {
        config = new Config(this);
        saveDefaultConfig();
        // Update old config file
        config.updateConfig();
        
        listeners = new Listeners(this);
        
        // Block "No light data found for chunk" messages
        if (config.getBlockNoLightDataWarnings())
        {
            ((Logger) LogManager.getRootLogger()).addFilter(new LogFilter("No light data found for chunk"));
        }
        
        // Prism
        if (config.getPrismIntegration())
        {
            Plugin prismPlugin = Bukkit.getPluginManager().getPlugin("Prism");
            if (prismPlugin != null && prismPlugin.isEnabled())
            {
                try
                {
                    prism = (Prism) prismPlugin;
                    prismVersion = prism.getDescription().getVersion();

                    // Register our custom event. We have to wait until Prism is fully up and running.
                    // We do this by waiting for PurgeManager to be set.

                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if (prism.getPurgeManager() != null)
                            {
                                try
                                {
                                    // Register the custom event
                                    ActionTypeImpl actionType = new ActionTypeImpl("vvs-client-connect", PrismPlayerAction.class, "client version");
                                    Prism.getActionRegistry().registerCustomAction(plugin, actionType);
                                    prismEvent = new PrismEvent();
                                    prismHooked = true;
                                    getLogger().info("Hooked into Prism version " + prismVersion);
                                    this.cancel();
                                }
                                catch (InvalidActionException e)
                                {
                                    // Exception thrown by Prism
                                    getLogger().warning("Unable to hook into Prism: ");
                                    getLogger().warning(e.getMessage());
                                    getLogger().warning("Check Prism's config to ensure that tracking.api is true");
                                    getLogger().warning("and ViaVersionStatus is in the allowed-plugins list.");
                                    this.cancel(); // only try once
                                }
                            }
                            else
                            {
                                ++prismCounter;
                                // Cancel if we tried 50 times
                                if (prismCounter >= 50)
                                {
                                    getLogger().warning("Unable to hook into Prism. Check if Prism is working.");
                                    this.cancel();
                                }
                            }
                        }
                    }.runTaskTimer(this,
                            1L,  // delay 1 tick
                            4L); // period 200 msec
                }
                catch (NoClassDefFoundError e)
                {
                    getLogger().warning("Unable to hook into Prism. Make sure you are using Prism version 3.x or later.");
                }
            }
            else
            {
                getLogger().warning("prism-integration is true but Prism is not present or not enabled.");
            }
        }
        
        // Metrics
        if (config.getEnableMetrics())
        {
            int pluginId = 4834;
            Metrics metrics = new Metrics(this, pluginId);
            
            metrics.addCustomChart(new SimplePie("connection_used",    () -> listeners.getConnectionUsed().toString()));
            metrics.addCustomChart(new SimplePie("viaversion",         () -> Bukkit.getPluginManager().isPluginEnabled("ViaVersion")                               ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("viabackwards",       () -> Bukkit.getPluginManager().isPluginEnabled("ViaBackwards")                             ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("viarewind",          () -> Bukkit.getPluginManager().isPluginEnabled("ViaRewind")                                ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("protocolsupport",    () -> Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport")                          ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("notify_ops",         () -> config.getNotifyOps()               && !config.getNotifyString().isEmpty()            ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("notify_command",     () -> config.getNotifyOps()               && !config.getNotifyCommand().isEmpty()           ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("warn_players",       () -> config.getOlderVersionWarnPlayers() && !config.getOlderVersionWarnString().isEmpty()  ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("warn_command",       () -> config.getOlderVersionWarnPlayers() && !config.getOlderVersionWarnCommand().isEmpty() ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("warn_players_newer", () -> config.getNewerVersionWarnPlayers() && !config.getNewerVersionWarnString().isEmpty()  ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("warn_command_newer", () -> config.getNewerVersionWarnPlayers() && !config.getNewerVersionWarnCommand().isEmpty() ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("listener_priority",  () -> config.getHighPriority()                                                              ? "Monitor" : "Normal"));
            metrics.addCustomChart(new SimplePie("list_protocols",     () -> config.getListSupportedProtocols()                                                    ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("block_no_light",     () -> config.getBlockNoLightDataWarnings()                                                  ? "Yes" : "No"));
            metrics.addCustomChart(new SimplePie("prism_integration",  () -> config.getPrismIntegration()                                                          ? prismVersion : "No"));
            
            getLogger().info("Metrics enabled if allowed by plugins/bStats/config.yml");
        }
    }
 
    @Override
    public void onDisable()
    {
    }
    
    public void shutdown()
    {
        setEnabled(false);
    }

}
