package com.github.bottlemc.sheet;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;

public class SheetTerminateListener implements Listener {

    @Override
    public void run() {
        Sheet sheet = GlassLoader.getInstance().getAPI(Sheet.class);
        sheet.saveToFiles();
    }

}
