package com.github.bottlemc.sheet;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;

public class TestSheetListener implements Listener {

    @Override
    public void run() {
        GlassLoader.getInstance().getAPI(Sheet.class).registerType("testType", TestType.class);
    }

}
