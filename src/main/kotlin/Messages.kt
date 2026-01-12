package me.minoneer.bukkit.endlessdispense

import io.github.kraftlin.config.AbstractConfig
import io.github.kraftlin.config.paper.wrapConfig
import java.nio.file.Path

class Messages(path: Path) : AbstractConfig(wrapConfig(path)) {

    val denyInventoryAccess: String by config(
        path = "denied.inventory-access",
        default = "&cThis block has a supply sign attached. Inventory not available.",
        translateColorCodes = true
    )

    val denyDestroySupplier: String by config(
        path = "denied.destroy-supplier",
        default = "&cYou are not allowed to destroy blocks with supply signs.",
        translateColorCodes = true
    )

    val denyDestroySign: String by config(
        path = "denied.destroy-sign",
        default = "&cYou are not allowed to destroy supply signs.",
        translateColorCodes = true
    )

    val denyCreateSign: String by config(
        path = "denied.create-sign",
        default = "&cYou are not allowed to create supply signs.",
        translateColorCodes = true
    )

    val denyEditSign: String by config(
        path = "denied.edit-sign",
        default = "&cYou are not allowed to edit supply signs.",
        translateColorCodes = true
    )

    val createSuccess: String by config(
        path = "create.success",
        default = "&eSupply sign was created successfully.",
        translateColorCodes = true
    )

    val createWrongBlock: String by config(
        path = "create.wrong-block",
        default = "&cYou have to attach supply signs to a dispenser or dropper.",
        translateColorCodes = true
    )
}
