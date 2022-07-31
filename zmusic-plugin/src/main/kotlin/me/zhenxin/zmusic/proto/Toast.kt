package me.zhenxin.zmusic.proto

import me.zhenxin.zmusic.logger
import me.zhenxin.zmusic.proto.packet.impl.*
import me.zhenxin.zmusic.utils.sendBridgeToast
import taboolib.common.platform.Platform
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.runningPlatform
import taboolib.platform.BukkitPlugin

/**
 * 吐司
 *
 * @author 真心
 * @since 2022/6/9 3:45
 */

/**
 * 发送Toast
 * @param title 信息
 */
@Suppress("MoveVariableDeclarationIntoWhen")
fun ProxyPlayer.sendToast(title: String) {
    when (runningPlatform) {
        Platform.BUKKIT -> {
            val bukkitPlugin = BukkitPlugin.getInstance()
            val pkgName = bukkitPlugin.server.javaClass.`package`.name
            val nms = pkgName.substring(pkgName.lastIndexOf('.') + 1)
            val packet = when (nms) {
                "v1_19_R1" -> AdvancementPacket_1_19_R1(cast(), title)
                "v1_18_R2" -> AdvancementPacket_1_18_R2(cast(), title)
                "v1_18_R1" -> AdvancementPacket_1_18_R1(cast(), title)
                "v1_17_R1" -> AdvancementPacket_1_17_R1(cast(), title)
                "v1_16_R3" -> AdvancementPacket_1_16_R3(cast(), title)
                "v1_16_R2" -> AdvancementPacket_1_16_R2(cast(), title)
                "v1_16_R1" -> AdvancementPacket_1_16_R1(cast(), title)
                "v1_15_R1" -> AdvancementPacket_1_15_R1(cast(), title)
                "v1_14_R1" -> AdvancementPacket_1_14_R1(cast(), title)
                "v1_13_R2" -> AdvancementPacket_1_13_R2(cast(), title)
                "v1_12_R1" -> AdvancementPacket_1_12_R1(cast(), title)
                else -> {
                    logger.info("&cToast not support this NMS version: $nms")
                    return;
                }
            }
            packet.grant()
            packet.revoke()
        }

        Platform.BUNGEE -> sendBridgeToast(title)
        else -> {}
    }
}