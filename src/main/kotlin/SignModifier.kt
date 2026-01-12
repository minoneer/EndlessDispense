package me.minoneer.bukkit.endlessdispense

import org.bukkit.block.Block
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.inventory.InventoryHolder

class SignModifier(private val messages: Messages, private val refiller: Refiller) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        val player = event.player
        val signBlock = event.block
        val firstLine = event.getFirstLine().stripColor()

        if (player.hasPermission(EndlessDispense.CREATE)
            && (firstLine.equals(SUPPLY_KEY, ignoreCase = true) || firstLine.equals(KEYWORD, ignoreCase = true))
        ) {
            val dispensingBlock = signBlock.getAttachedTo()
            if (dispensingBlock.isDispenser) {
                event.setLine(0, COLORED_SUPPLY_KEY)
                val inventory = (dispensingBlock.state as InventoryHolder).inventory
                refiller.refillInventory(inventory)
                player.sendMessage(messages.createSuccess)
                (event.block.blockData as Sign).apply {
                    isWaxed = true
                    update()
                }
            } else {
                player.sendMessage(messages.createWrongBlock)
                signBlock.breakNaturally()
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        if (block.isSupplySign) {
            resetBlockStacks(block.getAttachedTo())
        } else if (block.isEndless()) {
            resetBlockStacks(block)
        }
    }

    private fun resetBlockStacks(block: Block) {
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
