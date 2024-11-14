package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yuuki1293.mekanismtweaks.IProcess;
import yuuki1293.mekanismtweaks.Utils;

@Mixin(value = TileEntityMekanism.class, remap = false)
public abstract class TileEntityMekanismMixin implements IUpgradeTile, IProcess {
    @Unique
    private int mekanismtweaks$baselineMaxOperations;

    @Override
    public int mekanismtweaks$getBaselineMaxOperations() {
        return mekanismtweaks$baselineMaxOperations;
    }

    @Inject(method = "recalculateUpgrades", at = @At("HEAD"))
    private void recalculateUpgrades(Upgrade upgrade, CallbackInfo ci) throws NoSuchFieldException, IllegalAccessException {
        var clazz = this.getClass();
        int baseTick = 200;
        if (clazz.isAssignableFrom(TileEntityProgressMachine.class)) {
            var field = TileEntityProgressMachine.class.getDeclaredField("baseTicksRequired");
            field.setAccessible(true);
            baseTick = field.getInt(this);
        }
        if (clazz.isAssignableFrom(TileEntityFactory.class)) {
            var field = TileEntityFactory.class.getDeclaredField("BASE_TICKS_REQUIRED");
            field.setAccessible(true);
            baseTick = field.getInt(this);
        }
        Utils.setBaselineMaxOperation(baseTick, this, upgrade, x -> mekanismtweaks$baselineMaxOperations = x);
    }
}
