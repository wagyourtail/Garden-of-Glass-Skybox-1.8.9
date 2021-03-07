// =============================================== //
// Recompile disabled. Please run Recaf with a JDK //
// =============================================== //

// Decompiled with: CFR 0.151
// Class Version: 8
package vazkii.skybox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ModEventHandler {
    public static int ticksInGame = 0;
    public static float partialTicks = 0.0f;
    public static float delta = 0.0f;
    public static float total = 0.0f;
    
    private static void calcDelta() {
        float oldTotal = total;
        total = (float)ticksInGame + partialTicks;
        delta = total - oldTotal;
    }
    
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            partialTicks = event.renderTickTime;
        }
    }
    
    @SubscribeEvent
    public void clientTickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            GuiScreen gui = mc.currentScreen;
            if (gui == null || !gui.doesGuiPauseGame()) {
                ++ticksInGame;
                partialTicks = 0.0f;
            }
            ModEventHandler.calcDelta();
        }
    }
    
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        if (world.provider.getDimensionId() == 0 && !(world.provider.getSkyRenderer() instanceof SkyblockSkyRenderer)) {
            world.provider.setSkyRenderer(new SkyblockSkyRenderer());
        }
    }
}
 