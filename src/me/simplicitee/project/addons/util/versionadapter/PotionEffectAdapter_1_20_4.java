package me.simplicitee.project.addons.util.versionadapter;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class PotionEffectAdapter_1_20_4 implements PotionEffectAdapter {

    @Override
    public PotionEffectType getInstantHealingPotionType() {
        return PotionEffectType.getByName("HEAL");
    }

    @Override
    public PotionType getHarmingPotionType() {
        return PotionType.valueOf("INSTANT_DAMAGE");
    }

    @Override
    public PotionEffectType getNauseaPotionEffectType() {
        return PotionEffectType.getByName("CONFUSION");
    }

    @Override
    public PotionEffectType getSlownessPotionEffectType() {
        return PotionEffectType.getByName("SLOW");
    }

    @Override
    public PotionEffectType getJumpBoostPotionEffectType() {
        return PotionEffectType.getByName("JUMP");
    }

    @Override
    public PotionEffect getInstantHealingEffect(int duration, int strength) {
        return new PotionEffect(PotionEffectType.getByName("HEAL"), duration / 50, strength);
    }

    @Override
    public PotionEffect getSlownessEffect(int duration, int strength) {
        return new PotionEffect(PotionEffectType.getByName("SLOW"), duration / 50, strength);
    }

    @Override
    public PotionEffect getResistanceEffect(int duration, int strength) {
        return new PotionEffect(PotionEffectType.getByName("DAMAGE_RESISTANCE"), duration / 50, strength - 1);
    }

    @Override
    public PotionEffect getNauseaEffect(int duration, int strength) {
        return new PotionEffect(PotionEffectType.getByName("CONFUSION"), duration / 50, strength);
    }

    @Override
    public void applyJumpBoost(Player player, int duration, int strength) {
        if (player.hasPotionEffect(PotionEffectType.getByName("JUMP"))) {
            player.removePotionEffect(PotionEffectType.getByName("JUMP"));
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.getByName("JUMP"), duration / 50, strength - 1));
    }

    @Override
    public boolean hasWaterPotion(Inventory inventory) {
        if (inventory.contains(Material.POTION)) {
            ItemStack item = inventory.getItem(inventory.first(Material.POTION));
            if (item == null) return false;
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            if (meta == null) return false;

            PotionType potionType = PotionMetaUtil.getPotionType(meta);
            return potionType == PotionType.WATER;
        }
        return false;
    }
}
