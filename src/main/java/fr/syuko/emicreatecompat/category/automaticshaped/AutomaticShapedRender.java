package fr.syuko.emicreatecompat.category.automaticshaped;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.CrafterRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class AutomaticShapedRender {

    private static final int TEXT_COLOR = 0xFFFFFF;

    private AutomaticShapedRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, AutomaticShapedDisplay display) {
        for (int i = 0; i < display.ingredients().size(); i++) {
            if (display.ingredients().get(i).isEmpty()) {
                continue;
            }
            AllGuiTextures.JEI_SLOT.render(graphics, x + display.slotX(i), y + display.slotY(i));
        }

        AllGuiTextures.JEI_SLOT.render(graphics, x + 133, y + 80);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 128, y + 59);
        CrafterRender.draw(graphics, x + 129, y + 25);

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(0, 0, 300);
        graphics.drawString(Minecraft.getInstance().font,
                            Component.literal(String.valueOf(display.filledCount())),
                            x + 142,
                            y + 39,
                            TEXT_COLOR);
        pose.popPose();
    }
}
