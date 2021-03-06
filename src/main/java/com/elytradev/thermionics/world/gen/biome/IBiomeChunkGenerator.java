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

import net.minecraft.block.state.IBlockState;

/**
 * Potentially seed-specific or world-specific parts of A compositor-biome's chunk generation. Can be used outside
 * Minecraft.
 */
public interface IBiomeChunkGenerator {
	/** Get the height value, as an offset from sea level in meters. Positive numbers are up. */
	public double getHeightValue(int x, int z);
	/** Get the density value of this volume. Anything above 0.5 will usually end up as a solid block. */
	public double getDensityValue(double x, double y, double z);
	
	/**
	 * Assuming the block indicated is a solid block and part of the "height" section of the terrain of this biome,
	 * which block is it?
	 */
	public IBlockState getHeightBlockState(int x, int z, int depth);
	
	/** Assuming the block indicated is a solid block of this biome, which block is it? */
	public IBlockState getDensityBlockState(int x, int y, int z, double density);
}
