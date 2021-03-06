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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NeoBiome extends Biome {
	public static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
	private String name;
	
	float terrainHeight = 128f;
	
	IBlockState terrainSurfaceMaterial = Blocks.DIRT.getDefaultState();
	IBlockState terrainFillMaterial = Blocks.STONE.getDefaultState();
	
	float density = 1.0f;
	
	IBlockState densitySurfaceMaterial = Blocks.DIRT.getDefaultState();
	IBlockState densityCoreMaterial = Blocks.STONE.getDefaultState();
	
	float veinHeight = 200f;
	
	private ArrayList<WorldGenerator> generators = new ArrayList<>();
	private HashSet<BiomeDictionary.Type> types = new HashSet<>();

	
	public NeoBiome(String name, Biome.BiomeProperties properties) {
		super(properties.setRainDisabled());
		this.setRegistryName(name);
		this.name = name;
	}
	
	public NeoBiome withTerrainHeight(float height) {
		this.terrainHeight = height;
		return this;
	}
	
	public NeoBiome withDensity(float density) {
		this.density = density;
		return this;
	}
	
	public NeoBiome withSurfaceMaterial(IBlockState material) {
		this.terrainSurfaceMaterial = material;
		return this;
	}
	
	public NeoBiome withSurfaceMaterial(Block material) {
		this.terrainSurfaceMaterial = material.getDefaultState();
		return this;
	}
	
	public NeoBiome withTerrainFillMaterial(IBlockState material) {
		this.terrainFillMaterial = material;
		return this;
	}
	
	public NeoBiome withTerrainFillMaterial(Block material) {
		this.terrainFillMaterial = material.getDefaultState();
		return this;
	}
	
	public NeoBiome withDensitySurfaceMaterial(IBlockState material) {
		this.densitySurfaceMaterial = material;
		return this;
	}
	
	public NeoBiome withDensitySurfaceMaterial(Block material) {
		this.densitySurfaceMaterial = material.getDefaultState();
		return this;
	}
	
	public NeoBiome withDensityCoreMaterial(IBlockState material) {
		this.densityCoreMaterial = material;
		return this;
	}
	
	public NeoBiome withDensityCoreMaterial(Block material) {
		this.densityCoreMaterial = material.getDefaultState();
		return this;
	}
	
	public NeoBiome withWorldGenerator(WorldGenerator gen) {
		this.generators.add(gen);
		return this;
	}
	
	public NeoBiome withTypes(BiomeDictionary.Type... types) {
		for(BiomeDictionary.Type type : types) this.types.add(type);
		return this;
	}
	
	public Set<BiomeDictionary.Type> getTypes() {
		return types;
	}
	
	public float getTerrainHeight() { return terrainHeight; }
	public float getDensity() { return density; }
	public float getVeinHeight() { return veinHeight; }
	
	/**
	 * Gets the material to fill the very bottom of the map with
	 * @param y 0 at the top block, -1 at the block below that, and so on
	 * @return
	 */
	public IBlockState getTerrainMaterial(int y) {
		if (y<4) return terrainSurfaceMaterial;
		else return terrainFillMaterial;
	}
	
	/**
	 * Gets the material to fill biome nougat with.
	 * @param adjustedDensity
	 * @return
	 */
	public IBlockState getDensityMaterial(float adjustedDensity) {
		if (adjustedDensity<0.10f) return densitySurfaceMaterial;
		return densityCoreMaterial;
	}
	
	@Override
	public void decorate(World worldIn, Random random, BlockPos pos) {
		for(WorldGenerator generator : generators) {
			for(int i=0; i<3; i++) {
				BlockPos relative = new BlockPos(
						pos.getX() + random.nextInt(16) + 8,
						random.nextInt(128) + 64,
						pos.getZ() + random.nextInt(16) + 8
						);
				generator.generate(worldIn, random, relative);
			}
		}
	}

	/** Gets the biome name - available on the server, unlike getBiomeName */
	public String name() {
		return name;
	}
	
	@SideOnly(Side.CLIENT)
	public int getSkyColorByTemp(float currentTemperature) {
		return 0xffff2687;
	}
}
