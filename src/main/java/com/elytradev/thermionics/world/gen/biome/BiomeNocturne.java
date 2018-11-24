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

import com.elytradev.thermionics.world.block.TWBlocks;

import blue.endless.libnoise.generator.RidgedMulti;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BiomeNocturne extends HellCompositorBiome {

	public BiomeNocturne() {
		super("nocturne");
		
		a = 2;
		b = 2;
		
		/* Original properties:
		 * base height: 128f
		 * temperature: 0.625f
		 * rainfall: 0f
		 * rain-disabled: true
		 * 
		 * surfaceMaterial: GEMROCK_CASSITERITE
		 * terrainFillMaterial: GEMROCK_CHRYSOPRASE
		 * densitySurfaceMaterial: GEMROCK_CASSITERITE
		 * densityCoreMaterial: GEMROCK_CASSITERITE
		 * types: NETHER, DEAD, SPOOKY
		 * additional: magma spikes
		 */
		
		this.topBlock = TWBlocks.GEMROCK_CASSITERITE.getDefaultState();
		this.fillerBlock = TWBlocks.GEMROCK_CHRYSOPRASE.getDefaultState();
		this.densitySurfaceBlock = TWBlocks.GEMROCK_CASSITERITE.getDefaultState();
		this.densityFillerBlock = TWBlocks.GEMROCK_CHRYSOPRASE.getDefaultState();
	}
	
	@Override
	public IBiomeChunkGenerator createChunkGenerator(World world) {
		final int intSeed = (int)world.getWorldInfo().getSeed() ^ (int)(world.getWorldInfo().getSeed() >> 32);
		
		return new IBiomeChunkGenerator() {
			private RidgedMulti multi = new RidgedMulti().setFrequency(1/64f).setOctaveCount(3).setSeed(intSeed + 3);
			
			@Override
			public double getHeightValue(int x, int z) {
				double val = multi.getValue(x, -512, z) / 2.0 + 0.5;
				
				if (val<0) val = 0;
				if (val>1) val = 1;
				return val * 64.0 - 16.0;
			}

			@Override
			public double getDensityValue(double x, double y, double z) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public IBlockState getHeightBlockState(int x, int z, int depth) {
				if (depth<4) {
					return topBlock;
				} else {
					return fillerBlock;
				}
			}

			@Override
			public IBlockState getDensityBlockState(int x, int y, int z, double density) {
				if (density<0.6) {
					return densitySurfaceBlock;
				} else {
					return densityFillerBlock;
				}
			}
		};
	}
}
