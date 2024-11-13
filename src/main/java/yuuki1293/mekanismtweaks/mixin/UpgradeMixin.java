package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuki1293.mekanismtweaks.Config;

@Mixin(value = Upgrade.class, remap = false)
public abstract class UpgradeMixin {
    @Inject(method = "getMax", at = @At(value = "TAIL"), cancellable = true)
    private void getMax(CallbackInfoReturnable<Integer> cir) {
        var thisObj = (Upgrade) (Object) this;
        switch (thisObj) {
            case SPEED -> cir.setReturnValue(Config.speedStackSize);
            case ENERGY -> cir.setReturnValue(Config.energyStackSize);
            default -> {
            }
        }
    }

    @ModifyArg(method = "buildMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(III)I"), index = 2)
    private static int injectedMaxStack(int maxStack) {
        return Integer.MAX_VALUE;
    }
}
