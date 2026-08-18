package org.invisible.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TargetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            // 100 blocs max
            Entity entity = player.getTargetEntity(100);

            if (entity == null) {
                player.sendRichMessage("<red>Erreur : aucune entité visée.");
            } else if (entity instanceof LivingEntity lvEntity) {
                player.sendRichMessage("<red>Entité visée : <white>" + entity.getName() +
                        "\n<red>Points de vie : <gray>" + lvEntity.getHealth());
            } else {
                player.sendRichMessage("<red>Entité visée : <white>" + entity.getName());
            }
            return true;
        }
        sender.sendRichMessage("<red>Cette commande est réservée aux joueurs.");
        return true;
    }
}
