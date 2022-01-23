// ViaVersionStatus - Logs players' client versions
// Copyright 2021 Bobcat00
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

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import network.darkhelmet.prism.events.PrismCustomPlayerActionEvent;

public class PrismEvent implements PrismEventInterface
{
    public void callPrismEvent(Plugin plugin, String actionTypeName, Player player, String message)
    {
        PrismCustomPlayerActionEvent prismEvent = new PrismCustomPlayerActionEvent(plugin, actionTypeName, player, message);
        plugin.getServer().getPluginManager().callEvent(prismEvent);
    }
}
