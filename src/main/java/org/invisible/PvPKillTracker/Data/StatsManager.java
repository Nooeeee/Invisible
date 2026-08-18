package org.invisible.PvPKillTracker.Data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StatsManager {
    private static StatsManager instance;
    private Map<UUID, PlayerStats> statsLeaderboard;

    private StatsManager() {
        statsLeaderboard = new HashMap<UUID, PlayerStats>();
    }

    public static StatsManager getInstance() {
        if (instance == null) {
            instance = new StatsManager();
        }
        return instance;
    }

    public Map<UUID, PlayerStats> getStatsLeaderBoard() {
        return statsLeaderboard;
    }

    public PlayerStats getOrCreateStats(Player player) {
        PlayerStats playerStats = statsLeaderboard.get(player.getUniqueId());

        if (playerStats == null) {
            playerStats = new PlayerStats(player);
            statsLeaderboard.put(player.getUniqueId(), playerStats);
        }

        return playerStats;
    }

    public void addKill(Player killer) {
        PlayerStats killerStats = getOrCreateStats(killer);

        killerStats.addKill();
    }

    public void addDeath(Player victim) {
        PlayerStats killerStats = getOrCreateStats(victim);

        killerStats.addDeath();
    }

    public List<PlayerStats> getTopKillers(int limit) {
        return statsLeaderboard.values()
                .stream()
                .sorted(Comparator.comparing(PlayerStats::getKills).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<UUID, PlayerStats> getLeaderboard() {
        return statsLeaderboard;
    }

    public void saveToFile(File file) throws IOException {
        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<UUID, PlayerStats> entry : statsLeaderboard.entrySet()) {
            UUID playerUuid = entry.getKey();
            PlayerStats playerStats = entry.getValue();

            config.set("players." + playerUuid + ".name", playerStats.getPlayerName());
            config.set("players." + playerUuid + ".kills", playerStats.getKills());
            config.set("players." + playerUuid + ".deaths", playerStats.getDeaths());
        }

        config.save(file);
    }

    public void loadFromFile(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String pathConfig = "players";

        if (config.getConfigurationSection(pathConfig) != null) {
            for (String player : config.getConfigurationSection(pathConfig).getKeys(false)) {
                UUID uuid = UUID.fromString(player);
                String playerName = config.getString(pathConfig + "." + player + ".name");
                int kills = config.getInt(pathConfig + "." + player + ".kills");
                int deaths = config.getInt(pathConfig + "." + player + ".deaths");

                PlayerStats playerStats = new PlayerStats(uuid, playerName, kills, deaths);
                statsLeaderboard.put(uuid, playerStats);
            }
        }
    }
}
