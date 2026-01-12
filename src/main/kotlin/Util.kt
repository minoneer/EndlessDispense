package me.minoneer.bukkit.endlessdispense

import me.minoneer.bukkit.endlessdispense.legacy.Legacy
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.TileState
import org.bukkit.command.CommandSender
import org.bukkit.persistence.PersistentDataType


val Block.isDispenser: Boolean
    get() = (type == Material.DISPENSER || type == Material.DROPPER)

fun Block.isEndless(sender: CommandSender? = null): Boolean {
    val state = this.state as? TileState ?: return false
    val container = state.persistentDataContainer

    if (container.has(EndlessDispense.SUPPLY_KEY, PersistentDataType.BYTE)) return true

    return Legacy.checkAndMigrateDispenser(this, state, sender)
}
