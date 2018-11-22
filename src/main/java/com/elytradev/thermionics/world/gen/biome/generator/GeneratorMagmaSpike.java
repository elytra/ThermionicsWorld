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

package com.elytradev.thermionics.world.gen.biome.generator;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import com.elytradev.thermionics.world.gen.NeoHellGenerators;

import blue.endless.libnoise.Module;
import blue.endless.libnoise.generator.Perlin;
import blue.endless.libnoise.modifier.Power;
import blue.endless.libnoise.modifier.ScaleBias;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorMagmaSpike extends WorldGenerator {
	
	Perlin spikeSource;
	Module spikes;
	
	public GeneratorMagmaSpike() {
		spikeSource = new Perlin().setFrequency(1/32.0);
		
		spikes = new ScaleBias().setSources(
					new Power().setSources(spikeSource, (x,y,z)->6)
				).setScale(1/4.0).setBias(-1);
	}
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		
		
		
		BlockPos cur = findNonSpikeSurface(worldIn, position);
		if (cur==null) return false;
		
		IBlockState basalt = Blocks.OBSIDIAN.getDefaultState();
		IBlockState magma = Blocks.MAGMA.getDefaultState();
		
		
		//int ystart = cur.getY();
		//System.out.println("Generating at y="+cur.getY()+" for height="+height);
		//Chunk chunk = worldIn.getChunkFromBlockCoords(position);
		/*
		for(int z=0; z<16; z++) {
			for (int x=0; x<16; x++) {
				int ix = (cur.getX() & 15) + x;
				int iz = (cur.getZ() & 15) + z;
				
				int height = (int)(spikes.getValue(ix, 0.5, iz) * 50) + 46;
				if (height<=0) {
					//System.out.println("Skipped gen because height is "+height);
					break;
				}
				
				
				
				for(int y=0; y<height; y++) {
					if (cur.getY()+y >= 255) break;
					BlockPos pos = new BlockPos(ix, cur.getY() + y, iz);
					
					
					
					
					//BlockPos pos = cur.up(y);
					IBlockState state = magma;
					if (rand.nextInt(height)>y-(height/2)) state = basalt;
					worldIn.setBlockState(pos, state);
					
				}
			}
		}*/
		
		
		//worldIn.setBlockState(position, Blocks.GOLD_BLOCK.getDefaultState());
		/*
		int height = rand.nextInt(10)+40;
		float xtilt = rand.nextFloat()-0.499f;
		float ztilt = rand.nextFloat()-0.499f;
		float x = cur.getX()+0.5f;
		float z = cur.getZ()+0.5f;
		int ystart = cur.getY() - (height/2);
		ArrayList<BlockPos> shapeBuffer = new ArrayList<>();
		
		for(int y=0; y<height; y++) {
			float r = 1;
			if (y<height/2) {
				r = ((y) / (float)height) * 8;
			} else {
				r = (1 - (y / (float)height)) * 8;
			}
			//r = (1 - (y / (float)height)) * 8;
			cur = new BlockPos(x,ystart+y,z);
			shapeBuffer.clear();
			NeoHellGenerators.cylinderAround(cur, r, shapeBuffer);
			for(BlockPos pos : shapeBuffer) {
				IBlockState state = magma;
				if (rand.nextInt(height)>y-(height/2)) state = basalt;
				worldIn.setBlockState(pos, state);
			}
			
			x+=xtilt;
			z+=ztilt;
			
			if (ystart+y>=255) break; //Better than trying to get out into the upper void
			cur = cur.up();
		}*/
		
		return true;
	}

	@Nullable
	public static BlockPos findNonSpikeSurface(World world, BlockPos start) {
		BlockPos pos = new BlockPos(start.getX(), 255, start.getZ()); //start;
		while (pos.getY()>world.getSeaLevel()) {
			if (isNonSpikeSurface(world, pos)) return pos;
			pos = pos.down();
		}
		
		return null;
	}

	public static boolean isNonSpikeSurface(World world, BlockPos pos) {
		Block surface = world.getBlockState(pos.down()).getBlock();
		if (surface==Blocks.MAGMA || surface==Blocks.OBSIDIAN) return false;
		
		return world.isAirBlock(pos) &&
				world.isBlockFullCube(pos.down());
	}
}
