package de.codelix.firsttimespawn;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player player) {
            try {
                FirstTimeSpawn.INSTANCE.storeSpawnLocation(player.getLocation());
                commandSender.sendMessage(Component.text("Set the new spawn Location."));
            } catch (SQLException e) {
                commandSender.sendMessage(Component.text("An error occurred while storing spawn location"));
                throw new RuntimeException(e);
            }
        } else {
            commandSender.sendMessage(Component.text("This command can only be executed as a Player"));
        }
        return false;    }
}
