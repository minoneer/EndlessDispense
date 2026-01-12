package me.minoneer.bukkit.endlessdispense.legacy

import io.papermc.paper.event.player.PlayerOpenSignEvent
import me.minoneer.bukkit.endlessdispense.EndlessDispense
import me.minoneer.bukkit.endlessdispense.Messages
import me.minoneer.bukkit.endlessdispense.legacy.Legacy.getFirstLine
import me.minoneer.bukkit.endlessdispense.legacy.Legacy.stripColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.SignChangeEvent

class SupplySignListener(private val messages: Messages) : Listener {

    companion object {
        private const val KEYWORD = "Supply"
        private const val SUPPLY_KEY = "[Supply]"
    }

    // Migrate on a block break attempt
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    fun onBlockBreakHigh(event: BlockBreakEvent) {
        if (Legacy.checkAndMigrateSupplySign(event.block, event.player)) {
            event.isCancelled = true
        }
    }

    // Migrate on sign open
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onSignEdit(event: PlayerOpenSignEvent) {
        if (Legacy.checkAndMigrateSupplySign(event.sign.block, event.player)) {
            event.isCancelled = true
        }
    }

    // Inform admins of the new command system
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
            player.sendMessage(messages.suggestCommand)
        }
    }
}
