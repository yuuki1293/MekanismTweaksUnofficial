package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = UpgradeUtils.class, remap = false)
public abstract class UpgradeUtilsMixin {
    @Inject(method = "getMultScaledInfo", at = @At(value = "INVOKE", target = "Ljava/lang/Math;pow(DD)D", shift = At.Shift.AFTER), cancellable = true)
    private static void injectedEffect(IUpgradeTile tile, Upgrade upgrade, CallbackInfoReturnable<List<Component>> cir) {
        if (!(upgrade.equals(Upgrade.ENERGY) || upgrade.equals(Upgrade.SPEED))) return;

        var ret = new ArrayList<Component>();
        double effect = Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(), (float) tile.getComponent().getUpgrades(upgrade) / 8.0);
        ret.add(MekanismLang.UPGRADES_EFFECT.translate(Math.round(effect * 100) / 100F));
        cir.setReturnValue(ret);
        cir.cancel();
    }
}
