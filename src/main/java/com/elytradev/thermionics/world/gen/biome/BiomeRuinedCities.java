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

import com.elytradev.thermionics.world.block.BlockGemrock;
import com.elytradev.thermionics.world.block.BlockVarieties;
import com.elytradev.thermionics.world.block.TWBlocks;
import com.elytradev.thermionics.world.gen.ChunkProviderCompositor;
import com.elytradev.thermionics.world.gen.ImageModule;

import blue.endless.libnoise.generator.Perlin;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BiomeRuinedCities extends HellCompositorBiome {
	
	protected final ImageModule cities;
	
	public BiomeRuinedCities() {
		super("doom");
		
		a = 1;
		b = 2;
		
		/* Original properties:
		 * base height: 128f
		 * temperature: 0.75f
		 * rainfall: 0f
		 * rain-disabled: true
		 * 
		 * surfaceMaterial: GEMROCK_SPINEL
		 * terrainFillMaterial: GEMROCK_PYRITE
		 * densitySurfaceMaterial: GEMROCK_SPINEL
		 * densityCoreMaterial: GEMROCK_CASSITERITE
		 * types: NETHER, DRY
		 */
		
		topBlock = TWBlocks.GEMROCK_SPINEL.getDefaultState();
		fillerBlock = TWBlocks.GEMROCK_PYRITE.getDefaultState();
		densitySurfaceBlock = TWBlocks.GEMROCK_SPINEL.getDefaultState();
		densityFillerBlock = TWBlocks.GEMROCK_CASSITERITE.getDefaultState();
		
		cities = new ImageModule().setImage(BiomeFamily.unpackTerrainImage("cities_height"));
	}
	
	@Override
	public IBiomeChunkGenerator createChunkGenerator(World world) {
		final int intSeed = (int)world.getWorldInfo().getSeed() ^ (int)(world.getWorldInfo().getSeed() >> 32);
		
		
		return new IBiomeChunkGenerator() {
			private Perlin plains = new Perlin().setFrequency(1/128.0).setOctaveCount(2).setSeed(intSeed + 3);
			
			
			/**
			 * Creating/interpreting the Cities image
			 * 0x00: No building
			 * 
			 * 0x1B: 1-story room interior
			 * 0x30: 1-story room door
			 * 0x47: 1-story room wall
			 * 
			 * 0x5E: 2-story room interior
			 * 0x77: 2-story room door
			 * 0x91: 2-story room wall
			 * 
			 * 0xAB: 3-story room interior
			 * 0xC6: 3-story room door
			 * 0xE2: 3-story room wall
			 * 
			 * 0xFF: 4-story detail
			 */
			
			
			
			@Override
			public double getHeightValue(int x, int z) {
				//int val = (int)(cities.getValue(x, 0, z) * 255.0);
				
				//int stories = getStories(val);
				
				double h = plains.getValue(x, 0, z) / 2.0 + 0.5;
				if (h<0) h = 0;
				if (h>1) h = 1;
				h *= 3;
				return h;
				//return Math.max((int)h, stories * 5);
			}

			@Override
			public double getDensityValue(double x, double y, double z) {
				// TODO: Make a shelf or a cloud over the ruined cities
				return 0;
			}

			@Override
			public IBlockState getHeightBlockState(int x, int z, int depth) { //TODO: Carve room interiors
				int val = (int)(cities.getValue(x, 0, z) * 255.0);
				
				int stories = getStories(val);
				
				double h = plains.getValue(x, 0, z) / 2.0 + 0.5;
				if (h<0) h = 0;
				if (h>1) h = 1;
				h *= 3;
				
				if (depth < stories * 5) {
					return TWBlocks.GEMROCK_HEMATITE.getDefaultState();
				} else {
					if (depth<4) {
						return topBlock;
					} else {
						return fillerBlock;
					}
				}
			}

			@Override
			public IBlockState getDensityBlockState(int x, int y, int z, double density) {
				return densitySurfaceBlock;
			}
			
			
			
		};
	}
	
	@Override
	public void decorate(World worldIn, Random rand, BlockPos pos) {
		IBlockState wall = Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
				//TWBlocks.GEMROCK_CHRYSOPRASE.getDefaultState().withProperty(BlockVarieties.VARIANT, 1);
		IBlockState floor = Blocks.STONEBRICK.getDefaultState();
				//TWBlocks.GEMROCK_CASSITERITE.getDefaultState().withProperty(BlockVarieties.VARIANT, 2);
		
		if (!worldIn.isAreaLoaded(pos, 0)) {
			System.out.println("Asked to populate unloaded chunk at "+pos);
			//return;
		}
		
		Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
		int chunkX = chunk.x;
		int chunkZ = chunk.z;
		
		for(int z=0; z<16; z++) {
			for(int x=0; x<16; x++) {
				
				int val = (int)(cities.getValue((chunkX<<4) + x, 0, (chunkZ<<4) + z) * 255.0);
		
				int stories = getStories(val);
				if (stories>0) {
					for(int y=1; y>-5; y--) {
						BlockPos foundation = new BlockPos(
								(chunkX<<4) + x,
								ChunkProviderCompositor.SEA_LEVEL + y,
								(chunkZ<<4) + z
								);
						IBlockState existing = chunk.getBlockState(foundation);
						if (existing==null || existing==Blocks.AIR.getDefaultState()) {
							chunk.setBlockState(foundation, floor);
						}
					}
					
					for(int story = 0; story < stories; story++) {
						for(int y=0; y<5; y++) {
							BlockPos cur = new BlockPos(
									(chunkX<<4) + x,
									ChunkProviderCompositor.SEA_LEVEL + (story * 5) + y + 2,
									(chunkZ<<4) + z
									);
							
							chunk.setBlockState(cur, wall);
							//worldIn.setBlockState(cur, TWBlocks.GEMROCK_CHRYSOPRASE.getDefaultState(), 2);
						}
					}
				}
			}
		}
	}
	
	private int getStories(int imageValue) {
		return imageValue / 0x10;
		/*
		if (imageValue<=0x00) return 0; //No building
		if (imageValue<=0x47) return 1; //1 story
		if (imageValue<=0x91) return 2; //2 stories
		if (imageValue<=0xE2) return 3; //3 stories
		return 4; //4th story solid detail*/
	}
	
	private CityPart getPart(int imageValue) {
		return CityPart.WALL;
	}
	
	private static enum CityPart {
		FLOOR,
		DOOR,
		WINDOW,
		WALL;
	}
}
