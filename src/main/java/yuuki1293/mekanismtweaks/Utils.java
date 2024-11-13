package yuuki1293.mekanismtweaks;

import mekanism.api.Upgrade;
import mekanism.common.config.MekanismConfig;
import mekanism.common.tile.interfaces.IUpgradeTile;

import java.util.function.IntConsumer;

import static mekanism.common.util.MekanismUtils.fractionUpgrades;

public class Utils {
    public static void setBaselineMaxOperation(int baseTick, IUpgradeTile tile, Upgrade upgrade, IntConsumer setter) {
        if (upgrade == Upgrade.SPEED) {
            var tickRequired = baseTick * Math.pow(MekanismConfig.general.maxUpgradeMultiplier.get(), -fractionUpgrades(tile, Upgrade.SPEED));
            if (tickRequired < 1.0) {
                setter.accept((int) Math.min(1.0 / tickRequired, 64));
            } else {
                setter.accept(1);
            }
        }
    }
}
