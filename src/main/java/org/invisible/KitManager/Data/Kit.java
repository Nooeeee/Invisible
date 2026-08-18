package org.invisible.KitManager.Data;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit {
    private String id;
    private String nom;
    private List<ItemStack> items;
    private long cooldown;
    private ItemStack icon;

    public Kit(String id, String nom, List<ItemStack> items, long cooldown, ItemStack icon) {
        this.id = id;
        this.nom = nom;
        this.items = items;
        this.cooldown = cooldown;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public long getCooldown() {
        return cooldown;
    }

    public ItemStack getIcon() {
        return icon;
    }
}
