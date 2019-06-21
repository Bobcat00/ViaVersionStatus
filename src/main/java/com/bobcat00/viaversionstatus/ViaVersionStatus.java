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
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ViaVersionStatus extends JavaPlugin
{
    Config config;
    Listener listeners;

    @Override
    public void onEnable()
    {
        config = new Config(this);
        saveDefaultConfig();
        
        listeners = new Listeners(this);
        
        // Metrics
        Metrics metrics = new Metrics(this);
        if (metrics.isEnabled())
        {
            metrics.addCustomChart(new Metrics.SimplePie("warn_players", () -> config.getWarnPlayers() ? "Yes" : "No"));
            getLogger().info("Enabled metrics. You may opt-out by changing plugins/bStats/config.yml");
        }
    }
 
    @Override
    public void onDisable()
    {
    }

}
