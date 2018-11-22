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

package com.elytradev.thermionics.world.gen.biome;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import com.elytradev.thermionics.world.block.TWBlocks;
import com.google.common.collect.HashBiMap;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BiomeFamily {
	public static BiomeFamily NEO_HELL = new BiomeFamily();
	public static BiomeFamily SEDNA = new BiomeFamily();
	
	private static HashMap<String, BufferedImage> imageCache = new HashMap<>();
	private static BufferedImage errorImage = new BufferedImage(2,2,BufferedImage.TYPE_INT_ARGB);
	static {
		//Setup error image with an obvious checkerboard pattern
		//TODO: Maybe scale up to larger dimensions to prevent the image from appearing smooth in realized terrain?
		errorImage.setRGB(0, 0, 0xFF_FFFFFF); errorImage.setRGB(1, 0, 0xFF_000000);
		errorImage.setRGB(0, 1, 0xFF_000000); errorImage.setRGB(1, 1, 0xFF_FFFFFF);
	}
	
	public static BufferedImage unpackTerrainImage(String id) {
		synchronized(imageCache) {
			if (imageCache.containsKey(id)) return imageCache.get(id);
			
			InputStream stream = BiomeFamily.class.getResourceAsStream("/assets/thermionics_world/terrain/"+id+".png");
			if (stream==null) {
				imageCache.put(id, errorImage);
				return errorImage;
			}
			try {
				BufferedImage result = ImageIO.read(stream);
				imageCache.put(id, result);
				return result;
			} catch (Throwable t) {
				imageCache.put(id, errorImage);
				return errorImage;
			}
		}
	}
	
	protected HashBiMap<Integer, ICompositorBiome> registry = HashBiMap.create();
	protected BiomeMap<ICompositorBiome> biomeMap = new BiomeMap<>();
	
	
	
	//= new BiomeMap<>(CompositorBiomes.NEO_HELL.getObject("bridges")).setSize(3, 3);
	
	/* Best compromise I can make: Vanilla keeps the en_US biome name in BiomeProperties, instead of a localization
	 * key. This is because vanilla *only* uses it for F3 debug output. Mods, however, may use it for many more things,
	 * so regardless of whether we're client- or server-side we want to provide a best-effort localized string. On the
	 * server that'll wind up being en_US because for some stupid reason the server always runs in en_US, but we'll get
	 * properly-localized names on the client which will be stuffed into the F3 menu if it's up and untampered-with.
	 */
	public static void init() {
		//Grab image resources
		//BufferedImage rough = unpackTerrainImage("rough");
		
		NEO_HELL.biomeMap.setSize(3, 3);
		
		NEO_HELL.register(0, new BiomeBridges());
		//NEO_HELL.register(1, new HellCompositorBiome("strata"));
		//NEO_HELL.register(2, new HellCompositorBiome("cold"));
		//NEO_HELL.register(3, new HellCompositorBiome("barad_dur"));
		//NEO_HELL.register(4, new HellCompositorBiome("heartsblood"));
		//NEO_HELL.register(5, new HellCompositorBiome("sulfur"));
		//NEO_HELL.register(6, new HellCompositorBiome("nocturne"));
		//NEO_HELL.register(7, new HellCompositorBiome("doom"));
		
		//Individual blocks and ABs
		NEO_HELL.register(1, new BiomeBubbleMountain());
		/*
		HellCompositorBiome strata = new HellCompositorBiome("strata");
		strata.topBlock = TWBlocks.NORFAIRITE_REEF.getDefaultState().withProperty(BlockNorfairite.COLOR, EnumDyeColor.PURPLE);
		strata.fillerBlock = TWBlocks.GEMROCK_ROSE_QUARTZ.getDefaultState();
		strata.a = 0;
		strata.b = 1;
		NEO_HELL.register(1, strata);*/
		
		
		NEO_HELL.register(2, new BiomeCold());
		/*
		HellCompositorBiome cold = new HellCompositorBiome("cold");
		cold.topBlock = TWBlocks.GEMROCK_MAGNESITE.getDefaultState();
		cold.fillerBlock = TWBlocks.GEMROCK_SAPPHIRE.getDefaultState();
		cold.a = 1;
		cold.b = 0;
		NEO_HELL.register(2, cold);*/
		
		NEO_HELL.register(3, new BiomeBaradDur());
		/*
		HellCompositorBiome baradDur = new HellCompositorBiome("barad_dur");
		baradDur.topBlock = TWBlocks.GEMROCK_HEMATITE.getDefaultState();
		baradDur.fillerBlock = TWBlocks.GEMROCK_SODALITE.getDefaultState();
		baradDur.a = 1;
		baradDur.b = 1;
		NEO_HELL.register(3, baradDur);
		*/
		HellCompositorBiome heartsblood = new HellCompositorBiome("heartsblood");
		heartsblood.topBlock = TWBlocks.GEMROCK_GARNET.getDefaultState();
		heartsblood.fillerBlock = TWBlocks.GEMROCK_TOURMALINE.getDefaultState();
		heartsblood.a = 2;
		heartsblood.b = 0;
		NEO_HELL.register(4, heartsblood);
		
		HellCompositorBiome sulfur = new HellCompositorBiome("sulfur");
		sulfur.topBlock = TWBlocks.GEMROCK_HELIODOR.getDefaultState();
		sulfur.fillerBlock = TWBlocks.GEMROCK_PERIDOT.getDefaultState();
		sulfur.a = 2;
		sulfur.b = 1;
		NEO_HELL.register(5, sulfur);
		
		NEO_HELL.register(6, new BiomeNocturne());
		/*
		HellCompositorBiome nocturne = new HellCompositorBiome("nocturne");
		nocturne.topBlock = TWBlocks.GEMROCK_CASSITERITE.getDefaultState();
		nocturne.fillerBlock = TWBlocks.GEMROCK_CHRYSOPRASE.getDefaultState();
		nocturne.a = 2;
		nocturne.b = 2;
		NEO_HELL.register(6, nocturne);
		*/
		HellCompositorBiome doom = new HellCompositorBiome("doom");
		doom.topBlock = TWBlocks.GEMROCK_SPINEL.getDefaultState();
		doom.fillerBlock = TWBlocks.GEMROCK_PYRITE.getDefaultState();
		doom.a = 1;
		doom.b = 2;
		NEO_HELL.register(7, doom);
		
		/*
		map.setBiome(0, 0, CompositorBiomes.NEO_HELL.getObject("bridges"));
		map.setBiome(0, 1, CompositorBiomes.NEO_HELL.getObject("strata"));
		map.setBiome(1, 0, CompositorBiomes.NEO_HELL.getObject("cold"));
		map.setBiome(1, 1, CompositorBiomes.NEO_HELL.getObject("barad_dur"));
		map.setBiome(2, 0, CompositorBiomes.NEO_HELL.getObject("heartsblood"));
		map.setBiome(2, 1, CompositorBiomes.NEO_HELL.getObject("sulfur"));
		map.setBiome(2, 2, CompositorBiomes.NEO_HELL.getObject("nocturne"));
		map.setBiome(1, 2, CompositorBiomes.NEO_HELL.getObject("doom"));
		*/

		/*

		
		
		NEO_HELL.register(4,
			new NeoBiome("heartsblood", new Biome.BiomeProperties(I18n.translateToLocal("biome.thermionics.heartsblood"))
				.setBaseHeight(128f)
				.setTemperature(0.25f)
				.setRainfall(0)
				.setRainDisabled()
				//.setRainfall(0.5f)
			)
			.withSurfaceMaterial(TWBlocks.GEMROCK_GARNET)
			.withTerrainFillMaterial(TWBlocks.GEMROCK_TOURMALINE)
			.withDensitySurfaceMaterial(TWBlocks.GEMROCK_GARNET)
			.withDensityCoreMaterial(TWBlocks.GEMROCK_EMERALD)
			.withWorldGenerator(new GeneratorBoneTree())
			.withWorldGenerator(new GeneratorBoneShrub())
			.withTypes(BiomeDictionary.Type.NETHER, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET)
			);

		NEO_HELL.register(5,
			new NeoBiome("sulfur", new Biome.BiomeProperties(I18n.translateToLocal("biome.thermionics.sulfur"))
				.setBaseHeight(128f)
				.setTemperature(0.375f)
				.setRainfall(0)
				.setRainDisabled()
				//.setRainfall(0.5f)
			)
			.withSurfaceMaterial(TWBlocks.GEMROCK_HELIODOR)
			.withTerrainFillMaterial(TWBlocks.GEMROCK_PERIDOT)
			.withDensitySurfaceMaterial(TWBlocks.GEMROCK_HELIODOR)
			.withDensityCoreMaterial(TWBlocks.GEMROCK_PERIDOT)
			.withWorldGenerator(new GeneratorSulfurVent())
			.withTypes(BiomeDictionary.Type.NETHER)
			);
		
		NEO_HELL.register(6,
			new NeoBiome("nocturne", new Biome.BiomeProperties(I18n.translateToLocal("biome.thermionics.nocturne"))
				.setBaseHeight(128f)
				.setTemperature(0.625f)
				.setRainfall(0)
				.setRainDisabled()
				//.setRainfall(0.5f)
			)
			.withSurfaceMaterial(TWBlocks.GEMROCK_CASSITERITE)
			.withTerrainFillMaterial(TWBlocks.GEMROCK_CHRYSOPRASE)
			.withDensitySurfaceMaterial(TWBlocks.GEMROCK_CASSITERITE)
			.withDensityCoreMaterial(TWBlocks.GEMROCK_CASSITERITE)
			.withWorldGenerator(new GeneratorMagmaSpike())
			.withTypes(BiomeDictionary.Type.NETHER, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SPOOKY)
			);
		
		NEO_HELL.register(7,
			new NeoBiome("doom", new Biome.BiomeProperties(I18n.translateToLocal("biome.thermionics.doom"))
				.setBaseHeight(128f)
				.setTemperature(0.75f)
				.setRainfall(0)
				.setRainDisabled()
				//.setRainfall(0.5f)
			)
			.withSurfaceMaterial(TWBlocks.GEMROCK_SPINEL)
			.withTerrainFillMaterial(TWBlocks.GEMROCK_PYRITE)
			.withDensitySurfaceMaterial(TWBlocks.GEMROCK_SPINEL)
			.withDensityCoreMaterial(TWBlocks.GEMROCK_CASSITERITE)
			.withTypes(BiomeDictionary.Type.NETHER, BiomeDictionary.Type.DRY)
			);*/
	}
	
	public void register(int id, ICompositorBiome biome) {
		registry.put(id, biome);
		biomeMap.setBiome(biome.getA(), biome.getB(), biome);
	}
	
	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		NEO_HELL.registry.values().forEach((it)->{
			if (it instanceof Biome) {
				event.getRegistry().register((Biome)it);
				//for(BiomeDictionary.Type type : it.getTypes()) {
				//	BiomeDictionary.addTypes(it, type);
				//}
			}
		});
	}
	
	public int size() {
		return registry.size();
	}
	
	public BiomeMap<ICompositorBiome> getMap() {
		return biomeMap;
	}

	public byte getId(ICompositorBiome biome) {
		Integer id = registry.inverse().get(biome);
		return (id==null) ? 0 : (byte)id.intValue();
	}
	
	@Nullable
	public ICompositorBiome get(int id) {
		return registry.get(id);
	}
}
