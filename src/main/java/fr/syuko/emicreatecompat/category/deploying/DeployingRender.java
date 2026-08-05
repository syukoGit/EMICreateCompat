package fr.syuko.emicreatecompat.category.deploying;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.deployer.DeployerBlock;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.KineticsRender;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;

public final class DeployingRender {

    private static final int BLOCK_SCALE = 20;

    private static final float POLE_CYCLE_TICKS = 30f;

    private static final float POLE_TRAVEL = 17f;

    private DeployingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, int outputCount) {
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 62, y + 57);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics,
                                             x + 126,
                                             y + 29 + (outputCount > 2
                                                       ? -19
                                                       : 0));
        drawDeployer(graphics, x + 75, y + 22);
    }

    private static void drawDeployer(GuiGraphics graphics, int x, int y) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 100);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        GuiGameElement.of(KineticsRender.shaft(Direction.Axis.Z))
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(0, 0, KineticsRender.shaftAngle())
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        GuiGameElement.of(AllBlocks.DEPLOYER.getDefaultState()
                                            .setValue(DeployerBlock.FACING, Direction.DOWN)
                                            .setValue(DeployerBlock.AXIS_ALONG_FIRST_COORDINATE, false))
                      .lighting(KineticsRender.LIGHTING)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        pose.pushPose();
        pose.translate(0, poleOffset() * POLE_TRAVEL, 0);

        GuiGameElement.of(AllPartialModels.DEPLOYER_POLE)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(90, 0, 0)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        GuiGameElement.of(AllPartialModels.DEPLOYER_HAND_HOLDING)
                      .lighting(KineticsRender.LIGHTING)
                      .rotateBlock(90, 0, 0)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        pose.popPose();

        GuiGameElement.of(AllBlocks.DEPOT.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(0, 2, 0)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        pose.popPose();
    }

    private static float poleOffset() {
        float cycle = AnimationTickHolder.getRenderTime() % POLE_CYCLE_TICKS;
        if (cycle < 10) {
            return cycle / 10f;
        }
        if (cycle < 20) {
            return (20 - cycle) / 10f;
        }
        return 0;
    }
}
