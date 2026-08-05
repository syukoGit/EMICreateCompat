package fr.syuko.emicreatecompat.category.sawing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.saw.SawBlock;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.KineticsRender;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;

public final class SawingRender {

    private static final int BLOCK_SCALE = 25;

    private SawingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 70, y + 6);
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 55, y + 55);
        drawSaw(graphics, x + 72, y + 42);
    }

    private static void drawSaw(GuiGraphics graphics, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 200);
        pose.translate(2, 22, 0);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f + 90));

        GuiGameElement.of(KineticsRender.shaft(Direction.Axis.X))
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(-KineticsRender.shaftAngle(), 0, 0)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.MECHANICAL_SAW.getDefaultState().setValue(SawBlock.FACING, Direction.UP))
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, 0, 0)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        GuiGameElement.of(AllPartialModels.SAW_BLADE_VERTICAL_ACTIVE)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, -90, -90)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        pose.popPose();
    }
}
