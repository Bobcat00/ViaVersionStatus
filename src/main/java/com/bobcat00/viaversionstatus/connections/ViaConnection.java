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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;

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
            protocol.name = com.viaversion.viaversion.api.protocol.version.ProtocolVersion.getProtocol(protocol.id).getName();
        }
        
        return protocol;
    }
    
    // getServerProtocol
    
    public ProtocolVersion getServerProtocol()
    {
        ProtocolVersion protocol = new ProtocolVersion();
        
        protocol.id = api.getServerVersion().highestSupportedVersion();
        protocol.name = com.viaversion.viaversion.api.protocol.version.ProtocolVersion.getProtocol(protocol.id).getName();
        
        return protocol;
    }
    
    // getSupportedProtocols();
    
    public List<ProtocolVersion> getSupportedProtocols()
    {
        List<ProtocolVersion> versions = new ArrayList<ProtocolVersion>();
        
        if (api != null)
        {
            @SuppressWarnings("unchecked")
            SortedSet<Integer> ids = api.getSupportedVersions();
            
            for (Integer id : ids)
            {
                versions.add(new ProtocolVersion(id, com.viaversion.viaversion.api.protocol.version.ProtocolVersion.getProtocol(id).getName()));
            }
        }
        
        return versions;
    }

}
