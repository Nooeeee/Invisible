package org.invisible.PvPKillTracker.Data;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerStats {
    private UUID uuid;
    private String playerName;
    private int kills;
    private int deaths;

    public PlayerStats(Player player) {
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();
    }

    public PlayerStats(UUID uuid, String playerName, int kills, int deaths) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.kills = kills;
        this.deaths = deaths;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addKill() {
        kills++;
    }

    public void addDeath() {
        deaths++;
    }
}
