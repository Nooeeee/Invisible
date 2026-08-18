package org.invisible.PvPKillTracker.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.invisible.PvPKillTracker.Data.PlayerStats;
import org.invisible.PvPKillTracker.Data.StatsManager;
import org.jetbrains.annotations.NotNull;

public class TopCommand implements CommandExecutor {
    private final int LEADERBOARD_LIMIT = 5;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (args.length > 1) {
            sender.sendRichMessage("<Red>Erreur de syntaxe : <Gray>/top 5");
            return true;
        }

        StatsManager leaderboard = StatsManager.getInstance();

        if (leaderboard.getLeaderboard().isEmpty()) {
            sender.sendRichMessage("<Gray>Aucune statistique enregistrée.");
            return true;
        }

        int leaderboardLimit = 0;
        if (args.length != 0) {
            try {
                leaderboardLimit = Integer.parseInt(args[0]);
            } catch (NumberFormatException exception) {
                sender.sendRichMessage("<Red>Erreur l'argument doit être un nombre");
                return false;
            }
        }

        leaderboardLimit = (leaderboardLimit > 0 && leaderboardLimit <= 50) ? leaderboardLimit : LEADERBOARD_LIMIT;

        int top = 1;
        String topColor = "<Green>";
        for (PlayerStats player : leaderboard.getTopKillers(leaderboardLimit)) {
            if (top > 3) {
                topColor = "<Gray>";
            }
            sender.sendRichMessage(topColor + top + ". <White>" + player.getPlayerName() + "| Kills : <Yellow>"
                    + player.getKills() + "</Yellow><White> | Morts : </White>" + player.getDeaths() + "\n");
            top++;
        }


        return true;
    }
}
