package me.simplicitee.project.addons.util.versionadapter;

import me.simplicitee.project.addons.util.HexColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleAdapter_1_20_5 implements ParticleAdapter {

    @Override
    public void displayColoredParticles(HexColor hex, Location location, int amount, double offsetX, double offsetY, double offsetZ, double extra, int alpha) {
        if (location.getWorld() == null) return;
        int[] color = hex.toRGB();
        location.getWorld().spawnParticle(Particle.valueOf("ENTITY_EFFECT"), location, amount, extra, offsetX, offsetY, offsetZ, Color.fromARGB(alpha, color[0], color[1], color[2]));
    }
}
