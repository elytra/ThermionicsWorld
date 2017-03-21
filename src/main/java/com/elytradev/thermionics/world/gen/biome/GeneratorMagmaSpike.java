package com.elytradev.thermionics.world.gen.biome;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import com.elytradev.thermionics.world.gen.NeoHellGenerators;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorMagmaSpike extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		BlockPos cur = findNonSpikeSurface(worldIn, position);
		if (cur==null) return false;
		
		IBlockState basalt = Blocks.OBSIDIAN.getDefaultState();
		IBlockState magma = Blocks.MAGMA.getDefaultState();
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
		}
		
		return true;
	}

	@Nullable
	public static BlockPos findNonSpikeSurface(World world, BlockPos start) {
		BlockPos pos = start;
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
