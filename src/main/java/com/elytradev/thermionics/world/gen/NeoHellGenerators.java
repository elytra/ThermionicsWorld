package com.elytradev.thermionics.world.gen;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class NeoHellGenerators {
	public static class Glowstone extends WorldGenerator {
		@Override
		public boolean generate(World worldIn, Random rand, BlockPos position) {
			if (!worldIn.isAirBlock(position)) return false;
			if (worldIn.isAirBlock(position.up())) return false;

			worldIn.setBlockState(position, Blocks.GLOWSTONE.getDefaultState(), 2);

			for (int i = 0; i < 1500; ++i) {
				BlockPos scatter = position.add(rand.nextInt(8) - rand.nextInt(8), -rand.nextInt(12), rand.nextInt(8) - rand.nextInt(8));

				if (worldIn.isAirBlock(scatter)) {
					int adjacent = 0;

					for (EnumFacing enumfacing : EnumFacing.values()) {
						if (worldIn.getBlockState(scatter.offset(enumfacing)).getBlock() == Blocks.GLOWSTONE) {
							adjacent++;
						}

						if (adjacent > 1) {
							break;
						}
					}

					if (adjacent == 1) {
						worldIn.setBlockState(scatter, Blocks.GLOWSTONE.getDefaultState(), 2);
					}
				}
			}

			return true;
		}
	}

	public static class Lava extends WorldGenerator {
		private final Block block;
		private final boolean insideRock;

		public Lava(Block blockIn, boolean insideRockIn) {
			this.block = blockIn;
			this.insideRock = insideRockIn;
		}

		boolean isValidSpot(World world, BlockPos position) {
			return !world.isAirBlock(position);
		}

		@Override
		public boolean generate(World worldIn, Random rand, BlockPos position) {
			if (!isValidSpot(worldIn, position.up())) return false;

			int solidNeighbors = 0;

			if (isValidSpot(worldIn, position.west()))  solidNeighbors++;
			if (isValidSpot(worldIn, position.east()))  solidNeighbors++;
			if (isValidSpot(worldIn, position.north())) solidNeighbors++;
			if (isValidSpot(worldIn, position.south())) solidNeighbors++;
			if (isValidSpot(worldIn, position.down()))  solidNeighbors++;

			int airNeighbors = 5 - solidNeighbors;
			/*
			if (!isValidSpot(worldIn, position.west()))  airNeighbors++;
			if (!isValidSpot(worldIn, position.east()))  airNeighbors++;
			if (!isValidSpot(worldIn, position.north())) airNeighbors++;
			if (!isValidSpot(worldIn, position.south())) airNeighbors++;
			if (!isValidSpot(worldIn, position.down()))  airNeighbors++;
			 */
			
			if (!this.insideRock && solidNeighbors == 4 && airNeighbors == 1 || solidNeighbors == 5) {
				IBlockState iblockstate = this.block.getDefaultState();
				worldIn.setBlockState(position, iblockstate, 2);
				worldIn.immediateBlockTick(position, iblockstate, rand);
			}

			return true;
		}
	}
	
	public static class OreSimple extends WorldGenerator {
		private IBlockState[] varieties;
		private int blockCount;
		
		public OreSimple(int blockCount, IBlockState... varieties) {
			this.blockCount = blockCount;
			this.varieties = varieties;
		}
		
		@Override
		public boolean generate(World worldIn, Random rand, BlockPos position) {
			//Create locations around the position
			for(int i=0; i<blockCount; i++) {
				
			}
			
			
			
			if (worldIn.isAirBlock(position)) return false;
			
			IBlockState selected = varieties[rand.nextInt(varieties.length)];
			worldIn.setBlockState(position, selected, 2);
			
			return true;
		}
		
	}
	
	public static class OreInclusion extends WorldGenerator {
		private HashMap<IBlockState, IBlockState> inclusions = new HashMap<>();
		//private int blockCount;
		
		public OreInclusion(int blockCount, IBlockState... map) {
			//this.blockCount = blockCount;
			
			int numEntries = map.length/2;
			if (numEntries*2!=map.length) throw new IllegalArgumentException("Dangling blockstate ("+map[map.length-1]+") in ore inclusion map.");
			
			for(int i=0; i<numEntries; i++) {
				inclusions.put(map[i*2], map[i*2 + 1]);
			}
		}
		
		@Override
		public boolean generate(World worldIn, Random rand, BlockPos position) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
