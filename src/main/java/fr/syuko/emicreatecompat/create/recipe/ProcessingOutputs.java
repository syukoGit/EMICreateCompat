package fr.syuko.emicreatecompat.create.recipe;

import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class ProcessingOutputs {

    private ProcessingOutputs() {
    }

    public static List<ChancedStack> of(List<ProcessingOutput> results) {
        List<ChancedStack> stacks = new ArrayList<>();

        for (ProcessingOutput result : results) {
            ItemStack stack = result.getStack();
            if (!stack.isEmpty()) {
                stacks.add(new ChancedStack(stack, result.getChance()));
            }
        }

        return List.copyOf(stacks);
    }
}
