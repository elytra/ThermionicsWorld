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
package com.elytradev.thermionics.world.block;

import java.util.List;

import com.elytradev.thermionics.world.gen.biome.BiomeRegistry;
import com.elytradev.thermionics.world.item.ItemBlockGemrock;
import com.elytradev.thermionics.world.item.TWItems;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class TWBlocks {
	public static List<BlockGemrock> GROUP_GEMROCK;
	
	
	public static BlockFluidSimple FLUID_PAIN;
	public static BlockFluidSimple FLUID_SOYLENT;

	public static BlockGemrock     GEMROCK_MAGNESITE;
	public static BlockGemrock     GEMROCK_GARNET;
	public static BlockGemrock     GEMROCK_TOURMALINE;
	public static BlockGemrock     GEMROCK_SAPPHIRE;
	public static BlockGemrock     GEMROCK_HELIODOR;
	public static BlockGemrock     GEMROCK_PERIDOT;
	public static BlockGemrock     GEMROCK_ROSE_QUARTZ;
	public static BlockGemrock     GEMROCK_HEMATITE;
	public static BlockGemrock     GEMROCK_OPAL;
	public static BlockGemrock     GEMROCK_CHRYSOPRASE;
	public static BlockGemrock     GEMROCK_AMETHYST;
	public static BlockGemrock     GEMROCK_SODALITE;
	public static BlockGemrock     GEMROCK_PYRITE;
	public static BlockGemrock     GEMROCK_EMERALD;
	public static BlockGemrock     GEMROCK_SPINEL;
	public static BlockGemrock     GEMROCK_CASSITERITE;
	
	public static BlockMeat        MEAT_EDIBLE;
	public static BlockMeat        MEAT_FLESH;
	
	public static BlockShrubBone   SHRUB_BONE;
	public static BlockNorfairite  NORFAIRITE_CLEAR;
	
	
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> r = event.getRegistry();
		
		Fluid soylentFluid = new Fluid("soylent",
				new ResourceLocation("thermionics_world", "blocks/fluids/soylent_still"),
				new ResourceLocation("thermionics_world", "blocks/fluids/soylent_flowing"))
				.setDensity(1500)
				.setLuminosity(13)
				.setRarity(EnumRarity.COMMON)
				.setTemperature(295)
				.setViscosity(1000);
		FLUID_SOYLENT = fluidBlock(r, soylentFluid);
		
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
		FLUID_PAIN = fluidBlock(r, painFluid);
		
		
		//Gemrocks are not gems. But they're useful.
		GEMROCK_MAGNESITE  = block(r, new BlockGemrock("magnesite",   EnumDyeColor.WHITE));
		GEMROCK_GARNET     = block(r, new BlockGemrock("garnet",      EnumDyeColor.ORANGE));
		GEMROCK_TOURMALINE = block(r, new BlockGemrock("tourmaline",  EnumDyeColor.MAGENTA));
		GEMROCK_SAPPHIRE   = block(r, new BlockGemrock("sapphire",    EnumDyeColor.LIGHT_BLUE));
		GEMROCK_HELIODOR   = block(r, new BlockGemrock("heliodor",    EnumDyeColor.YELLOW));
		GEMROCK_PERIDOT    = block(r, new BlockGemrock("peridot",     EnumDyeColor.LIME));
		GEMROCK_ROSE_QUARTZ= block(r, new BlockGemrock("rosequartz",  EnumDyeColor.PINK));
		GEMROCK_HEMATITE   = block(r, new BlockGemrock("hematite",    EnumDyeColor.GRAY));
		GEMROCK_OPAL       = block(r, new BlockGemrock("opal",        EnumDyeColor.SILVER));
		GEMROCK_CHRYSOPRASE= block(r, new BlockGemrock("chrysoprase", EnumDyeColor.CYAN));
		GEMROCK_AMETHYST   = block(r, new BlockGemrock("amethyst",    EnumDyeColor.PURPLE));
		GEMROCK_SODALITE   = block(r, new BlockGemrock("sodalite",    EnumDyeColor.BLUE));
		GEMROCK_PYRITE     = block(r, new BlockGemrock("pyrite",      EnumDyeColor.BROWN));
		GEMROCK_EMERALD    = block(r, new BlockGemrock("emerald",     EnumDyeColor.GREEN));
		GEMROCK_SPINEL     = block(r, new BlockGemrock("spinel",      EnumDyeColor.RED));
		GEMROCK_CASSITERITE= block(r, new BlockGemrock("cassiterite", EnumDyeColor.BLACK));
		
		GROUP_GEMROCK = ImmutableList.of(
				GEMROCK_MAGNESITE,   GEMROCK_GARNET,   GEMROCK_TOURMALINE,
				GEMROCK_SAPPHIRE,    GEMROCK_HELIODOR, GEMROCK_PERIDOT,
				GEMROCK_ROSE_QUARTZ, GEMROCK_HEMATITE, GEMROCK_OPAL,
				GEMROCK_CHRYSOPRASE, GEMROCK_AMETHYST, GEMROCK_SODALITE,
				GEMROCK_PYRITE,      GEMROCK_EMERALD,  GEMROCK_SPINEL,
				GEMROCK_CASSITERITE );
		
		MEAT_EDIBLE = block(r, new BlockMeatEdible());
		
		//What are you saying? Bones aren't shrubs?!? Nonsense!
		SHRUB_BONE = block(r, new BlockShrubBone());
		
		//Bubble mountain ain't gonna attack itself.
		NORFAIRITE_CLEAR = block(r, new BlockNorfairite("clear"));
		
		//We know enough to make these registrations now!
		BiomeRegistry.NEO_HELL.init();
	}
	
	public static <B extends Block> B block(IForgeRegistry<Block> registry, B b) {
		registry.register(b);
		TWItems.scheduleForItem(b);
		return b;
	}
	
	public static BlockFluidSimple fluidBlock(IForgeRegistry<Block> registry, Fluid f) {
		FluidRegistry.registerFluid(f);
		BlockFluidSimple result = block(registry, new BlockFluidSimple(f, f.getName()));
		f.setBlock(result);
		FluidRegistry.addBucketForFluid(f);
		return result;
	}
}
