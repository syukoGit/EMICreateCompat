package fr.syuko.emicreatecompat.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;

public final class CrafterRender {

    private static final int SCALE = 22;

    private CrafterRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        AllGuiTextures.JEI_SHADOW.render(graphics, -16, 13);

        pose.translate(3, 16, 0);
        TransformStack.of(pose).rotateXDegrees(-12.5f).rotateYDegrees(-22.5f);

        GuiGameElement.of(AllPartialModels.SHAFTLESS_COGWHEEL)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(90, 0, KineticsRender.shaftAngle())
                      .scale(SCALE)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.MECHANICAL_CRAFTER.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, 180, 0)
                      .scale(SCALE)
                      .render(graphics);

        pose.popPose();
    }
}
