package yuuki1293.mekanismtweaks;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = MekanismTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue SPEED_UPGRADE_STACK_SIZE = BUILDER
        .comment("Speed Upgrade max stack size")
        .defineInRange("speedUpgradeStackSize", 64, 8, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue ENERGY_UPGRADE_STACK_SIZE = BUILDER
        .comment("Energy Upgrade max stack size")
        .defineInRange("energyUpgradeStackSize", 64, 8, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int speedStackSize;
    public static int energyStackSize;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        speedStackSize = SPEED_UPGRADE_STACK_SIZE.get();
        energyStackSize = ENERGY_UPGRADE_STACK_SIZE.get();
    }
}
