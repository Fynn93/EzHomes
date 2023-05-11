/*
 * This file is part of EzHomes.
 *
 * EzHomes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EzHomes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EzHomes.  If not, see <https://www.gnu.org/licenses/>.
 */

package lol.hyper.ezhomes.events;

import lol.hyper.ezhomes.EzHomes;
import lol.hyper.ezhomes.tools.HomeManagement;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMove implements Listener {

    private final EzHomes ezHomes;
    private final BukkitAudiences audiences;
    private final HomeManagement homeManagement;

    public PlayerMove(EzHomes ezHomes) {
        this.ezHomes = ezHomes;
        this.audiences = ezHomes.getAdventure();
        this.homeManagement = ezHomes.homeManagement;
    }

    public final HashMap<UUID, BukkitTask> teleportTasks = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!teleportTasks.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ() || event.getFrom().getBlockY() != event.getTo().getBlockY()) {
            Player player = event.getPlayer();
            teleportTasks.get(player.getUniqueId()).cancel();
            teleportTasks.remove(player.getUniqueId());
            audiences.player(player).sendMessage(ezHomes.getMessage("errors.teleport-canceled"));
            homeManagement.guiManagers.remove(player.getUniqueId());
        }
    }
}
