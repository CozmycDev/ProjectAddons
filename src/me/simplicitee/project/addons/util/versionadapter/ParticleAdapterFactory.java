package me.simplicitee.project.addons.util.versionadapter;

import com.projectkorra.projectkorra.GeneralMethods;

public class ParticleAdapterFactory {

    private ParticleAdapter adapter;

    public ParticleAdapterFactory() {
        int serverVersion = GeneralMethods.getMCVersion();

        if (serverVersion >= 1205) {
            adapter = new ParticleAdapter_1_20_5();
        } else {
            adapter = new ParticleAdapter_1_20_4();
        }
    }

    public ParticleAdapter getAdapter() {
        return adapter;
    }
}