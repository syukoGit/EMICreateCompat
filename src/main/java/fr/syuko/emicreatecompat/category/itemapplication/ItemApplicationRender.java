package fr.syuko.emicreatecompat.category.itemapplication;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import fr.syuko.emicreatecompat.create.render.KineticsRender;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public final class ItemApplicationRender {

    private static final int BLOCK_SCALE = 20;

    private ItemApplicationRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, Ingredient processed) {
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 62, y + 47);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, x + 74, y + 10);

        ItemStack[] matching = processed.getItems();
        if (matching.length == 0 || !(matching[0].getItem() instanceof BlockItem blockItem)) {
            return;
        }

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x + 74, y + 51, 100);
        pose.mulPose(Axis.XP.rotationDegrees(-15.5f));
        pose.mulPose(Axis.YP.rotationDegrees(22.5f));

        GuiGameElement.of(blockItem.getBlock().defaultBlockState())
                      .lighting(KineticsRender.LIGHTING)
                      .scale(BLOCK_SCALE)
                      .render(graphics);

        pose.popPose();
    }
}
