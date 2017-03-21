package com.elytradev.thermionics.world.gen.biome;

import java.util.ArrayList;
import java.util.Random;

import com.elytradev.thermionics.world.block.TerrainBlocks;
import com.elytradev.thermionics.world.gen.NeoHellGenerators;

import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorNorfairiteBush extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		BlockPos cur = NeoHellGenerators.findSurface(worldIn, position);
		if (cur==null) return false;
		
		IBlockState norfairite = TerrainBlocks.NORFAIRITE_CLEAR.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GREEN);
		
		ArrayList<BlockPos> bushArea = new ArrayList<BlockPos>();
		NeoHellGenerators.sphereAround(cur.up(), 1.5f + rand.nextFloat(), bushArea);
		for(BlockPos leaf : bushArea) {
			IBlockState state = worldIn.getBlockState(leaf);
			if (state.getBlock().canBeReplacedByLeaves(state, worldIn, leaf)) {
				worldIn.setBlockState(leaf, norfairite);
			}
		}
		
		return true;
	}
	
}
