package com.elytradev.thermionics.world.gen;

import java.util.HashMap;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
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
		private final IBlockState block;

		public Lava(Block blockIn) {
			this.block = blockIn.getDefaultState();
		}

		boolean isValidSpot(World world, BlockPos position) {
			return world.isBlockFullCube(position);
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

			if(solidNeighbors==4) {
				worldIn.setBlockState(position, block, 2);
				worldIn.immediateBlockTick(position, block, rand);
				return true;
			}

			return false;
		}
	}
	
	public static class LavaTrap extends WorldGenerator {
		private final IBlockState block;

		public LavaTrap(IBlockState blockIn) {
			this.block = blockIn;
		}

		boolean isValidSpot(World world, BlockPos position) {
			return world.isBlockFullCube(position);
		}

		@Override
		public boolean generate(World worldIn, Random rand, BlockPos position) {
			if (!isValidSpot(worldIn, position.up())) return false;
			if (!isValidSpot(worldIn, position.down())) return false;
			if (!isValidSpot(worldIn, position.north())) return false;
			if (!isValidSpot(worldIn, position.south())) return false;
			if (!isValidSpot(worldIn, position.east())) return false;
			if (!isValidSpot(worldIn, position.west())) return false;
			
			worldIn.setBlockState(position, block, 2);
			return true;
		}
	}
	
	public static class Quartz extends WorldGenerator {
		private static final Predicate<IBlockState> REPLACEABLE = it->it.isFullCube();
		
		private final IBlockState oreBlock;
	    private final int numberOfBlocks;
	    //private final Predicate<IBlockState> predicate;

	    //public WorldGenMinable(IBlockState state, int blockCount) {
	    //    this(state, blockCount, new WorldGenMinable.StonePredicate());
	    //}

	    public Quartz(IBlockState state, int blockCount) {
	        this.oreBlock = state;
	        this.numberOfBlocks = blockCount;
	        //this.predicate = p_i45631_3_;
	    }

	    public boolean generate(World worldIn, Random rand, BlockPos position) {
	        float f = rand.nextFloat() * (float)Math.PI;
	        double d0 = (double)((float)(position.getX() + 8) + MathHelper.sin(f) * (float)this.numberOfBlocks / 8.0F);
	        double d1 = (double)((float)(position.getX() + 8) - MathHelper.sin(f) * (float)this.numberOfBlocks / 8.0F);
	        double d2 = (double)((float)(position.getZ() + 8) + MathHelper.cos(f) * (float)this.numberOfBlocks / 8.0F);
	        double d3 = (double)((float)(position.getZ() + 8) - MathHelper.cos(f) * (float)this.numberOfBlocks / 8.0F);
	        double d4 = (double)(position.getY() + rand.nextInt(3) - 2);
	        double d5 = (double)(position.getY() + rand.nextInt(3) - 2);

	        for (int i = 0; i < this.numberOfBlocks; ++i) {
	            float f1 = (float)i / (float)this.numberOfBlocks;
	            double d6 = d0 + (d1 - d0) * (double)f1;
	            double d7 = d4 + (d5 - d4) * (double)f1;
	            double d8 = d2 + (d3 - d2) * (double)f1;
	            double d9 = rand.nextDouble() * (double)this.numberOfBlocks / 16.0D;
	            double d10 = (double)(MathHelper.sin((float)Math.PI * f1) + 1.0F) * d9 + 1.0D;
	            double d11 = (double)(MathHelper.sin((float)Math.PI * f1) + 1.0F) * d9 + 1.0D;
	            int j = MathHelper.floor(d6 - d10 / 2.0D);
	            int k = MathHelper.floor(d7 - d11 / 2.0D);
	            int l = MathHelper.floor(d8 - d10 / 2.0D);
	            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
	            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
	            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

	            for (int l1 = j; l1 <= i1; ++l1)
	            {
	                double d12 = ((double)l1 + 0.5D - d6) / (d10 / 2.0D);

	                if (d12 * d12 < 1.0D)
	                {
	                    for (int i2 = k; i2 <= j1; ++i2)
	                    {
	                        double d13 = ((double)i2 + 0.5D - d7) / (d11 / 2.0D);

	                        if (d12 * d12 + d13 * d13 < 1.0D)
	                        {
	                            for (int j2 = l; j2 <= k1; ++j2)
	                            {
	                                double d14 = ((double)j2 + 0.5D - d8) / (d10 / 2.0D);

	                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D)
	                                {
	                                    BlockPos blockpos = new BlockPos(l1, i2, j2);

	                                    IBlockState state = worldIn.getBlockState(blockpos);
	                                    
	                                    if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, it->true)) {
	                                        worldIn.setBlockState(blockpos, this.oreBlock, 2);
	                                    }
	                                }
	                            }
	                        }
	                    }
	                }
	            }
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
