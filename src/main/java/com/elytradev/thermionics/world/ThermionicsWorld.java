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

import com.elytradev.thermionics.world.block.BlockBasalt;
import com.elytradev.thermionics.world.block.BlockFluidSimple;
import com.elytradev.thermionics.world.block.BlockGemrock;
import com.elytradev.thermionics.world.gen.WorldProviderNeoHell;
import com.elytradev.thermionics.world.item.ItemBlockGemrock;
import com.elytradev.thermionics.world.item.ItemBlockVarieties;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;

@Mod(modid = "thermionics_world", name="Thermionics|World", version="@VERSION@")
public class ThermionicsWorld {
	
	static {
		//Don't kill me. I swear the javadocs say to do this.
		FluidRegistry.enableUniversalBucket();
	}
	
	public static CreativeTabs TAB_THERMIONICS_WORLD = new CreativeTabs("thermionics_world") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.NETHERRACK);
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
		WorldProvider provider = DimensionManager.createProviderFor(-1);
		System.out.println(provider.getDimensionType());
		
		//Breaking obsidian ironically makes it easier to collect
		try {
			BlockBasalt basalt = new BlockBasalt();
			ItemBlockVarieties basaltItem = new ItemBlockVarieties(basalt);
			GameRegistry.addSubstitutionAlias("minecraft:obsidian", Type.BLOCK, basalt);
			GameRegistry.addSubstitutionAlias("minecraft:obsidian", Type.ITEM, basaltItem);
			
			proxy.registerItemModel(basaltItem);
		} catch (ExistingSubstitutionException e) {
			e.printStackTrace();
		}
		
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
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
}
