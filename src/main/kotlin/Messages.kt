package me.minoneer.bukkit.endlessdispense

import io.github.kraftlin.config.AbstractConfig
import io.github.kraftlin.config.paper.wrapConfig
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.nio.file.Path
import kotlin.properties.ReadOnlyProperty

class Messages(path: Path) : AbstractConfig(wrapConfig(path)) {
    private val mm = MiniMessage.miniMessage()
    private val legacy = LegacyComponentSerializer.legacyAmpersand()

    private fun configComponent(path: String, default: String): ReadOnlyProperty<Any?, Component> {
        val rawString: String by config(path = path, default = default, translateColorCodes = false)
        return ReadOnlyProperty { _, _ ->
            if (rawString.contains("&") && !rawString.contains("<")) {
                legacy.deserialize(rawString)
            } else {
                mm.deserialize(rawString)
            }
        }
    }

    val denyInventoryAccess by configComponent(
        path = "denied.inventory-access",
        default = "<red>This block is in Endless mode. Inventory not available."
    )

    val denyDestroySupplier by configComponent(
        path = "denied.destroy-supplier",
        default = "<red>You are not allowed to destroy blocks in Endless mode."
    )

    val supplyEnabled by configComponent(
        path = "command.supply-enabled",
        default = "<white>Dispenser is now <green>Endless<white>!"
    )

    val supplyDisabled by configComponent(
        path = "command.supply-disabled",
        default = "<white>Dispenser is no longer <red>Endless<white>."
    )

    val commandWrongBlock by configComponent(
        path = "command.wrong-block",
        default = "<yellow>[EndlessDispense] <red>You must be looking at a dispenser or dropper."
    )

    val suggestCommand by configComponent(
        path = "create.suggest-command",
        default = "<yellow>[EndlessDispense] <white>Please use <green><click:suggest_command:'/supply on'><hover:show_text:'Click to suggest command'>/supply on</hover></click><white> while looking at the dispenser instead of using signs."
    )

    val migrated by configComponent(
        path = "migration.success",
        default = "<yellow>[EndlessDispense] <white>Legacy dispenser migrated to modern system. Sign removed."
    )

}
