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

package com.elytradev.thermionics.world.gen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fluids.BlockFluidBase;

public class ChunkProviderNeo implements IChunkGenerator {
	public static final int HEIGHT = 256;
	public static final int SEA_LEVEL = 64;
	
	protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState NETHERRACK = Blocks.NETHERRACK.getDefaultState();
    protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
    protected static IBlockState PAIN;
    //protected static final IBlockState LAVA = Blocks.LAVA.getDefaultState(); //Use sparingly
    //protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    //protected static final IBlockState SOUL_SAND = Blocks.SOUL_SAND.getDefaultState();
	
	protected World world;
	protected long seed;
	protected Random random;
	
	protected ScaledNoiseField noiseTerrainBase;
	protected ScaledNoiseField noiseTerrainFine;
	
	protected ScaledNoiseVolume noiseVolumeBase;
	
	
	//Old ChunkProviderHell stuff
	double[] buffer;
	
	
	public ChunkProviderNeo(World world, long seed) {
		this.world = world;
		this.seed = seed;
		this.random = new Random(seed);
		
		this.noiseTerrainBase = new ScaledNoiseField(random.nextLong(), 64f);
		this.noiseTerrainFine = new ScaledNoiseField(random.nextLong(), 32f);
		this.noiseVolumeBase = new ScaledNoiseVolume(random.nextLong(), 40f);
		
		PAIN = Block.getBlockFromName("thermionics_world:pain").getDefaultState().withProperty(BlockFluidBase.LEVEL, 3);
	}
	
	protected void generateShape(int chunkX, int chunkZ, ChunkPrimer primer) {

		
		for(int z=0; z<16; z++) {
			for(int x=0; x<16; x++) {
				
				float cell = 16;
				
				int blockX = chunkX*16+x;
				int blockZ = chunkZ*16+z;
				
				cell += noiseTerrainBase.getFiltered(blockX, blockZ)*128;
				cell += noiseTerrainFine.getFiltered(blockX, blockZ)* 32;
				int columnHeight = (int)cell;

				for(int y=0; y<255; y++) {
					float density = noiseVolumeBase.get(blockX, y, blockZ);
					
					IBlockState cur = AIR;
					if (y==0) {
						cur = BEDROCK;
					} else if (y<columnHeight) {
						cur = NETHERRACK;
					} else {
						if (density>0.6f) {
							cur=STONE;
						} else {
							if (y<SEA_LEVEL) cur=PAIN;
						}
						
					}
					
					primer.setBlockState(x, y, z, cur);
				}
			}
		}
		
		//if (numErroredHeights>0) System.out.println("Errored heights: "+numErroredHeights);
	}

	/*
	public void prepareHeights(int p_185936_1_, int p_185936_2_, ChunkPrimer primer) {
        int i = 4;
        int j = this.world.getSeaLevel() / 2 + 1;
        int k = 5;
        int l = 17;
        int i1 = 5;
        this.buffer = this.getHeights(this.buffer, p_185936_1_ * 4, 0, p_185936_2_ * 4, 5, 17, 5);

        for (int j1 = 0; j1 < 4; ++j1)
        {
            for (int k1 = 0; k1 < 4; ++k1)
            {
                for (int l1 = 0; l1 < 16; ++l1)
                {
                    double d0 = 0.125D;
                    double d1 = this.buffer[((j1 + 0) * 5 + k1 + 0) * 17 + l1 + 0];
                    double d2 = this.buffer[((j1 + 0) * 5 + k1 + 1) * 17 + l1 + 0];
                    double d3 = this.buffer[((j1 + 1) * 5 + k1 + 0) * 17 + l1 + 0];
                    double d4 = this.buffer[((j1 + 1) * 5 + k1 + 1) * 17 + l1 + 0];
                    double d5 = (this.buffer[((j1 + 0) * 5 + k1 + 0) * 17 + l1 + 1] - d1) * 0.125D;
                    double d6 = (this.buffer[((j1 + 0) * 5 + k1 + 1) * 17 + l1 + 1] - d2) * 0.125D;
                    double d7 = (this.buffer[((j1 + 1) * 5 + k1 + 0) * 17 + l1 + 1] - d3) * 0.125D;
                    double d8 = (this.buffer[((j1 + 1) * 5 + k1 + 1) * 17 + l1 + 1] - d4) * 0.125D;

                    for (int i2 = 0; i2 < 8; ++i2)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.25D;
                        double d13 = (d4 - d2) * 0.25D;

                        for (int j2 = 0; j2 < 4; ++j2)
                        {
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * 0.25D;

                            for (int k2 = 0; k2 < 4; ++k2)
                            {
                                IBlockState iblockstate = null;

                                if (l1 * 8 + i2 < j)
                                {
                                    iblockstate = LAVA;
                                }

                                if (d15 > 0.0D)
                                {
                                    iblockstate = NETHERRACK;
                                }

                                int l2 = j2 + j1 * 4;
                                int i3 = i2 + l1 * 8;
                                int j3 = k2 + k1 * 4;
                                primer.setBlockState(l2, i3, j3, iblockstate);
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }*/
	
	
	@Override
	public Chunk provideChunk(int x, int z) {
		this.random.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        generateShape(x,z,chunkprimer);
        //this.prepareHeights(x, z, chunkprimer);
        //this.buildSurfaces(x, z, chunkprimer);
        //this.genNetherCaves.generate(this.world, x, z, chunkprimer);

        //if (this.generateStructures)
        //{
        //    this.genNetherBridge.generate(this.world, x, z, chunkprimer);
        //}

        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        
        //Copy biomes in from biomeProvider
        Biome[] abiome = this.world.getBiomeProvider().getBiomes((Biome[])null, x * 16, z * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte)Biome.getIdForBiome(abiome[i]);
        }

        chunk.resetRelightChecks();
        return chunk;
		
		
		
		/* #### old NeoHellGen ####
		//System.out.println("NeoHellGen: "+x+","+z);
		this.random.setSeed(x * 341873128712L + z * 132897987541L);
		Block[] ablock = new Block[16*16*256];
		byte[] meta = new byte[ablock.length];
		//BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager()
		//		.loadBlockGeneratorData((BiomeGenBase[]) null, x * 16, z * 16, 16, 16);
		
		this.generateShape(x, z, ablock, meta);
		//this.replaceBiomeBlocks(x, z, ablock, meta, abiomegenbase);
		//this.genNetherBridge.func_151539_a(this, this.worldObj, x, z, ablock);
		Chunk chunk = new Chunk(this.world, x, z);
		
		
		
		byte[] abyte = chunk.getBiomeArray();

		for (int k = 0; k < abyte.length; ++k) {
			abyte[k] = (byte) Biome.getIdForBiome(Biomes.HELL);
		}
		
		chunk.generateSkylightMap();
		//chunk.enqueueRelightChecks();
		//chunk.generateHeightMap();
		chunk.resetRelightChecks();
		//chunk.isTerrainPopulated = false;
		
		
		return chunk;*/
	}

	@Override
	public void populate(int x, int z) {
		
		//TODO: Unpack and selectively enable
		
		/*
		BlockFalling.fallInstantly = true;
        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, false);
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        ChunkPos chunkpos = new ChunkPos(x, z);
        this.genNetherBridge.generateStructure(this.world, this.rand, chunkpos);

        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_LAVA))
        for (int k = 0; k < 8; ++k)
        {
            this.hellSpringGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }

        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.FIRE))
        for (int i1 = 0; i1 < this.rand.nextInt(this.rand.nextInt(10) + 1) + 1; ++i1)
        {
            this.fireFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }

        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.GLOWSTONE))
        {
        for (int j1 = 0; j1 < this.rand.nextInt(this.rand.nextInt(10) + 1); ++j1)
        {
            this.lightGemGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }

        for (int k1 = 0; k1 < 10; ++k1)
        {
            this.hellPortalGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
        }
        }//Forge: End doGLowstone

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, false);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre(this.world, this.rand, blockpos));

        if (net.minecraftforge.event.terraingen.TerrainGen.decorate(this.world, this.rand, blockpos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM))
        {
        if (this.rand.nextBoolean())
        {
            this.brownMushroomFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
        }

        if (this.rand.nextBoolean())
        {
            this.redMushroomFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
        }
        }

        if (net.minecraftforge.event.terraingen.TerrainGen.generateOre(this.world, this.rand, quartzGen, blockpos, net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.QUARTZ))
        for (int l1 = 0; l1 < 16; ++l1)
        {
            this.quartzGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
        }

        int i2 = this.world.getSeaLevel() / 2 + 1;

        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_MAGMA))
        for (int l = 0; l < 4; ++l)
        {
            this.magmaGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), i2 - 5 + this.rand.nextInt(10), this.rand.nextInt(16)));
        }

        if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_LAVA2))
        for (int j2 = 0; j2 < 16; ++j2)
        {
            this.lavaTrapGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
        }

        biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));

        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post(this.world, this.rand, blockpos));

        BlockFalling.fallInstantly = false;*/
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return false;
	}

	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER) { /*
            if (this.genNetherBridge.isInsideStructure(pos)) {
                return this.genNetherBridge.getSpawnList();
            }

            if (this.genNetherBridge.isPositionInStructure(this.world, pos) && this.world.getBlockState(pos.down()).getBlock() == Blocks.NETHER_BRICK) {
                return this.genNetherBridge.getSpawnList();
            }*/
        }

        Biome biome = this.world.getBiome(pos);
        return biome.getSpawnableList(creatureType);
    }

	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_) {
		//return "Fortress".equals(structureName) && this.genNetherBridge != null ? this.genNetherBridge.getClosestStrongholdPos(worldIn, position, p_180513_4_) : null;
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {
		//this.genNetherBridge.generate(this.world, x, z, (ChunkPrimer)null);
	}
}
