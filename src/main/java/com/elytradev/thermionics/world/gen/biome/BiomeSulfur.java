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
import com.elytradev.thermionics.world.gen.ImageModule;

import blue.endless.libnoise.generator.Perlin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BiomeSulfur extends HellCompositorBiome {
	private final int SULFUR_PER_COLUMN = 8;
	
	//protected GeneratorSulfurVent vents = new GeneratorSulfurVent();
	ImageModule vents = new ImageModule().setImage(BiomeFamily.unpackTerrainImage("vents")).setPixelsPerUnit(1/2.0);
	
	public BiomeSulfur() {
		super("sulfur");
		
		a = 2;
		b = 1;
		
		/* Original properties:
		 * base height: 128f
		 * temperature: 0.375f
		 * rainfall: 0f
		 * rain-disabled: true
		 * 
		 * surfaceMaterial: GEMROCK_HELIODOR
		 * terrainFillMaterial: GEMROCK_PERIDOT
		 * densitySurfaceMaterial: GEMROCK_HELIODOR
		 * densityCoreMaterial: GEMROCK_PERIDOT
		 * types: NETHER
		 * additional: sulfurVent
		 */
		
		topBlock = TWBlocks.GEMROCK_HELIODOR.getDefaultState();
		fillerBlock = TWBlocks.GEMROCK_PERIDOT.getDefaultState();
		densitySurfaceBlock = TWBlocks.GEMROCK_HELIODOR.getDefaultState();
		densityFillerBlock = TWBlocks.GEMROCK_PERIDOT.getDefaultState();
	}
	
	@Override
	public IBiomeChunkGenerator createChunkGenerator(World world) {
		final int intSeed = (int)world.getWorldInfo().getSeed() ^ (int)(world.getWorldInfo().getSeed() >> 32);
		
		return new IBiomeChunkGenerator() {
			Perlin height = new Perlin(intSeed + 3)
					.setFrequency(1/128.0)
					.setOctaveCount(2);
			//Perlin density = new Perlin(intSeed + 4)
			//		.setFrequency(1/128.0)
			//		.setOctaveCount(6);
			//ImageModule vents = new ImageModule().setImage(BiomeFamily.unpackTerrainImage("vents")).setPixelsPerUnit(1/2.0);
			
			@Override
			public double getHeightValue(int x, int z) {
				double h = height.getValue(x, 0, z) / 2.0 + 0.5;
				if (h<0) h=0;
				if (h>1) h=1;
				
				double v = vents.getValue(x, 0, z);
				
				if (v<0.2) return -(int)(h * 6); //We're deep in a vent.
				
				return (int)(h * 2) + (int)((v-0.7)*14) + 26;
			}
			
			@Override
			public double getDensityValue(double x, double y, double z) {
				//double d = density.getValue(x, y, z);
				
				//return d;
				return 0.4;
			}
			
			@Override
			public IBlockState getHeightBlockState(int x, int z, int depth) {
				
				
				if (depth<4) {
					double d = vents.getValue(x, 0, z);
					if (d - 0.3 < 0) return fillerBlock;
					if (d - 0.6 < 0) return topBlock;
					return Blocks.SOUL_SAND.getDefaultState();
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
	public void decorate(World worldIn, Random rand, BlockPos pos) {
		final IBlockState AIR = Blocks.AIR.getDefaultState();
		final IBlockState SULFUR = TWBlocks.SULFUR.getDefaultState();
		
		Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
		for(int z=0; z<16; z++) {
			for(int x=0; x<16; x++) {
				double v = vents.getValue(x+pos.getX(), 0, z+pos.getZ());
				
				if (v>=0.2) continue; //We're not in a vent
				
				
				for(int i=0; i<SULFUR_PER_COLUMN; i++) {
					int y = rand.nextInt(128);
					
					IBlockState existing = chunk.getBlockState(x, y, z);
					if (existing==null || existing==AIR) {
						chunk.setBlockState(new BlockPos(x, y, z), SULFUR);
					}
				}
				
				
			}
		}
		
	}
	
}
