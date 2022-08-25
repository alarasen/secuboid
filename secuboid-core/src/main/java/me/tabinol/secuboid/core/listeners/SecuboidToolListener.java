/*
 Secuboid: Lands and Protection plugin for Minecraft server
 Copyright (C) 2014 Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.secuboid.core.listeners;

import me.tabinol.secuboid.core.SecuboidImpl;
import me.tabinol.secuboid.core.commands.items.SecuboidTool;
import me.tabinol.secuboid.core.players.PlayerInfoImpl;
import me.tabinol.secuboid.core.selection.PlayerSelection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class SecuboidToolListener extends AbstractListener {

    SecuboidToolListener() {
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractNormal(@NotNull PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();

        if (!isSecuboidTool(itemStack)) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();
        PlayerInfoImpl playerInfoImpl = getPlayerInfoImpl(player);
        PlayerSelection playerSelection = playerInfoImpl.getPlayerSelection();
        Action action = event.getAction();

        if (action == LEFT_CLICK_BLOCK) {
            leftClick(playerInfoImpl, playerSelection);
        } else if (action == RIGHT_CLICK_BLOCK) {
            rightClick(playerInfoImpl, playerSelection);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntityEventNormal(@NotNull EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();

        if (entity instanceof Player player) {

            PlayerInventory inventory = player.getInventory();

            ItemStack itemStack = inventory.getItemInMainHand();

            if (isSecuboidTool(itemStack)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isSecuboidTool(@Nullable ItemStack itemStack) {
        return getSecuboidTool().isSecuboidTool(itemStack);
    }

    private void leftClick(@NotNull PlayerInfoImpl playerInfoImpl, @NotNull PlayerSelection playerSelection) {
        if (!playerSelection.hasSelection()) {
            // todo select here
        }
    }

    private void rightClick(@NotNull PlayerInfoImpl playerInfoImpl, @NotNull PlayerSelection playerSelection) {

    }

    @NotNull
    private SecuboidTool getSecuboidTool() {
        return SecuboidImpl.instance().getSecuboidTool();
    }
}
