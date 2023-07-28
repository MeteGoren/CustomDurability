package megagrnyt.customdurability;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DurabilityManager {

    public Integer getDurability(ItemStack item) {
        return new NBTItem(item).getCompound("customdurability").getInteger("durability");
    }

    public Integer getMaxDurability(ItemStack item) {
        return new NBTItem(item).getCompound("customdurability").getInteger("maxdurability");
    }
    public Short getItemDurability(ItemStack item) {
        return item.getType().getMaxDurability();
    }

    public boolean hasCustomDurability(ItemStack item) {
        return NBT.get(item, nbt -> nbt.getCompound("customdurability")) != null && NBT.get(item, nbt -> nbt.getCompound("customdurability")).getInteger("maxdurability") != null && NBT.get(item, nbt -> nbt.getCompound("customdurability")).getInteger("durability") != null;
    }

    public void applyCustomDurabilityItem(ItemStack item, int maxdurability, int durability) {
        NBT.modify(item, nbt -> {
            ReadWriteNBT compound = nbt.getOrCreateCompound("customdurability");
            compound.setInteger("durability", durability);
            compound.setInteger("maxdurability", maxdurability);
            short itemDurability = item.getType().getMaxDurability();
            nbt.setInteger("Damage", itemDurability - (itemDurability * durability / maxdurability));
        });
    }

    public ItemStack initGuiItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(meta.hasDisplayName() ? meta.getDisplayName() : item.getType().name());
        List<String> lore = new ArrayList<>();
        if(meta.hasLore()) {
            lore = meta.getLore();
        }
        lore.add("");
        lore.add("§8▪ §fPlayer: §b" + name);

        int durability = getDurability(item);
        int maxdurability = getMaxDurability(item);
        int percent = 100 * durability / maxdurability;

        lore.add("§8▪ §fCustom Durability: §c" + durability + "§8/§c" + maxdurability + " §8[§e" + percent + "%§8]");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
