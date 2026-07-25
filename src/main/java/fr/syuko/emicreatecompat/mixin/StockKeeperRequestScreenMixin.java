package fr.syuko.emicreatecompat.mixin;

import com.simibubi.create.content.logistics.BigItemStack;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen.CategoryEntry;
import fr.syuko.emicreatecompat.emi.RecipeTreeResources;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Injects a category at the very top of Create's Stock Keeper request screen listing the items
 * available in the network that match the resources still required by the active EMI recipe tree.
 * Updates live: it runs on every {@code refreshSearchResults}, which Create calls each tick.
 */
@Mixin(StockKeeperRequestScreen.class)
public abstract class StockKeeperRequestScreenMixin {

    @Shadow(remap = false)
    public List<List<BigItemStack>> currentItemSource;

    @Shadow(remap = false)
    public List<CategoryEntry> categories;

    @Shadow(remap = false)
    public List<List<BigItemStack>> displayedItems;

    @Shadow(remap = false)
    @Final
    int rowHeight;

    @Shadow(remap = false)
    @Final
    int cols;

    @Inject(method = "refreshSearchResults(Z)V", at = @At("RETURN"), remap = false)
    private void emicreatecompat$injectRecipeTreeCategory(boolean scrollBackUp, CallbackInfo ci) {
        if (currentItemSource == null || displayedItems == null || displayedItems.isEmpty()) {
            return;
        }

        // Resources the EMI recipe tree still needs (final + intermediates + leaves).
        Set<Item> needed = RecipeTreeResources.neededTreeItems();
        if (needed.isEmpty()) {
            return;
        }

        // Stock items that match a needed resource, de-duplicated by item.
        List<BigItemStack> matches = new ArrayList<>();
        Set<Item> seen = new HashSet<>();
        for (List<BigItemStack> category : currentItemSource) {
            for (BigItemStack entry : category) {
                if (entry.stack == null || entry.stack.isEmpty()) {
                    continue;
                }
                Item item = entry.stack.getItem();
                if (needed.contains(item) && seen.add(item)) {
                    matches.add(entry);
                }
            }
        }
        if (matches.isEmpty()) {
            return;
        }

        // Create empties `categories` when there are no user-defined ones; rebuild neutral
        // placeholders so it stays index-aligned with displayedItems before we prepend ours.
        if (categories.size() != displayedItems.size()) {
            List<CategoryEntry> rebuilt = new ArrayList<>();
            for (int i = 0; i < displayedItems.size(); i++) {
                rebuilt.add(new CategoryEntry(-1, "", 0));
            }
            categories = rebuilt;
        }

        // Prepend our category at the top of both parallel lists.
        String title = Component.translatable("gui.emicreatecompat.stock_keeper.recipe_tree").getString();
        categories.add(0, new CategoryEntry(-1, title, 0));
        List<List<BigItemStack>> newDisplayed = new ArrayList<>();
        newDisplayed.add(matches);
        newDisplayed.addAll(displayedItems);
        displayedItems = newDisplayed;

        // Recompute vertical offsets — mirrors Create's own layout math in refreshSearchResults.
        int y = 0;
        for (int i = 0; i < displayedItems.size(); i++) {
            CategoryEntry entry = categories.get(i);
            CategoryEntryAccessor accessor = (CategoryEntryAccessor) (Object) entry;
            accessor.emicreatecompat$setY(y);
            List<BigItemStack> items = displayedItems.get(i);
            if (items.isEmpty()) {
                continue;
            }
            y += rowHeight;
            if (!accessor.emicreatecompat$isHidden()) {
                y += (int) Math.ceil(items.size() / (float) cols) * rowHeight;
            }
        }
    }
}
