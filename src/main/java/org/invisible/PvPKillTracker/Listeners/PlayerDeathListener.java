package org.invisible.PvPKillTracker.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.invisible.PvPKillTracker.Data.StatsManager;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeathListener(PlayerDeathEvent event) {
        StatsManager leaderboard = StatsManager.getInstance();

        Player victim = event.getPlayer();
        Player killer = event.getEntity().getKiller();

        leaderboard.addDeath(victim);
        if (killer != null) {
            leaderboard.addKill(killer);
        }
    }
}
