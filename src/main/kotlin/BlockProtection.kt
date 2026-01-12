package me.minoneer.bukkit.endlessdispense

import io.papermc.paper.event.player.PlayerOpenSignEvent
import org.bukkit.block.BlockState
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class BlockProtection(private val messages: Messages) : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val inventoryHolder = event.inventory.holder
        if (inventoryHolder is BlockState) {
            val block = inventoryHolder.block

            if (block.isEndless()) {
                event.isCancelled = true
                event.player.sendMessage(messages.denyInventoryAccess)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockBreakHigh(event: BlockBreakEvent) {
        val block = event.block
        val player = event.player

        if (!player.hasPermission(EndlessDispense.DESTROY)) {
            if (block.isEndless()) {
                event.isCancelled = true
                player.sendMessage(messages.denyDestroySupplier)
            } else if (block.isSupplySign) {
                event.isCancelled = true
                player.sendMessage(messages.denyDestroySign)
            }
        }
    }

    // Protect legacy supply signs while we still use them for conversion

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onSignEdit(event: PlayerOpenSignEvent) {
        val player = event.player
        val block = event.sign.block
        if (block.isSupplySign && !player.hasPermission(EndlessDispense.CREATE)) {
            event.isCancelled = true
            player.sendMessage(messages.denyEditSign)
        }
    }

    @EventHandler
    fun onSignChange(event: SignChangeEvent) {
        val player = event.player
        val firstLine = event.getFirstLine().stripColor()

        if (firstLine.equals(SUPPLY_KEY, ignoreCase = true) && !player.hasPermission(EndlessDispense.CREATE)) {
            event.isCancelled = true
            player.sendMessage(messages.denyCreateSign)
        }
    }
}
