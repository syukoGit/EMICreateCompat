package fr.syuko.emicreatecompat.category.conversion;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import fr.syuko.emicreatecompat.Emicreatecompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class ConversionRecipes {

    private ConversionRecipes() {
    }

    public static List<ConversionDisplay> all() {
        return List.of(display(AllItems.EMPTY_BLAZE_BURNER.asStack(), AllBlocks.BLAZE_BURNER.asStack()),
                       display(AllBlocks.PECULIAR_BELL.asStack(), AllBlocks.HAUNTED_BELL.asStack()));
    }

    private static ConversionDisplay display(ItemStack input, ItemStack output) {
        String path = "/mystery_conversion/" + BuiltInRegistries.ITEM.getKey(output.getItem()).getPath();
        return new ConversionDisplay(ResourceLocation.fromNamespaceAndPath(Emicreatecompat.MODID, path), input, output);
    }
}
