package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.factory.TileEntityItemStackToItemStackFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuki1293.mekanismtweaks.Utils;

import java.util.List;
import java.util.Set;

//Smelting, enriching, crushing
@Mixin(value = TileEntityItemStackToItemStackFactory.class, remap = false)
public abstract class TileEntityItemStackToItemStackFactoryMixin extends TileEntityFactory<MekanismRecipe> {
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    protected TileEntityItemStackToItemStackFactoryMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes, Set<CachedRecipe.OperationTracker.RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state, errorTypes, globalErrorTypes);
    }


    @Inject(method = "createNewCachedRecipe(Lmekanism/api/recipes/ItemStackToItemStackRecipe;I)Lmekanism/api/recipes/cache/CachedRecipe;", at = @At(value = "TAIL"))
    private void createNewCachedRecipe(ItemStackToItemStackRecipe recipe, int flags, CallbackInfoReturnable<CachedRecipe<?>> cir) {
        cir.getReturnValue().setBaselineMaxOperations(() -> mekanismtweaks$baselineMaxOperations);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        mekanismtweaks$baselineMaxOperations = Utils.setBaselineMaxOperation(BASE_TICKS_REQUIRED, this, upgrade);
    }
}
