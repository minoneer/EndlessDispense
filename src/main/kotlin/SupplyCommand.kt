@file:Suppress("UnstableApiUsage")

package me.minoneer.bukkit.endlessdispense

import io.github.kraftlin.command.literal
import io.github.kraftlin.command.paper.executesPlayer
import io.github.kraftlin.command.paper.kraftlinCommand
import io.github.kraftlin.command.paper.requiresPermission
import io.github.kraftlin.command.paper.requiresPlayer
import org.bukkit.Sound
import org.bukkit.block.Container
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class SupplyCommand(private val messages: Messages) {

    fun buildCommand() = kraftlinCommand(
        name = "supply",
        description = "Toggles endless dispense mode for the dispenser or dropper you are looking at.",
    ) {
        requiresPermission(EndlessDispense.CREATE)
        requiresPlayer()

        literal("on") {
            executesPlayer { player, _ ->
                toggleDispenser(player, true)
            }
        }

        literal("off") {
            executesPlayer { player, _ ->
                toggleDispenser(player, false)
            }
        }
    }

    fun toggleDispenser(sender: Player, infinite: Boolean) {
        val block = sender.getTargetBlockExact(6)

        if (block == null || !block.isDispenser) {
            sender.sendMessage("You are not looking at a dispenser or dropper.")
            return
        }

        val state = block.state as Container
        val container = state.persistentDataContainer

        if (infinite) {
            container.set(EndlessDispense.SUPPLY_KEY, PersistentDataType.BYTE, 1.toByte())
            state.update()
            Refiller.refillInventory(state.inventory)
            sender.sendMessage("§e[EndlessDispense] §fDispenser is now §aEndless§f!")
            sender.playSound(block.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.2f)
        } else {
            container.remove(EndlessDispense.SUPPLY_KEY)
            state.update()
            Refiller.resetInventoryStacks(block)
            sender.sendMessage("§e[EndlessDispense] §fDispenser is no longer §cEndless§f.")
            sender.playSound(block.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.8f)
        }
    }
}
