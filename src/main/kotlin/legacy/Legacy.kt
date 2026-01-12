package me.minoneer.bukkit.endlessdispense.legacy

import me.minoneer.bukkit.endlessdispense.EndlessDispense
import me.minoneer.bukkit.endlessdispense.Messages
import me.minoneer.bukkit.endlessdispense.PluginConfig
import me.minoneer.bukkit.endlessdispense.isDispenser
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.block.*
import org.bukkit.block.data.type.WallHangingSign
import org.bukkit.block.data.type.WallSign
import org.bukkit.block.sign.Side
import org.bukkit.block.sign.SignSide
import org.bukkit.command.CommandSender
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.persistence.PersistentDataType

object Legacy {

    internal lateinit var messages: Messages
    internal lateinit var config: PluginConfig
    private val SIGN_LOCATIONS: Collection<BlockFace> = setOf(
        BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN
    )

    private const val COLORED_SUPPLY_KEY = "§1[Supply]"

    internal fun checkAndMigrateDispenser(
        block: Block,
        state: TileState,
        sender: CommandSender? = null
    ): Boolean {
        if (!config.migrateLegacy) return false
        val signBlock = block.getSupplySignBlock() ?: return false
        applyMetadata(state)
        signBlock.type = Material.AIR
        sender?.sendMessage(messages.migrated)
        return true
    }

    internal fun checkAndMigrateSupplySign(signBlock: Block, sender: CommandSender): Boolean {
        if (!config.migrateLegacy) return false
        if (!signBlock.isSupplySign) return false
        val dispenserBlock = signBlock.getAttachedTo()
        if (!dispenserBlock.isDispenser) return false
        val state = dispenserBlock.state as Container
        applyMetadata(state)
        signBlock.type = Material.AIR
        sender.sendMessage(messages.migrated)
        return true
    }

    private fun applyMetadata(state: TileState) {
        val container = state.persistentDataContainer
        container.set(EndlessDispense.SUPPLY_KEY, PersistentDataType.BYTE, 1.toByte())
        state.update()
    }

    private val Block.isSupplySign: Boolean
        get() {
            return if (this.isSign) {
                val sign = state as Sign
                sign.getSide(Side.FRONT).getFirstLine() == COLORED_SUPPLY_KEY
                        || sign.getSide(Side.BACK).getFirstLine() == COLORED_SUPPLY_KEY
            } else {
                false
            }
        }

    private fun Block.getSupplySignBlock(): Block? {
        for (direction in SIGN_LOCATIONS) {
            val signBlock = getRelative(direction)
            if (signBlock.isSupplySign && signBlock.isAttachedTo(this)) {
                return signBlock
            }
        }
        return null
    }

    private fun Block.isAttachedTo(other: Block): Boolean = getAttachedTo() == other

    private fun Block.getAttachedTo(): Block {
        val signBlock = this
        return when {
            signBlock.isWallSign -> {
                val blockData = signBlock.blockData as WallSign
                signBlock.getRelative(blockData.facing.oppositeFace)
            }

            signBlock.isStandingSign -> {
                signBlock.getRelative(BlockFace.UP)
            }

            signBlock.isWallHangingSign -> {
                val blockData = signBlock.blockData as WallHangingSign
                signBlock.getRelative(blockData.facing.oppositeFace)
            }

            signBlock.isCeilingHangingSign -> {
                signBlock.getRelative(BlockFace.UP)
            }

            else -> {
                throw IllegalArgumentException("Block is not a sign: $this")
            }
        }
    }

    private val Block.isSign: Boolean get() = Tag.SIGNS.isTagged(type)
    private val Block.isWallSign: Boolean get() = Tag.WALL_SIGNS.isTagged(type)
    private val Block.isStandingSign: Boolean get() = Tag.STANDING_SIGNS.isTagged(type)
    private val Block.isCeilingHangingSign: Boolean get() = Tag.CEILING_HANGING_SIGNS.isTagged(type)
    private val Block.isWallHangingSign: Boolean get() = Tag.WALL_HANGING_SIGNS.isTagged(type)

    internal fun String.stripColor(): String {
        return PlainTextComponentSerializer.plainText()
            .serialize(LegacyComponentSerializer.legacyAmpersand().deserialize(this))
    }

    @Suppress("DEPRECATION")
    internal fun SignChangeEvent.getFirstLine(): String = getLine(0) ?: ""

    @Suppress("DEPRECATION")
    private fun SignSide.getFirstLine(): String = getLine(0)
}
