package fr.syuko.emicreatecompat.create.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public final class EncasedFan {

    private EncasedFan() {
    }

    public static ItemStack labelled(String recipeName) {
        ItemStack stack = AllBlocks.ENCASED_FAN.asStack();
        stack.set(DataComponents.CUSTOM_NAME,
                  CreateLang.translateDirect("recipe." + recipeName + ".fan")
                            .withStyle(style -> style.withItalic(false)));
        return stack;
    }
}
