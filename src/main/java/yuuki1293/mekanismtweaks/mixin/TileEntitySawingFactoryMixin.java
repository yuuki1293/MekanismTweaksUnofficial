package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.factory.TileEntitySawingFactory;
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
import java.util.function.IntConsumer;

// Sawing
@Mixin(value = TileEntitySawingFactory.class, remap = false)
public abstract class TileEntitySawingFactoryMixin extends TileEntityFactory<MekanismRecipe> {
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    protected TileEntitySawingFactoryMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes, Set<CachedRecipe.OperationTracker.RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state, errorTypes, globalErrorTypes);
    }

    @Inject(method = "createNewCachedRecipe(Lmekanism/api/recipes/SawmillRecipe;I)Lmekanism/api/recipes/cache/CachedRecipe;", at = @At(value = "TAIL"))
    private void createNewCachedRecipe(@NotNull SawmillRecipe recipe, int cacheIndex, CallbackInfoReturnable<CachedRecipe<SawmillRecipe>> cir) {
        cir.getReturnValue().setBaselineMaxOperations(() -> mekanismtweaks$baselineMaxOperations);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        Utils.setBaselineMaxOperation(BASE_TICKS_REQUIRED, this, upgrade, x -> mekanismtweaks$baselineMaxOperations = x);
    }
}