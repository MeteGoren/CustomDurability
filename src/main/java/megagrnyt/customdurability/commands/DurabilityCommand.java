package megagrnyt.customdurability.commands;

import megagrnyt.customdurability.CustomDurability;
import megagrnyt.customdurability.DurabilityManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class DurabilityCommand implements CommandExecutor, TabCompleter {
    private CustomDurability customDurability = CustomDurability.getInstance();
    private DurabilityManager manager = CustomDurability.getDurabilityManager();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission(command.getPermission())) {
                return true;
            }
            if(args.length == 1 && args[0].equalsIgnoreCase("gui")) {
                Inventory inventory = Bukkit.createInventory(player, 54, ChatColor.DARK_RED + "Custom Durability Items (online player)");
                player.openInventory(inventory);
                player.setMetadata("customdurability", new FixedMetadataValue(customDurability, true));

                for (Player p : Bukkit.getOnlinePlayers()) {
                    ItemStack[] contents = p.getInventory().getContents();
                    for (ItemStack item : contents) {
                        if (item != null && item.getType() != Material.AIR) {
                            if (manager.hasCustomDurability(item)) {
                                ItemStack invItem = item.clone();
                                inventory.addItem(manager.initGuiItem(invItem, p.getName()));
                            }
                        }
                    }
                }
                return true;
            }else if(args.length == 3 && args[0].equalsIgnoreCase("set")) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if(item != null && item.getType() != Material.AIR) {
                    int maxdurability, durability;
                    try {
                        maxdurability = Integer.valueOf(args[1]);
                        durability = Integer.valueOf(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage("Something went wrong: " + e.getMessage());
                        return true;
                    }
                    if(durability > maxdurability) {
                        durability = maxdurability;
                    }
                    manager.applyCustomDurabilityItem(item, maxdurability, durability);
                    player.sendMessage("§fCustomDurability has been applied to the item you're holding.");
                    player.sendMessage("§fIf you want to see the §bcustomdurability §fitems owned by online players.");
                } else {
                    player.sendMessage("§eYou must hold a tool in your hand.");
                }

            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> output = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission(command.getPermission())) {
            output.add("gui");
            output.add("set <maxdurability> <durability>");
            return output;
        }
        return null;
    }
}
