package org.invisible.KitManager;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class KitMenuHolder implements InventoryHolder {
    private final Inventory inventory;

    public KitMenuHolder() {
        Inventory inventory = Bukkit.createInventory(this, 9, "Menu des kits");
        this.inventory = inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
