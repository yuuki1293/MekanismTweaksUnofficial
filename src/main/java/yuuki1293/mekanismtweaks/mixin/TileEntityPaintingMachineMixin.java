package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.PaintingRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.machine.TileEntityPaintingMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuki1293.mekanismtweaks.Utils;

// Painting Machine
@Mixin(value = TileEntityPaintingMachine.class, remap = false)
public abstract class TileEntityPaintingMachineMixin extends TileEntityMekanism {
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    public TileEntityPaintingMachineMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "createNewCachedRecipe(Lmekanism/api/recipes/PaintingRecipe;I)Lmekanism/api/recipes/cache/CachedRecipe;", at = @At(value = "TAIL"))
    private void createNewCachedRecipe(@NotNull PaintingRecipe recipe, int cacheIndex, CallbackInfoReturnable<CachedRecipe<PaintingRecipe>> cir) {
        cir.getReturnValue().setBaselineMaxOperations(() -> mekanismtweaks$baselineMaxOperations);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        Utils.setBaselineMaxOperation(200, this, upgrade, x -> mekanismtweaks$baselineMaxOperations = x);
    }
}