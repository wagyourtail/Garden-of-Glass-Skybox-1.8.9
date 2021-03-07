// =============================================== //
// Recompile disabled. Please run Recaf with a JDK //
// =============================================== //

// Decompiled with: CFR 0.151
// Class Version: 8
package vazkii.skybox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class SkyblockSkyRenderer
    extends IRenderHandler {
    private static final ResourceLocation textureSkybox = new ResourceLocation("gogskybox:textures/skybox.png");
    private static final ResourceLocation textureRainbow = new ResourceLocation("gogskybox:textures/rainbow.png");
    private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation[] planetTextures = new ResourceLocation[]{new ResourceLocation("gogskybox:textures/planet0.png"), new ResourceLocation("gogskybox:textures/planet1.png"), new ResourceLocation("gogskybox:textures/planet2.png"), new ResourceLocation("gogskybox:textures/planet3.png"), new ResourceLocation("gogskybox:textures/planet4.png"), new ResourceLocation("gogskybox:textures/planet5.png")};
    
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        int i;
        float celAng;
        VertexBuffer skyVBO;
        int glSkyList;
        try {
            glSkyList = (Integer) ModMethodHandles.glSkyList_getter.invoke(mc.renderGlobal);
            skyVBO = (VertexBuffer) ModMethodHandles.skyVBO_getter.invoke(mc.renderGlobal);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        GlStateManager.disableTexture2D();
        Vec3 vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
        float f = (float)vec3d.xCoord;
        float f1 = (float)vec3d.yCoord;
        float f2 = (float)vec3d.zCoord;
        float insideVoid = 0.0f;
        if (mc.thePlayer.posY <= -2.0) {
            insideVoid = (float)Math.min(1.0, -(mc.thePlayer.posY + 2.0) / 30.0);
        }
        f = Math.max(0.0f, f - insideVoid);
        f1 = Math.max(0.0f, f1 - insideVoid);
        f2 = Math.max(0.0f, f2 - insideVoid);
        GlStateManager.color(f, f1, f2);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
        GlStateManager.depthMask(false);
        GlStateManager.enableFog();
        GlStateManager.color(f, f1, f2);
        drawVboOrList(skyVBO, glSkyList);
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        RenderHelper.disableStandardItemLighting();
        float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
        if (afloat != null) {
            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0f ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
            float f6 = afloat[0];
            float f7 = afloat[1];
            float f8 = afloat[2];
            vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            vertexbuffer.pos(0.0, 100.0, 0.0).color(f6, f7, f8, afloat[3] * (1.0f - insideVoid)).endVertex();
            for (int l = 0; l <= 16; ++l) {
                float f21 = (float)l * ((float)Math.PI * 2) / 16.0f;
                float f12 = MathHelper.sin(f21);
                float f13 = MathHelper.cos(f21);
                vertexbuffer.pos(f12 * 120.0f, f13 * 120.0f, -f13 * 40.0f * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0f).endVertex();
            }
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
        }
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.pushMatrix();
        float f16 = 1.0f - world.getRainStrength(partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, f16);
        GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
        float effCelAng = celAng = world.getCelestialAngle(partialTicks);
        if ((double)celAng > 0.5) {
            effCelAng = 0.5f - (celAng - 0.5f);
        }
        float f17 = 20.0f;
        float lowA = Math.max(0.0f, effCelAng - 0.3f) * f16;
        float a = Math.max(0.1f, lowA);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, a * 4.0f * (1.0f - insideVoid));
        GlStateManager.rotate(90.0f, 0.5f, 0.5f, 0.0f);
        block14: for (int p = 0; p < planetTextures.length; ++p) {
            mc.renderEngine.bindTexture(planetTextures[p]);
            tessellator.getWorldRenderer().begin(7, DefaultVertexFormats.POSITION_TEX);
            tessellator.getWorldRenderer().pos(-f17, 100.0, -f17).tex(0.0, 0.0).endVertex();
            tessellator.getWorldRenderer().pos(f17, 100.0, -f17).tex(1.0, 0.0).endVertex();
            tessellator.getWorldRenderer().pos(f17, 100.0, f17).tex(1.0, 1.0).endVertex();
            tessellator.getWorldRenderer().pos(-f17, 100.0, f17).tex(0.0, 1.0).endVertex();
            tessellator.draw();
            switch (p) {
                case 0: {
                    GlStateManager.rotate(70.0f, 1.0f, 0.0f, 0.0f);
                    f17 = 12.0f;
                    continue block14;
                }
                case 1: {
                    GlStateManager.rotate(120.0f, 0.0f, 0.0f, 1.0f);
                    f17 = 15.0f;
                    continue block14;
                }
                case 2: {
                    GlStateManager.rotate(80.0f, 1.0f, 0.0f, 1.0f);
                    f17 = 25.0f;
                    continue block14;
                }
                case 3: {
                    GlStateManager.rotate(100.0f, 0.0f, 0.0f, 1.0f);
                    f17 = 10.0f;
                    continue block14;
                }
                case 4: {
                    GlStateManager.rotate(-60.0f, 1.0f, 0.0f, 0.5f);
                    f17 = 40.0f;
                }
            }
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        mc.renderEngine.bindTexture(textureSkybox);
        f17 = 20.0f;
        a = lowA;
        GlStateManager.pushMatrix();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        GlStateManager.rotate(220.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, a);
        int angles = 90;
        float y = 2.0f;
        float y0 = 0.0f;
        float uPer = 0.0027777778f;
        float anglePer = 360.0f / (float)angles;
        double fuzzPer = Math.PI * 10 / (double)angles;
        float rotSpeed = 1.0f;
        float rotSpeedMod = 0.4f;
        block15: for (int p = 0; p < 3; ++p) {
            float baseAngle = rotSpeed * rotSpeedMod * ((float)ModEventHandler.ticksInGame + ModEventHandler.partialTicks);
            GlStateManager.rotate(((float)ModEventHandler.ticksInGame + ModEventHandler.partialTicks) * 0.25f * rotSpeed * rotSpeedMod, 0.0f, 1.0f, 0.0f);
            tessellator.getWorldRenderer().begin(7, DefaultVertexFormats.POSITION_TEX);
            for (int i2 = 0; i2 < angles; ++i2) {
                int j = i2;
                if (i2 % 2 == 0) {
                    --j;
                }
                float ang = (float)j * anglePer + baseAngle;
                double xp = Math.cos((double)ang * Math.PI / 180.0) * (double)f17;
                double zp = Math.sin((double)ang * Math.PI / 180.0) * (double)f17;
                double yo = Math.sin(fuzzPer * (double) j);
                float ut = ang * uPer;
                if (i2 % 2 == 0) {
                    tessellator.getWorldRenderer().pos(xp, yo + (double)y0 + (double)y, zp).tex(ut, 1.0).endVertex();
                    tessellator.getWorldRenderer().pos(xp, yo + (double)y0, zp).tex(ut, 0.0).endVertex();
                    continue;
                }
                tessellator.getWorldRenderer().pos(xp, yo + (double)y0, zp).tex(ut, 0.0).endVertex();
                tessellator.getWorldRenderer().pos(xp, yo + (double)y0 + (double)y, zp).tex(ut, 1.0).endVertex();
            }
            tessellator.draw();
            switch (p) {
                case 0: {
                    GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.color(1.0f, 0.4f, 0.4f, a);
                    fuzzPer = 43.982297150257104 / (double)angles;
                    rotSpeed = 0.2f;
                    continue block15;
                }
                case 1: {
                    GlStateManager.rotate(50.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.color(0.4f, 1.0f, 0.7f, a);
                    fuzzPer = Math.PI * 6 / (double)angles;
                    rotSpeed = 2.0f;
                }
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        mc.renderEngine.bindTexture(textureRainbow);
        f17 = 10.0f;
        float effCelAng1 = celAng;
        if (effCelAng1 > 0.25f) {
            effCelAng1 = 1.0f - effCelAng1;
        }
        effCelAng1 = 0.25f - Math.min(0.25f, effCelAng1);
        long time = world.getWorldTime() + 1000L;
        long day = time / 24000L;
        Random rand = new Random(day * 255L);
        float angle1 = rand.nextFloat() * 360.0f;
        float angle2 = rand.nextFloat() * 360.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f, effCelAng1 * (1.0f - insideVoid));
        GlStateManager.rotate(angle1, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(angle2, 0.0f, 0.0f, 1.0f);
        tessellator.getWorldRenderer().begin(7, DefaultVertexFormats.POSITION_TEX);
        for (i = 0; i < angles; ++i) {
            int j = i;
            if (i % 2 == 0) {
                --j;
            }
            float ang = (float)j * anglePer;
            double xp = Math.cos((double)ang * Math.PI / 180.0) * (double)f17;
            double zp = Math.sin((double)ang * Math.PI / 180.0) * (double)f17;
            double yo = 0.0;
            float ut = ang * uPer;
            if (i % 2 == 0) {
                tessellator.getWorldRenderer().pos(xp, yo + (double)y0 + (double)y, zp).tex(ut, 1.0).endVertex();
                tessellator.getWorldRenderer().pos(xp, yo + (double)y0, zp).tex(ut, 0.0).endVertex();
                continue;
            }
            tessellator.getWorldRenderer().pos(xp, yo + (double)y0, zp).tex(ut, 0.0).endVertex();
            tessellator.getWorldRenderer().pos(xp, yo + (double)y0 + (double)y, zp).tex(ut, 1.0).endVertex();
        }
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f - insideVoid);
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0f, 1.0f, 0.0f, 0.0f);
        f17 = 60.0f;
        mc.renderEngine.bindTexture(SUN_TEXTURES);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(-f17, 100.0, -f17).tex(0.0, 0.0).endVertex();
        vertexbuffer.pos(f17, 100.0, -f17).tex(1.0, 0.0).endVertex();
        vertexbuffer.pos(f17, 100.0, f17).tex(1.0, 1.0).endVertex();
        vertexbuffer.pos(-f17, 100.0, f17).tex(0.0, 1.0).endVertex();
        tessellator.draw();
        f17 = 60.0f;
        mc.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
        i = world.getMoonPhase();
        int k = i % 4;
        int i1 = i / 4 % 2;
        float f22 = k / 4.0f;
        float f23 = i1 / 2.0f;
        float f24 = (k + 1) / 4.0f;
        float f14 = (i1 + 1) / 2.0f;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(-f17, -100.0, f17).tex(f24, f14).endVertex();
        vertexbuffer.pos(f17, -100.0, f17).tex(f22, f14).endVertex();
        vertexbuffer.pos(f17, -100.0, -f17).tex(f22, f23).endVertex();
        vertexbuffer.pos(-f17, -100.0, -f17).tex(f24, f23).endVertex();
        tessellator.draw();
        GlStateManager.disableTexture2D();
        this.renderStars(mc, f16 * Math.max(0.1f, effCelAng * 2.0f), partialTicks);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
    }
    
    private void renderStars(Minecraft mc, float alpha, float partialTicks) {
        VertexBuffer starVBO;
        int starGLCallList;
        try {
            starGLCallList = (Integer) ModMethodHandles.starGLCallList_getter.invoke(mc.renderGlobal);
            starVBO = (VertexBuffer) ModMethodHandles.starVBO_getter.invoke(mc.renderGlobal);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        float t = ((float)ModEventHandler.ticksInGame + partialTicks + 2000.0f) * 0.005f;
        GlStateManager.pushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t * 3.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t, 0.0f, 1.0f, 0.0f);
        GlStateManager.color(0.5f, 1.0f, 1.0f, alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t * 2.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.color(1.0f, 0.75f, 0.75f, alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t * 3.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.25f * alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t, 0.0f, 0.0f, 1.0f);
        GlStateManager.color(0.5f, 1.0f, 1.0f, 0.25f * alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(t * 2.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.color(1.0f, 0.75f, 0.75f, 0.25f * alpha);
        this.drawVboOrList(starVBO, starGLCallList);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
    
    private void drawVboOrList(VertexBuffer skyVBO, int glSkyList) {
        if (OpenGlHelper.useVbo()) {
            skyVBO.bindBuffer();
            GL11.glEnableClientState(32884);
            GL11.glVertexPointer(3, 5126, 12, 0);
            skyVBO.drawArrays(7);
            skyVBO.unbindBuffer();
            GL11.glDisableClientState(32884);
        } else {
            GlStateManager.callList(glSkyList);
        }
    }
}
 