package com.github.bottlemc.sheet;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;

public class SheetInitializeListener implements Listener {

    @Override
    public void run() {
        Sheet sheet = new Sheet();
        GlassLoader.getInstance().registerAPI(sheet);
        GlassLoader.getInstance().runHooks("sheet");
        sheet.loadFromFiles();
    }

}
