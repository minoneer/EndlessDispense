package me.minoneer.bukkit.endlessdispense

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.SignChangeEvent

class SignModifier(private val messages: Messages) : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onSignChange(event: SignChangeEvent) {
        val player = event.player
        val firstLine = event.getFirstLine().stripColor()

        if (player.hasPermission(EndlessDispense.CREATE)
            && (firstLine.equals(SUPPLY_KEY, ignoreCase = true) || firstLine.equals(KEYWORD, ignoreCase = true))
        ) {
            // Cancel the sign creation and suggest the command
            event.isCancelled = true
            event.block.breakNaturally()
            player.sendMessage("§e[EndlessDispense] §fPlease use §a/supply on§f while looking at the dispenser instead of using signs.")
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        if (block.isEndless()) {
            Refiller.resetInventoryStacks(block)
        } else if (block.isSupplySign) {
            // legacy setups that have not yet been converted
            Refiller.resetInventoryStacks(block.getAttachedTo())
        }
    }
}
