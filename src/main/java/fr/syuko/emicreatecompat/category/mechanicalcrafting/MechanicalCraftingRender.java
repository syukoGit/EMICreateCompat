package fr.syuko.emicreatecompat.category.mechanicalcrafting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.CrafterRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class MechanicalCraftingRender {

    private static final int TEXT_COLOR = 0xFFFFFF;

    private MechanicalCraftingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, MechanicalCraftingDisplay display) {
        PoseStack pose = graphics.pose();
        float scale = display.scale();
        int columns = display.width();
        int count = display.ingredients().size();

        pose.pushPose();
        pose.translate(x + display.gridX(), y + display.gridY(), 0);

        for (int i = 0; i < count; i++) {
            if (display.ingredients().get(i).isEmpty()) {
                continue;
            }
            pose.pushPose();
            pose.translate((i % columns) * 19 * scale, ((float) i / columns) * 19 * scale, 0);
            pose.scale(scale, scale, scale);
            AllGuiTextures.JEI_SLOT.render(graphics, 0, 0);
            pose.popPose();
        }

        pose.popPose();

        AllGuiTextures.JEI_SLOT.render(graphics, x + 133, y + 80);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 128, y + 59);
        CrafterRender.draw(graphics, x + 129, y + 25);

        pose.pushPose();
        pose.translate(0, 0, 300);
        graphics.drawString(Minecraft.getInstance().font,
                            Component.literal(String.valueOf(filled(display))),
                            x + 142,
                            y + 39,
                            TEXT_COLOR);
        pose.popPose();
    }

    private static int filled(MechanicalCraftingDisplay display) {
        int amount = 0;
        for (int i = 0; i < display.ingredients().size(); i++) {
            if (!display.ingredients().get(i).isEmpty()) {
                amount++;
            }
        }
        return amount;
    }
}
