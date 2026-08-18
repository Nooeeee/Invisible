package org.invisible;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.invisible.Commands.TargetCommand;
import org.invisible.KitManager.Data.KitManager;
import org.invisible.PvPKillTracker.Commands.TopCommand;
import org.invisible.PvPKillTracker.Data.StatsManager;
import org.invisible.PvPKillTracker.Listeners.PlayerDeathListener;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        File statsFile = new File(this.getDataFolder(), "stats.yml");
        File cooldownsKitsFile = new File(this.getDataFolder(), "cooldownsKits.yml");

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        StatsManager leaderboard = StatsManager.getInstance();
        leaderboard.loadFromFile(statsFile);

        KitManager kitManager = KitManager.getInstance();
        kitManager.loadCooldowns(cooldownsKitsFile);

        // Listeners
        registerListener(new PlayerDeathListener());

        // Commands
        registerCommand("top", new TopCommand());
        registerCommand("target", new TargetCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        File statsFile = new File(this.getDataFolder(), "stats.yml");
        File cooldownsKitsFile = new File(this.getDataFolder(), "cooldownsKits.yml");

        StatsManager leaderboard = StatsManager.getInstance();
        KitManager kitManager = KitManager.getInstance();

        try {
            leaderboard.saveToFile(statsFile);
            kitManager.saveCooldowns(cooldownsKitsFile);
        } catch (IOException e) {
            getLogger().warning("Erreur lors de la sauvegarde des statistiques ou des cooldowns des kits: "
                    + e.getMessage());
        }
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);

        if (command == null) {
            getLogger().warning("La command /" + name + " n'a pas pu être enregistrée (vérifier plugin.yml).");
            return;
        }
        command.setExecutor(executor);
    }

    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
