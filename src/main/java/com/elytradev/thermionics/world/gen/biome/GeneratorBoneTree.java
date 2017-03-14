package com.elytradev.thermionics.world.gen.biome;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorBoneTree extends WorldGenerator {
	
	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		if (!world.getBlockState(position.down()).isFullBlock()) return false;
		
		
		// TODO Auto-generated method stub
		return false;
	}
}
