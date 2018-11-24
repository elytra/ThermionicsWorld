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

import com.elytradev.thermionics.world.block.BlockNorfairite;
import com.elytradev.thermionics.world.block.TWBlocks;

import blue.endless.libnoise.generator.RidgedMulti;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.World;

public class BiomeBubbleMountain extends HellCompositorBiome {

	public BiomeBubbleMountain() {
		super("strata"); //registry-name is for historical and map compatibility reasons
		
		a = 0;
		b = 1;
		
		/* Original properties:
		 * base height: 128f
		 * temperature: 0.375f
		 * rainfall: 0f
		 * rain-disabled: true
		 * 
		 * surfaceMaterial: NORFAIRITE_REEF:PURPLE
		 * terrainFillMaterial: GEMROCK_ROSE_QUARTZ
		 * densitySurfaceMaterial: NORFAIRITE_REEF:CYAN
		 * densityCoreMaterial: NETHERRACK
		 * types: NETHER
		 */
		
		this.topBlock = TWBlocks.NORFAIRITE_REEF.getDefaultState().withProperty(BlockNorfairite.COLOR, EnumDyeColor.PURPLE);
		this.fillerBlock = TWBlocks.GEMROCK_ROSE_QUARTZ.getDefaultState();
		this.densitySurfaceBlock = TWBlocks.NORFAIRITE_REEF.getDefaultState().withProperty(BlockNorfairite.COLOR, EnumDyeColor.CYAN);
		this.densityFillerBlock = Blocks.NETHERRACK.getDefaultState();
	}
	
	@Override
	public IBiomeChunkGenerator createChunkGenerator(World world) {
		final int intSeed = (int)world.getWorldInfo().getSeed() ^ (int)(world.getWorldInfo().getSeed() >> 32);
		
		return new IBiomeChunkGenerator() {
			private RidgedMulti multi = new RidgedMulti().setFrequency(1/128f).setOctaveCount(5).setSeed(intSeed + 3);
			
			
			@Override
			public double getHeightValue(int x, int z) {
				double val = multi.getValue(x, -512, z) / 2.0 + 0.5;
				
				if (val<0) val = 0; //never dip below sea level
				if (val>1) val = 1; //never soar above the ordained height
				return val * 64.0;  //be proud, tall peaks, befitting the dignity and gravity of irridescent radioactive space lava bubbles
			}
			
			@Override
			public double getDensityValue(double x, double y, double z) {
				//return multi.getValue(x, y, z);
				return 0.3; //Amenable to volumes, but doesn't force them.
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
