package fr.syuko.emicreatecompat.category.polishing;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItemComponent;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public final class PolishingRender {

    private static final int SANDPAPER_X = 72;

    private static final int SANDPAPER_SCALE = 2;

    private PolishingRender() {
    }

    public static void draw(GuiGraphics graphics, int x, int y, Ingredient polished) {
        AllGuiTextures.JEI_SHADOW.render(graphics, x + 61, y + 21);
        AllGuiTextures.JEI_LONG_ARROW.render(graphics, x + 52, y + 32);

        ItemStack[] matching = polished.getItems();
        if (matching.length == 0) {
            return;
        }

        ItemStack sandpaper = AllItems.SAND_PAPER.asStack();
        sandpaper.set(AllDataComponents.SAND_PAPER_POLISHING, new SandPaperItemComponent(matching[0]));
        sandpaper.set(AllDataComponents.SAND_PAPER_JEI, Unit.INSTANCE);

        GuiGameElement.of(sandpaper)
                      .<GuiGameElement.GuiRenderBuilder>at(x + SANDPAPER_X, y, 0)
                      .scale(SANDPAPER_SCALE)
                      .render(graphics);
    }
}
