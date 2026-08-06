package fr.syuko.emicreatecompat.plugin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.render.EmiRenderable;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

class DoubleItemIcon implements EmiRenderable {

    private static final int SECONDARY_OFFSET = 8;

    private static final float SECONDARY_SCALE = 0.5f;

    private final ItemStack primary;

    private final ItemStack secondary;

    DoubleItemIcon(ItemStack primary, ItemStack secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public void render(GuiGraphics draw, int x, int y, float delta) {
        PoseStack pose = draw.pose();
        RenderSystem.enableDepthTest();

        pose.pushPose();
        pose.translate(x, y, 0);
        GuiGameElement.of(primary).render(draw);
        pose.popPose();

        pose.pushPose();
        pose.translate(x + SECONDARY_OFFSET, y + SECONDARY_OFFSET, 100);
        pose.scale(SECONDARY_SCALE, SECONDARY_SCALE, SECONDARY_SCALE);
        GuiGameElement.of(secondary).render(draw);
        pose.popPose();
    }
}
