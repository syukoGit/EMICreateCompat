package fr.syuko.emicreatecompat.emi.bom;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.bom.BoM;
import fr.syuko.emicreatecompat.config.ChanceAmounts;
import fr.syuko.emicreatecompat.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public final class ChanceModeButton {

    private static final int BACKGROUND = 0xDD101010;

    private static final int BORDER = 0xFF5A5A5A;

    private static final int BORDER_HOVERED = 0xFF8099FF;

    private static final int GLYPH_HEIGHT = 8;

    private static final int GLYPH_TRAILING_SPACING = 1;

    private static final float GLYPH_SCALE = 2f;

    private static final int GLYPH_VERTICAL_NUDGE = -1;

    public static final int X = 4;

    public static final int Y = 4;

    public static final int SIZE = 16;

    private ChanceModeButton() {
    }

    public static boolean visible() {
        return BoM.tree != null && TreeChance.present();
    }

    public static boolean contains(double mouseX, double mouseY) {
        return mouseX >= X && mouseX < X + SIZE && mouseY >= Y && mouseY < Y + SIZE;
    }

    public static void render(GuiGraphics graphics, int mouseX, int mouseY) {
        int border = contains(mouseX, mouseY)
                     ? BORDER_HOVERED
                     : BORDER;
        graphics.fill(X, Y, X + SIZE, Y + SIZE, BACKGROUND);
        graphics.renderOutline(X, Y, SIZE, SIZE, border);

        ModeGlyph glyph = ModeGlyph.current();
        Component text = glyph.text();
        Font font = Minecraft.getInstance().font;
        int inkWidth = Math.round((font.width(text) - GLYPH_TRAILING_SPACING) * GLYPH_SCALE);
        int inkHeight = Math.round(GLYPH_HEIGHT * GLYPH_SCALE);
        int glyphX = X + Math.round((SIZE - inkWidth) / 2f);
        int glyphY = Y + Math.round((SIZE - inkHeight) / 2f) + GLYPH_VERTICAL_NUDGE;

        graphics.pose().pushPose();
        graphics.pose().translate(glyphX, glyphY, 0f);
        graphics.pose().scale(GLYPH_SCALE, GLYPH_SCALE, 1f);
        graphics.drawString(font, text, 0, 0, glyph.color, false);
        graphics.pose().popPose();
    }

    public static List<ClientTooltipComponent> tooltip() {
        String mode = BomAmountMode.rawAmounts()
                      ? "raw"
                      : "expected";
        List<ClientTooltipComponent> components = new ArrayList<>();
        components.add(EmiTooltipComponents.of(Component.translatable("tooltip.emicreatecompat.chance_mode." + mode)));

        components.add(EmiTooltipComponents.of(Component.translatable("tooltip.emicreatecompat.chance_mode." + mode + ".description")
                                                        .withStyle(ChatFormatting.GRAY)));
        components.add(EmiTooltipComponents.of(Component.translatable("tooltip.emicreatecompat.chance_mode.switch")
                                                        .withStyle(ChatFormatting.DARK_GRAY)));
        return components;
    }

    public static void toggle() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        Config.setChanceAmounts(BomAmountMode.rawAmounts()
                                ? ChanceAmounts.EXPECTED
                                : ChanceAmounts.RAW);
    }

    private enum ModeGlyph {
        EXPECTED("≈", 0xFFFFAA00),
        RAW("=", 0xFFFFFFFF);

        private final String character;

        private final int color;

        ModeGlyph(String character, int color) {
            this.character = character;
            this.color = color;
        }

        static ModeGlyph current() {
            return BomAmountMode.rawAmounts()
                   ? RAW
                   : EXPECTED;
        }

        Component text() {
            return Component.literal(character).withStyle(style -> style.withFont(Minecraft.UNIFORM_FONT));
        }
    }
}
