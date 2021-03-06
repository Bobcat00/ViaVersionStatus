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
    
    public String getNotifyString()
    {
        return plugin.getConfig().getString("notify-string");
    }
    
    public String getNotifyCommand()
    {
        return plugin.getConfig().getString("notify-command");
    }
    
    // Warning for players with mismatched version
    
    public boolean getWarnPlayers()
    {
        return plugin.getConfig().getBoolean("warn-players");
    }
    
    public String getWarnString()
    {
        return plugin.getConfig().getString("warn-string");
    }
    
    public String getWarnCommand()
    {
        return plugin.getConfig().getString("warn-command");
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
        if (!contains("notify-command", true))
        {
            plugin.getConfig().set("notify-command", "");
        }
        
        if (!contains("warn-command", true))
        {
            plugin.getConfig().set("warn-command", "");
        }
        
        if (!contains("list-supported-protocols", true))
        {
            plugin.getConfig().set("list-supported-protocols", true);
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
            
            writer.write("# Strings should be enclosed in double quotes: \"...\"" + "\n");
            writer.write("# Supported variables are %player%, %displayname%, %version%, and %server%" + "\n");
            writer.write("# \\n can be used as a line break" + "\n");
            writer.write("\n");
            
            writer.write("# The string to send to ops when a player joins" + "\n");
            writer.write("notify-string: \"" + plugin.getConfig().getString("notify-string").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("notify-command: \"" + plugin.getConfig().getString("notify-command") + "\"" + "\n");
            writer.write("\n");
            
            writer.write("# Warn players when they have a mismatched version" + "\n");
            writer.write("warn-players: " + plugin.getConfig().getBoolean("warn-players") + "\n");
            writer.write("warn-string: \"" + plugin.getConfig().getString("warn-string").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("warn-command: \"" + plugin.getConfig().getString("warn-command") + "\"" + "\n");
            writer.write("\n");
            
            writer.write("# Run at the highest priority (MONITOR)" + "\n");
            writer.write("# Set to true if %displayname% doesn't work as expected" + "\n");
            writer.write("high-priority: " + plugin.getConfig().getBoolean("high-priority") + "\n");
            writer.write("\n");
            
            writer.write("# At startup, list the protocols supported by ViaVersion" + "\n");
            writer.write("list-supported-protocols: " + plugin.getConfig().getBoolean("list-supported-protocols") + "\n");
            writer.write("\n");
            
            writer.write("# Record data via Prism, with the action vvs-client-connect" + "\n");
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
