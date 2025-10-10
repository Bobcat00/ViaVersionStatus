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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class Config
{
    private ViaVersionStatus plugin;
    
    // Constructor
    
    public Config(ViaVersionStatus plugin)
    {
        this.plugin = plugin;
    }
    
    // Get the string to be sent to ops
    
    public boolean getNotifyOps()
    {
        return plugin.getConfig().getBoolean("notify-ops");
    }
    
    public String getNotifyString()
    {
        return plugin.getConfig().getString("notify-string");
    }
    
    public String getNotifyCommand()
    {
        return plugin.getConfig().getString("notify-command");
    }
    
    // Warning for players with mismatched version
    
    public boolean getOlderVersionWarnPlayers()
    {
        return plugin.getConfig().getBoolean("warn-players");
    }
    
    public String getOlderVersionWarnString()
    {
        return plugin.getConfig().getString("warn-string");
    }
    
    public String getOlderVersionWarnCommand()
    {
        return plugin.getConfig().getString("warn-command");
    }

    public boolean getNewerVersionWarnPlayers()
    {
        return plugin.getConfig().getBoolean("warn-players-newer");
    }

    public String getNewerVersionWarnString()
    {
        return plugin.getConfig().getString("warn-string-newer");
    }

    public String getNewerVersionWarnCommand()
    {
        return plugin.getConfig().getString("warn-command-newer");
    }
    
    // Listener priority
    
    public boolean getHighPriority()
    {
        return plugin.getConfig().getBoolean("high-priority");
    }
    
    // Supported protocols
    
    public boolean getListSupportedProtocols()
    {
        return plugin.getConfig().getBoolean("list-supported-protocols");
    }
    
    // Block no light data warnings
    
    public boolean getBlockNoLightDataWarnings()
    {
        return plugin.getConfig().getBoolean("block-no-light-data-warnings");
    }
    
    // Metrics
    
    public boolean getEnableMetrics()
    {
        return plugin.getConfig().getBoolean("enable-metrics");
    }
    
    // Prism integration
    
    public boolean getPrismIntegration()
    {
        return plugin.getConfig().getBoolean("prism-integration");
    }
    
    //--------------------------------------------------------------------------
    
    // Update the config file with new fields.
    
    private boolean contains(String path, boolean ignoreDefault)
    {
        // This duplicates the method added in 1.9, Bukkit commit facc9c353c3
        return ((ignoreDefault) ? plugin.getConfig().get(path, null) : plugin.getConfig().get(path)) != null;
    }
    
    public void updateConfig()
    {
        if (!contains("notify-ops", true))
        {
            plugin.getConfig().set("notify-ops", true);
        }
        
        if (!contains("notify-command", true))
        {
            plugin.getConfig().set("notify-command", "");
        }
        
        if (!contains("warn-command", true))
        {
            plugin.getConfig().set("warn-command", "");
        }

        if (!contains("warn-players-newer", true))
        {
            plugin.getConfig().set("warn-players-newer", plugin.getConfig().getBoolean("warn-players"));
        }

        if (!contains("warn-string-newer", true))
        {
            plugin.getConfig().set("warn-string-newer", plugin.getConfig().getString("warn-string"));
        }

        if (!contains("warn-command-newer", true))
        {
            plugin.getConfig().set("warn-command-newer", plugin.getConfig().getString("warn-command"));
        }
        
        if (!contains("list-supported-protocols", true))
        {
            plugin.getConfig().set("list-supported-protocols", true);
        }
        
        if (!contains("block-no-light-data-warnings", true))
        {
            plugin.getConfig().set("block-no-light-data-warnings", false);
        }
        
        if (!contains("enable-metrics", true))
        {
            plugin.getConfig().set("enable-metrics", true);
        }
        
        if (!contains("prism-integration", true))
        {
            plugin.getConfig().set("prism-integration",  false);
        }
        
        saveConfig();
    }
    
    //--------------------------------------------------------------------------
    
    // Save config to disk with embedded comments. Any change to the config file
    // format must also be changed here. Newlines are written as \n so they will
    // be the same on all platforms.
    
    public void saveConfig()
    {
        try
        {
            File outFile = new File(plugin.getDataFolder(), "config.yml");
            
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile.getAbsolutePath()), Charset.forName("UTF-8")));
            
            writer.write("# Strings must be enclosed in double quotes: \"...\"" + "\n");
            writer.write("# Supported placeholders: %player%, %displayname%, %version%, %server%" + "\n");
            writer.write("# Use \\n to insert a line break" + "\n");
            writer.write("\n");
            
            writer.write("# Notify server operators when a player joins." + "\n");
            writer.write("notify-ops: " + plugin.getConfig().getBoolean("notify-ops") + "\n");
            writer.write("notify-string: \"" + plugin.getConfig().getString("notify-string").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("notify-command: \"" + plugin.getConfig().getString("notify-command") + "\"" + "\n");
            writer.write("\n");
            
            writer.write("# Warn players if they are using an older client version." + "\n");
            writer.write("warn-players: " + plugin.getConfig().getBoolean("warn-players") + "\n");
            writer.write("warn-string: \"" + plugin.getConfig().getString("warn-string").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("warn-command: \"" + plugin.getConfig().getString("warn-command") + "\"" + "\n");
            writer.write("\n");

            writer.write("# Warn players if they are using a newer client version." + "\n");
            writer.write("warn-players-newer: " + plugin.getConfig().getBoolean("warn-players-newer") + "\n");
            writer.write("warn-string-newer: \"" + plugin.getConfig().getString("warn-string-newer").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("warn-command-newer: \"" + plugin.getConfig().getString("warn-command-newer") + "\"" + "\n");
            writer.write("\n");
            
            writer.write("# Run at the highest priority (MONITOR)." + "\n");
            writer.write("# Set this to true if %displayname% is not displayed correctly." + "\n");
            writer.write("high-priority: " + plugin.getConfig().getBoolean("high-priority") + "\n");
            writer.write("\n");
            
            writer.write("# At startup, list all protocols supported by ViaVersion." + "\n");
            writer.write("list-supported-protocols: " + plugin.getConfig().getBoolean("list-supported-protocols") + "\n");
            writer.write("\n");
            
            writer.write("# Suppress \"No light data found for chunk\" warning messages." + "\n");
            writer.write("block-no-light-data-warnings: " + plugin.getConfig().getBoolean("block-no-light-data-warnings") + "\n");
            writer.write("\n");
            
            writer.write("# Enable metrics collection (subject to the global bStats configuration)." + "\n");
            writer.write("enable-metrics: " + plugin.getConfig().getBoolean("enable-metrics") + "\n");
            writer.write("\n");
            
            writer.write("# Record data via Prism using the action \"vvs-client-connect\"." + "\n");
            writer.write("prism-integration: " + plugin.getConfig().getBoolean("prism-integration") + "\n");

            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //plugin.getLogger().info("Exception creating config file.");
        }
        
        plugin.reloadConfig();
        
    }
}
