package de.codelix.firsttimespawn;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class SetSpawnCommand extends Command {
    protected SetSpawnCommand() {
        super("setspawn", "Sets the first time world spawn",
                "Sets the first time spawn Location to your Location", List.of("fts", "firsttimespawn"));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof Player player) {
            try {
                FirstTimeSpawn.INSTANCE.storeSpawnLocation(player.getLocation());
            } catch (SQLException e) {
                commandSender.sendMessage(Component.text("An error occurred while storing spawn location"));
                throw new RuntimeException(e);
            }
        } else {
            commandSender.sendMessage(Component.text("This command can only be executed as a Player"));
        }
        return false;
    }
}
