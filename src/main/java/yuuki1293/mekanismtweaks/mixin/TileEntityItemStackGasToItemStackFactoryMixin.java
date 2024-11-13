package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.factory.TileEntityItemStackGasToItemStackFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuki1293.mekanismtweaks.Utils;

import java.util.List;
import java.util.Set;

// Compressing, Injecting, Purifying
@Mixin(value = TileEntityItemStackGasToItemStackFactory.class, remap = false)
public abstract class TileEntityItemStackGasToItemStackFactoryMixin extends TileEntityFactory<MekanismRecipe> {
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    protected TileEntityItemStackGasToItemStackFactoryMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes, Set<CachedRecipe.OperationTracker.RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state, errorTypes, globalErrorTypes);
    }

    @Inject(method = "createNewCachedRecipe(Lmekanism/api/recipes/ItemStackGasToItemStackRecipe;I)Lmekanism/api/recipes/cache/CachedRecipe;", at = @At(value = "TAIL"))
    private void createNewCachedRecipe(@NotNull ItemStackGasToItemStackRecipe recipe, int cacheIndex, CallbackInfoReturnable<CachedRecipe<ItemStackGasToItemStackRecipe>> cir) {
        cir.getReturnValue().setBaselineMaxOperations(() -> mekanismtweaks$baselineMaxOperations);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        Utils.setBaselineMaxOperation(BASE_TICKS_REQUIRED, this, upgrade, x -> mekanismtweaks$baselineMaxOperations = x);
    }
}
