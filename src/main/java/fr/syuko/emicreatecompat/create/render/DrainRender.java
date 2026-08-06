package fr.syuko.emicreatecompat.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.neoforged.neoforge.fluids.FluidStack;

public final class DrainRender {

    private static final int SCALE = 20;

    private DrainRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, FluidStack fluid) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 100);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        GuiGameElement.of(AllBlocks.ITEM_DRAIN.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .scale(SCALE)
                      .render(graphics);

        if (fluid != null && !fluid.isEmpty()) {
            UIRenderHelper.flipForGuiRender(pose);
            pose.scale(SCALE, SCALE, SCALE);
            float from = 2 / 16f;
            float to = 1f - from;
            NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluid,
                                                                 from,
                                                                 from,
                                                                 from,
                                                                 to,
                                                                 3 / 4f,
                                                                 to,
                                                                 graphics.bufferSource(),
                                                                 pose,
                                                                 LightTexture.FULL_BRIGHT,
                                                                 false,
                                                                 true);
            graphics.flush();
        }

        pose.popPose();
    }
}
