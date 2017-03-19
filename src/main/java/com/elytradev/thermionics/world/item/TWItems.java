package com.elytradev.thermionics.world.item;

import java.util.List;

import com.elytradev.thermionics.world.block.BlockMeatEdible;
import com.elytradev.thermionics.world.block.EnumEdibleMeat;
import com.elytradev.thermionics.world.block.TerrainBlocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("thermionics_world")
public class TWItems {
	/*ItemGroups! DO NOT USE DURING PRE-INIT! These things are filled in there. Init is okay.*/
	public static List<ItemStack> GROUP_MEAT_RAW = null;
	public static List<ItemStack> GROUP_MEAT_COOKED = null;
	public static List<ItemStack> GROUP_BLOCK_MEAT_RAW = null;
	public static List<ItemStack> GROUP_BLOCK_MEAT_COOKED = null;
	
	/*Static item accessors. Take the constants at the bottom, apply metadata magic, and produce useful ItemStacks!*/
	
	public static ItemStack meat(EnumEdibleMeat kind, boolean cooked) {
		return new ItemStack(TerrainBlocks.MEAT_EDIBLE, 1, BlockMeatEdible.getMetaFromValue(kind, cooked));
	}
	
	/*Quick item references. These aren't always the most useful due to heavy metadata gimmicks.*/
	
	//@ObjectHolder("meat.flesh")
	//public static final ItemBlockMeatEdible MEAT_EDIBLE = null;
	
}
