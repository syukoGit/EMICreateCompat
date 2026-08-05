package fr.syuko.emicreatecompat.emi;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.gui.GuiGraphics;

public class ScaledSlotWidget extends SlotWidget {

    private static final int SIZE = 18;

    private final int originX;

    private final int originY;

    private final float scale;

    public ScaledSlotWidget(EmiIngredient stack, int x, int y, float scale) {
        super(stack, x, y);
        this.originX = x;
        this.originY = y;
        this.scale = scale;
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(originX, originY, Math.round(SIZE * scale), Math.round(SIZE * scale));
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        PoseStack pose = draw.pose();
        pose.pushPose();
        pose.translate(originX, originY, 0);
        pose.scale(scale, scale, 1);
        pose.translate(-originX, -originY, 0);
        super.render(draw, mouseX, mouseY, delta);
        pose.popPose();
    }
}
