/*
 * MIT License
 *
 * Copyright (c) 2017-2018 Isaac Ellingson (Falkreon) and contributors
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

import com.elytradev.thermionics.world.Benchmark;
import com.elytradev.thermionics.world.block.TWBlocks;
import com.elytradev.thermionics.world.gen.biome.ICompositorBiome;
import com.elytradev.thermionics.world.gen.biome.BiomeModule;
import com.elytradev.thermionics.world.gen.biome.BiomeFamily;
import com.elytradev.thermionics.world.gen.biome.CompositorBiome;
import com.elytradev.thermionics.world.gen.biome.IBiomeChunkGenerator;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraftforge.fluids.BlockFluidBase;

public class ChunkProviderCompositor implements IChunkGenerator {
	public static final int HEIGHT = 256;
	public static final int SEA_LEVEL = 48;
	public static final int DENSITY_SCALING_START = 200;
	public static final int DENSITY_SCALING_LENGTH = HEIGHT - DENSITY_SCALING_START;
	
	//private static final long MILLIS_PER_NANO = 1_000_000L;
	

	protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
	protected static final IBlockState NETHERRACK = Blocks.NETHERRACK.getDefaultState();
	protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
	protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
	protected static final IBlockState SOUL_SAND = Blocks.SOUL_SAND.getDefaultState();
	
	protected static IBlockState PAIN;
	//protected static final IBlockState LAVA = Blocks.LAVA.getDefaultState(); //Use sparingly
	//protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
	

	protected World world;
	protected long seed;
	protected Random random;
	
	//Old noise primitives
	//protected ScaledNoiseField noiseTerrainBase;
	//protected ScaledNoiseField noiseTerrainFine;
	//protected ScaledNoiseVolume noiseVolumeBase;
	//protected ScaledNoiseVolume noiseVolumeFine;
	
	//New noise primitives
	protected BiomeModule biomeModule;
	//protected Module volumeNoise;
	

	private final NeoHellGenerators.Glowstone lightGemGen = new NeoHellGenerators.Glowstone();
	private final WorldGenFire fireFeature = new WorldGenFire();
	private final NeoHellGenerators.Lava hellSpringGen = new NeoHellGenerators.Lava(Blocks.FLOWING_LAVA);
	private MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
	private final WorldGenBush brownMushroomFeature = new WorldGenBush(Blocks.BROWN_MUSHROOM);
	private final WorldGenBush redMushroomFeature = new WorldGenBush(Blocks.RED_MUSHROOM);
	private final WorldGenerator quartzGen = new WorldGenMinable(Blocks.QUARTZ_ORE.getDefaultState(), 14, (it)->it.isFullBlock());
	private final WorldGenerator magmaGen = new WorldGenMinable(Blocks.MAGMA.getDefaultState(), 33, (it)->it.isFullBlock());
	private final NeoHellGenerators.LavaTrap lavaTrapGen = new NeoHellGenerators.LavaTrap(TWBlocks.FLUID_PAIN.getDefaultState());

	
	//Benchmarking. Do you even bench, bro?
	//int timedChunks = 0;
	//long totalTime         = 0L;
	//long totalTimeShape    = 0L;
	//long totalTimePopulate = 0L;
	//Benchmark popBench = new Benchmark();

	public ChunkProviderCompositor(World world, long seed, BiomeProviderNeo biomeProvider) {
		this.world = world;
		this.seed = seed;
		this.random = new Random(seed);
		
		//int intSeed = (int)seed; intSeed ^= (int)(seed >> 32);
		
		//Perlin biomeA = new Perlin().setSeed(intSeed+1).setFrequency(1/379.0).setOctaveCount(7);
		//Perlin biomeB = new Perlin().setSeed(intSeed+2).setFrequency(1/367.0).setOctaveCount(6);
		//this.biomeModule = new BiomeModule(biomeA, biomeB);
		
		
		
		//this.biomeModule.setBiomeMap(map);

		//this.noiseTerrainBase = new ScaledNoiseField(random.nextLong(), 64f);
		//this.noiseTerrainFine = new ScaledNoiseField(random.nextLong(), 32f);
		//this.noiseVolumeBase = new ScaledNoiseVolume(random.nextLong(), 40f);
		//this.noiseVolumeFine = new ScaledNoiseVolume(random.nextLong(), 20f);
		//this.biomeSelector = new VoronoiClusterField<NeoBiome>(random.nextLong(), 16*9);
		
		/*
		
		RidgedMulti mountains = new RidgedMulti().setFrequency(1/256.0).setOctaveCount(6).setSeed(intSeed);
		Module      plains    = new ScaleBias().setSources(
		                        new Perlin()     .setFrequency(1/128.0).setOctaveCount(2).setSeed(intSeed+1)
		                        ).setScale(0.25).setBias(-0.70);
		Perlin      mask      = new Perlin()     .setFrequency(1/256.0).setOctaveCount(3).setSeed(intSeed+2);
		Blend       blend     = new Blend().setSources(mountains, plains, mask);
		
		volumeNoise = blend;
		*/
		PAIN = TWBlocks.FLUID_PAIN.getDefaultState().withProperty(BlockFluidBase.LEVEL, 3);
		
		biomeModule = biomeProvider.getBiomeModule();
		biomeModule.setWorld(world);
	}

	protected void generateShape(int chunkX, int chunkZ, ChunkPrimer primer) {
		for(int z=0; z<16; z++) {
			for(int x=0; x<16; x++) {
				int blockX = chunkX*16+x;
				int blockZ = chunkZ*16+z;
				
				//Function<Integer, IBlockState> terrainMaterialFunction = (it)->Blocks.NETHERRACK.getDefaultState();
				//Function<Float, IBlockState> densityMaterialFunction = (it)->Blocks.NETHERRACK.getDefaultState();
				ICompositorBiome columnBiome = biomeModule.biomeAtTurbulent(blockX, blockZ);
				IBiomeChunkGenerator generator = biomeModule.getOrCreateGenerator(columnBiome);
				
				double columnHeightScale = biomeModule.getHeightValue(blockX, blockZ);
				
				//if (columnHeightScale<0) columnHeightScale *= 0.5;
				
				int columnHeight = SEA_LEVEL + (int)columnHeightScale;
				if (columnHeight<8) columnHeight = 8;
				
				//We sample from y=-512.0 because that's out-of-bounds and sufficiently decorrelated from the volumetric
				//nose we sample later
				//columnHeight += (volumeNoise.getValue(blockX, -512.0, blockZ)/2+0.5) * 128;
				
				//double densityScale = biomeModule.getDensityScale(blockX, blockZ);
				
				for(int y=0; y<255; y++) {
					//float density = 1.5f;
					
					//double density = volumeNoise.getValue(blockX, y, blockZ)/2+0.5;
					double density = biomeModule.getDensityValue(blockX, y, blockZ);
					/*
					float density = noiseVolumeBase.get(blockX, y, blockZ);
					density*= 0.75f;
					density = density + (noiseVolumeFine.get(blockX, y, blockZ)-0.5f)*0.25f;
					if (y>DENSITY_SCALING_START) {
						float densityProgress = (y - DENSITY_SCALING_START) / (float)DENSITY_SCALING_LENGTH;
						float scalingFactor = (float)Math.cos(densityProgress*Math.PI/2f);
						density *= scalingFactor;
					}
					if (density>1.0f) density=1.0f;
					*/
					IBlockState cur = AIR;
					if (y==0) {
						cur = BEDROCK;
					} else if (y<=(int)columnHeight) {
						//if (density>0.1) {
							int depth = columnHeight - y;
							cur = generator.getHeightBlockState(blockX, blockZ, depth);
							//if (columnBiome instanceof CompositorBiome) {
							//	cur = (depth<=4) ? ((CompositorBiome)columnBiome).topBlock : ((CompositorBiome)columnBiome).fillerBlock;
							//}
							//cur = terrainMaterialFunction.apply((int)columnHeight-y);
						/*} else { //Extremely low densities carve pits and caves into terrain
							if (y<SEA_LEVEL) {
								cur = PAIN;
							} else {
								cur = AIR;
							}
							
						}*/
					} else {
						//scale density
						if (y>DENSITY_SCALING_START) {
							float densityProgress = (y - DENSITY_SCALING_START) / (float)DENSITY_SCALING_LENGTH;
							float scalingFactor = (float)Math.cos(densityProgress*Math.PI/2f);
							density *= scalingFactor;
						}
						if (density>1.0f) density=1.0f;
						
						
						if (density>0.5) {
							cur = generator.getDensityBlockState(blockX, y, blockZ, density); // engage nougat
							
							/*
							if (columnBiome instanceof CompositorBiome) {
								
								cur = ((CompositorBiome)columnBiome).topBlock; //TODO: BRING BACK NOUGAT
							}*/
							
							
							//cur = densityMaterialFunction.apply(((float)density-0.5f)*2);
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
	public Chunk generateChunk(int x, int z) {
		Benchmark genBench = new Benchmark();
		genBench.startFrame();
		
		//this.random.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		generateShape(x,z,chunkprimer);
		//this.prepareHeights(x, z, chunkprimer);
		//this.buildSurfaces(x, z, chunkprimer);
		//this.genNetherCaves.generate(this.world, x, z, chunkprimer);

		//if (this.generateStructures) {
			this.genNetherBridge.generate(this.world, x, z, chunkprimer);
		//}

		Chunk chunk = new Chunk(this.world, chunkprimer, x, z);

		//Copy biomes in from biomeProvider
		//Biome[] abiome = this.world.getBiomeProvider().getBiomes((Biome[])null, x * 16, z * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();
		
		for(int zi=0; zi<16; zi++) {
			for(int xi=0; xi<16; xi++) {
				int baseX = x; if (baseX<0) baseX-=1; baseX*=16;
				int baseZ = z; if (baseZ<0) baseZ-=1; baseZ*=16;
				
				
				Biome vanillaBiome = world.getBiome(new BlockPos(baseX+xi, 0, baseZ+zi));
				byte id = (byte)Biome.getIdForBiome(Biomes.HELL);
				if (vanillaBiome instanceof ICompositorBiome) {
					id = BiomeFamily.NEO_HELL.getId((ICompositorBiome)vanillaBiome);
					//id = (byte)BiomeFamily.NEO_HELL.getIDForObject((NeoBiome)vanillaBiome);
					//id = (byte)((NeoBiome)vanillaBiome).getId();
				}
				abyte[zi*16+xi] = id;
			}
		}
		
		genBench.endFrame();
		//System.out.println("GenBench frame:" +genBench.getTotalTime());
		//chunk.setTerrainPopulated(true);
		//chunk.setLightPopulated(true);
		return chunk;
	}

	@Override
	public void populate(int x, int z) {
		Benchmark popBench = new Benchmark();
		popBench.startFrame();

		BlockFalling.fallInstantly = true;

		//Let everyone else know we're populating
		net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.random, x, z, false);

		//grab chunkpos
		int blockX = x * 16;
		int blockZ = z * 16;
		BlockPos blockpos = new BlockPos(blockX, 0, blockZ);
		Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
		ChunkPos chunkpos = new ChunkPos(x, z);
		
		this.genNetherBridge.generateStructure(this.world, this.random, chunkpos);

		popBench.endSection("bridge");
		
		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_LAVA)) {
			for (int i = 0; i < 8; ++i) {
				this.hellSpringGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(255-SEA_LEVEL) + SEA_LEVEL, this.random.nextInt(16) + 8));
			}
		}

		popBench.endSection("spring");
		
		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.FIRE)) {
			for (int i = 0; i < this.random.nextInt(this.random.nextInt(10) + 1) + 1; ++i) {
				this.fireFeature.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(120) + 4, this.random.nextInt(16) + 8));
			}
		}
		
		popBench.endSection("fire");

		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.GLOWSTONE)) {
			for (int i = 0; i < this.random.nextInt(this.random.nextInt(10) + 1); ++i) {
				this.lightGemGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(120) + 4, this.random.nextInt(16) + 8));
			}

			for (int i = 0; i < 20; ++i) {
				this.lightGemGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16) + 8, this.random.nextInt(228), this.random.nextInt(16) + 8));
			}
		}
		
		popBench.endSection("gem");

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
		
		popBench.endSection("shroom");

		if (net.minecraftforge.event.terraingen.TerrainGen.generateOre(this.world, this.random, quartzGen, blockpos, net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.QUARTZ)) {
			for (int i = 0; i < 8; ++i) {
				this.quartzGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16), this.random.nextInt(208) + 10, this.random.nextInt(16)));
			}
		}

		popBench.endSection("quartz");
		
		//int i2 = this.world.getSeaLevel() / 2 + 1;
		int baseMagmaHeight = SEA_LEVEL / 2 + 1;
		
		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_MAGMA)) {
			for (int i = 0; i < 4; ++i)
			{
				this.magmaGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16), baseMagmaHeight - 5 + this.random.nextInt(10), this.random.nextInt(16)));
			}
		}
		
		popBench.endSection("magma");
		
		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.random, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.NETHER_LAVA2)) {
	        for (int i = 0; i < 16; ++i) {
	            this.lavaTrapGen.generate(this.world, this.random, blockpos.add(this.random.nextInt(16), this.random.nextInt(255-SEA_LEVEL) + SEA_LEVEL, this.random.nextInt(16)));
	        }
		}
		
		popBench.endSection("trap");
		
		biome.decorate(this.world, this.random, blockpos);
		
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post(this.world, this.random, blockpos));

		BlockFalling.fallInstantly = false;
		popBench.endFrame();
		//System.out.println("GenTime: "+popBench.getTotalTime());
		//if (popBench.getTotalTime()>200) popBench.printDebug(); //we're done doing comprehensive profiling, but alert us to issues bigger than 200msec
		
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return false;
	}

	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		if (pos==null) return ImmutableList.of();
		
		if (creatureType == EnumCreatureType.MONSTER) {
            //if (this.genNetherBridge.isInsideStructure(pos)) {
            //    return this.genNetherBridge.getSpawnList();
            //}

            if (this.genNetherBridge.isPositionInStructure(this.world, pos) && this.world.getBlockState(pos.down()).getBlock() == Blocks.NETHER_BRICK) {
                return this.genNetherBridge.getSpawnList();
            }
            
            return Biomes.HELL.getSpawnableList(EnumCreatureType.MONSTER); //TODO: Replace with actual-biome lists.
		}
		return ImmutableList.of();

		//Biome biome = this.world.getBiome(pos);
		//return biome.getSpawnableList(creatureType);
	}

	//@Override
	//public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_) {
		
		//return "Fortress".equals(structureName) && this.genNetherBridge != null ? this.genNetherBridge.getClosestStrongholdPos(worldIn, position, p_180513_4_) : null;
	//	return null;
	//}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {
		//this.genNetherBridge.generate(this.world, x, z, (ChunkPrimer)null);
	}

	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		// TODO Auto-generated method stub
		return false;
	}
}
