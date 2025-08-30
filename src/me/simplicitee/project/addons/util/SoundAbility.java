package me.simplicitee.project.addons.util;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.ability.SubAbility;
import me.simplicitee.project.addons.ProjectAddons;
import org.bukkit.entity.Player;

public abstract class SoundAbility extends AirAbility implements SubAbility{

	public SoundAbility(Player player) {
		super(player);
	}
	
	@Override
	public Class<? extends Ability> getParentAbility() {
		return AirAbility.class;
	}
	
	@Override
	public Element getElement() {
		return ProjectAddons.instance.getSoundElement();
	}
}
