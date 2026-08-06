package fr.syuko.emicreatecompat.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;

public final class PressRender {

    private static final int SCALE = 24;

    private static final int SCALE_WITH_BASIN = 23;

    private static final float HEAD_CYCLE_TICKS = 30f;

    private PressRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, boolean withBasin) {
        draw(graphics, x, y, withBasin, 0);
    }

    public static void draw(GuiGraphics graphics, int x, int y, boolean withBasin, int offset) {
        int
                scale =
                withBasin
                ? SCALE_WITH_BASIN
                : SCALE;

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 200);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        GuiGameElement.of(KineticsRender.shaft(Direction.Axis.Z))
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, 0, KineticsRender.shaftAngle())
                      .scale(scale)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.MECHANICAL_PRESS.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .scale(scale)
                      .render(graphics);

        GuiGameElement.of(AllPartialModels.MECHANICAL_PRESS_HEAD)
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(0, -headOffset(offset), 0)
                      .scale(scale)
                      .render(graphics);

        if (withBasin) {
            GuiGameElement.of(AllBlocks.BASIN.getDefaultState())
                          .lighting(KineticsRender.LIGHTING)
                          .atLocal(0, 1.65, 0)
                          .scale(scale)
                          .render(graphics);
        }

        pose.popPose();
    }

    private static float headOffset(int offset) {
        float cycle = (AnimationTickHolder.getRenderTime() - offset * 8f) % HEAD_CYCLE_TICKS;
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
