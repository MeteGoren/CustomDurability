package megagrnyt.customdurability.events;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import megagrnyt.customdurability.DurabilityManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ItemDamageListener implements Listener {

    DurabilityManager manager = new DurabilityManager();

    @EventHandler(priority = EventPriority.HIGH)
    public void ItemDamage(PlayerItemDamageEvent event) {
        if(event.isCancelled()) {
            return;
        }
        ItemStack item = event.getItem();
        if(item == null || item.getType() == Material.AIR) {
            return;
        }
        if(!item.hasItemMeta()) {
            return;
        }
        if(manager.hasCustomDurability(item)) {
            Player player = event.getPlayer();
            int damage = event.getDamage();
            NBT.modify(item, nbt -> {
                ReadWriteNBT compound = nbt.getCompound("customdurability");
                compound.setInteger("durability", compound.getInteger("durability") - damage);

                int durability = compound.getInteger("durability");
                int maxdurability = compound.getInteger("maxdurability");

                short itemDurability = manager.getItemDurability(item);

                nbt.setInteger("Damage", itemDurability - (itemDurability * durability / maxdurability));
            });
            int durability = manager.getDurability(item);
            if(durability <= 0) {
                item.setAmount(0);
            }
        }
        return;
    }

}
