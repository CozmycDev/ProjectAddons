package me.simplicitee.project.addons.util.versionadapter;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public interface PotionEffectAdapter {
    PotionType getHarmingPotionType();
    PotionEffectType getInstantHealingPotionType();
    PotionEffectType getNauseaPotionEffectType();
    PotionEffectType getSlownessPotionEffectType();
    PotionEffectType getJumpBoostPotionEffectType();
    PotionEffect getInstantHealingEffect(int duration, int strength);
    PotionEffect getSlownessEffect(int duration, int strength);
    PotionEffect getResistanceEffect(int duration, int strength);
    PotionEffect getNauseaEffect(int duration, int strength);
    void applyJumpBoost(Player player, int duration, int strength);
    boolean hasWaterPotion(Inventory inventory);
}
