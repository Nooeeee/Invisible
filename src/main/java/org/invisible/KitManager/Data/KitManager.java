package org.invisible.KitManager.Data;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class KitManager {
    private static KitManager instance;
    private Map<String, Kit> kits;
    private Map<UUID, Map<String, Long>> cooldowns;

    private KitManager() {
        kits = new HashMap<String, Kit>();
        fillKits();
        cooldowns = new HashMap<UUID, Map<String, Long>>();
    }

    public static KitManager getInstance() {
        if (instance == null) {
            instance = new KitManager();
        }
        return instance;
    }

    public Kit getKit(String id) {
        return kits.get(id);
    }

    public Map<String, Kit> getAllKits() {
        return kits;
    }

    public boolean isOnCooldown(UUID player, String kitId) {
        return cooldowns.getOrDefault(player, new HashMap<String, Long>()).containsKey(kitId);
    }

    public long getRemainingCooldown(UUID player, String kitId) {
        if (!cooldowns.getOrDefault(player, new HashMap<String, Long>()).containsKey(kitId)) {
            return 0L;
        }

        long lastUsed = cooldowns.get(player).get(kitId);
        if ((System.currentTimeMillis() - lastUsed) < kits.get(kitId).getCooldown()) {
            return kits.get(kitId).getCooldown() - (System.currentTimeMillis() - lastUsed);
        }
        return 0L;
    }

    public void setCooldown(UUID player, String kitId) {
        Map<String, Long> cooldownKit = cooldowns.getOrDefault(player, new HashMap<String, Long>());
        cooldownKit.put(kitId, System.currentTimeMillis());

        cooldowns.put(player, cooldownKit);
    }

    public void saveCooldowns(File file) throws IOException {
        YamlConfiguration cooldownsConfig = new YamlConfiguration();

        for (Map.Entry<UUID, Map<String, Long>> entry : cooldowns.entrySet()) {
            UUID playerUuid = entry.getKey();

            for (Map.Entry<String, Long> entryKit : entry.getValue().entrySet()) {
                String kitId = entryKit.getKey();
                long timestamp = entryKit.getValue();

                cooldownsConfig.set("players." + playerUuid + "." + kitId, timestamp);
            }
        }
        cooldownsConfig.save(file);
    }

    public void loadCooldowns(File file) {
        YamlConfiguration cooldownsConfig = YamlConfiguration.loadConfiguration(file);
        String pathConfig = "players";

        if (cooldownsConfig.getConfigurationSection(pathConfig) != null) {
            for (String player : cooldownsConfig.getConfigurationSection(pathConfig).getKeys(false)) {
                UUID uuid = UUID.fromString(player);

                for (String kitId : cooldownsConfig.getConfigurationSection(pathConfig + "." + uuid)
                        .getKeys(false)) {
                    long timestamp = cooldownsConfig.getLong(pathConfig + "." + uuid + "." + kitId);

                    Map<String, Long> cooldownKit = cooldowns.getOrDefault(uuid, new HashMap<String, Long>());
                    cooldownKit.put(kitId, timestamp);
                    cooldowns.put(uuid, cooldownKit);
                }
            }
        }
    }

    private void fillKits() {
        List<ItemStack> itemsStarter = List.of(
                new ItemStack(Material.IRON_SWORD, 1),
                new ItemStack(Material.WATER_BUCKET, 1),
                new ItemStack(Material.GOLDEN_APPLE, 16),
                new ItemStack(Material.ARROW, 12)
        );

        kits.put("starter", new Kit("starter", "Starter"
                , itemsStarter
                , TimeUnit.HOURS.toMillis(1)
                , new ItemStack(Material.IRON_SWORD, 1))
        );

        List<ItemStack> itemsMedium = List.of(
                new ItemStack(Material.GOLDEN_SWORD, 1),
                new ItemStack(Material.LAVA_BUCKET, 1),
                new ItemStack(Material.WATER_BUCKET, 1),
                new ItemStack(Material.GOLDEN_APPLE, 16),
                new ItemStack(Material.ARROW, 16)
        );
        kits.put("medium", new Kit("medium", "Medium"
                , itemsMedium
                , TimeUnit.HOURS.toMillis(3)
                , new ItemStack(Material.GOLDEN_SWORD, 1))
        );

        List<ItemStack> itemsPro = List.of(
                new ItemStack(Material.DIAMOND, 1),
                new ItemStack(Material.BOW, 1),
                new ItemStack(Material.LAVA_BUCKET, 1),
                new ItemStack(Material.WATER_BUCKET, 1),
                new ItemStack(Material.GOLDEN_APPLE, 16),
                new ItemStack(Material.ARROW, 16)
        );

        kits.put("pro", new Kit("pro", "Pro"
                , itemsPro
                , TimeUnit.HOURS.toMillis(12)
                , new ItemStack(Material.DIAMOND_SWORD, 1))
        );
    }
}
