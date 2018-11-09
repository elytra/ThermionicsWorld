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

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.elytradev.thermionics.world.gen.VoronoiClusterField;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.storage.WorldInfo;

public class BiomeProviderNeo extends BiomeProvider {
	VoronoiClusterField<Biome> biomeSelector;

	
	public BiomeProviderNeo(WorldInfo info) {
		super(info);
		
		biomeSelector = new VoronoiClusterField<Biome>(info.getSeed(), 16*9);
		for(NeoBiome biome : BiomeRegistry.NEO_HELL) {
			biomeSelector.registerCell(biome, biome.getDefaultTemperature(), biome.getRainfall());
		}
	}
	
	@Override
	public Biome getBiome(BlockPos pos) {
        return this.getBiome(pos, (Biome)null);
    }

	@Override
	public Biome getBiome(BlockPos pos, Biome defaultBiome) {
        return biomeSelector.get(pos.getX(), pos.getZ());
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

    @Override
    public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed) {
        for(Biome biome : allowed) {
        	if (!this.biomeSelector.containsCell(biome)) return false;
        }
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
}
