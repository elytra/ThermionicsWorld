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
package com.elytradev.thermionics.world.gen.biome;

import com.elytradev.thermionics.world.block.TerrainBlocks;

import net.minecraft.init.Blocks;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.biome.Biome;

public class BiomeRegistry extends RegistryNamespaced<String, NeoBiome> {
	public static BiomeRegistry NEO_HELL = new BiomeRegistry();
	
	static {
		NEO_HELL.register(0,
			new NeoBiome(new Biome.BiomeProperties("bridges")
				.setBaseHeight(128f)
				.setTemperature(0.25f)
				.setRainfall(0.25f)
			)
			.withSurfaceMaterial(Blocks.NETHERRACK)
			.withTerrainFillMaterial(Blocks.NETHERRACK)
			.withDensitySurfaceMaterial(Blocks.NETHERRACK)
			.withDensityCoreMaterial(Blocks.SOUL_SAND)
			);
		
		NEO_HELL.register(1,
			new NeoBiome(new Biome.BiomeProperties("strata")
				.setBaseHeight(128f)
				.setTemperature(0.375f)
				.setRainfall(0.25f)
			)
			.withSurfaceMaterial(TerrainBlocks.GEMROCK_PYRITE)
			.withTerrainFillMaterial(TerrainBlocks.GEMROCK_ROSE_QUARTZ)
			.withDensitySurfaceMaterial(TerrainBlocks.GEMROCK_PYRITE)
			.withDensityCoreMaterial(Blocks.NETHERRACK)
			);
		
		NEO_HELL.register(2,
			new NeoBiome(new Biome.BiomeProperties("cold")
				.setBaseHeight(128f)
				.setTemperature(0.625f)
				.setRainfall(0.25f)
			)
			.withSurfaceMaterial(TerrainBlocks.GEMROCK_MAGNESITE)
			.withTerrainFillMaterial(TerrainBlocks.GEMROCK_SAPPHIRE)
			.withDensitySurfaceMaterial(TerrainBlocks.GEMROCK_OPAL)
			.withDensityCoreMaterial(Blocks.PACKED_ICE)
			);
		
		NEO_HELL.register(3,
			new NeoBiome(new Biome.BiomeProperties("barad_dur")
				.setBaseHeight(128f)
				.setTemperature(0.75f)
				.setRainfall(0.25f)
			)
			.withSurfaceMaterial(TerrainBlocks.GEMROCK_HEMATITE)
			.withTerrainFillMaterial(Blocks.NETHERRACK)
			.withDensitySurfaceMaterial(TerrainBlocks.GEMROCK_HEMATITE)
			.withDensityCoreMaterial(Blocks.NETHERRACK)
			);
		
		NEO_HELL.register(4,
			new NeoBiome(new Biome.BiomeProperties("heartsblood")
				.setBaseHeight(128f)
				.setTemperature(0.25f)
				.setRainfall(0.5f)
			)
			.withSurfaceMaterial(TerrainBlocks.GEMROCK_GARNET)
			.withTerrainFillMaterial(TerrainBlocks.GEMROCK_TOURMALINE)
			.withDensitySurfaceMaterial(TerrainBlocks.GEMROCK_GARNET)
			.withDensityCoreMaterial(TerrainBlocks.GEMROCK_EMERALD)
			.withWorldGenerator(new GeneratorBoneTree())
			);

		NEO_HELL.register(5,
			new NeoBiome(new Biome.BiomeProperties("sulfur")
				.setBaseHeight(128f)
				.setTemperature(0.375f)
				.setRainfall(0.5f)
			)
			.withSurfaceMaterial(TerrainBlocks.GEMROCK_HELIODOR)
			.withTerrainFillMaterial(TerrainBlocks.GEMROCK_PERIDOT)
			.withDensitySurfaceMaterial(TerrainBlocks.GEMROCK_HELIODOR)
			.withDensityCoreMaterial(TerrainBlocks.GEMROCK_PERIDOT)
			);
		
		NEO_HELL.register(6,
			new NeoBiome(new Biome.BiomeProperties("nocturne")
				.setBaseHeight(128f)
				.setTemperature(0.625f)
				.setRainfall(0.5f)
			)
			.withSurfaceMaterial(TerrainBlocks.GEMROCK_CASSITERITE)
			.withTerrainFillMaterial(Blocks.OBSIDIAN)
			.withDensitySurfaceMaterial(TerrainBlocks.GEMROCK_CASSITERITE)
			.withDensityCoreMaterial(TerrainBlocks.GEMROCK_CASSITERITE)
			);
		
		NEO_HELL.register(7,
			new NeoBiome(new Biome.BiomeProperties("doom")
				.setBaseHeight(128f)
				.setTemperature(0.75f)
				.setRainfall(0.5f)
			)
			.withSurfaceMaterial(TerrainBlocks.GEMROCK_SPINEL)
			.withTerrainFillMaterial(TerrainBlocks.GEMROCK_PYRITE)
			.withDensitySurfaceMaterial(TerrainBlocks.GEMROCK_SPINEL)
			.withDensityCoreMaterial(TerrainBlocks.GEMROCK_CASSITERITE)
			);
	}
	
	
	
	public void register(int id, NeoBiome biome) {
		this.register(id, biome.getBiomeName(), biome);
	}
}
