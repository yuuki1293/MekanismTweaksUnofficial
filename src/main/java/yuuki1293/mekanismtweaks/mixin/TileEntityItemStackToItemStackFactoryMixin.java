package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.factory.TileEntityItemStackToItemStackFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine.BASE_TICKS_REQUIRED;
import static mekanism.common.util.MekanismUtils.fractionUpgrades;

//Smelting, enriching, crushing
@Mixin(value = TileEntityItemStackToItemStackFactory.class, remap = false)
public abstract class TileEntityItemStackToItemStackFactoryMixin extends TileEntityMekanism {
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    public TileEntityItemStackToItemStackFactoryMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "createNewCachedRecipe(Lmekanism/api/recipes/ItemStackToItemStackRecipe;I)Lmekanism/api/recipes/cache/CachedRecipe;", at = @At(value = "TAIL"))
    private void createNewCachedRecipe(ItemStackToItemStackRecipe recipe, int flags, CallbackInfoReturnable<CachedRecipe<?>> cir) {
        cir.getReturnValue().setBaselineMaxOperations(() -> mekanismtweaks$baselineMaxOperations);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            var tickRequired = BASE_TICKS_REQUIRED * Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(), -fractionUpgrades((TileEntityItemStackToItemStackFactory) (Object) this, Upgrade.SPEED));
            if (tickRequired < 1.0) {
                mekanismtweaks$baselineMaxOperations = (int) Math.min(1.0 / tickRequired, 64);
            } else
                mekanismtweaks$baselineMaxOperations = 1;
        }
    }
}
