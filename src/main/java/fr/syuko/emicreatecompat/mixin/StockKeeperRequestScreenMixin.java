package fr.syuko.emicreatecompat.mixin;

import com.simibubi.create.content.logistics.BigItemStack;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen.CategoryEntry;
import fr.syuko.emicreatecompat.config.Config;
import fr.syuko.emicreatecompat.create.PendingOrder;
import fr.syuko.emicreatecompat.create.StockCategoryInjector;
import fr.syuko.emicreatecompat.create.StockMatcher;
import fr.syuko.emicreatecompat.emi.EmiRecipeTreeReader;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(StockKeeperRequestScreen.class)
public abstract class StockKeeperRequestScreenMixin {

    @Shadow(remap = false)
    public List<List<BigItemStack>> currentItemSource;

    @Shadow(remap = false)
    public List<CategoryEntry> categories;

    @Shadow(remap = false)
    public List<List<BigItemStack>> displayedItems;

    @Shadow(remap = false)
    public List<BigItemStack> itemsToOrder;

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

        List<ItemStack> pendingOrder = Config.countPendingOrder ? PendingOrder.stacks(itemsToOrder) : List.of();

        Set<Item> needed = EmiRecipeTreeReader.neededTreeItems(pendingOrder);
        List<BigItemStack> matches = StockMatcher.match(needed, currentItemSource);
        if (matches.isEmpty()) {
            return;
        }

        String title = Component.translatable("gui.emicreatecompat.stock_keeper.recipe_tree").getString();
        StockCategoryInjector.Result result =
                StockCategoryInjector.prepend(title, matches, categories, displayedItems, rowHeight, cols);
        categories = result.categories();
        displayedItems = result.displayedItems();
    }

    @Inject(method = "removed", at = @At("TAIL"))
    private void emicreatecompat$restoreEmiFavorites(CallbackInfo ci) {
        if (Config.countPendingOrder) {
            EmiRecipeTreeReader.restoreNativeFavorites();
        }
    }
}
