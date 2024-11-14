package yuuki1293.mekanismtweaks.mixin;

import mekanism.api.Upgrade;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import mekanism.common.recipe.lookup.monitor.RecipeCacheLookupMonitor;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yuuki1293.mekanismtweaks.IProcess;

import java.lang.reflect.InvocationTargetException;

@Mixin(value = RecipeCacheLookupMonitor.class, remap = false)
public class RecipeCacheLookupMonitorMixin {
    @Shadow
    @Final
    private IRecipeLookupHandler<?> handler;

    @Inject(method = "createNewCachedRecipe", at = @At(value = "RETURN"))
    private <RECIPE extends MekanismRecipe> void createNewCachedRecipe(@NotNull RECIPE recipe, int cacheIndex, CallbackInfoReturnable<CachedRecipe<RECIPE>> cir) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var ret = cir.getReturnValue();
        if (ret != null) {
            var clazz = this.handler.getClass();
            if ((TileEntityProgressMachine.class.isAssignableFrom(clazz)
                || TileEntityFactory.class.isAssignableFrom(clazz))
                && (Boolean) clazz.getMethod("supportsUpgrade", Upgrade.class).invoke(this.handler, Upgrade.SPEED)) {
                ret.setBaselineMaxOperations(() -> ((IProcess) this.handler).mekanismtweaks$getBaselineMaxOperations());
            }
        }
    }
}
