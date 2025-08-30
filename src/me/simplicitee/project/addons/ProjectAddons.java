package me.simplicitee.project.addons;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.ElementType;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.util.Collision;
import com.projectkorra.projectkorra.airbending.AirShield;
import com.projectkorra.projectkorra.firebending.FireShield;
import me.simplicitee.project.addons.ability.air.GaleGust;
import me.simplicitee.project.addons.ability.earth.Crumble;
import me.simplicitee.project.addons.ability.fire.CombustBeam;
import me.simplicitee.project.addons.ability.fire.FireDisc;
import me.simplicitee.project.addons.ability.water.RazorLeaf;
import me.simplicitee.project.addons.util.versionadapter.ParticleAdapter;
import me.simplicitee.project.addons.util.versionadapter.ParticleAdapterFactory;
import me.simplicitee.project.addons.util.versionadapter.PotionEffectAdapter;
import me.simplicitee.project.addons.util.versionadapter.PotionEffectAdapterFactory;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ProjectAddons extends JavaPlugin {
	
	public static ProjectAddons instance;
	
	private FileConfiguration config;
	private MainListener listener;
	private Element soundElement;

	private ParticleAdapter particleAdapter;
	private PotionEffectAdapter potionEffectAdapter;

	@Override
	public void onEnable() {
		instance = this;

		if (!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}

		File configFile = new File(getDataFolder(), "project_addons.yml");
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.config = YamlConfiguration.loadConfiguration(configFile);
		this.setupConfig();

		soundElement = new SubElement("Sound", Element.AIR, ElementType.BENDING, this);
		CoreAbility.registerPluginAbilities(this, "me.simplicitee.project.addons.ability");

		this.setupCollisions();

		ParticleAdapterFactory particleAdapterFactory = new ParticleAdapterFactory();
		particleAdapter = particleAdapterFactory.getAdapter();

		PotionEffectAdapterFactory potionEffectAdapterFactory = new PotionEffectAdapterFactory();
		potionEffectAdapter = potionEffectAdapterFactory.getAdapter();

		this.listener = new MainListener(this);

		this.getCommand("projectaddons").setExecutor(new ProjectCommand());
	}
	
	@Override
	public void onDisable() {
		listener.revertSwappedBinds();
		
		if (CoreAbility.getAbility(Crumble.class) != null) {
			for (Crumble c : CoreAbility.getAbilities(Crumble.class)) {
				c.revert();
			}
		}
	}
	
	public String prefix() {
		return ChatColor.GRAY + "[" + ChatColor.GREEN + "ProjectAddons" + ChatColor.GRAY + "]";
	}
	
	public String version() {
		return prefix() + " v." + this.getDescription().getVersion();
	}
	
	public Element getSoundElement() {
		return soundElement;
	}

	public ParticleAdapter getParticleAdapter() {
		return this.particleAdapter;
	}

	public PotionEffectAdapter getPotionEffectAdapter() {
		return this.potionEffectAdapter;
	}
	
	@NotNull
	@Override
	public FileConfiguration getConfig() {
		return config;
	}
	
	public FileConfiguration config() {
		return config;
	}
	
	private void setupConfig() {
		config.options().copyDefaults(true);

		config.addDefault("Chat.Colors.Sound", "#3e4d52"); //Make soundbending have a color

		config.addDefault("Properties.MetallicBlocks", Arrays.asList("GOLD_BLOCK", "IRON_BLOCK", "NETHERITE_BLOCK"));

		config.addDefault("Properties.Fire.DynamicLight.Enabled", true);
		config.addDefault("Properties.Fire.DynamicLight.Brightness", 13);
		config.addDefault("Properties.Fire.DynamicLight.KeepAlive", 600);

		// ---- Avatar ----
		// EnergyBeam
		config.addDefault("Abilities.Avatar.EnergyBeam.Enabled", true);
		config.addDefault("Abilities.Avatar.EnergyBeam.Cooldown", 12000);
		config.addDefault("Abilities.Avatar.EnergyBeam.Duration", 10000);
		config.addDefault("Abilities.Avatar.EnergyBeam.Damage", 3);
		config.addDefault("Abilities.Avatar.EnergyBeam.Range", 40);
		config.addDefault("Abilities.Avatar.EnergyBeam.EasterEgg", true);
		
		// ---- Airbending ----
		// Deafen
		config.addDefault("Abilities.Air.Deafen.Enabled", true);
		config.addDefault("Abilities.Air.Deafen.Cooldown", 10000);
		config.addDefault("Abilities.Air.Deafen.Duration", 6000);
		
		// GaleGust
		config.addDefault("Abilities.Air.GaleGust.Enabled", true);
		config.addDefault("Abilities.Air.GaleGust.Cooldown", 9000);
		config.addDefault("Abilities.Air.GaleGust.Damage", 4);
		config.addDefault("Abilities.Air.GaleGust.Radius", 1);
		config.addDefault("Abilities.Air.GaleGust.Range", 18);
		config.addDefault("Abilities.Air.GaleGust.Knockback", 0.67);
		
		// SonicWave
		config.addDefault("Abilities.Air.SonicWave.Enabled", true);
		config.addDefault("Abilities.Air.SonicWave.Cooldown", 4000);
		config.addDefault("Abilities.Air.SonicWave.Range", 25);
		config.addDefault("Abilities.Air.SonicWave.Width", 10);
		config.addDefault("Abilities.Air.SonicWave.Nausea.Duration", 120);
		config.addDefault("Abilities.Air.SonicWave.Nausea.Power", 2);
		
		// VocalManipulation
		config.addDefault("Abilities.Air.VocalMimicry.Enabled", true);
		config.addDefault("Abilities.Air.VocalMimicry.Volume", 0.7);
		config.addDefault("Abilities.Air.VocalMimicry.Pitch", 1);
		config.addDefault("Abilities.Air.VocalMimicry.SoundBlacklist", Arrays.asList("SOUND_NAME_HERE"));
		
		// Zephyr
		config.addDefault("Abilities.Air.Zephyr.Enabled", true);
		config.addDefault("Abilities.Air.Zephyr.Cooldown", 1000);
		config.addDefault("Abilities.Air.Zephyr.Radius", 4);
		
		// Tailwind
		config.addDefault("Combos.Air.Tailwind.Enabled", true);
		config.addDefault("Combos.Air.Tailwind.Cooldown", 7000);
		config.addDefault("Combos.Air.Tailwind.Duration", 22000);
		config.addDefault("Combos.Air.Tailwind.Speed", 9);
		
		// ---- Earthbending ----
		// LandLaunch
		config.addDefault("Passives.Earth.LandLaunch.Enabled", true);
		config.addDefault("Passives.Earth.LandLaunch.Power", 3);

		// Accretion
		config.addDefault("Abilities.Earth.Accretion.Enabled", true);
		config.addDefault("Abilities.Earth.Accretion.Cooldown", 10000);
		config.addDefault("Abilities.Earth.Accretion.Damage", 1);
		config.addDefault("Abilities.Earth.Accretion.Blocks", 8);
		config.addDefault("Abilities.Earth.Accretion.SelectRange", 6);
		config.addDefault("Abilities.Earth.Accretion.RevertTime", 20000);
		config.addDefault("Abilities.Earth.Accretion.ThrowSpeed", 1.6);

		// Bulwark
		config.addDefault("Abilities.Earth.Bulwark.Enabled", true);
		config.addDefault("Abilities.Earth.Bulwark.Cooldown", 6000);
		config.addDefault("Abilities.Earth.Bulwark.Damage", 1);
		config.addDefault("Abilities.Earth.Bulwark.ThrowSpeed", 0.94);
		config.addDefault("Abilities.Earth.Bulwark.Height", 2);
		
		// Crumble
		config.addDefault("Abilities.Earth.Crumble.Enabled", true);
		config.addDefault("Abilities.Earth.Crumble.Cooldown", 3000);
		config.addDefault("Abilities.Earth.Crumble.Radius", 6);
		config.addDefault("Abilities.Earth.Crumble.SelectRange", 9);
		config.addDefault("Abilities.Earth.Crumble.RevertTime", 60);
		
		// Dig
		config.addDefault("Abilities.Earth.Dig.Enabled", true);
		config.addDefault("Abilities.Earth.Dig.Cooldown", 3000);
		config.addDefault("Abilities.Earth.Dig.Duration", -1);
		config.addDefault("Abilities.Earth.Dig.RevertTime", 3500);
		config.addDefault("Abilities.Earth.Dig.Speed", 0.51);
		
		// EarthKick
		config.addDefault("Abilities.Earth.EarthKick.Enabled", true);
		config.addDefault("Abilities.Earth.EarthKick.Cooldown", 4000);
		config.addDefault("Abilities.Earth.EarthKick.Damage", 0.5);
		config.addDefault("Abilities.Earth.EarthKick.MaxBlocks", 9);
		config.addDefault("Abilities.Earth.EarthKick.LavaMultiplier", 1.5);
		
		// LavaSurge
		config.addDefault("Abilities.Earth.LavaSurge.Enabled", true);
		config.addDefault("Abilities.Earth.LavaSurge.Cooldown", 4000);
		config.addDefault("Abilities.Earth.LavaSurge.Damage", 0.5);
		config.addDefault("Abilities.Earth.LavaSurge.Speed", 1.14);
		config.addDefault("Abilities.Earth.LavaSurge.SelectRange", 5);
		config.addDefault("Abilities.Earth.LavaSurge.SourceRadius", 3);
		config.addDefault("Abilities.Earth.LavaSurge.MaxBlocks", 10);
		config.addDefault("Abilities.Earth.LavaSurge.Burn.Enabled", true);
		config.addDefault("Abilities.Earth.LavaSurge.Burn.Duration", 3000);

		// MagmaSlap
		config.addDefault("Abilities.Earth.MagmaSlap.Enabled", true);
		config.addDefault("Abilities.Earth.MagmaSlap.Cooldown", 4000);
		config.addDefault("Abilities.Earth.MagmaSlap.Offset", 1.5);
		config.addDefault("Abilities.Earth.MagmaSlap.Damage", 2);
		config.addDefault("Abilities.Earth.MagmaSlap.Length", 13);
		config.addDefault("Abilities.Earth.MagmaSlap.Width", 1);
		config.addDefault("Abilities.Earth.MagmaSlap.RevertTime", 7000);

		// QuickWeld
		config.addDefault("Abilities.Earth.QuickWeld.Enabled", true);
		config.addDefault("Abilities.Earth.QuickWeld.Cooldown", 1000);
		config.addDefault("Abilities.Earth.QuickWeld.RepairAmount", 25);
		config.addDefault("Abilities.Earth.QuickWeld.RepairInterval", 1250);
		
		// Shrapnel
		config.addDefault("Abilities.Earth.Shrapnel.Enabled", true);
		config.addDefault("Abilities.Earth.Shrapnel.Shot.Cooldown", 2000);
		config.addDefault("Abilities.Earth.Shrapnel.Shot.Damage", 2);
		config.addDefault("Abilities.Earth.Shrapnel.Shot.Speed", 2.3);
		config.addDefault("Abilities.Earth.Shrapnel.Blast.Cooldown", 8000);
		config.addDefault("Abilities.Earth.Shrapnel.Blast.Shots", 9);
		config.addDefault("Abilities.Earth.Shrapnel.Blast.Spread", 24);
		config.addDefault("Abilities.Earth.Shrapnel.Blast.Speed", 1.7);
		
		// RockSlide
		config.addDefault("Combos.Earth.RockSlide.Enabled", true);
		config.addDefault("Combos.Earth.RockSlide.Cooldown", 7000);
		config.addDefault("Combos.Earth.RockSlide.Damage", 1);
		config.addDefault("Combos.Earth.RockSlide.Knockback", 0.9);
		config.addDefault("Combos.Earth.RockSlide.Knockup", 0.4);
		config.addDefault("Combos.Earth.RockSlide.Speed", 0.68);
		config.addDefault("Combos.Earth.RockSlide.RequiredRockCount", 6);
		config.addDefault("Combos.Earth.RockSlide.TurningSpeed", 0.086);
		config.addDefault("Combos.Earth.RockSlide.Duration", -1);
		
		// ---- Firebending ----
		// ArcSpark
		config.addDefault("Abilities.Fire.ArcSpark.Enabled", true);
		config.addDefault("Abilities.Fire.ArcSpark.Speed", 6);
		config.addDefault("Abilities.Fire.ArcSpark.Length", 7);
		config.addDefault("Abilities.Fire.ArcSpark.Damage", 1);
		config.addDefault("Abilities.Fire.ArcSpark.Cooldown", 6500);
		config.addDefault("Abilities.Fire.ArcSpark.Duration", 4000);
		config.addDefault("Abilities.Fire.ArcSpark.ChargeTime", 500);
		
		// CombustBeam
		config.addDefault("Abilities.Fire.CombustBeam.Enabled", true);
		config.addDefault("Abilities.Fire.CombustBeam.Range", 50);
		config.addDefault("Abilities.Fire.CombustBeam.Cooldown", 5000);
		config.addDefault("Abilities.Fire.CombustBeam.Minimum.Power", 0.6);
		config.addDefault("Abilities.Fire.CombustBeam.Minimum.Angle", 0.2);
		config.addDefault("Abilities.Fire.CombustBeam.Minimum.ChargeTime", 1000);
		config.addDefault("Abilities.Fire.CombustBeam.Minimum.Damage", 2);
		config.addDefault("Abilities.Fire.CombustBeam.Maximum.Power", 2.7);
		config.addDefault("Abilities.Fire.CombustBeam.Maximum.Angle", 40);
		config.addDefault("Abilities.Fire.CombustBeam.Maximum.ChargeTime", 5000);
		config.addDefault("Abilities.Fire.CombustBeam.Maximum.Damage", 10);
		config.addDefault("Abilities.Fire.CombustBeam.InterruptedDamage", 10);
		config.addDefault("Abilities.Fire.CombustBeam.RevertTime", 13000);
		
		// ChargeBolt
		config.addDefault("Abilities.Fire.ChargeBolt.Enabled", true);
		config.addDefault("Abilities.Fire.ChargeBolt.Damage", 2);
		config.addDefault("Abilities.Fire.ChargeBolt.Cooldown", 8000);
		config.addDefault("Abilities.Fire.ChargeBolt.Speed", 6);
		config.addDefault("Abilities.Fire.ChargeBolt.ChargeTime", 3000);
		config.addDefault("Abilities.Fire.ChargeBolt.BoltRange", 26);
		config.addDefault("Abilities.Fire.ChargeBolt.BlastRadius", 13);
		config.addDefault("Abilities.Fire.ChargeBolt.DischargeBoltCount", 6);
		
		// Electrify
		config.addDefault("Abilities.Fire.Electrify.Enabled", true);
		config.addDefault("Abilities.Fire.Electrify.Cooldown", 4000);
		config.addDefault("Abilities.Fire.Electrify.Duration", 7000);
		config.addDefault("Abilities.Fire.Electrify.DamageInWater", 2);
		config.addDefault("Abilities.Fire.Electrify.Slowness", 2);
		config.addDefault("Abilities.Fire.Electrify.Weakness", 1);
		
		// Explode
		config.addDefault("Abilities.Fire.Explode.Enabled", true);
		config.addDefault("Abilities.Fire.Explode.Cooldown", 4500);
		config.addDefault("Abilities.Fire.Explode.Damage", 2);
		config.addDefault("Abilities.Fire.Explode.Radius", 2.4);
		config.addDefault("Abilities.Fire.Explode.Knockback", 1.94);
		config.addDefault("Abilities.Fire.Explode.Range", 7.4);
		
		// FireDisc
		config.addDefault("Abilities.Fire.FireDisc.Enabled", true);
		config.addDefault("Abilities.Fire.FireDisc.Cooldown", 1700);
		config.addDefault("Abilities.Fire.FireDisc.AvatarState.Control", true);
		config.addDefault("Abilities.Fire.FireDisc.AvatarState.NoCooldown", true);
		config.addDefault("Abilities.Fire.FireDisc.Damage", 1.5);
		config.addDefault("Abilities.Fire.FireDisc.Range", 32);
		config.addDefault("Abilities.Fire.FireDisc.Knockback", 0.84);
		config.addDefault("Abilities.Fire.FireDisc.Controllable", true);
		config.addDefault("Abilities.Fire.FireDisc.RevertCutBlocks", true);
		config.addDefault("Abilities.Fire.FireDisc.DropCutBlocks", false);
		config.addDefault("Abilities.Fire.FireDisc.CuttableBlocks", Arrays.asList("ACACIA_LOG", "OAK_LOG", "JUNGLE_LOG", "BIRCH_LOG", "DARK_OAK_LOG", "SPRUCE_LOG"));

		// Jets
		config.addDefault("Abilities.Fire.Jets.Enabled", true);
		config.addDefault("Abilities.Fire.Jets.Cooldown.Minimum", 4000);
		config.addDefault("Abilities.Fire.Jets.Cooldown.Maximum", 12000);
		config.addDefault("Abilities.Fire.Jets.Duration", 20000);
		config.addDefault("Abilities.Fire.Jets.FlySpeed", 0.65);
		config.addDefault("Abilities.Fire.Jets.HoverSpeed", 0.065);
		config.addDefault("Abilities.Fire.Jets.SpeedThreshold", 2.4);
		config.addDefault("Abilities.Fire.Jets.DamageThreshold", 4);
		config.addDefault("Abilities.Fire.Jets.MaxHeight", -1);
		
		// FlameBreath
		config.addDefault("Combos.Fire.FlameBreath.Enabled", true);
		config.addDefault("Combos.Fire.FlameBreath.Cooldown", 8000);
		config.addDefault("Combos.Fire.FlameBreath.Damage", 0.2);
		config.addDefault("Combos.Fire.FlameBreath.FireTick", 30);
		config.addDefault("Combos.Fire.FlameBreath.Range", 5);
		config.addDefault("Combos.Fire.FlameBreath.Speed", 0.65);
		config.addDefault("Combos.Fire.FlameBreath.Duration", 4000);
		config.addDefault("Combos.Fire.FlameBreath.Burn.Ground", true);
		config.addDefault("Combos.Fire.FlameBreath.Burn.Entities", true);
		config.addDefault("Combos.Fire.FlameBreath.Rainbow", true);

		// TurboJet
		config.addDefault("Combos.Fire.TurboJet.Enabled", true);
		config.addDefault("Combos.Fire.TurboJet.Cooldown", 12000);
		config.addDefault("Combos.Fire.TurboJet.Speed", 1.95);
		
		// ---- Waterbending ----
		// Hydrojet
		config.addDefault("Passives.Water.Hydrojet.Enabled", true);
		config.addDefault("Passives.Water.Hydrojet.Speed", 8);
		
		// BloodGrip
		/*
		config.addDefault("Abilities.Water.BloodGrip.Enabled", true);
		config.addDefault("Abilities.Water.BloodGrip.Cooldown", 6000);
		config.addDefault("Abilities.Water.BloodGrip.Range", 8);
		config.addDefault("Abilities.Water.BloodGrip.DragSpeed", 0.32);
		config.addDefault("Abilities.Water.BloodGrip.ThrowPower", 1.3);
		config.addDefault("Abilities.Water.BloodGrip.MangleDamage", 3);
		config.addDefault("Abilities.Water.BloodGrip.SlamSpeed", 2);
		config.addDefault("Abilities.Water.BloodGrip.DamageThreshold", 4);
		config.addDefault("Abilities.Water.BloodGrip.EntityFilter", Arrays.asList(EntityType.ENDER_CRYSTAL.toString(), EntityType.ENDER_DRAGON.toString(), EntityType.ARMOR_STAND.toString(), EntityType.BLAZE.toString(), EntityType.WITHER.toString()));
		config.addDefault("Abilities.Water.BloodGrip.BasicAbilities", Arrays.asList("AirBlast", "AirSwipe", "EarthBlast", "FireBlast", "FireDisc", "WaterManipulation"));
		*/

		// RazorLeaf
		config.addDefault("Abilities.Water.RazorLeaf.Enabled", true);
		config.addDefault("Abilities.Water.RazorLeaf.Cooldown", 3000);
		config.addDefault("Abilities.Water.RazorLeaf.Damage", 2);
		config.addDefault("Abilities.Water.RazorLeaf.Radius", 0.7);
		config.addDefault("Abilities.Water.RazorLeaf.Range", 24);
		config.addDefault("Abilities.Water.RazorLeaf.MaxRecalls", 3);
		config.addDefault("Abilities.Water.RazorLeaf.Particles", 300);
		
		// PlantArmor
		config.addDefault("Abilities.Water.PlantArmor.Enabled", true);
		config.addDefault("Abilities.Water.PlantArmor.Cooldown", 10000);
		config.addDefault("Abilities.Water.PlantArmor.Duration", -1);
		config.addDefault("Abilities.Water.PlantArmor.Durability", 2000);
		config.addDefault("Abilities.Water.PlantArmor.SelectRange", 9);
		config.addDefault("Abilities.Water.PlantArmor.RequiredPlants", 14);
		config.addDefault("Abilities.Water.PlantArmor.Boost.Swim", 3);
		config.addDefault("Abilities.Water.PlantArmor.Boost.Speed", 2);
		config.addDefault("Abilities.Water.PlantArmor.Boost.Jump", 2);
		
		// PlantArmor - VineWhip
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.VineWhip.Cost", 50);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.VineWhip.Cooldown", 2000);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.VineWhip.Damage", 2);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.VineWhip.Range", 18);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.VineWhip.Speed", 3);
		
		// PlantArmor - RazorLeaf
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.RazorLeaf.Cost", 150);
		
		// PlantArmor - LeafShield
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.LeafShield.Cost", 100);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.LeafShield.Cooldown", 1500);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.LeafShield.Radius", 2);
		
		// PlantArmor - Tangle
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Tangle.Cost", 200);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Tangle.Cooldown", 7000);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Tangle.Radius", 0.45);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Tangle.Duration", 3000);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Tangle.Range", 18);
		
		// PlantArmor - Leap
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Leap.Cost", 100);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Leap.Cooldown", 2500);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Leap.Power", 1.4);
		
		// PlantArmor - Grapple
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Grapple.Cost", 100);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Grapple.Cooldown", 2000);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Grapple.Range", 25);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Grapple.Speed", 1.24);
		
		// PlantArmor - LeafDome
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.LeafDome.Cost", 400);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.LeafDome.Cooldown", 5000);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.LeafDome.Radius", 3);
		
		// PlantArmor - Regenerate
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Regenerate.Cooldown", 10000);
		config.addDefault("Abilities.Water.PlantArmor.SubAbilities.Regenerate.RegenAmount", 150);
		
		// LeafStorm
		config.addDefault("Combos.Water.LeafStorm.Enabled", true);
		config.addDefault("Combos.Water.LeafStorm.Cooldown", 7000);
		config.addDefault("Combos.Water.LeafStorm.PlantArmorCost", 800);
		config.addDefault("Combos.Water.LeafStorm.LeafCount", 10);
		config.addDefault("Combos.Water.LeafStorm.LeafSpeed", 14);
		config.addDefault("Combos.Water.LeafStorm.Damage", 0.5);
		config.addDefault("Combos.Water.LeafStorm.Radius", 6);
		
		// MistShards
		config.addDefault("Combos.Water.MistShards.Enabled", true);
		config.addDefault("Combos.Water.MistShards.Cooldown", 7000);
		config.addDefault("Combos.Water.MistShards.Damage", 1);
		config.addDefault("Combos.Water.MistShards.Range", 20);
		config.addDefault("Combos.Water.MistShards.IcicleCount", 8);
		
		// ---- Chiblocking ----
		// Dodging
		config.addDefault("Passives.Chi.Dodging.Enabled", true);
		config.addDefault("Passives.Chi.Dodging.Chance", 18);
		
		// Camouflage
		config.addDefault("Passives.Chi.Camouflage.Enabled", true);
		
		// Jab
		config.addDefault("Abilities.Chi.Jab.Enabled", true);
		config.addDefault("Abilities.Chi.Jab.Cooldown", 3000);
		config.addDefault("Abilities.Chi.Jab.MaxUses", 4);
		
		// NinjaStance
		config.addDefault("Abilities.Chi.NinjaStance.Enabled", true);
		config.addDefault("Abilities.Chi.NinjaStance.Cooldown", 0);
		config.addDefault("Abilities.Chi.NinjaStance.Stealth.Duration", 5000);
		config.addDefault("Abilities.Chi.NinjaStance.Stealth.ChargeTime", 2000);
		config.addDefault("Abilities.Chi.NinjaStance.Stealth.Cooldown", 8000);
		config.addDefault("Abilities.Chi.NinjaStance.SpeedAmplifier", 5);
		config.addDefault("Abilities.Chi.NinjaStance.JumpAmplifier", 5);
		config.addDefault("Abilities.Chi.NinjaStance.DamageModifier", 0.75);
		
		// ChiblockJab
		config.addDefault("Combos.Chi.ChiblockJab.Enabled", true);
		config.addDefault("Combos.Chi.ChiblockJab.Cooldown", 5000);
		config.addDefault("Combos.Chi.ChiblockJab.Duration", 2000);
		
		// FlyingKick
		config.addDefault("Combos.Chi.FlyingKick.Enabled", true);
		config.addDefault("Combos.Chi.FlyingKick.Cooldown", 4000);
		config.addDefault("Combos.Chi.FlyingKick.Damage", 2.0);
		config.addDefault("Combos.Chi.FlyingKick.LaunchPower", 1.85);
		
		// WeakeningJab
		config.addDefault("Combos.Chi.WeakeningJab.Enabled", true);
		config.addDefault("Combos.Chi.WeakeningJab.Cooldown", 6000);
		config.addDefault("Combos.Chi.WeakeningJab.Duration", 4000);
		config.addDefault("Combos.Chi.WeakeningJab.Modifier", 1.5);

		try {
			config.save(new File(getDataFolder(), "project_addons.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	private void setupCollisions() {
		if (CoreAbility.getAbility(FireDisc.class) != null) {
			ProjectKorra.getCollisionInitializer().addSmallAbility(CoreAbility.getAbility(FireDisc.class));
		}
		
		if (CoreAbility.getAbility(RazorLeaf.class) != null) {
			ProjectKorra.getCollisionInitializer().addSmallAbility(CoreAbility.getAbility(RazorLeaf.class));
		}
		
		if (CoreAbility.getAbility(GaleGust.class) != null) {
			ProjectKorra.getCollisionInitializer().addSmallAbility(CoreAbility.getAbility(GaleGust.class));
		}
		
		if (CoreAbility.getAbility(CombustBeam.class) != null) {
			ProjectKorra.getCollisionInitializer().addLargeAbility(CoreAbility.getAbility(CombustBeam.class));
			ProjectKorra.getCollisionManager().addCollision(new Collision(CoreAbility.getAbility(FireShield.class), CoreAbility.getAbility(CombustBeam.class), false, true));
			ProjectKorra.getCollisionManager().addCollision(new Collision(CoreAbility.getAbility(AirShield.class), CoreAbility.getAbility(CombustBeam.class), false, true));
		}
	}
}
