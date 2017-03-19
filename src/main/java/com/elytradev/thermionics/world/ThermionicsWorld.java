/**
 * MIT License
 *
 * Copyright (c) 2017 Isaac Ellingson (Falkreon) and contributors
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.elytradev.thermionics.world.block.BlockFluidSimple;
import com.elytradev.thermionics.world.block.BlockGemrock;
import com.elytradev.thermionics.world.block.BlockMeat;
import com.elytradev.thermionics.world.block.BlockMeatEdible;
import com.elytradev.thermionics.world.block.EnumEdibleMeat;
import com.elytradev.thermionics.world.block.TerrainBlocks;
import com.elytradev.thermionics.world.gen.WorldProviderNeoHell;
import com.elytradev.thermionics.world.gen.biome.BiomeRegistry;
import com.elytradev.thermionics.world.gen.biome.NeoBiome;
import com.elytradev.thermionics.world.item.ItemBlockGemrock;
import com.elytradev.thermionics.world.item.ItemBlockMeatEdible;
import com.elytradev.thermionics.world.item.TWItems;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
	
	public static CreativeTabs TAB_THERMIONICS_WORLD = new CreativeTabs("thermionics_world") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(TerrainBlocks.GEMROCK_TOURMALINE, 1, 1);
		}
	};
	
	@SidedProxy(clientSide="com.elytradev.thermionics.world.ClientProxy", serverSide="com.elytradev.thermionics.world.Proxy")
	static Proxy proxy;
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.init();
		
		//Nuking hell just makes it stronger
		DimensionManager.unregisterDimension(-1);
		DimensionType neohellType = DimensionType.register("Neo-Hell", "_neohell", -1, WorldProviderNeoHell.class, false);
		
		DimensionManager.registerDimension(-1, neohellType);
		@SuppressWarnings("unused")
		WorldProvider provider = DimensionManager.createProviderFor(-1);
		
		//Breaking obsidian ironically makes it easier to collect
		/*
		try {
			BlockBasalt basalt = new BlockBasalt();
			//ItemBlockVarieties basaltItem = new ItemBlockVarieties(basalt);
			GameRegistry.addSubstitutionAlias("minecraft:obsidian", Type.BLOCK, basalt);
			//GameRegistry.addSubstitutionAlias("minecraft:obsidian", Type.ITEM, basaltItem);
			
			//proxy.registerItemModel(basaltItem);
		} catch (ExistingSubstitutionException e) {
			e.printStackTrace();
		}*/
		//Turns out this REALLY BROKE obsidian, way more than intended. So instead, until substitutionAlias becomes more stable:
		Blocks.OBSIDIAN.setUnlocalizedName("thermionics_world.basalt");
		Blocks.OBSIDIAN.setHardness(2.5f);
		Blocks.OBSIDIAN.setHarvestLevel("pickaxe",2);
		
		
		//You might want to look away for a second, we produce some fluids here
		Fluid soylentFluid = new Fluid("soylent",
				new ResourceLocation("thermionics_world", "blocks/fluids/soylent_still"),
				new ResourceLocation("thermionics_world", "blocks/fluids/soylent_flowing"))
				.setDensity(1500)
				.setLuminosity(13)
				.setRarity(EnumRarity.COMMON)
				.setTemperature(295)
				.setViscosity(1000);
		FluidRegistry.registerFluid(soylentFluid);
		
		
		BlockFluidSimple blockFluidSoylent = new BlockFluidSimple(soylentFluid, "soylent");
		GameRegistry.register(blockFluidSoylent);
		soylentFluid.setBlock(blockFluidSoylent);
		
		FluidRegistry.addBucketForFluid(soylentFluid);
		
		
		Fluid painFluid = new Fluid("pain",
				new ResourceLocation("thermionics_world", "blocks/fluids/pain"),
				new ResourceLocation("thermionics_world", "blocks/fluids/pain"))
				.setDensity(2000)
				.setLuminosity(15)
				.setRarity(EnumRarity.COMMON)
				/* 600 degrees kelvin is a "mere" 620 degrees fahrenheit. Even for the smartest thermopile block that
				 * never existed in modded minecraft, this would be very weak power in comparison to lava, which is
				 * 1300K, and yet scalding is way down at 322K/120F, so this fluid? This is a deadly fluid, causing
				 * first-degree burns on contact.
				 * 
				 * The fact that pain is hotter than the air temperature in neo-hell was a mystery for more than a year
				 * after it was discovered. The breakthrough happened with a sudden, unexplained migration of wolf
				 * spiders, which allowed researchers to "safely" study the dimension's temperature conditions more
				 * closely. It turns out that there are subtle air convection patterns which give rise to phenomena
				 * like the well-studied sulfurous vents. Convections suggest that not everywhere in neo-hell is the
				 * same temperature. In fact, from careful measurements, we estimate that there must be yet-undiscovered
				 * infernos of 1000K or more!
				 * 
				 * If you discover such a location, *do not approach*. Neo-hell has much higher levels of radioactive
				 * isotopes than the nether or the overworld, so it's likely that infernos are caused by subcritical
				 * fission. No currently-available protective gear can provide any kind of safety in such a region.
				 * Further, blazes thermoregulate, often favoring warmer zones, so it's likely also crawling with deadly
				 * monsters.
				 */
				.setTemperature(600)
				.setViscosity(1000);
		FluidRegistry.registerFluid(painFluid);
		
		BlockFluidSimple blockFluidPain = new BlockFluidSimple(painFluid, "pain");
		GameRegistry.register(blockFluidPain);
		painFluid.setBlock(blockFluidPain);
		
		FluidRegistry.addBucketForFluid(painFluid);
		
		//Gemrocks are not gems. But they're useful.
		ArrayList<BlockGemrock> gemrocks = new ArrayList<BlockGemrock>();
		gemrocks.add(new BlockGemrock("magnesite",   EnumDyeColor.WHITE));
		gemrocks.add(new BlockGemrock("garnet",      EnumDyeColor.ORANGE));
		gemrocks.add(new BlockGemrock("tourmaline",  EnumDyeColor.MAGENTA));
		gemrocks.add(new BlockGemrock("sapphire",    EnumDyeColor.LIGHT_BLUE));
		gemrocks.add(new BlockGemrock("heliodor",    EnumDyeColor.YELLOW));
		gemrocks.add(new BlockGemrock("peridot",     EnumDyeColor.LIME));
		gemrocks.add(new BlockGemrock("rosequartz",  EnumDyeColor.PINK));
		gemrocks.add(new BlockGemrock("hematite",    EnumDyeColor.GRAY));
		gemrocks.add(new BlockGemrock("opal",        EnumDyeColor.SILVER));
		gemrocks.add(new BlockGemrock("chrysoprase", EnumDyeColor.CYAN));
		gemrocks.add(new BlockGemrock("amethyst",    EnumDyeColor.PURPLE));
		gemrocks.add(new BlockGemrock("sodalite",    EnumDyeColor.BLUE));
		gemrocks.add(new BlockGemrock("pyrite",      EnumDyeColor.BROWN));
		gemrocks.add(new BlockGemrock("emerald",     EnumDyeColor.GREEN));
		gemrocks.add(new BlockGemrock("spinel",      EnumDyeColor.RED));
		gemrocks.add(new BlockGemrock("cassiterite", EnumDyeColor.BLACK));
		for(BlockGemrock block : gemrocks) {
			GameRegistry.register(block);
			ItemBlockGemrock item = new ItemBlockGemrock(block);
			GameRegistry.register(item);
			proxy.registerItemModel(item);
		}
		
		
		//We're back in weird territory here, with Several Kinds of Meat
		GameRegistry.register(SOUNDEVENT_SQUISH_STEP);
		GameRegistry.register(SOUNDEVENT_SQUISH_BREAK);
		
		
		Block edibleMeat = new BlockMeatEdible();
		GameRegistry.register(edibleMeat);
		ItemBlockMeatEdible edibleMeatBlockItem = new ItemBlockMeatEdible(edibleMeat);
		GameRegistry.register(edibleMeatBlockItem);
		proxy.registerItemModel(edibleMeatBlockItem);
		
		TWItems.GROUP_MEAT_RAW = ImmutableList.of(
				new ItemStack(Items.PORKCHOP),
				new ItemStack(Items.BEEF),
				new ItemStack(Items.CHICKEN),
				new ItemStack(Items.FISH,1,0),
				new ItemStack(Items.FISH,1,1),
				new ItemStack(Items.MUTTON),
				new ItemStack(Items.RABBIT)
				);
		
		TWItems.GROUP_MEAT_COOKED = ImmutableList.of(
				new ItemStack(Items.COOKED_PORKCHOP),
				new ItemStack(Items.COOKED_BEEF),
				new ItemStack(Items.COOKED_CHICKEN),
				new ItemStack(Items.COOKED_FISH,1,0),
				new ItemStack(Items.COOKED_FISH,1,1),
				new ItemStack(Items.COOKED_MUTTON),
				new ItemStack(Items.COOKED_RABBIT)
				);
		
		TWItems.GROUP_BLOCK_MEAT_RAW = ImmutableList.of(
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.PORK,    false)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.BEEF,    false)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.CHICKEN, false)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.FISH,    false)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.SALMON,  false)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.MUTTON,  false)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.RABBIT,  false))
				);
		
		TWItems.GROUP_BLOCK_MEAT_COOKED = ImmutableList.of(
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.PORK,    true)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.BEEF,    true)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.CHICKEN, true)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.FISH,    true)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.SALMON,  true)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.MUTTON,  true)),
				new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.RABBIT,  true))
				);
		
		//Category registrations (and some HarvestCraft-style registrations to keep compatibiltiy high)
		forEachItem(oreDict("listAllRawMeat"),    TWItems.GROUP_MEAT_RAW);    forEachItem(oreDict("listAllmeatraw"),    TWItems.GROUP_MEAT_RAW);
		forEachItem(oreDict("listAllCookedMeat"), TWItems.GROUP_MEAT_COOKED); forEachItem(oreDict("listAllmeatcooked"), TWItems.GROUP_MEAT_COOKED);
		forEachItem(oreDict("listAllMeat"),       TWItems.GROUP_MEAT_RAW, TWItems.GROUP_MEAT_COOKED);
		
		forEachItem(oreDict("listAllBlockRawMeat"),    TWItems.GROUP_BLOCK_MEAT_RAW);
		forEachItem(oreDict("listAllBlockCookedMeat"), TWItems.GROUP_BLOCK_MEAT_COOKED);
		forEachItem(oreDict("listAllBlockMeat"),       TWItems.GROUP_BLOCK_MEAT_RAW, TWItems.GROUP_BLOCK_MEAT_COOKED);
		
		//These are the most specific (and probably the most useful) entries.
		OreDictionary.registerOre("blockRawPorkchop", new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.PORK,    false)));
		OreDictionary.registerOre("blockRawBeef",     new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.BEEF,    false)));
		OreDictionary.registerOre("blockRawChicken",  new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.CHICKEN, false)));
		OreDictionary.registerOre("blockRawFish",     new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.FISH,    false)));
		OreDictionary.registerOre("blockRawSalmon",   new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.SALMON,  false)));
		OreDictionary.registerOre("blockRawMutton",   new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.MUTTON,  false)));
		OreDictionary.registerOre("blockRawRabbit",   new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.RABBIT,  false)));
		
		OreDictionary.registerOre("blockCookedPorkchop", new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.PORK,    true)));
		OreDictionary.registerOre("blockCookedBeef",     new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.BEEF,    true)));
		OreDictionary.registerOre("blockCookedChicken",  new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.CHICKEN, true)));
		OreDictionary.registerOre("blockCookedFish",     new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.FISH,    true)));
		OreDictionary.registerOre("blockCookedSalmon",   new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.SALMON,  true)));
		OreDictionary.registerOre("blockCookedMutton",   new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.MUTTON,  true)));
		OreDictionary.registerOre("blockCookedRabbit",   new ItemStack(edibleMeat, 1, BlockMeatEdible.getMetaFromValue(EnumEdibleMeat.RABBIT,  true)));

		MinecraftForge.EVENT_BUS.register(this);
		
		System.out.println("END PREINIT");
	}
	
	public static Consumer<ItemStack> oreDict(String ore) {
		return it->OreDictionary.registerOre(ore, it);
	}
	
	public static void forEachItem(Consumer<ItemStack> consumer, ItemStack... items) {
		for(ItemStack stack : items) {
			consumer.accept(stack);
		}
	}
	
	@SafeVarargs
	public static void forEachItem(Consumer<ItemStack> consumer, List<ItemStack>... lists) {
		for(List<ItemStack> list : lists) {
			for(ItemStack stack : list) {
				consumer.accept(stack);
			}
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		//System.out.println("BEGIN INIT");
		
		//Craft meat into blocks
		addMeatCompressionRecipe(EnumEdibleMeat.PORK,    false, new ItemStack(Items.PORKCHOP));
		addMeatCompressionRecipe(EnumEdibleMeat.BEEF,    false, new ItemStack(Items.BEEF));
		addMeatCompressionRecipe(EnumEdibleMeat.CHICKEN, false, new ItemStack(Items.CHICKEN));
		addMeatCompressionRecipe(EnumEdibleMeat.FISH,    false, new ItemStack(Items.FISH,1,0));
		addMeatCompressionRecipe(EnumEdibleMeat.SALMON,  false, new ItemStack(Items.FISH,1,1));
		addMeatCompressionRecipe(EnumEdibleMeat.MUTTON,  false, new ItemStack(Items.MUTTON));
		addMeatCompressionRecipe(EnumEdibleMeat.RABBIT,  false, new ItemStack(Items.RABBIT));
		
		addMeatCompressionRecipe(EnumEdibleMeat.PORK,    true,  new ItemStack(Items.COOKED_PORKCHOP));
		addMeatCompressionRecipe(EnumEdibleMeat.BEEF,    true,  new ItemStack(Items.COOKED_BEEF));
		addMeatCompressionRecipe(EnumEdibleMeat.CHICKEN, true,  new ItemStack(Items.COOKED_CHICKEN));
		addMeatCompressionRecipe(EnumEdibleMeat.FISH,    true,  new ItemStack(Items.COOKED_FISH,1,0));
		addMeatCompressionRecipe(EnumEdibleMeat.SALMON,  true,  new ItemStack(Items.COOKED_FISH,1,1));
		addMeatCompressionRecipe(EnumEdibleMeat.MUTTON,  true,  new ItemStack(Items.COOKED_MUTTON));
		addMeatCompressionRecipe(EnumEdibleMeat.RABBIT,  true,  new ItemStack(Items.COOKED_RABBIT));
		
		//Uncraft blocks into meat
		addMeatUncraftingRecipe(EnumEdibleMeat.PORK,     false, new ItemStack(Items.PORKCHOP, 9));
		addMeatUncraftingRecipe(EnumEdibleMeat.BEEF,     false, new ItemStack(Items.BEEF,     9));
		addMeatUncraftingRecipe(EnumEdibleMeat.CHICKEN,  false, new ItemStack(Items.CHICKEN,  9));
		addMeatUncraftingRecipe(EnumEdibleMeat.FISH,     false, new ItemStack(Items.FISH,     9, 0));
		addMeatUncraftingRecipe(EnumEdibleMeat.SALMON,   false, new ItemStack(Items.FISH,     9, 1));
		addMeatUncraftingRecipe(EnumEdibleMeat.MUTTON,   false, new ItemStack(Items.MUTTON,   9));
		addMeatUncraftingRecipe(EnumEdibleMeat.RABBIT,   false, new ItemStack(Items.RABBIT,   9));
		
		addMeatUncraftingRecipe(EnumEdibleMeat.PORK,     true,  new ItemStack(Items.COOKED_PORKCHOP, 9));
		addMeatUncraftingRecipe(EnumEdibleMeat.BEEF,     true,  new ItemStack(Items.COOKED_BEEF,     9));
		addMeatUncraftingRecipe(EnumEdibleMeat.CHICKEN,  true,  new ItemStack(Items.COOKED_CHICKEN,  9));
		addMeatUncraftingRecipe(EnumEdibleMeat.FISH,     true,  new ItemStack(Items.COOKED_FISH,     9, 0));
		addMeatUncraftingRecipe(EnumEdibleMeat.SALMON,   true,  new ItemStack(Items.COOKED_FISH,     9, 1));
		addMeatUncraftingRecipe(EnumEdibleMeat.MUTTON,   true,  new ItemStack(Items.COOKED_MUTTON,   9));
		addMeatUncraftingRecipe(EnumEdibleMeat.RABBIT,   true,  new ItemStack(Items.COOKED_RABBIT,   9));
		
		//Smelt raw meat blocks into cooked meat blocks
		for(EnumEdibleMeat meat : EnumEdibleMeat.values()) {
			FurnaceRecipes.instance().addSmeltingRecipe(
					TWItems.meat(meat, false),
					TWItems.meat(meat, true),
					0.0f);
		}
		
		
		//Hellmeat is very juicy meat. But if you just need to get rid of the juice, you could always... cook it?
		//NonNullList<ItemStack> rottenFleshBlocks = OreDictionary.getOres("blockRottenFlesh");
		//if (!rottenFleshBlocks.isEmpty()) {
		//	FurnaceRecipes.instance().addSmelting(ItemBlock.getItemFromBlock(TerrainBlocks.MEAT_FLESH), rottenFleshBlocks.get(0), 0);
		//}
	}
	
	public void addMeatCompressionRecipe(EnumEdibleMeat meat, boolean cooked, Object ingredient) {
		GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(TerrainBlocks.MEAT_EDIBLE, 1, BlockMeatEdible.getMetaFromValue(meat, cooked)),
				"xxx", "xxx", "xxx", 'x', ingredient
				));
	}
	
	public void addMeatUncraftingRecipe(EnumEdibleMeat meat, boolean cooked, ItemStack result) {
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				result,
				new ItemStack(TerrainBlocks.MEAT_EDIBLE, 1, BlockMeatEdible.getMetaFromValue(meat, cooked))
				));
	}
	
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
		if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) return;
		
		if (Minecraft.getMinecraft().world.provider.getDimension()!=-1) return;
		
		
		ArrayList<String> left = event.getLeft();
		final int LINE_BIOME = 11;
		if (left.size()<=LINE_BIOME || !left.get(LINE_BIOME).startsWith("Biome:")) return; //Someone's already tampered

		
		String biomeName = "MEMES(NULL)";
		BlockPos pos = Minecraft.getMinecraft().player.getPosition();
		Chunk chunk = Minecraft.getMinecraft().world.getChunkFromBlockCoords(pos);
		byte[] biomeArray = chunk.getBiomeArray();
		int x = pos.getX() & 15;
		int z = pos.getZ() & 15;
		int biomeId = biomeArray[z*16+x];
		NeoBiome biome = BiomeRegistry.NEO_HELL.getObjectById(biomeId);
		if (biome!=null) biomeName = biome.getBiomeName();
		
		left.set(LINE_BIOME, "Biome: "+biomeId+" ("+biomeName+")");
	}
	
	@SubscribeEvent
	public void cropGrowEvent(BlockEvent.CropGrowEvent.Pre event) {
		IBlockState cropState = event.getState();
		Block land = event.getWorld().getBlockState(event.getPos().down()).getBlock();
		if (land instanceof BlockMeat) {
			//vanilla ticks give a 1 in 10 chance. We're forcing it on a 1 in 4, with the remaining 3 potentially ALSO
			//growing at the normal 10%. So this should be pretty fast.
			int growthChance = (int)(Math.random()*4);
			if (growthChance==0) event.setResult(Event.Result.ALLOW);
		}
	}
}
