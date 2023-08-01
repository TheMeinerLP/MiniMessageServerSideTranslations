package dev.themeinerlp.mmsst.commands

import cloud.commandframework.annotations.Argument
import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import cloud.commandframework.annotations.specifier.Greedy
import dev.themeinerlp.mmsst.MMSST
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlightCommand(val mmsst: MMSST) {

    @CommandMethod("flight|fly [player]")
    @CommandPermission("mmsst.command.flight")
    @CommandDescription("Allows a player to flight.")
    fun handleFlightCommand(commandSender: CommandSender, @Greedy @Argument(value = "player") target: Player?) {
        if (commandSender is Player && target == null) {
            handleFlight(commandSender, commandSender)
            return
        }

        if (target != null) {
            handleFlight(commandSender, target)
        }
    }

    private fun handleFlight(commandSender: CommandSender, target: Player) {
        try {

            val enabledMessage =
                "<lang:commands.flight.enable:${mmsst.getPluginPrefix()}:${coloredDisplayName(target)}>"
            val disabledMessage =
                "<lang:commands.flight.disable:${mmsst.getPluginPrefix()}:${coloredDisplayName(target)}>"

            if (commandSender != target && !commandSender.hasPermission("stardust.command.flight.others")) {
                commandSender.sendMessage(
                    MiniMessage.miniMessage()
                        .deserialize("<lang:plugin.not-enough-permissions:${mmsst.getPluginPrefix()}>")
                )
                return
            }

            if (target.gameMode == GameMode.CREATIVE) {
                commandSender.sendMessage(
                    MiniMessage.miniMessage().deserialize(
                        "<lang:commands.flight.already-in-creative:${mmsst.getPluginPrefix()}:${
                            coloredDisplayName(target)
                        }>"
                    )
                )
                return
            }

            target.allowFlight = !target.allowFlight
            commandSender.sendMessage(
                MiniMessage.miniMessage().deserialize(if (target.allowFlight) enabledMessage else disabledMessage)
            )

            if (commandSender != target) {
                target.sendMessage(
                    MiniMessage.miniMessage().deserialize(if (target.allowFlight) enabledMessage else disabledMessage)
                )
            }
        } catch (e: Exception) {
            this.mmsst.getLogger().throwing(FlightCommand::class.java.simpleName, "handleFlight", e)
        }
    }

    private fun coloredDisplayName(player: Player): String {
        return MiniMessage.miniMessage().serialize(
            LegacyComponentSerializer.legacyAmpersand()
                .deserialize(LegacyComponentSerializer.legacyAmpersand().serialize(player.displayName()))
        )
    }
}