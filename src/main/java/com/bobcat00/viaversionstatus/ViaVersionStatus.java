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

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ViaVersionStatus extends JavaPlugin
{
    Config config;
    Listeners listeners;
    
    @Override
    public void onEnable()
    {
        config = new Config(this);
        saveDefaultConfig();
        reloadConfig();
        config.saveConfig(); // Ensure config is up to date
        
        listeners = new Listeners(this);
        
        // Metrics
        Metrics metrics = new Metrics(this);
        if (metrics.isEnabled())
        {
            metrics.addCustomChart(new Metrics.SimplePie("connection_used", () -> listeners.getConnectionUsed().toString()));
            metrics.addCustomChart(new Metrics.SimplePie("warn_players",    () -> config.getWarnPlayers()                                      ? "Yes" : "No"));
            metrics.addCustomChart(new Metrics.SimplePie("viaversion",      () -> Bukkit.getPluginManager().isPluginEnabled("ViaVersion")      ? "Yes" : "No"));
            metrics.addCustomChart(new Metrics.SimplePie("viabackwards",    () -> Bukkit.getPluginManager().isPluginEnabled("ViaBackwards")    ? "Yes" : "No"));
            metrics.addCustomChart(new Metrics.SimplePie("viarewind",       () -> Bukkit.getPluginManager().isPluginEnabled("ViaRewind")       ? "Yes" : "No"));
            metrics.addCustomChart(new Metrics.SimplePie("protocolsupport", () -> Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport") ? "Yes" : "No"));
            
            getLogger().info("Enabled metrics. You may opt-out by changing plugins/bStats/config.yml");
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
