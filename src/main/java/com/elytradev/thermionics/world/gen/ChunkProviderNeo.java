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
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.fluids.BlockFluidBase;

public class ChunkProviderNeo implements IChunkGenerator {
	public static final int HEIGHT = 256;
	public static final int SEA_LEVEL = 64;
	public static final int DENSITY_SCALING_START = 200;
	public static final int DENSITY_SCALING_LENGTH = HEIGHT - DENSITY_SCALING_START;

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

	private final NeoHellGenerators.Glowstone lightGemGen = new NeoHellGenerators.Glowstone();
	private final WorldGenFire fireFeature = new WorldGenFire();
	private final NeoHellGenerators.Lava hellSpringGen = new NeoHellGenerators.Lava(Blocks.FLOWING_LAVA, false);
	private MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
	private final WorldGenBush brownMushroomFeature = new WorldGenBush(Blocks.BROWN_MUSHROOM);
	private final WorldGenBush redMushroomFeature = new WorldGenBush(Blocks.RED_MUSHROOM);
	private final WorldGenerator quartzGen = new WorldGenMinable(Blocks.QUARTZ_ORE.getDefaultState(), 14, (it)->it.isFullBlock());
	private final WorldGenerator magmaGen = new WorldGenMinable(Blocks.MAGMA.getDefaultState(), 33, (it)->it.isFullBlock());
	private final NeoHellGenerators.Lava lavaTrapGen = new NeoHellGenerators.Lava(Blocks.FLOWING_LAVA, true);
	
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
					if (y>DENSITY_SCALING_START) {
						float densityProgress = (y - DENSITY_SCALING_START) / (float)DENSITY_SCALING_LENGTH;
						float scalingFactor = (float)Math.cos(densityProgress*Math.PI/2f);
						density *= scalingFactor;
					}

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

	}


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
		//Biome[] abiome = this.world.getBiomeProvider().getBiomes((Biome[])null, x * 16, z * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();

		Biome hellBiomeBase = Biome.REGISTRY.getObject(new ResourceLocation("hell"));

		for (int i = 0; i < abyte.length; ++i) {

			abyte[i] = (hellBiomeBase==null) ? 0 : (byte)Biome.getIdForBiome(hellBiomeBase);

			//abyte[i] = (byte)Biome.getIdForBiome(abiome[i]);
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

		BlockFalling.fallInstantly = true;

		//Let everyone else know we're populating
		net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.random, x, z, false);

		//grab chunkpos
		int i = x * 16;
		int j = z * 16;
		BlockPos blockpos = new BlockPos(i, 0, j);
		Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
		ChunkPos chunkpos = new ChunkPos(x, z);

		this.genNetherBridge.generateStructure(this.world, this.random, chunkpos);

		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_LAVA)) {
			for (int k = 0; k < 8; ++k) {
				this.hellSpringGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(255-SEA_LEVEL) + SEA_LEVEL, this.random.nextInt(16) + 8));
			}
		}

		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.FIRE)) {
			for (int i1 = 0; i1 < this.random.nextInt(this.random.nextInt(10) + 1) + 1; ++i1) {
				this.fireFeature.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(120) + 4, this.random.nextInt(16) + 8));
			}
		}

		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.GLOWSTONE)) {
			for (int j1 = 0; j1 < this.random.nextInt(this.random.nextInt(10) + 1); ++j1) {
				this.lightGemGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(120) + 4, this.random.nextInt(16) + 8));
			}

			for (int k1 = 0; k1 < 20; ++k1) {
				this.lightGemGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(228), this.random.nextInt(16) + 8));
			}
		}

		net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.random, x, z, false);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre(this.world, this.random, blockpos));


		if (net.minecraftforge.event.terraingen.TerrainGen.decorate(this.world, this.random, blockpos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM)) {
			if (this.random.nextBoolean()) {
				this.brownMushroomFeature.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(228), this.random.nextInt(16) + 8));
			}

			if (this.random.nextBoolean()) {
				this.redMushroomFeature.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(228), this.random.nextInt(16) + 8));
			}
		}

		if (net.minecraftforge.event.terraingen.TerrainGen.generateOre(this.world, this.random, quartzGen, blockpos, net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.QUARTZ)) {
			for (int l1 = 0; l1 < 16; ++l1) {
				this.quartzGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16), this.random.nextInt(208) + 10, this.random.nextInt(16)));
			}
		}

		//int i2 = this.world.getSeaLevel() / 2 + 1;
		int baseMagmaHeight = SEA_LEVEL / 2 + 1;
		
		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_MAGMA)) {
			for (int l = 0; l < 4; ++l)
			{
				this.magmaGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16), baseMagmaHeight - 5 + this.random.nextInt(10), this.random.nextInt(16)));
			}
		}
		
		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_LAVA2)) {
	        for (int j2 = 0; j2 < 16; ++j2) {
	            this.lavaTrapGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16), this.random.nextInt(255-SEA_LEVEL) + SEA_LEVEL, this.random.nextInt(16)));
	        }
		}
		
		
		
		biome.decorate(this.world, this.random, new BlockPos(i, 0, j));
		
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post(this.world, this.random, blockpos));

		BlockFalling.fallInstantly = false;
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
