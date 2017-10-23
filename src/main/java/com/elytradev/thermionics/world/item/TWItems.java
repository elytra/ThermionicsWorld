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
package com.elytradev.thermionics.world.item;

import java.util.ArrayList;
import java.util.List;

import com.elytradev.thermionics.world.block.BlockMeatEdible;
import com.elytradev.thermionics.world.block.EnumEdibleMeat;
import com.elytradev.thermionics.world.block.TWBlocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class TWItems {
	private static List<Block> blocksThatNeedItems = new ArrayList<>();
	private static List<Item> itemsThatNeedModels = new ArrayList<>();
	
	/*ItemGroups! DO NOT USE DURING PRE-INIT! These things are filled in there. Init is okay.*/
	public static List<ItemStack> GROUP_MEAT_RAW = null;
	public static List<ItemStack> GROUP_MEAT_COOKED = null;
	public static List<ItemStack> GROUP_BLOCK_MEAT_RAW = null;
	public static List<ItemStack> GROUP_BLOCK_MEAT_COOKED = null;
	public static List<ItemStack> GROUP_GEMROCK = null;
	
	/*Static item accessors. Take the constants at the bottom, apply metadata magic, and produce useful ItemStacks!*/
	
	public static ItemStack meat(EnumEdibleMeat kind, boolean cooked) {
		return new ItemStack(TWBlocks.MEAT_EDIBLE, 1, BlockMeatEdible.getMetaFromValue(kind, cooked));
	}
	
	public static void scheduleForItem(Block b) {
		blocksThatNeedItems.add(b);
	}
	/*Quick item references. These aren't always the most useful due to heavy metadata gimmicks.*/
	
	//@ObjectHolder("meat.flesh")
	//public static final ItemBlockMeatEdible MEAT_EDIBLE = null;
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		
		for(Block b : blocksThatNeedItems) {
			ItemBlockEquivalentState item = new ItemBlockEquivalentState(b);
			//item.setRegistryName(b.getRegistryName());
			r.register(item);
			itemsThatNeedModels.add(item);
		}
	}
	
	public static List<Item> itemsForModels() {
		return itemsThatNeedModels;
	}
}
