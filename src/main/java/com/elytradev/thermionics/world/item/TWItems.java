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
	public static List<ItemStack> GROUP_GEMROCK = null;
	
	/*Static item accessors. Take the constants at the bottom, apply metadata magic, and produce useful ItemStacks!*/
	
	public static ItemStack meat(EnumEdibleMeat kind, boolean cooked) {
		return new ItemStack(TerrainBlocks.MEAT_EDIBLE, 1, BlockMeatEdible.getMetaFromValue(kind, cooked));
	}
	
	/*Quick item references. These aren't always the most useful due to heavy metadata gimmicks.*/
	
	//@ObjectHolder("meat.flesh")
	//public static final ItemBlockMeatEdible MEAT_EDIBLE = null;
	
}
