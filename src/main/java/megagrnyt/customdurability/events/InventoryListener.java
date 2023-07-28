package megagrnyt.customdurability.events;

import megagrnyt.customdurability.CustomDurability;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    private CustomDurability customDurability = CustomDurability.getInstance();

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if(player.hasMetadata("customdurability")) {
            player.removeMetadata("customdurability", customDurability);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if(player.hasMetadata("customdurability")) {
            event.setCancelled(true);
        }
    }
}
