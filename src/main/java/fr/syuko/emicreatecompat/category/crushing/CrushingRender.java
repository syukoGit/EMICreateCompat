package fr.syuko.emicreatecompat.category.crushing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.KineticsRender;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class CrushingRender {

    private static final int BLOCK_SCALE = 22;

    private static final BlockState
            WHEEL =
            AllBlocks.CRUSHING_WHEEL.getDefaultState().setValue(BlockStateProperties.AXIS, Direction.Axis.X);

    private CrushingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 72, y + 7);
        drawWheels(graphics, x + 62, y + 59);
    }

    private static void drawWheels(GuiGraphics graphics, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 100);
        pose.mulPose(Axis.YP.rotationDegrees(-22.5f));

        GuiGameElement.of(WHEEL)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, 90, -KineticsRender.shaftAngle())
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        GuiGameElement.of(WHEEL)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, 90, KineticsRender.shaftAngle())
                      .atLocal(2, 0, 0)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        pose.popPose();
    }
}
