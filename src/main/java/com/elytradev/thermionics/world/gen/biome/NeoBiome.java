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
