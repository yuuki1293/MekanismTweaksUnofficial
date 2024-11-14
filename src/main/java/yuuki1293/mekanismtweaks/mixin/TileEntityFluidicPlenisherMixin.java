package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.tile.machine.TileEntityFluidicPlenisher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yuuki1293.mekanismtweaks.Utils;

import java.lang.reflect.InvocationTargetException;

@Mixin(value = TileEntityFluidicPlenisher.class, remap = false)
public abstract class TileEntityFluidicPlenisherMixin implements IUpgradeTile {
    @Shadow
    @Final
    public static int BASE_TICKS_REQUIRED;
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;

    @Unique
    private Boolean mekanismtweaks$lock = false;

    @Inject(method = "onUpdateServer", at = @At(value = "TAIL"))
    private void onUpdateServer(CallbackInfo ci) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (mekanismtweaks$lock) return;

        mekanismtweaks$lock = true;
        var count = mekanismtweaks$baselineMaxOperations;
        for (int i = 1; i < count; i++) {
            var clazz = this.getClass();
            clazz.getDeclaredMethod("onUpdateServer").invoke(this);
        }
        mekanismtweaks$lock = false;
    }

    @Inject(method = "recalculateUpgrades", at = @At(value = "TAIL"))
    private void recalculateUpgrades(Upgrade upgrade, CallbackInfo ci) {
        Utils.setBaselineMaxOperation(BASE_TICKS_REQUIRED, this, upgrade, x -> mekanismtweaks$baselineMaxOperations = x);
    }
}
