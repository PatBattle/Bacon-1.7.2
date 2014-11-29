package bacon;

import java.io.File;

import bacon.items.CookedBacon;
import bacon.items.RawBacon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.RegistryNamespaced;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Mod(modid=BaconMain.modId, name="Bacon", version="0.01.0000")
public class BaconMain {

	// The instance of your mod that Forge uses.
    @Instance(value = "Bacon")
    public static BaconMain instance;
    public static final String modId = "Bacon";
    public static Item baconRaw;
    public static Item baconCooked;
    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="bacon.client.ClientProxy", serverSide="bacon.CommonProxy")
    public static CommonProxy proxy;
    public static Achievement bringBacon;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        String category = "Item IDs";
        Property rawBaconID = config.get(category, "rawBaconID", 6000);
        Property cookedBaconID = config.get(category, "cookedBaconID", 6001);
        config.save();
        baconRaw = new RawBacon(2, 0.3F, true).setMaxStackSize(64).setUnlocalizedName("baconRaw");
        baconCooked = new CookedBacon(5, 0.6F, true).setMaxStackSize(64).setUnlocalizedName("baconCooked");
        bringBacon = new Achievement("TestString", "BringBacon", 5, -4, baconRaw, AchievementList.buildSword).registerStat();
        // The second parameter is an unique registry identifier (not the displayed name)
        // Please don't use item1.getUnlocalizedName(), or you will make Lex sad
        LanguageRegistry.addName(baconRaw, "Raw Bacon");
        LanguageRegistry.addName(baconCooked, "Cooked Bacon");
        RegistryNamespaced itemRegistry = GameData.getItemRegistry();
        itemRegistry.addObject(rawBaconID.getInt(), "baconRaw", baconRaw );
        itemRegistry.addObject(cookedBaconID.getInt(), "baconCooked", baconCooked );
        GameRegistry.addSmelting(BaconMain.baconRaw, new ItemStack(BaconMain.baconCooked), 0.8F);
    }
       
    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.registerRenderers();
        this.addAchievementLocalizations();
        MinecraftForge.EVENT_BUS.register(new EventHooks());
    }
       
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Stub Method
    }
    private void addAchievementName(String ach, String name)
    {
        LanguageRegistry.instance().addStringLocalization("achievement." + ach, "en_US", name);
    }

    private void addAchievementDesc(String ach, String desc)
    {
        LanguageRegistry.instance().addStringLocalization("achievement." + ach + ".desc", "en_US", desc);
    }
        
    public void addAchievementLocalizations()
    {
        this.addAchievementName("BringBacon", "Bring Home the Bacon!");
        this.addAchievementDesc("BringBacon", "You have been granted bacon!");
    }
}