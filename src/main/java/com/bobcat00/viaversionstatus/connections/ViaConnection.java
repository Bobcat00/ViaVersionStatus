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

package com.bobcat00.viaversionstatus.connections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;

public class ViaConnection implements Connection
{
    private boolean valid;
    @SuppressWarnings("rawtypes")
    private ViaAPI api;
    
    // Constructor
    
    public ViaConnection()
    {
        valid = Bukkit.getPluginManager().isPluginEnabled("ViaVersion");
        if (valid)
        {
            api = Via.getAPI();
        }
    }
    
    // isValid
    
    public boolean isValid()
    {
        return valid;
    }
    
    // getProtocol
    
    @SuppressWarnings("unchecked")
    public ProtocolVersion getProtocol(Player player)
    {
        ProtocolVersion protocol = new ProtocolVersion();
        
        if (api != null)
        {
            protocol.id = api.getPlayerVersion(player);
            protocol.name = us.myles.ViaVersion.api.protocol.ProtocolVersion.getProtocol(protocol.id).getName();
        }
        
        return protocol;
    }
    
    // getServerProtocol
    
    public ProtocolVersion getServerProtocol()
    {
        ProtocolVersion protocol = new ProtocolVersion();
        
        protocol.id = ProtocolRegistry.SERVER_PROTOCOL;
        protocol.name = us.myles.ViaVersion.api.protocol.ProtocolVersion.getProtocol(protocol.id).getName();
        
        return protocol;
    }
}
