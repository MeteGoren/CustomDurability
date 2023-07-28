package megagrnyt.customdurability;

import megagrnyt.customdurability.commands.DurabilityCommand;
import megagrnyt.customdurability.events.InventoryListener;
import megagrnyt.customdurability.events.ItemDamageListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomDurability extends JavaPlugin {

    private static CustomDurability instance;

    private static DurabilityManager durabilityManager;

    public static DurabilityManager getDurabilityManager() {
        return durabilityManager;
    }

    public static CustomDurability getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        durabilityManager = new DurabilityManager();
        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ItemDamageListener(), this);
        pm.registerEvents(new InventoryListener(), this);
    }

    private void registerCommands() {
        getCommand("customdurability").setExecutor(new DurabilityCommand());
        getCommand("customdurability").setTabCompleter(new DurabilityCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasMetadata("customdurability")).forEach(Player::closeInventory);
    }
}
