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

import java.util.Random;

import com.elytradev.thermionics.world.block.TWBlocks;
import com.elytradev.thermionics.world.gen.biome.generator.GeneratorBoneShrub;
import com.elytradev.thermionics.world.gen.biome.generator.GeneratorBoneTree;

import blue.endless.libnoise.generator.Perlin;
import blue.endless.libnoise.generator.RidgedMulti;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BiomeHeartsblood extends HellCompositorBiome {
	protected GeneratorBoneShrub shrubs = new GeneratorBoneShrub();
	protected GeneratorBoneTree trees = new GeneratorBoneTree();
	
	public BiomeHeartsblood() {
		super("heartsblood");
		
		a = 2;
		b = 0;
		
		/* Original properties:
		 * base height: 128f
		 * temperature: 0.25f
		 * rainfall: 0f
		 * rain-disabled: true
		 * 
		 * surfaceMaterial: GEMROCK_GARNET
		 * terrainFillMaterial: GEMROCK_TOURMALINE
		 * densitySurfaceMaterial: GEMROCK_GARNET
		 * densityCoreMaterial: GEMROCK_EMERALD
		 * types: NETHER, HOT, WET
		 * additional: BoneTree, BoneShrub
		 */
		
		topBlock = TWBlocks.GEMROCK_GARNET.getDefaultState();
		fillerBlock = TWBlocks.GEMROCK_TOURMALINE.getDefaultState();
		densitySurfaceBlock = TWBlocks.GEMROCK_PYRITE.getDefaultState();
		densityFillerBlock = TWBlocks.GEMROCK_EMERALD.getDefaultState();
	}
	
	@Override
	public IBiomeChunkGenerator createChunkGenerator(World world) {
		final int intSeed = (int)world.getWorldInfo().getSeed() ^ (int)(world.getWorldInfo().getSeed() >> 32);
		
		return new IBiomeChunkGenerator() {
			Perlin height = new Perlin(intSeed + 3)
					.setFrequency(1/64.0)
					.setOctaveCount(2);
			
			RidgedMulti volumeNoise = new RidgedMulti(intSeed + 4)
					.setFrequency(1/60.0)
					//.setLacunarity(1.5)
					.setOctaveCount(2);
			
			@Override
			public double getHeightValue(int x, int z) {
				double h = height.getValue(x, -512, z);
				
				return h * 3;
			}

			@Override
			public double getDensityValue(double x, double y, double z) {
				int maxHeight = 150;
				if (y>maxHeight) return 0;
				double heightScale = (maxHeight - y) / (double)(maxHeight-48);
				
				double d = (volumeNoise.getValue(x, y, z)+0.2) * heightScale;
				
				return d;
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
	
	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		splash(world, rand, pos, trees, 3);
		splash(world, rand, pos, shrubs, 3);
	}
}
