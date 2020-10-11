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

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.protocol.utils.ProtocolVersionsHelper;

public class PSConnection implements Connection
{
    private boolean valid;
    
    // Constructor
    
    public PSConnection()
    {
        valid = Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport");
    }
    
    // isValid
    
    public boolean isValid()
    {
        return valid;
    }
    
    // ProtocolVersion
    
    public ProtocolVersion getProtocol(Player player)
    {
        ProtocolVersion protocol = new ProtocolVersion();
        
        protocolsupport.api.ProtocolVersion psProtocol = ProtocolSupportAPI.getProtocolVersion(player);
        
        protocol.id = psProtocol.getId();
        protocol.name = psProtocol.getName();
        // ProtocolSupport returns a null string if the version is newer than what it knows about
        if (protocol.name == null)
        {
            protocol.name = "Unknown";
        }
        
        return protocol;
    }
    
    // getServerProtocol
    
    public ProtocolVersion getServerProtocol()
    {
        ProtocolVersion protocol = new ProtocolVersion();

        // Server version is always the latest version supported
        protocol.id = ProtocolVersionsHelper.LATEST_PC.getId();
        protocol.name = ProtocolVersionsHelper.LATEST_PC.getName();
        
        return protocol;
    }
    
    // getSupportedProtocols();
    
    public List<ProtocolVersion> getSupportedProtocols()
    {
        return null; // not implemented
    }
}
