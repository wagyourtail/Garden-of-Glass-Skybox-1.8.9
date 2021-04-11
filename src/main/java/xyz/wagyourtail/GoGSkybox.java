package xyz.wagyourtail;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class GoGSkybox implements ClientModInitializer {
    public static int ticks = 0;
    public static float partial = 0;
    
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> {
            if (!mc.isPaused()) {
                ++ticks;
                partial = 0;
            }
        });
    }
    
    public static void renderTick(float renderTickTime) {
        partial = renderTickTime;
    }
}
