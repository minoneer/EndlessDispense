package me.minoneer.bukkit.endlessdispense

import org.bukkit.block.BlockState
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class BlockProtection(private val messages: Messages) : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val inventoryHolder = event.inventory.holder
        if (inventoryHolder is BlockState) {
            val block = inventoryHolder.block

            if (block.isEndless(event.player)) {
                event.isCancelled = true
                event.player.sendActionBar(messages.denyInventoryAccess)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockBreakHigh(event: BlockBreakEvent) {
        val block = event.block
        val player = event.player

        if (!player.hasPermission(EndlessDispense.DESTROY)) {
            if (block.isEndless(player)) {
                event.isCancelled = true
                player.sendActionBar(messages.denyDestroySupplier)
            }
        }
    }
}
