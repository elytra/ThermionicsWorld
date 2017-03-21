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

import java.util.Random;

import com.elytradev.thermionics.world.gen.NeoHellGenerators;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorBoneTree extends WorldGenerator {
	
	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		BlockPos pos = NeoHellGenerators.findSurface(world, position);
		if (pos==null) {
			//System.out.println("Failed to generate at "+position);
			return false;
		} else {
			//System.out.println("Generating at "+pos);
		}
		
		int height = rand.nextInt(4) + 7;
		
		IBlockState trunkState = Blocks.BONE_BLOCK.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y);
		//EnumFacing.Axis axis = rand.nextBoolean() ? EnumFacing.Axis.X : EnumFacing.Axis.Z;
		EnumAxis axis = rand.nextBoolean() ? EnumAxis.X : EnumAxis.Z;
		BlockPos section = pos;
		
		for(int y=0; y<height; y++) {
			
			world.setBlockState(section, trunkState);
			
			if (y>3 && y%2==0) {
				axis = rand.nextBoolean() ? EnumAxis.X : EnumAxis.Z;
				BlockPos arm = section;
				int width = (height-y) / 2;
				
				for(int i=0; i<width; i++) {
					arm = arm.add(axis.xofs, axis.yofs, axis.zofs);
					world.setBlockState(arm, axis==EnumAxis.X ?
							trunkState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X) :
							trunkState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z));
				}
				arm = section;
				for(int i=0; i<width; i++) {
					arm = arm.add(-axis.xofs, -axis.yofs, -axis.zofs);
					world.setBlockState(arm, axis==EnumAxis.X ?
							trunkState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X) :
							trunkState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z));
				}
			}
			section = section.up();
		}
		
		return true;
	}
	
	public static enum EnumAxis {
		X(1,0,0),
		Z(0,0,1);
		
		private final int xofs;
		private final int yofs;
		private final int zofs;
		
		EnumAxis(int x, int y, int z) {
			xofs = x;
			yofs = y;
			zofs = z;
		}
	}
}
