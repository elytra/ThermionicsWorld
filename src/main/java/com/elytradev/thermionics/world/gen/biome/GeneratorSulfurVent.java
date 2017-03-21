package com.elytradev.thermionics.world.gen.biome;

import java.util.ArrayList;
import java.util.Random;

import com.elytradev.thermionics.world.block.TerrainBlocks;
import com.elytradev.thermionics.world.gen.ChunkProviderNeo;
import com.elytradev.thermionics.world.gen.NeoHellGenerators;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorSulfurVent extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		BlockPos cur = NeoHellGenerators.findSurface(worldIn, position);
		if (cur==null) return false;
		cur = cur.up(4); //punch through a certain amount of incline, too.
		
		ArrayList<BlockPos> buffer = new ArrayList<BlockPos>();
		int depth = 20 + rand.nextInt(20);
		for(int i=0; i<30; i++) {
			float radius = 3f + rand.nextFloat()*4;
			
			NeoHellGenerators.cylinderAround(cur, radius, buffer);
			cur = cur.down();
		}
		
		for(BlockPos pos : buffer) {
			if (pos.getY()<ChunkProviderNeo.SEA_LEVEL) {
				worldIn.setBlockState(pos, TerrainBlocks.FLUID_PAIN.getDefaultState());
			} else {
				worldIn.setBlockToAir(pos); //TODO: randomy spawn sulfurgas pockets
			}
		}
		
		return true;
	}
	
}
