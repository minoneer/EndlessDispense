package me.minoneer.bukkit.endlessdispense

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.meta.Damageable

private const val FULL_STACK_SIZE = 64

class Refiller : Listener {

    @EventHandler
    fun onBlockDispense(event: BlockDispenseEvent) {
        if (event.block.isEndless()) {
            val inventory = (event.block.state as? InventoryHolder)?.inventory ?: return
            refillInventory(inventory)
        }
    }

    internal fun refillInventory(inventory: Inventory) {
        inventory.maxStackSize = FULL_STACK_SIZE
        for (i in 0 until inventory.size) {
            val itemStack = inventory.getItem(i)
            if (itemStack != null && itemStack.amount != FULL_STACK_SIZE) {
                itemStack.amount = FULL_STACK_SIZE
                inventory.setItem(i, itemStack)
            }
            if (itemStack != null && itemStack.type == Material.SHEARS) {
                val meta = itemStack.itemMeta
                (meta as Damageable).damage = 0
                itemStack.itemMeta = meta
                inventory.setItem(i, itemStack)
            }
        }
    }
}
