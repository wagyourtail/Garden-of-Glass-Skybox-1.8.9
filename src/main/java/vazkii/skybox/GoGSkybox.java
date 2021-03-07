// =============================================== //
// Recompile disabled. Please run Recaf with a JDK //
// =============================================== //

// Decompiled with: CFR 0.151
// Class Version: 8
package vazkii.skybox;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid="gogskybox", name="Garden of Glass Skybox", version="1.1-3", clientSideOnly=true, acceptedMinecraftVersions="[1.8.9]")
public class GoGSkybox {
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ModEventHandler());
    }
}
