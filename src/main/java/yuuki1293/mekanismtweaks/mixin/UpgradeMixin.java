package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuki1293.mekanismtweaks.Config;

@Mixin(value = Upgrade.class, remap = false)
public abstract class UpgradeMixin {
    @Unique
    private static String mekanismtweaks$name = "";

    @ModifyVariable(method = "<init>", at = @At(value = "HEAD"), ordinal = 1, argsOnly = true)
    private static String initName(String name) {
        mekanismtweaks$name = name;
        return name;
    }

    @ModifyVariable(method = "<init>", at = @At(value = "HEAD"), ordinal = 1, argsOnly = true)
    private static int initMaxStack(int maxStack) {
        if (mekanismtweaks$name.equals("speed"))
            return Integer.MAX_VALUE;
        if (mekanismtweaks$name.equals("energy"))
            return Integer.MAX_VALUE;
        return maxStack;
    }

    @Inject(method = "getMax", at = @At(value = "TAIL"), cancellable = true)
    private void getMax(CallbackInfoReturnable<Integer> cir) {
        var thisObj = (Upgrade) (Object) this;
        switch (thisObj) {
            case SPEED -> cir.setReturnValue(Config.speedStackSize);
            case ENERGY -> cir.setReturnValue(Config.energyStackSize);
        }
    }
}
