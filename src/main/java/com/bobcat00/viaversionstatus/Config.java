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
    
    // Warning for players with mismatched version
    
    public boolean getWarnPlayers()
    {
        return plugin.getConfig().getBoolean("warn-players");
    }
    
    public String getWarnString()
    {
        return plugin.getConfig().getString("warn-string");
    }
    
    // Listener priority
    
    public boolean getHighPriority()
    {
        return plugin.getConfig().getBoolean("high-priority");
    }

}
