package fr.syuko.emicreatecompat.category.sequencedassembly;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import fr.syuko.emicreatecompat.create.render.DeployerRender;
import fr.syuko.emicreatecompat.create.render.PressRender;
import fr.syuko.emicreatecompat.create.render.SawRender;
import fr.syuko.emicreatecompat.create.render.SpoutRender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class SequencedAssemblyRender {

    private static final String[] STEP_NUMERALS = { "I", "II", "III", "IV", "V", "VI", "-" };

    private static final int STEP_LABEL_COLOR = 0x888888;

    private static final int CHANCE_LABEL_COLOR = 0xefefef;

    private SequencedAssemblyRender() {
    }

    public static int stepsWidth(List<AssemblyStep> steps) {
        return steps.size() * (AssemblyStep.WIDTH + AssemblyStep.MARGIN) - AssemblyStep.MARGIN;
    }

    public static void draw(GuiGraphics graphics,
                            int x,
                            int y,
                            int width,
                            List<AssemblyStep> steps,
                            float outputChance,
                            int loops) {
        Font font = Minecraft.getInstance().font;
        boolean singleOutput = outputChance == 1;
        int
                shift =
                singleOutput
                ? 0
                : -7;

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);

        pose.pushPose();
        pose.translate(0, 15, 0);
        AllGuiTextures.JEI_LONG_ARROW.render(graphics, 52 + shift, 79);

        if (!singleOutput) {
            AllGuiTextures.JEI_CHANCE_SLOT.render(graphics, 150 + shift, 75);
            Component mark = Component.literal("?").withStyle(ChatFormatting.BOLD);
            graphics.drawString(font, mark, font.width(mark) / -2 + 8 + 150 + shift, 80, CHANCE_LABEL_COLOR);
        }

        if (loops > 1) {
            pose.pushPose();
            pose.translate(15, 9, 0);
            AllIcons.I_SEQ_REPEAT.render(graphics, 50 + shift, 75);
            graphics.drawString(font, Component.literal("x" + loops), 66 + shift, 80, STEP_LABEL_COLOR, false);
            pose.popPose();
        }

        pose.popPose();

        pose.translate(stepsWidth(steps) / -2.0 + width / 2.0, 0, 0);
        pose.pushPose();

        for (int i = 0; i < steps.size(); i++) {
            Component numeral = Component.literal(STEP_NUMERALS[Math.min(i, STEP_NUMERALS.length - 1)]);
            graphics.drawString(font,
                                numeral,
                                font.width(numeral) / -2 + AssemblyStep.WIDTH / 2,
                                2,
                                STEP_LABEL_COLOR,
                                false);
            drawStep(graphics, steps.get(i), i);
            pose.translate(AssemblyStep.WIDTH + AssemblyStep.MARGIN, 0, 0);
        }

        pose.popPose();
        pose.popPose();
    }

    private static void drawStep(GuiGraphics graphics, AssemblyStep step, int index) {
        PoseStack pose = graphics.pose();
        int center = AssemblyStep.WIDTH / 2;

        switch (step.kind()) {
            case PRESSING -> {
                pose.pushPose();
                pose.translate(-5, 50, 0);
                pose.scale(.6f, .6f, .6f);
                PressRender.draw(graphics, center, 0, false, index);
                pose.popPose();
            }
            case CUTTING -> {
                pose.pushPose();
                pose.translate(0, 51.5f, 0);
                pose.scale(.6f, .6f, .6f);
                SawRender.draw(graphics, center, 30);
                pose.popPose();
            }
            case DEPLOYING -> {
                pose.pushPose();
                pose.translate(-7, 50, 0);
                pose.scale(.75f, .75f, .75f);
                DeployerRender.draw(graphics, center, 0, index);
                pose.popPose();
            }
            case SPOUTING -> {
                pose.pushPose();
                pose.translate(-7, 50, 0);
                pose.scale(.75f, .75f, .75f);
                SpoutRender.draw(graphics,
                                 center,
                                 0,
                                 step.fluids().isEmpty()
                                 ? null
                                 : step.fluids().getFirst(),
                                 index);
                pose.popPose();
            }
        }
    }
}
