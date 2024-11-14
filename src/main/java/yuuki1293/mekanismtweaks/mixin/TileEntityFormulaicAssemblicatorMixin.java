package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.tile.machine.TileEntityFormulaicAssemblicator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yuuki1293.mekanismtweaks.Utils;

import java.lang.reflect.InvocationTargetException;

// Formulaic Assemblicator
@Mixin(value = TileEntityFormulaicAssemblicator.class, remap = false)
public abstract class TileEntityFormulaicAssemblicatorMixin implements IUpgradeTile {
    @Shadow
    @Final
    private static int BASE_TICKS_REQUIRED;
    @Unique
    private int mekanismtweaks$baselineMaxOperations = 1;
    @Unique
    private boolean mekanismtweaks$lock;

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

    @Inject(method = "recalculateUpgrades", at = @At(value = "HEAD"))
    public void recalculateUpgrades(Upgrade upgrade, CallbackInfo ci) {
        Utils.setBaselineMaxOperation(BASE_TICKS_REQUIRED, this, upgrade, x -> mekanismtweaks$baselineMaxOperations = x);
    }
}
