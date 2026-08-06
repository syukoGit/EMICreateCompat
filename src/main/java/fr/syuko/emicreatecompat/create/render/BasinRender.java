package fr.syuko.emicreatecompat.create.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.recipe.HeatRequirement;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public final class BasinRender {

    private static final int BURNER_SCALE = 23;

    private BasinRender() {
    }

    public static void drawFrame(GuiGraphics graphics,
                                 int x,
                                 int y,
                                 HeatRequirement heat,
                                 boolean showHeatBar,
                                 int outputRows) {
        boolean noHeat = heat == HeatRequirement.NONE;

        if (outputRows <= 2) {
            AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 136, y - 19 * (outputRows - 1) + 32);
        }

        AllGuiTextures
                shadow =
                noHeat
                ? AllGuiTextures.JEI_SHADOW
                : AllGuiTextures.JEI_LIGHT;
        shadow.render(graphics,
                      x + 81,
                      y + 58 + (noHeat
                                ? 10
                                : 30));

        if (!showHeatBar) {
            return;
        }

        AllGuiTextures
                heatBar =
                noHeat
                ? AllGuiTextures.JEI_NO_HEAT_BAR
                : AllGuiTextures.JEI_HEAT_BAR;
        heatBar.render(graphics, x + 4, y + 80);
        graphics.drawString(Minecraft.getInstance().font,
                            Component.translatable(heat.translationKey()),
                            x + 9,
                            y + 86,
                            heat.color(),
                            false);
    }

    public static void drawBlazeBurner(GuiGraphics graphics, int x, int y, HeatRequirement heat) {
        if (heat == HeatRequirement.NONE) {
            return;
        }

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 200);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        float offset = (Mth.sin(AnimationTickHolder.getRenderTime() / 16f) + 0.5f) / 16f;
        boolean superheated = heat == HeatRequirement.SUPERHEATED;

        GuiGameElement.of(AllBlocks.BLAZE_BURNER.getDefaultState())
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(0, 1.65, 0)
                      .scale(BURNER_SCALE)
                      .render(graphics);

        GuiGameElement.of(superheated
                          ? AllPartialModels.BLAZE_SUPER
                          : AllPartialModels.BLAZE_ACTIVE)
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(1, 1.8, 1)
                      .rotate(0, 180, 0)
                      .scale(BURNER_SCALE)
                      .render(graphics);

        GuiGameElement.of(superheated
                          ? AllPartialModels.BLAZE_BURNER_SUPER_RODS_2
                          : AllPartialModels.BLAZE_BURNER_RODS_2)
                      .lighting(KineticsRender.LIGHTING)
                      .atLocal(1, 1.7 + offset, 1)
                      .rotate(0, 180, 0)
                      .scale(BURNER_SCALE)
                      .render(graphics);

        pose.popPose();
    }
}
