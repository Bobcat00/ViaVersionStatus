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
import org.prism_mc.prism.api.actions.types.ActionType;
import org.prism_mc.prism.paper.api.PrismPaperApi;

public interface PrismEventInterface
{
    public void callPrismEvent(PrismPaperApi prism, ActionType actionType, Player player, String message);
}
