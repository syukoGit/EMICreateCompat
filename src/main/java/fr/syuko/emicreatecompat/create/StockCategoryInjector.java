package fr.syuko.emicreatecompat.create;

import com.simibubi.create.content.logistics.BigItemStack;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperRequestScreen.CategoryEntry;
import fr.syuko.emicreatecompat.mixin.CategoryEntryAccessor;

import java.util.ArrayList;
import java.util.List;

public final class StockCategoryInjector {

    private StockCategoryInjector() {
    }

    public static Result prepend(String title,
                                 List<BigItemStack> matches,
                                 List<CategoryEntry> categories,
                                 List<List<BigItemStack>> displayedItems,
                                 int rowHeight,
                                 int cols) {
        List<CategoryEntry> newCategories;
        if (categories.size() != displayedItems.size()) {
            newCategories = new ArrayList<>();
            for (int i = 0; i < displayedItems.size(); i++) {
                newCategories.add(new CategoryEntry(-1, "", 0));
            }
        } else {
            newCategories = new ArrayList<>(categories);
        }

        newCategories.addFirst(new CategoryEntry(-1, title, 0));
        List<List<BigItemStack>> newDisplayed = new ArrayList<>();
        newDisplayed.add(matches);
        newDisplayed.addAll(displayedItems);

        layout(newCategories, newDisplayed, rowHeight, cols);
        return new Result(newCategories, newDisplayed);
    }

    private static void layout(List<CategoryEntry> categories,
                               List<List<BigItemStack>> displayedItems,
                               int rowHeight,
                               int cols) {
        int y = 0;
        for (int i = 0; i < displayedItems.size(); i++) {
            CategoryEntryAccessor accessor = (CategoryEntryAccessor) categories.get(i);
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

    public record Result(List<CategoryEntry> categories, List<List<BigItemStack>> displayedItems) {
    }
}
