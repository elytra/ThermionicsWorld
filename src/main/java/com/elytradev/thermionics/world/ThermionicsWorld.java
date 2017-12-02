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
import java.util.function.Consumer;

import com.elytradev.thermionics.world.block.BlockGemrock;
import com.elytradev.thermionics.world.block.BlockMeat;
import com.elytradev.thermionics.world.block.BlockMeatEdible;
import com.elytradev.thermionics.world.block.EnumEdibleMeat;
import com.elytradev.thermionics.world.block.TWBlocks;
import com.elytradev.thermionics.world.gen.WorldProviderNeoHell;
import com.elytradev.thermionics.world.gen.biome.BiomeRegistry;
import com.elytradev.thermionics.world.gen.biome.NeoBiome;
import com.elytradev.thermionics.world.item.TWItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
		proxy.init();
		
		//Nuking hell just makes it stronger
		DimensionManager.unregisterDimension(-1);
		DimensionType neohellType = DimensionType.register("Neo-Hell", "_neohell", -1, WorldProviderNeoHell.class, false);
		
		DimensionManager.registerDimension(-1, neohellType);
		@SuppressWarnings("unused")
		WorldProvider provider = DimensionManager.createProviderFor(-1);
		
		/* Obsidian is a glossy, vitreous rock, useful because when struck it easily forms conchoidal fractures,
		 * meaning you can hit it with an ordinary rock, and it shatters, making a super-sharp edge you can use for an
		 * axe head or a knife blade. It's frequently found
		 * just lying around volcanic islands. None of this describes what we see in Minecraft when we look at an
		 * obsidian block or the way we use that block.
		 * 
		 * 
		 * 
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
		MinecraftForge.EVENT_BUS.register(BiomeRegistry.class);
		
		/*
		ImmutableList.Builder<ItemStack> gemrockBuilder = ImmutableList.builder();
		ImmutableList.Builder<BlockGemrock> gemrockBlockBuilder = ImmutableList.builder();
		for(BlockGemrock block : gemrocks) {
			gemrockBlockBuilder.add(block);
			gemrockBuilder.add(new ItemStack(block));
		}
		TWItems.GROUP_GEMROCK = gemrockBuilder.build();
		TWBlocks.GROUP_GEMROCK = gemrockBlockBuilder.build();
		*/
		
		//We're back in weird territory here, with Several Kinds of Meat
		
		
		

		//ItemBlockMeatEdible edibleMeatBlockItem = new ItemBlockMeatEdible(edibleMeat);
		/*
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
				);*/
		
		//Category registrations (and some HarvestCraft-style registrations to keep compatibiltiy high)
		/*
		TWItems.GROUP_MEAT_RAW.forEach(it->OreDictionary.registerOre("listAllRawMeat", it));
		forEach(oreDict("listAllRawMeat"),    TWItems.GROUP_MEAT_RAW);    forEach(oreDict("listAllmeatraw"),    TWItems.GROUP_MEAT_RAW);
		forEach(oreDict("listAllCookedMeat"), TWItems.GROUP_MEAT_COOKED); forEach(oreDict("listAllmeatcooked"), TWItems.GROUP_MEAT_COOKED);
		forEach(oreDict("listAllMeat"),       TWItems.GROUP_MEAT_RAW, TWItems.GROUP_MEAT_COOKED);
		
		forEach(oreDict("listAllBlockRawMeat"),    TWItems.GROUP_BLOCK_MEAT_RAW);
		forEach(oreDict("listAllBlockCookedMeat"), TWItems.GROUP_BLOCK_MEAT_COOKED);
		forEach(oreDict("listAllBlockMeat"),       TWItems.GROUP_BLOCK_MEAT_RAW, TWItems.GROUP_BLOCK_MEAT_COOKED);
		
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
		*/
		
		
		System.out.println("END PREINIT");
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
	/*
	@SafeVarargs
	public static <T> void forEach(Consumer<T> consumer, T... ts) {
		for(T t : ts) { consumer.accept(t); }
	}
	
	@SafeVarargs
	public static <T> void forEach(Consumer<T> consumer, List<T>... lists) {
		for(List<T> list : lists) {
			for(T t : list) {
				consumer.accept(t);
			}
		}
	}*/
	
	/*
	@SafeVarargs
	public static void forEachItem(Consumer<ItemStack> consumer, List<ItemStack>... lists) {
		for(List<ItemStack> list : lists) {
			for(ItemStack stack : list) {
				consumer.accept(stack);
			}
		}
	}*/
	/*
	@SubscribeEvent
	public void onEntityFall(LivingFallEvent evt) {
		handleFallingEntity(evt.getEntityLiving());
	}
	
	@SubscribeEvent
	public void onFlyableFall(PlayerFlyableFallEvent evt) {
		handleFallingEntity(evt.getEntityPlayer());
	}
	
	public void handleFallingEntity(EntityLivingBase entity) {
		if (entity==null) return;
		World world = entity.getEntityWorld();
		BlockPos pos = entity.getPosition();
		IBlockState blockAtEntity = world.getBlockState(pos);
		if (blockAtEntity==null || blockAtEntity.getBlock()==null) return;
		
		if (blockAtEntity.getBlock()==TerrainBlocks.FLUID_PAIN) {
			if (entity instanceof EntityPlayer) {
				System.out.println("Slowing down entity motion:"+entity.motionY);
			}
			
			if (entity.motionY<-0.1d) entity.motionY += 0.1d;
			
			if (entity instanceof EntityPlayer) {
				System.out.println("After slowdown:"+entity.motionY);
			}
		}
	}*/
	//@EventHandler
	//public void init(FMLInitializationEvent event) {
		//BiomeRegistry.NEO_HELL.init();
	//}
	
	//@EventHandler
	//public void init(FMLInitializationEvent event) {
		//System.out.println("BEGIN INIT");
	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> evt) {	
		IForgeRegistry<IRecipe> r = evt.getRegistry();
		
		//Craft meat into blocks
		addMeatCompressionRecipe(r, EnumEdibleMeat.PORK,    false, new ItemStack(Items.PORKCHOP));
		addMeatCompressionRecipe(r, EnumEdibleMeat.BEEF,    false, new ItemStack(Items.BEEF));
		addMeatCompressionRecipe(r, EnumEdibleMeat.CHICKEN, false, new ItemStack(Items.CHICKEN));
		addMeatCompressionRecipe(r, EnumEdibleMeat.FISH,    false, new ItemStack(Items.FISH,1,0));
		addMeatCompressionRecipe(r, EnumEdibleMeat.SALMON,  false, new ItemStack(Items.FISH,1,1));
		addMeatCompressionRecipe(r, EnumEdibleMeat.MUTTON,  false, new ItemStack(Items.MUTTON));
		addMeatCompressionRecipe(r, EnumEdibleMeat.RABBIT,  false, new ItemStack(Items.RABBIT));
		
		addMeatCompressionRecipe(r, EnumEdibleMeat.PORK,    true,  new ItemStack(Items.COOKED_PORKCHOP));
		addMeatCompressionRecipe(r, EnumEdibleMeat.BEEF,    true,  new ItemStack(Items.COOKED_BEEF));
		addMeatCompressionRecipe(r, EnumEdibleMeat.CHICKEN, true,  new ItemStack(Items.COOKED_CHICKEN));
		addMeatCompressionRecipe(r, EnumEdibleMeat.FISH,    true,  new ItemStack(Items.COOKED_FISH,1,0));
		addMeatCompressionRecipe(r, EnumEdibleMeat.SALMON,  true,  new ItemStack(Items.COOKED_FISH,1,1));
		addMeatCompressionRecipe(r, EnumEdibleMeat.MUTTON,  true,  new ItemStack(Items.COOKED_MUTTON));
		addMeatCompressionRecipe(r, EnumEdibleMeat.RABBIT,  true,  new ItemStack(Items.COOKED_RABBIT));
		
		//Uncraft blocks into meat
		addMeatUncraftingRecipe(r, EnumEdibleMeat.PORK,     false, new ItemStack(Items.PORKCHOP, 9));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.BEEF,     false, new ItemStack(Items.BEEF,     9));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.CHICKEN,  false, new ItemStack(Items.CHICKEN,  9));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.FISH,     false, new ItemStack(Items.FISH,     9, 0));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.SALMON,   false, new ItemStack(Items.FISH,     9, 1));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.MUTTON,   false, new ItemStack(Items.MUTTON,   9));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.RABBIT,   false, new ItemStack(Items.RABBIT,   9));
		
		addMeatUncraftingRecipe(r, EnumEdibleMeat.PORK,     true,  new ItemStack(Items.COOKED_PORKCHOP, 9));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.BEEF,     true,  new ItemStack(Items.COOKED_BEEF,     9));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.CHICKEN,  true,  new ItemStack(Items.COOKED_CHICKEN,  9));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.FISH,     true,  new ItemStack(Items.COOKED_FISH,     9, 0));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.SALMON,   true,  new ItemStack(Items.COOKED_FISH,     9, 1));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.MUTTON,   true,  new ItemStack(Items.COOKED_MUTTON,   9));
		addMeatUncraftingRecipe(r, EnumEdibleMeat.RABBIT,   true,  new ItemStack(Items.COOKED_RABBIT,   9));
		
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
		recipe.setRegistryName(new ResourceLocation("thermionics_world", meat.getName()+"_CompressToBlock"));
		registry.register(recipe);
	}
	
	public static void addMeatUncraftingRecipe(IForgeRegistry<IRecipe> registry, EnumEdibleMeat meat, boolean cooked, ItemStack result) {
		ResourceLocation group = new ResourceLocation("thermionics_world", "uncompress.meat");
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(group,
				result,
				new ItemStack(TWBlocks.MEAT_EDIBLE, 1, BlockMeatEdible.getMetaFromValue(meat, cooked)) );
		recipe.setRegistryName(new ResourceLocation("thermionics_world", meat.getName()+"_DecompressFromBlock"));
		registry.register(recipe);
	}
	
	public static void addBrickRecipes(IForgeRegistry<IRecipe> registry, BlockGemrock gem) {
		ResourceLocation group = new ResourceLocation("thermionics_world", "gemrock.chisel");
		ShapedOreRecipe a = new ShapedOreRecipe(group,
				new ItemStack(gem, 4, 1),
				"xx", "xx", 'x', new ItemStack(gem,1,0)
				);
		a.setRegistryName(new ResourceLocation("thermionics_world", "gemrock.chisel.intoBrick"));
		registry.register(a);
		
		registry.register(recipe("gemrock.chisel",
				new ItemStack(gem, 1, 2),
				new ItemStack(gem, 1, 1)
				));
		registry.register(recipe("gemrock.chisel",
				new ItemStack(gem, 1, 3),
				new ItemStack(gem, 1, 2)
				));
		registry.register(recipe("gemrock.chisel",
				new ItemStack(gem, 1, 4),
				new ItemStack(gem, 1, 3)
				));
		registry.register(recipe("gemrock.chisel",
				new ItemStack(gem, 1, 1),
				new ItemStack(gem, 1, 4)
				));
		
	}
	
	public static IRecipe recipe(String group, ItemStack result, ItemStack ingredient) {
		ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ResourceLocation("thermionics_world",group), result, ingredient);
		recipe.setRegistryName("thermionics_world", ingredient.getItem().getRegistryName().getResourcePath()+"_to_"+result.getUnlocalizedName());
		return recipe;
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
