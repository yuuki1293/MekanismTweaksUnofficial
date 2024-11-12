package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.MekanismUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MekanismUtils.class, remap = false)
public abstract class MekanismUtilsMixin {
    @Inject(method = "fractionUpgrades", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private static void fractionUpgrades(IUpgradeTile tile, Upgrade type, CallbackInfoReturnable<Double> info) {
        switch (type) {
            case SPEED -> info.setReturnValue(tile.getComponent().getUpgrades(type) / 8.0);
            case ENERGY -> {
                var energyCount = tile.getComponent().getUpgrades(Upgrade.ENERGY);
                if (tile.supportsUpgrade(Upgrade.SPEED)) {
                    var speedCount = tile.getComponent().getUpgrades(Upgrade.SPEED);
                    var available = Math.min(energyCount, Math.max(8, speedCount));
                    info.setReturnValue(available / 8.0);
                }
                info.setReturnValue(energyCount / 8.0);
            }
            default -> {
            }
        }
    }
}
