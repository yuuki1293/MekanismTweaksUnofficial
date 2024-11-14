package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.machine.TileEntityFluidicPlenisher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yuuki1293.mekanismtweaks.Utils;

@Mixin(value = TileEntityFluidicPlenisher.class, remap = false)
public class TileEntityFluidicPlenisherMixin extends TileEntityMekanism {
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    @Unique
    private Boolean mekanismtweaks$lock = false;

    public TileEntityFluidicPlenisherMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Inject(method = "onUpdateServer", at = @At(value = "TAIL"))
    private void onUpdateServer(CallbackInfo ci) {
        if (mekanismtweaks$lock) return;

        mekanismtweaks$lock = true;
        var count = mekanismtweaks$baselineMaxOperations;
        for (int i = 1; i < count; i++) {
            this.onUpdateServer();
        }
        mekanismtweaks$lock = false;
    }

    @Inject(method = "recalculateUpgrades", at = @At(value = "TAIL"))
    private void recalculateUpgrades(Upgrade upgrade, CallbackInfo ci) {
        Utils.setBaselineMaxOperation(MekanismConfig.general.minerTicksPerMine.get(), this, upgrade, x -> mekanismtweaks$baselineMaxOperations = x);
    }
}
