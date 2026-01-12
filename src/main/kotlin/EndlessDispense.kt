package me.minoneer.bukkit.endlessdispense

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.permissions.Permission
import org.bukkit.plugin.java.JavaPlugin

class EndlessDispense : JavaPlugin() {

    companion object {
        val CREATE = Permission("endlessdispense.create")
        val DESTROY = Permission("endlessdispense.destroy")
        lateinit var SUPPLY_KEY: NamespacedKey
    }

    override fun onEnable() {
        SUPPLY_KEY = NamespacedKey(this, "infinite_supply")
        val messages = loadMessages()
        val refiller = Refiller()
        registerEvents(messages, refiller)
        logger.info("${pluginMeta.displayName} by minoneer activated.")
    }

    private fun loadMessages(): Messages {
        val path = dataFolder.toPath().resolve("messages.yml")
        val config = Messages(path)
        config.saveDefaults()
        config.reloadConfig()
        return config
    }

    private fun registerEvents(messages: Messages, refiller: Refiller) {
        val plugin = this
        with(Bukkit.getPluginManager()) {
            registerEvents(refiller, plugin)
            registerEvents(SignModifier(messages, refiller), plugin)
            registerEvents(BlockProtection(messages), plugin)
        }
    }

    override fun onDisable() {
        logger.info("${pluginMeta.displayName} by minoneer deactivated.")
    }
}
