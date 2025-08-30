package me.simplicitee.project.addons.util.versionadapter;

import me.simplicitee.project.addons.util.HexColor;
import org.bukkit.Location;

public interface ParticleAdapter {
    void displayColoredParticles(HexColor hex, Location location, int amount, double offsetX, double offsetY, double offsetZ, double extra, int alpha);
}
