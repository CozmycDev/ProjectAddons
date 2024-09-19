package me.simplicitee.project.addons;

import com.projectkorra.projectkorra.GeneralMethods;
import me.simplicitee.project.addons.util.LightManager;
import org.bukkit.Location;

public final class Util {

	private Util() {}
	
	private static String[] lightning = {"e6efef", "03d2d2", "33e6ff", "03d2d2", "03d2d2", "33e6ff", "03d2d2", "33e6ff", "33e6ff"};
	
	public static final String LEAF_COLOR = "48B518";
	
	public static void playLightningParticles(Location loc, int amount, double xOff, double yOff, double zOff) {
		int i = (int) Math.round(Math.random() * (lightning.length - 1));
		GeneralMethods.displayColoredParticle(lightning[i], loc, amount, xOff, yOff, zOff);
	}

	public static void emitFireLight(Location loc) {
		LightManager.createLight(loc).brightness(13).timeUntilFadeout(600).emit();
	}
	
	public static double clamp(double min, double max, double value) {
		if (value < min) {
			return min;
		} else return Math.min(value, max);
	}
}
