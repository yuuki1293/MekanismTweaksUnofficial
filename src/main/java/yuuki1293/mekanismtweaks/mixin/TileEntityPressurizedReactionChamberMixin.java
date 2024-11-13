package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.machine.TileEntityPressurizedReactionChamber;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuki1293.mekanismtweaks.Utils;

// Pressurized Reaction Chamber
@Mixin(value = TileEntityPressurizedReactionChamber.class, remap = false)
public abstract class TileEntityPressurizedReactionChamberMixin extends TileEntityMekanism {
    @Shadow @Final private static int BASE_DURATION;
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    public TileEntityPressurizedReactionChamberMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "createNewCachedRecipe(Lmekanism/api/recipes/PressurizedReactionRecipe;I)Lmekanism/api/recipes/cache/CachedRecipe;", at = @At(value = "TAIL"))
    private void createNewCachedRecipe(@NotNull PressurizedReactionRecipe recipe, int cacheIndex, CallbackInfoReturnable<CachedRecipe<PressurizedReactionRecipe>> cir) {
        cir.getReturnValue().setBaselineMaxOperations(() -> mekanismtweaks$baselineMaxOperations);
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        mekanismtweaks$baselineMaxOperations = Utils.setBaselineMaxOperation(BASE_DURATION, this, upgrade);
    }
}
