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

package com.elytradev.thermionics.world.gen;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.elytradev.thermionics.world.gen.biome.BiomeFamily;
import com.elytradev.thermionics.world.gen.biome.BiomeMap;
import com.elytradev.thermionics.world.gen.biome.BiomeModule;
import com.elytradev.thermionics.world.gen.biome.ICompositorBiome;
import com.google.common.collect.ImmutableList;

import blue.endless.libnoise.generator.Perlin;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.storage.WorldInfo;

public class BiomeProviderNeo extends BiomeProvider {
	
	/** Maps XZ map-coordinates to AB values */
	BiomeModule biomeModule;
	/** Maps AB values to biomes */
	BiomeMap<ICompositorBiome> biomeMap;
	
	
	public BiomeProviderNeo(WorldInfo info, BiomeFamily biomeFamily) {
		super(info);
		
		biomeMap = biomeFamily.getMap();
		
		int intSeed = (int)info.getSeed(); intSeed ^= (int)(info.getSeed() >> 32);
		
		Perlin biomeA = new Perlin().setSeed(intSeed+1).setFrequency(1/379.0).setOctaveCount(7);
		Perlin biomeB = new Perlin().setSeed(intSeed+2).setFrequency(1/367.0).setOctaveCount(6);
		biomeModule = new BiomeModule(biomeA, biomeB);
		biomeModule.setBiomeMap(biomeMap);
	}
	
	@Override
	public Biome getBiome(BlockPos pos) {
		return this.getBiome(pos, (Biome)null);
	}
	
	@Override
	public Biome getBiome(BlockPos pos, Biome defaultBiome) {
		ICompositorBiome result = biomeModule.biomeAtTurbulent(pos.getX(), pos.getZ());
		if (result instanceof Biome) return (Biome) result;
		
		return defaultBiome;
	}
	
	@Override
	public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) {
		throw new UnsupportedOperationException("This biome provider does not support tile-based generation.");
	}
	
	@Override
	public List<Biome> getBiomesToSpawnIn() {
		return ImmutableList.of();
	}
	
	@Override
	public float getTemperatureAtHeight(float temp, int height) {
		return temp;
	}
	
	@Override
	public Biome[] getBiomes(@Nullable Biome[] oldBiomeList, int x, int z, int width, int depth) {
		throw new UnsupportedOperationException("This biome provider does not support tile-based generation.");
	}
	
	@Override
	public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag) {
		throw new UnsupportedOperationException("This biome provider does not support tile-based generation.");
	}
	
	/**
	 * This method is here to support context-sensitive generation which we do not allow. All biomes are viable here.
	 */
	@Override
	public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) {
		return true;
	}
	
	@Override
	@Nullable
	public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
		return null;
	}

	@Override
	public void cleanupCache() { }

	@Override
	public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original) {
		return original;
	}

	@Override
	public boolean isFixedBiome() {
		return false;
	}

	public Biome getFixedBiome() {
		return null;
	}

	public BiomeModule getBiomeModule() {
		return biomeModule;
	}
}
