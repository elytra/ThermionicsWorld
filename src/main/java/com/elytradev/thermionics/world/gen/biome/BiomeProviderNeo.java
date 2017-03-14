package com.elytradev.thermionics.world.gen.biome;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.elytradev.thermionics.world.block.TerrainBlocks;
import com.elytradev.thermionics.world.gen.VoronoiClusterField;
import com.google.common.collect.ImmutableList;

import net.minecraft.init.Blocks;
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
		BiomeRegistry.NEO_HELL.forEach( it-> {
			biomeSelector.registerCell(it, it.getTemperature(), it.getRainfall());
		});
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
