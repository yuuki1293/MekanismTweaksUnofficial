package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.machine.TileEntityChemicalDissolutionChamber;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuki1293.mekanismtweaks.Utils;

// Chemical Dissolution Chamber
@Mixin(value = TileEntityChemicalDissolutionChamber.class, remap = false)
public abstract class TileEntityChemicalDissolutionChamberMixin extends TileEntityMekanism {
    @Shadow
    @Final
    public static int BASE_TICKS_REQUIRED;
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    public TileEntityChemicalDissolutionChamberMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "createNewCachedRecipe(Lmekanism/api/recipes/ChemicalDissolutionRecipe;I)Lmekanism/api/recipes/cache/CachedRecipe;", at = @At(value = "TAIL"))
    private void createNewCachedRecipe(@NotNull ChemicalDissolutionRecipe recipe, int cacheIndex, CallbackInfoReturnable<CachedRecipe<ChemicalDissolutionRecipe>> cir) {
        cir.getReturnValue().setBaselineMaxOperations(() -> mekanismtweaks$baselineMaxOperations);
    }

    @Inject(method = "recalculateUpgrades", at = @At(value = "HEAD"))
    public void recalculateUpgrades(Upgrade upgrade, CallbackInfo ci) {
        super.recalculateUpgrades(upgrade);
        Utils.setBaselineMaxOperation(BASE_TICKS_REQUIRED, this, upgrade, x -> mekanismtweaks$baselineMaxOperations = x);
    }
}
