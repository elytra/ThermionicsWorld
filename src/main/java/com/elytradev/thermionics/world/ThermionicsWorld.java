/*
 * MIT License
 *
 * Copyright (c) 2017-2018 Isaac Ellingson (Falkreon) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.elytradev.thermionics.world;

import java.util.function.Consumer;

import com.elytradev.thermionics.world.block.BlockGemrock;
import com.elytradev.thermionics.world.block.BlockMeat;
import com.elytradev.thermionics.world.block.BlockMeatEdible;
import com.elytradev.thermionics.world.block.EnumEdibleMeat;
import com.elytradev.thermionics.world.block.TWBlocks;
import com.elytradev.thermionics.world.gen.WorldProviderNeoHell;
import com.elytradev.thermionics.world.gen.biome.BiomeFamily;
import com.elytradev.thermionics.world.item.TWItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(modid = "thermionics_world", name="Thermionics|World", version="@VERSION@")
public class ThermionicsWorld {
	
	static {
		//Don't kill me. I swear the javadocs say to do this.
		FluidRegistry.enableUniversalBucket();
	}
	
	private static final SoundEvent SOUNDEVENT_SQUISH_STEP  = new SoundEvent(new ResourceLocation("thermionics_world","squish.step")).setRegistryName("squish.step");
	private static final SoundEvent SOUNDEVENT_SQUISH_BREAK = new SoundEvent(new ResourceLocation("thermionics_world","squish.dig")).setRegistryName("squish.dig");
	
	public static final SoundType SOUNDTYPE_SQUISH = new SoundType(1.0f, 1.0f,
			SOUNDEVENT_SQUISH_BREAK,
			SOUNDEVENT_SQUISH_STEP,
			SOUNDEVENT_SQUISH_BREAK,
			SOUNDEVENT_SQUISH_BREAK,
			SOUNDEVENT_SQUISH_STEP
			);
	
	
	public static Configuration CONFIG;
	public static boolean CONFIG_SHOULD_REGISTER_NEOHELL = true;
	public static int CONFIG_DIMENSION_ID_NEOHELL = -1; //For dim remaps
	
	
	public static CreativeTabs TAB_THERMIONICS_WORLD = new CreativeTabs("thermionics_world") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(TWBlocks.GEMROCK_TOURMALINE, 1, 1);
		}
	};
	
	@SidedProxy(clientSide="com.elytradev.thermionics.world.ClientProxy", serverSide="com.elytradev.thermionics.world.Proxy")
	static Proxy proxy;
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
		
		CONFIG = new Configuration(event.getSuggestedConfigurationFile());
		CONFIG.setCategoryComment("neohell", "These keys affect how the world is generated, and whether it is generated.");
		CONFIG_SHOULD_REGISTER_NEOHELL = CONFIG.getBoolean("enabled", "neohell", CONFIG_SHOULD_REGISTER_NEOHELL,
				"Setting this to false disables neohell entirely, and this mod's blocks will be unobtainable unless tweaked in.");
		CONFIG_DIMENSION_ID_NEOHELL = CONFIG.getInt("id", "neohell", CONFIG_DIMENSION_ID_NEOHELL, Integer.MIN_VALUE, Integer.MAX_VALUE,
				"Remaps neohell to a different dimension ID, possibly causing it to replace a different dimension. May be catastrophically bad for existing maps.");
		CONFIG.save();
		if (CONFIG_SHOULD_REGISTER_NEOHELL) {
			if (DimensionManager.isDimensionRegistered(CONFIG_DIMENSION_ID_NEOHELL)) {
				//Nuking hell just makes it stronger
				DimensionManager.unregisterDimension(CONFIG_DIMENSION_ID_NEOHELL);
			}
			
			DimensionType neohellType = DimensionType.register("Neo-Hell", "_neohell", CONFIG_DIMENSION_ID_NEOHELL, WorldProviderNeoHell.class, false);
			DimensionManager.registerDimension(CONFIG_DIMENSION_ID_NEOHELL, neohellType);
			
			@SuppressWarnings("unused")
			WorldProvider provider = DimensionManager.createProviderFor(CONFIG_DIMENSION_ID_NEOHELL);
		}
		
		/* Obsidian is a glossy, vitreous rock, useful because when struck it easily forms conchoidal fractures,
		 * meaning you can hit it with an ordinary rock, and it shatters, making a super-sharp edge you can use for an
		 * axe head or a knife blade. It's frequently found
		 * just lying around volcanic islands. None of this describes what we see in Minecraft when we look at an
		 * obsidian block or the way we use that block.
		 */
		Blocks.OBSIDIAN.setUnlocalizedName("thermionics_world.basalt");
		Blocks.OBSIDIAN.setHardness(2.5f);
		Blocks.OBSIDIAN.setHarvestLevel("pickaxe",2);
		
		//Ender Chest and Enchantment Table should have the same hardness as raw Basalt
		Blocks.ENDER_CHEST.setHardness(2.5f);
		Blocks.ENCHANTING_TABLE.setHardness(2.5f);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);
		MinecraftForge.EVENT_BUS.register(TWBlocks.class);
		MinecraftForge.EVENT_BUS.register(TWItems.class);
		MinecraftForge.EVENT_BUS.register(BiomeFamily.class);
	}
	
	@SubscribeEvent
	public void onRegisterSounds(RegistryEvent.Register<SoundEvent> evt) {
		evt.getRegistry().register(SOUNDEVENT_SQUISH_STEP);
		evt.getRegistry().register(SOUNDEVENT_SQUISH_BREAK);
	}
	
	public static Consumer<ItemStack> oreDict(String ore) {
		return it->OreDictionary.registerOre(ore, it);
	}
	
	public static void forEachItem(Consumer<ItemStack> consumer, ItemStack... items) {
		for(ItemStack stack : items) {
			consumer.accept(stack);
		}
	}
	
	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> evt) {	
		IForgeRegistry<IRecipe> r = evt.getRegistry();
		
		for(EnumEdibleMeat meat : EnumEdibleMeat.values()) {
			ItemStack uncraftedRaw = meat.getRawItem().copy(); uncraftedRaw.setCount(9);
			ItemStack uncraftedCooked = meat.getCookedItem().copy(); uncraftedCooked.setCount(9);
			addMeatCompressionRecipe(r, meat, false, meat.getRawItem().copy());
			addMeatCompressionRecipe(r, meat, true,  meat.getCookedItem().copy());
			addMeatUncraftingRecipe(r, meat, false, uncraftedRaw);
			addMeatUncraftingRecipe(r, meat, true, uncraftedCooked);
			
			FurnaceRecipes.instance().addSmeltingRecipe(
					TWItems.meat(meat, false),
					TWItems.meat(meat, true),
					0.0f);
			
			Ingredient obsidian = Ingredient.fromStacks(new ItemStack(Blocks.OBSIDIAN));
			Ingredient flintSteel = Ingredient.fromStacks(new ItemStack(Items.FLINT_AND_STEEL));
			ShapedRecipes hellPortalRecipe = new ShapedRecipes("thermionics_world:portal.neohell", 3, 3,
					NonNullList.from(null,
							obsidian, obsidian, obsidian,
							obsidian, flintSteel, obsidian,
							obsidian, obsidian, obsidian
							),
					
					new ItemStack(TWBlocks.TELEPORT_NEOHELL)
					);
			hellPortalRecipe.setRegistryName(new ResourceLocation("thermionics_world", "portal.neohell.recipe"));
			r.register(hellPortalRecipe);
			
			Ingredient clay = Ingredient.fromStacks(new ItemStack(Items.CLAY_BALL));
			Ingredient gravel = Ingredient.fromStacks(new ItemStack(Blocks.GRAVEL));
			ShapedRecipes overworldPortalRecipe = new ShapedRecipes("thermionics_world:portal.overworld", 3, 3,
					NonNullList.from(null,
							clay, clay, clay,
							clay, gravel, clay,
							clay, clay, clay
							),
					
					new ItemStack(TWBlocks.TELEPORT_OVERWORLD)
					);
			overworldPortalRecipe.setRegistryName(new ResourceLocation("thermionics_world", "portal.overworld.recipe"));
			r.register(overworldPortalRecipe);
		}
		
		for(BlockGemrock block : TWBlocks.GROUP_GEMROCK) addBrickRecipes(r, block);
		
		//Norfairite can be dyed. This is surprisingly hard to get right.
		addDyeRecipes(r, TWBlocks.NORFAIRITE_CLEAR);
	}
	
	public static void addDyeRecipes(IForgeRegistry<IRecipe> registry, BlockColored block) {
		ResourceLocation group = new ResourceLocation("thermionics_world", "dye");
		for(EnumDyeColor dye : EnumDyeColor.values()) {
			ShapelessOreRecipe recipe =
					new ShapelessOreRecipe(group, new ItemStack(TWBlocks.NORFAIRITE_CLEAR,1,dye.getMetadata()),
					new ItemStack(Items.DYE,1,dye.getDyeDamage()),
					new ItemStack(TWBlocks.NORFAIRITE_CLEAR,1,OreDictionary.WILDCARD_VALUE));
			recipe.setRegistryName(new ResourceLocation("thermionics_world", block.getRegistryName().getResourcePath()+"_DyeTo_"+dye.getUnlocalizedName()) );
			registry.register(recipe);
		}
	}
	
	public static void addMeatCompressionRecipe(IForgeRegistry<IRecipe> registry, EnumEdibleMeat meat, boolean cooked, ItemStack ingredient) {
		ResourceLocation group = new ResourceLocation("thermionics_world", "compress.meat");
		Ingredient input = Ingredient.fromStacks(ingredient);
		ShapedRecipes recipe = 
				new ShapedRecipes(group.toString(), 3, 3,
				NonNullList.withSize(3*3, input),
				new ItemStack(TWBlocks.MEAT_EDIBLE, 1, BlockMeatEdible.getMetaFromValue(meat, cooked)) );
		recipe.setRegistryName(new ResourceLocation("thermionics_world", meat.getName()+((cooked)?".cooked":".raw")+"_CompressToBlock"));
		registry.register(recipe);
	}
	
	public static void addMeatUncraftingRecipe(IForgeRegistry<IRecipe> registry, EnumEdibleMeat meat, boolean cooked, ItemStack result) {
		ResourceLocation group = new ResourceLocation("thermionics_world", "uncompress.meat");
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(group,
				result,
				new ItemStack(TWBlocks.MEAT_EDIBLE, 1, BlockMeatEdible.getMetaFromValue(meat, cooked)) );
		recipe.setRegistryName(new ResourceLocation("thermionics_world", meat.getName()+((cooked)?".cooked":".raw")+"_DecompressFromBlock"));
		registry.register(recipe);
	}
	
	public static void addBrickRecipes(IForgeRegistry<IRecipe> registry, BlockGemrock gem) {
		ResourceLocation group = new ResourceLocation("thermionics_world", "gemrock.chisel."+gem.getUnlocalizedName());
		ShapedOreRecipe a = new ShapedOreRecipe(group,
				new ItemStack(gem, 4, 1),
				"xx", "xx", 'x', new ItemStack(gem,1,0)
				);
		a.setRegistryName(new ResourceLocation("thermionics_world", "gemrock.chisel.intoBrick."+gem.getUnlocalizedName()));
		registry.register(a);
		
		registry.register(recipe("gemrock.chisel."+gem.getUnlocalizedName(),
				new ItemStack(gem, 1, 2),
				new ItemStack(gem, 1, 1)
				));
		registry.register(recipe("gemrock.chisel."+gem.getUnlocalizedName(),
				new ItemStack(gem, 1, 3),
				new ItemStack(gem, 1, 2)
				));
		registry.register(recipe("gemrock.chisel."+gem.getUnlocalizedName(),
				new ItemStack(gem, 1, 4),
				new ItemStack(gem, 1, 3)
				));
		registry.register(recipe("gemrock.chisel."+gem.getUnlocalizedName(),
				new ItemStack(gem, 1, 1),
				new ItemStack(gem, 1, 4)
				));
		
	}
	
	public static IRecipe recipe(String group, ItemStack result, ItemStack ingredient) {
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ResourceLocation("thermionics_world",group), result, ingredient);
		recipe.setRegistryName("thermionics_world", ingredient.getItem().getRegistryName().getResourcePath()+"."+ingredient.getMetadata()+"_to_"+result.getUnlocalizedName()+"."+result.getMetadata());
		return recipe;
	}
	
	public static IRecipe recipe(String group, ItemStack result, ItemStack... ingredients) {
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ResourceLocation("thermionics_world",group), result, (Object[])ingredients);
		recipe.setRegistryName("thermionics_world", result.getUnlocalizedName()+"."+result.getMetadata());
		return recipe;
	}
	
	@SubscribeEvent
	public void cropGrowEvent(BlockEvent.CropGrowEvent.Pre event) {
		//IBlockState cropState = event.getState();
		Block land = event.getWorld().getBlockState(event.getPos().down()).getBlock();
		if (land instanceof BlockMeat) {
			//vanilla ticks give a 1 in 10 chance. We're forcing it on a 1 in 4, with the remaining 3 potentially ALSO
			//growing at the normal 10%. So this should be pretty fast.
			int growthChance = (int)(Math.random()*4);
			if (growthChance==0) event.setResult(Event.Result.ALLOW);
		}
	}
}
