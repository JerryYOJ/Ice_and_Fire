package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.io.Charsets;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.jline.utils.AttributedStyle.WHITE;

public class IceAndFireMainMenu extends GuiMainMenu {
    public static final int LAYER_COUNT = 2;
    public static final ResourceLocation splash = new ResourceLocation(IceAndFire.MODID, "splashes.txt");
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation BESTIARY_TEXTURE = new ResourceLocation("iceandfire:textures/gui/main_menu/bestiary_menu.png");
    private static final ResourceLocation TABLE_TEXTURE = new ResourceLocation("iceandfire:textures/gui/main_menu/table.png");
    public static ResourceLocation[] pageFlipTextures;
    public static ResourceLocation[] drawingTextures = new ResourceLocation[18];
    private int layerTick;
    private String splashText;
    private boolean isFlippingPage = false;
    private int pageFlip = 0;
    private Picture drawnPictures[];
    private Enscription drawnEnscriptions[];
    private float globalAlpha = 1F;
    public IceAndFireMainMenu() {
        pageFlipTextures = new ResourceLocation[]{new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_1.png"),
                new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_2.png"),
                new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_3.png"),
                new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_4.png"),
                new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_5.png"),
                new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_6.png")};
        for (int i = 0; i < drawingTextures.length; i++) {
            drawingTextures[i] = new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/drawing_" + (int) (i + 1) + ".png");
        }
        resetDrawnImages();
        BufferedReader reader = null;
        try {
            List<String> list = new ArrayList<>();
            reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splash).getInputStream(), Charsets.UTF_8));
            String s;

            while ((s = reader.readLine()) != null) {
                s = s.trim();

                if (!s.isEmpty()) {
                    list.add(s);
                }
            }

            if (!list.isEmpty()) {
                do {
                    this.splashText = list.get(new Random().nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void drawCenteredString(FontRenderer fontRenderer, String string, int x, int y, int color) {
        if (string.equals(this.splashText)) {
            fontRenderer.drawStringWithShadow(string, x - fontRenderer.getStringWidth(string) / 2, y, 0xF1E961);
        } else {
            fontRenderer.drawStringWithShadow(string, x - fontRenderer.getStringWidth(string) / 2, y, color);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    private void resetDrawnImages() {
        globalAlpha = 0;
        Random random = new Random();
        drawnPictures = new Picture[2 + random.nextInt(2)];
        int cornerRight = 32;
        int middle = width / 2;
        int cornerLeft = 32;
        boolean left = random.nextBoolean();
        for (int i = 0; i < drawnPictures.length; i++) {
            left = !left;
            int x;
            int y = 45 + random.nextInt(25);
            if (left) {
                x = 50 + random.nextInt(60);
            } else {
                x = middle + random.nextInt(cornerRight);
            }
            drawnPictures[i] = new Picture(random.nextInt(drawingTextures.length - 1), x, y, 0.5F, random.nextFloat() * 0.5F + 0.5F);
        }
        drawnEnscriptions = new Enscription[4 + random.nextInt(8)];
        for (int i = 0; i < drawnEnscriptions.length; i++) {
            left = !left;
            int x;
            int y = 60 + random.nextInt(130);
            if (left) {
                x = 60 + random.nextInt(70);
            } else {
                x = middle + random.nextInt(cornerRight);
            }
            String s1 = generateNewRandomName(Minecraft.getMinecraft().standardGalacticFontRenderer, 50, random);
            drawnEnscriptions[i] = new Enscription(s1, x, y, random.nextFloat() * 0.5F + 0.5F, 0X9C8B7B);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        float flipTick = layerTick % 40;
        if(globalAlpha < 1 && !isFlippingPage && flipTick < 30){
            globalAlpha += 0.1F;
        }

        if(globalAlpha > 0 && flipTick > 30){
            globalAlpha -= 0.1F;
        }
        if (flipTick == 0 && !isFlippingPage) {
            isFlippingPage = true;
        }
        if (isFlippingPage) {
            if (layerTick % 2 == 0) {
                pageFlip++;
            }
            if (pageFlip == 6) {
                pageFlip = 0;
                isFlippingPage = false;
                resetDrawnImages();
            }
        }

        this.layerTick++;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        this.mc.getTextureManager().bindTexture(TABLE_TEXTURE);
        this.drawTexturedModalRect(0, 0, 0, 0, this.width, this.height, this.width, this.height, this.zLevel);
        this.mc.getTextureManager().bindTexture(BESTIARY_TEXTURE);
        this.drawTexturedModalRect(50, 0, 0, 0, this.width - 100, this.height, this.width - 100, this.height, this.zLevel);
        if (this.isFlippingPage) {
            this.mc.getTextureManager().bindTexture(pageFlipTextures[Math.min(5, pageFlip)]);
            this.drawTexturedModalRect(50, 0, 0, 0, this.width - 100, this.height, this.width - 100, this.height, this.zLevel);
        } else {
            for (Enscription enscription : drawnEnscriptions) {
                float f2 = (float)60 - partialTicks;
                int color = 0X9C8B7B;
                int opacity = 10 + (int)(255 * enscription.alpha * globalAlpha);
                this.mc.standardGalacticFontRenderer.drawString(enscription.text, enscription.x, enscription.y, color | (opacity << 24));
            }
            for (Picture picture : drawnPictures) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, picture.alpha * globalAlpha + 0.01F);
                this.mc.getTextureManager().bindTexture(drawingTextures[picture.image]);
                this.drawTexturedModalRect(picture.x, picture.y, 0, 0, 128, 128, 128, 128, this.zLevel);
            }
        }
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        this.fontRenderer.drawStringWithShadow("Ice and Fire " + ChatFormatting.YELLOW + IceAndFire.VERSION, 2, this.height - 10, 0xFFFFFFFF);
        GlStateManager.pushMatrix();
        this.mc.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.width / 2 - 274 / 2, 10, 0, 0, 155, 44);
        this.drawTexturedModalRect(this.width / 2 - 274 / 2 + 155, 10, 0, 45, 155, 44);
        GlStateManager.translate((float) (this.width / 2 + 100), 85.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f1 = 1.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        f1 = f1 * 100.0F / (float) (this.fontRenderer.getStringWidth(this.splashText) + 32);
        GlStateManager.translate(0, f1 * 10, 0.0F);
        GlStateManager.scale(f1, f1, f1);
        this.drawCenteredString(this.fontRenderer, this.splashText, 0, -40, 0xFFFFFF);
        GlStateManager.popMatrix();

        ForgeHooksClient.renderMainMenu(this, this.fontRenderer, this.width, this.height, "");
        String s1 = "Copyright Mojang AB. Do not distribute!";
        this.drawString(this.fontRenderer, s1, this.width - this.fontRenderer.getStringWidth(s1) - 2, this.height - 10, 0xFFFFFFFF);
        for (int i = 0; i < this.buttonList.size(); i++) {
            buttonList.get(i).drawButton(this.mc, mouseX, mouseY, LLibrary.PROXY.getPartialTicks());
        }
        for (int i = 0; i < this.labelList.size(); i++) {
            labelList.get(i).drawLabel(this.mc, mouseX, mouseY);
        }
    }

    public void drawTexturedModalRect(double x, double y, double u, double v, double width, double height, double textureWidth, double textureHeight, double zLevel) {
        float f = 1.0F / (float) textureWidth;
        float f1 = 1.0F / (float) textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y + height, zLevel).tex(u * f, (v + (float) height) * f1).endVertex();
        builder.pos(x + width, y + height, zLevel).tex((u + (float) width) * f, (v + (float) height) * f1).endVertex();
        builder.pos(x + width, y, zLevel).tex((u + (float) width) * f, v * f1).endVertex();
        builder.pos(x, y, zLevel).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }

    private final String[] namePartsArray = "Go Play My Other Mods Like Fossils Archeology Revival and Rats And Soon To Be Joined By Other Cool Stuff Too Dont Play The Knock Off Mods".split(" ");

    public String generateNewRandomName(FontRenderer fontRendererIn, int length, Random rand) {
        int i = rand.nextInt(2) + 3;
        String s = "";
        for (int j = 0; j < i; ++j) {
            if (j > 0) {
                s = s + " ";
            }
            s = s + this.namePartsArray[rand.nextInt(this.namePartsArray.length)];
        }
        List<String> list = fontRendererIn.listFormattedStringToWidth(s, length);
        return org.apache.commons.lang3.StringUtils.join((Iterable) (list.size() >= 2 ? list.subList(0, 2) : list), " ");
    }


    private class Picture {
        int image;
        int x;
        int y;
        float alpha;
        float scale;

        public Picture(int image, int x, int y, float alpha, float scale) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.alpha = alpha;
            this.scale = scale;
        }
    }

    private class Enscription {
        String text;
        int x;
        int y;
        int color;
        float alpha;

        public Enscription(String text, int x, int y, float alpha, int color) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.alpha = alpha;
            this.color = color;
        }
    }
}
