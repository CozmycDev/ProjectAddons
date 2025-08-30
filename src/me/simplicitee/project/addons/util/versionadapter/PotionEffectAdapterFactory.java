package me.simplicitee.project.addons.util.versionadapter;

import com.projectkorra.projectkorra.GeneralMethods;

public class PotionEffectAdapterFactory {

    private PotionEffectAdapter adapter;

    public PotionEffectAdapterFactory() {
        int serverVersion = GeneralMethods.getMCVersion();

        if (serverVersion >= 1205) {
            adapter = new PotionEffectAdapter_1_20_5();
        } else {
            adapter = new PotionEffectAdapter_1_20_4();
        }
    }

    public PotionEffectAdapter getAdapter() {
        return adapter;
    }
}
