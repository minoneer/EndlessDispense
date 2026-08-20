package me.minoneer.bukkit.endlessdispense

import io.papermc.paper.event.block.BlockPreDispenseEvent
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.plugin.Plugin

class Refiller(private val plugin: Plugin) : Listener {

    /**
     * Restores the slot a dispense is about to draw from.
     *
     * Topping the slot back up in [BlockDispenseEvent] only works while something is left in it.
     * An item that does not stack leaves the slot empty, and by then neither the slot number nor
     * what used to be in it can be recovered. This event still sees both, so take a copy here and
     * put it back once the dispense has run.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockPreDispense(event: BlockPreDispenseEvent) {
        val block = event.block
        if (!block.isEndless()) return
        val slot = event.slot
        val stock = (block.state as? InventoryHolder)?.inventory?.getItem(slot)?.clone() ?: return

        // Vanilla writes the reduced stack back after the dispense behaviour has run, so the
        // slot cannot be restored from within the dispense itself.
        plugin.server.scheduler.runTask(plugin, Runnable { restock(block, slot, stock) })
    }

    private fun restock(block: Block, slot: Int, stock: ItemStack) {
        if (!block.isEndless()) return
        val inventory = (block.state as? InventoryHolder)?.inventory ?: return
        if (slot !in 0 until inventory.size) return

        val restored = stock.clone().apply { amount = inventory.fillAmount(this) }
        if (restored != inventory.getItem(slot)) {
            inventory.setItem(slot, restored)
        }
    }

    @EventHandler
    fun onBlockDispense(event: BlockDispenseEvent) {
        if (event.block.isEndless()) {
            val inventory = (event.block.state as? InventoryHolder)?.inventory ?: return
            refillInventory(inventory)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        if (block.isEndless(event.player)) {
            resetInventoryStacks(block)
            event.isDropItems = false
        }
    }

    companion object {

        /**
         * How many of [itemStack] a slot of this inventory holds.
         *
         * Since Minecraft 1.20.5 an item carries its own `max_stack_size`, and a container can
         * only ever lower that, never raise it. Asking for more than the item allows is silently
         * clamped on write, which used to leave tools sitting at a single item.
         */
        private fun Inventory.fillAmount(itemStack: ItemStack) =
            minOf(maxStackSize, itemStack.maxStackSize)

        internal fun refillInventory(inventory: Inventory) {
            for (i in 0 until inventory.size) {
                val itemStack = inventory.getItem(i) ?: continue
                var changed = false

                val full = inventory.fillAmount(itemStack)
                if (itemStack.amount != full) {
                    itemStack.amount = full
                    changed = true
                }

                if (itemStack.type == Material.SHEARS) {
                    val meta = itemStack.itemMeta
                    (meta as Damageable).damage = 0
                    itemStack.itemMeta = meta
                    changed = true
                }

                if (changed) inventory.setItem(i, itemStack)
            }
        }

        internal fun resetInventoryStacks(block: Block) {
            val blockState = block.state
            if (blockState is InventoryHolder) {
                val inventory = blockState.inventory
                for (i in 0 until inventory.size) {
                    val itemStack = inventory.getItem(i)
                    if (itemStack != null && itemStack.amount > itemStack.maxStackSize) {
                        itemStack.amount = itemStack.maxStackSize
                    }
                }
            }
        }
    }
}
