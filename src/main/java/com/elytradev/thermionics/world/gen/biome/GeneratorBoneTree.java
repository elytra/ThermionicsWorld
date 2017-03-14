package com.elytradev.thermionics.world.gen.biome;

import java.util.Random;

import javax.annotation.Nullable;

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
		BlockPos pos = findSurface(world, position);
		if (pos==null) {
			System.out.println("Failed to generate at "+position);
			return false;
		} else {
			System.out.println("Generating at "+pos);
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
	
	@Nullable
	public static BlockPos findSurface(World world, BlockPos start) {
		BlockPos pos = start;
		while (pos.getY()>world.getSeaLevel()) {
			if (isSurface(world, pos)) return pos;
			pos = pos.down();
		}
		
		return null;
	}
	
	public static boolean isSurface(World world, BlockPos pos) {
		return world.isAirBlock(pos) &&
				world.isBlockFullCube(pos.down());
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