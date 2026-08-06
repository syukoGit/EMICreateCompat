package fr.syuko.emicreatecompat.category.pressing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.KineticsRender;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;

public final class PressingRender {

    private static final int BLOCK_SCALE = 24;

    private static final float HEAD_CYCLE_TICKS = 30f;

    private PressingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 61, y + 41);
        AllGuiTextures.JEI_LONG_ARROW.render(graphics, x + 52, y + 54);
        drawPress(graphics, x + 71, y + 22);
    }

    private static void drawPress(GuiGraphics graphics, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 200);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        GuiGameElement.of(KineticsRender.shaft(Direction.Axis.Z))
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, 0, KineticsRender.shaftAngle())
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.MECHANICAL_PRESS.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        GuiGameElement.of(AllPartialModels.MECHANICAL_PRESS_HEAD)
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(0, -headOffset(), 0)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        pose.popPose();
    }

    private static float headOffset() {
        float cycle = AnimationTickHolder.getRenderTime() % HEAD_CYCLE_TICKS;
        if (cycle < 10) {
            float progress = cycle / 10;
            return -(progress * progress * progress);
        }
        if (cycle < 15) {
            return -1;
        }
        if (cycle < 20) {
            return -1 + (1 - ((20 - cycle) / 5));
        }
        return 0;
    }
}
