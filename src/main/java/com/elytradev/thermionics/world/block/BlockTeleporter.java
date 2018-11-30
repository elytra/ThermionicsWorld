package com.elytradev.thermionics.world.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.elytradev.thermionics.world.StringExtras;
import com.elytradev.thermionics.world.ThermionicsWorld;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTeleporter extends Block {
	private String id;
	private int destination;
	
	public BlockTeleporter(String id, int destination, MapColor mapColor) {
		super(Material.ROCK, mapColor);
		
		this.id = id;
		
		this.setRegistryName("portal."+id);
		this.setUnlocalizedName("thermionics_world.portal."+id);
		
		this.setHarvestLevel("pickaxe", 0);
		this.setHardness(1.5f);
		this.setResistance(125f);
		
		this.setCreativeTab(ThermionicsWorld.TAB_THERMIONICS_WORLD);
		
		this.destination = destination;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) return true;
		
		if (worldIn.provider.getDimension()==destination) {
			System.out.println("Already in world "+worldIn.provider.getDimension());
			//TODO: Fizzle
			return false;
		} else {
			if (playerIn.isRiding() || playerIn.isBeingRidden()) return false;
			
			System.out.println("Transferring from world "+worldIn.provider.getDimension()+" to world "+destination);
			
			playerIn.changeDimension(destination, new ITeleporter() {
				@Override
				public void placeEntity(World world, Entity entity, float yaw) {
					if (entity instanceof EntityPlayer) {
						EntityPlayer spawnPlayer = (EntityPlayer)entity;
						
						System.out.println("isNether: "+world.provider.isNether()+" <- "+worldIn.provider.isNether());
						if (world.provider.isNether() || worldIn.provider.isNether()) {
							//Do some math and place them at an equivalent point in the destination.
							double factorIn = worldIn.provider.getMovementFactor();
							double factorOut = world.provider.getMovementFactor();
							
							/* overworld -> neohell   == 1->4 == 1/4
							 * neohell   -> overworld == 4->1 == 4/1
							 * zeroworld -> neohell   == 0->4 == 0/4
							 * neohell   -> zeroworld == 4->0 == 0 (special case)
							 * zeroworld doesn't exist, but it's good to know the edge behaviors.
							 */
							double conversion = (factorOut==0) ? 0 : factorIn / factorOut;
							
							/*The player is already at a corrected location, but as we do math based on both the source
							 * and destination [hell]worlds, we offset from the original clicked block instead of the
							 * player's new location.
							 */
							BlockPos loc = new BlockPos(
									(int)(pos.getX() * conversion),
									pos.getY(),
									(int)(pos.getZ() * conversion)
									);
							
							BlockPos loc2 = fixSpawnLocation(world, loc);
							if (loc2==null) loc2 = fuzzAndFix(world, loc);
							
							System.out.println("Shoving player into equivalent location: "+pos+" -> "+loc2);
							
							
							spawnPlayer.posX = loc2.getX()+0.5;
							spawnPlayer.posY = loc2.getY()+0.7;
							spawnPlayer.posZ = loc2.getZ()+0.5;
							spawnPlayer.lastTickPosX = spawnPlayer.posX;
							spawnPlayer.lastTickPosY = spawnPlayer.posY;
							spawnPlayer.lastTickPosZ = spawnPlayer.posZ;
							
						} else {
							//Shove the player somewhere valid and respawn-point-like
							System.out.println("Shoving player into a spawn-like location.");
							
							BlockPos loc = spawnPlayer.getBedLocation(destination);
							if (loc!=null) {
								loc = EntityPlayer.getBedSpawnLocation(world, loc, false);
							}
							
							if (loc==null) {
								loc = world.getSpawnPoint();
							}
							
							loc = fuzzAndFix(world, loc);
							
							spawnPlayer.posX = loc.getX()+0.5;
							spawnPlayer.posY = loc.getY()+0.7;
							spawnPlayer.posZ = loc.getZ()+0.5;
							spawnPlayer.lastTickPosX = spawnPlayer.posX;
							spawnPlayer.lastTickPosY = spawnPlayer.posY;
							spawnPlayer.lastTickPosZ = spawnPlayer.posZ;
						}
					} else {
						BlockPos spawn = world.getSpawnPoint();
						entity.posX = spawn.getX()+0.5;
						entity.posY = spawn.getY()+0.7;
						entity.posZ = spawn.getZ()+0.5;
					}
				}
				
			});
			return true;
		}
	}
	
	/** Returns true if the player would spawn in two blocks of air, with a valid solid surface beneath them */
	private static boolean isGoodSpawn(World world, BlockPos pos) {
		return
				 world.isAirBlock(pos) &&
				 world.isAirBlock(pos.up()) &&
				!world.isAirBlock(pos.down()) &&
				 world.isSideSolid(pos.down(), EnumFacing.UP);
	}
	
	private static Random fuzz = new Random();
	private static int fuzz(int in) {
		return in + fuzz.nextInt(5) - fuzz.nextInt(2);
	}
	private static BlockPos fuzzAndFix(World world, BlockPos pos) {
		if (isGoodSpawn(world, pos)) return pos;
		
		for(int i=0; i<10; i++) {
			BlockPos lateral = new BlockPos(fuzz(pos.getX()), pos.getY(), fuzz(pos.getZ()));
			lateral = fixSpawnLocation(world, lateral);
			if (lateral!=null) return lateral;
		}
		
		return pos; //Give up.
	}
	
	@Nullable
	private static BlockPos fixSpawnLocation(World world, BlockPos pos) {
		if (isGoodSpawn(world, pos)) return pos;
		
		//Try to scan upwards for a good spot
		for(int y=pos.getY()+1; y<255; y++) {
			BlockPos fix = new BlockPos(pos.getX(), y, pos.getZ());
			if (isGoodSpawn(world, fix)) return fix;
		}
		
		//Failing that, scan downwards.
		for(int y=pos.getY()-1; y>0; y--) {
			BlockPos fix = new BlockPos(pos.getX(), y, pos.getZ());
			if (isGoodSpawn(world, fix)) return fix;
		}
		
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag advanced) {
		StringExtras.addSplitInformation("tooltip.thermionics_world.portal."+id, "§9§o", tooltip);
	}
}
