package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.factory.TileEntityCombiningFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuki1293.mekanismtweaks.Utils;

// Combiner
@Mixin(value = TileEntityCombiningFactory.class, remap = false)
public abstract class TileEntityCombiningFactoryMixin extends TileEntityMekanism {
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    public TileEntityCombiningFactoryMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "createNewCachedRecipe(Lmekanism/api/recipes/CombinerRecipe;I)Lmekanism/api/recipes/cache/CachedRecipe;", at = @At(value = "TAIL"))
    private void createNewCachedRecipe(@NotNull CombinerRecipe recipe, int cacheIndex, CallbackInfoReturnable<CachedRecipe<CombinerRecipe>> cir) {
        cir.getReturnValue().setBaselineMaxOperations(() -> mekanismtweaks$baselineMaxOperations);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        mekanismtweaks$baselineMaxOperations = Utils.setBaselineMaxOperation(200, this, upgrade);
    }
}