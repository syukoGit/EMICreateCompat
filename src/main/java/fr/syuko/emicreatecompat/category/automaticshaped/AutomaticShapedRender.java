package fr.syuko.emicreatecompat.category.automaticshaped;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import fr.syuko.emicreatecompat.create.render.KineticsRender;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class AutomaticShapedRender {

    private static final int CRAFTER_SCALE = 22;

    private static final int TEXT_COLOR = 0xFFFFFF;

    private AutomaticShapedRender() {
    }

    public static void draw(GuiGraphics graphics,
                            int x,
                            int y,
                            int gridX,
                            int gridY,
                            int width,
                            int height,
                            int ingredientCount) {
        PoseStack pose = graphics.pose();

        pose.pushPose();
        pose.translate(x + gridX, y + gridY, 0);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (row * width + col >= ingredientCount) {
                    break;
                }
                AllGuiTextures.JEI_SLOT.render(graphics, col * 19, row * 19);
            }
        }
        pose.popPose();

        AllGuiTextures.JEI_SLOT.render(graphics, x + 133, y + 80);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 128, y + 59);
        drawCrafter(graphics, x + 129, y + 25);

        pose.pushPose();
        pose.translate(0, 0, 300);
        graphics.drawString(Minecraft.getInstance().font, String.valueOf(ingredientCount), x + 142, y + 39, TEXT_COLOR);
        pose.popPose();
    }

    private static void drawCrafter(GuiGraphics graphics, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        AllGuiTextures.JEI_SHADOW.render(graphics, -16, 13);

        pose.translate(3, 16, 0);
        TransformStack.of(pose).rotateXDegrees(-12.5f).rotateYDegrees(-22.5f);

        GuiGameElement.of(AllPartialModels.SHAFTLESS_COGWHEEL)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(90, 0, KineticsRender.shaftAngle())
                      .scale(CRAFTER_SCALE)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.MECHANICAL_CRAFTER.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, 180, 0)
                      .scale(CRAFTER_SCALE)
                      .render(graphics);

        pose.popPose();
    }
}
