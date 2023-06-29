package me.minoneer.bukkit.endlessdispense

import org.bukkit.ChatColor
import org.bukkit.ChatColor.DARK_BLUE
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.block.data.type.WallHangingSign
import org.bukkit.block.data.type.WallSign
import org.bukkit.block.sign.Side
import org.bukkit.event.block.SignChangeEvent
import java.util.*

private val SIGN_LOCATIONS: Collection<BlockFace> = EnumSet.of(
    BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN
)
const val KEYWORD = "Supply"
const val SUPPLY_KEY = "[Supply]"
val COLORED_SUPPLY_KEY = "$DARK_BLUE$SUPPLY_KEY"

val Block.isDispenser: Boolean
    get() = (type == Material.DISPENSER || type == Material.DROPPER)

val Block.isSupplySign: Boolean
    get() {
        return if (this.isSign) {
            val sign = state as Sign
            sign.getSide(Side.FRONT).getLine(0) == COLORED_SUPPLY_KEY ||
                    sign.getSide(Side.BACK).getLine(0) == COLORED_SUPPLY_KEY
        } else {
            false
        }
    }

fun Block.hasSupplySign(): Boolean {
    if (!isDispenser) {
        return false
    }
    for (direction in SIGN_LOCATIONS) {
        val signBlock = getRelative(direction)
        if (signBlock.isSupplySign && signBlock.isAttachedTo(this)) {
            return true
        }
    }
    return false
}

private fun Block.isAttachedTo(other: Block): Boolean = getAttachedTo() == other

fun Block.getAttachedTo(): Block {
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

fun String.stripColor(): String {
    val colorCoded = ChatColor.translateAlternateColorCodes('&', this)
    return ChatColor.stripColor(colorCoded)!!
}

fun SignChangeEvent.getFirstLine(): String = getLine(0) ?: ""
