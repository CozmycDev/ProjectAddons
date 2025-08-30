package me.simplicitee.project.addons.util.versionadapter;

import me.simplicitee.project.addons.util.HexColor;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticleAdapter_1_20_4 implements ParticleAdapter {

    @Override
    public void displayColoredParticles(HexColor hex, Location location, int amount, double offsetX, double offsetY, double offsetZ, double extra, int alpha) {
        if (location.getWorld() == null) return;
        int[] color = hex.toRGB();
        if (alpha < 255) {
            location.getWorld().spawnParticle(Particle.valueOf("SPELL_MOB_AMBIENT"), location, 0, color[0] / 255D, color[1] / 255D, color[2] / 255D, 1);
        } else {
            location.getWorld().spawnParticle(Particle.valueOf("SPELL_MOB"), location, 0, color[0] / 255D, color[1] / 255D, color[2] / 255D, 1);
        }
    }
}