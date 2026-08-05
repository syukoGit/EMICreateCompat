package fr.syuko.emicreatecompat.create.render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.FluidStack;

public final class SpoutRender {

    private static final int SCALE = 20;

    private static final float CYCLE_TICKS = 30f;

    private SpoutRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, FluidStack fluid, int offset) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 100);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        GuiGameElement.of(AllBlocks.SPOUT.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .scale(SCALE)
                      .render(graphics);

        float cycle = (AnimationTickHolder.getRenderTime() - offset * 8f) % CYCLE_TICKS;
        float
                squeeze =
                (cycle < 20
                 ? Mth.sin((float) (cycle / 20f * Math.PI))
                 : 0) * 20;

        pose.pushPose();
        GuiGameElement.of(AllPartialModels.SPOUT_TOP).lighting(KineticsRender.LIGHTING).scale(SCALE).render(graphics);
        pose.translate(0, -3 * squeeze / 32f, 0);
        GuiGameElement.of(AllPartialModels.SPOUT_MIDDLE)
                      .lighting(KineticsRender.LIGHTING)
                      .scale(SCALE)
                      .render(graphics);
        pose.translate(0, -3 * squeeze / 32f, 0);
        GuiGameElement.of(AllPartialModels.SPOUT_BOTTOM)
                      .lighting(KineticsRender.LIGHTING)
                      .scale(SCALE)
                      .render(graphics);
        pose.popPose();

        GuiGameElement.of(AllBlocks.DEPOT.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(0, 2, 0)
                      .scale(SCALE)
                      .render(graphics);

        if (fluid == null || fluid.isEmpty()) {
            pose.popPose();
            return;
        }

        KineticsRender.LIGHTING.applyLighting();

        pose.pushPose();
        UIRenderHelper.flipForGuiRender(pose);
        pose.scale(16, 16, 16);
        float from = 3f / 16f;
        float to = 17f / 16f;
        NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluid,
                                                             from,
                                                             from,
                                                             from,
                                                             to,
                                                             to,
                                                             to,
                                                             graphics.bufferSource(),
                                                             pose,
                                                             LightTexture.FULL_BRIGHT,
                                                             false,
                                                             true);
        pose.popPose();

        float width = 1 / 128f * squeeze;
        pose.translate(SCALE / 2f, SCALE * 1.5f, SCALE / 2f);
        UIRenderHelper.flipForGuiRender(pose);
        pose.scale(16, 16, 16);
        pose.translate(-0.5f, 0, -0.5f);
        NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluid,
                                                             -width / 2 + 0.5f,
                                                             0,
                                                             -width / 2 + 0.5f,
                                                             width / 2 + 0.5f,
                                                             2,
                                                             width / 2 + 0.5f,
                                                             graphics.bufferSource(),
                                                             pose,
                                                             LightTexture.FULL_BRIGHT,
                                                             false,
                                                             true);
        graphics.flush();
        Lighting.setupFor3DItems();

        pose.popPose();
    }
}
