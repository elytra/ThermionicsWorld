package com.elytradev.thermionics.world.gen.biome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class NeoBiome {
	public static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
	
	float terrainHeight = 128f;
	
	IBlockState terrainSurfaceMaterial = Blocks.DIRT.getDefaultState();
	IBlockState terrainFillMaterial = Blocks.STONE.getDefaultState();
	
	float density = 1.0f;
	
	IBlockState densitySurfaceMaterial = Blocks.DIRT.getDefaultState();
	IBlockState densityCoreMaterial = Blocks.STONE.getDefaultState();
	
	float veinHeight = 200f;
	
	/**
	 * Gets the material to fill the very bottom of the map with
	 * @param y
	 * @return
	 */
	public IBlockState getTerrainMaterial(int y) {
		if (y==0) return BEDROCK;
		if (y<terrainHeight-4) return terrainFillMaterial;
		return terrainSurfaceMaterial;
	}
	
	/**
	 * Gets the material to fill biome nougat with.
	 * @param adjustedDensity
	 * @return
	 */
	public IBlockState getDensityMaterial(float adjustedDensity) {
		if (density>0.8f) return densitySurfaceMaterial;
		return densityCoreMaterial;
	}
}
