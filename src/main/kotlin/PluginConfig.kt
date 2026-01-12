package me.minoneer.bukkit.endlessdispense

import io.github.kraftlin.config.AbstractConfig
import io.github.kraftlin.config.paper.wrapConfig
import org.bukkit.plugin.Plugin

class PluginConfig(plugin: Plugin) : AbstractConfig(wrapConfig(plugin)) {
    val migrateLegacy: Boolean by config(
        path = "migrate_legacy",
        default = false
    )
}
