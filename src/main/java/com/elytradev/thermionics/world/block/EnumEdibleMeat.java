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
package com.elytradev.thermionics.world.block;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public enum EnumEdibleMeat implements IStringSerializable {
	PORK    ("pork",    new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP)),
	BEEF    ("beef",    new ItemStack(Items.BEEF),     new ItemStack(Items.COOKED_BEEF)),
	CHICKEN ("chicken", new ItemStack(Items.CHICKEN),  new ItemStack(Items.COOKED_CHICKEN)),
	FISH    ("fish",    new ItemStack(Items.FISH,1,0), new ItemStack(Items.COOKED_FISH,1,0)),
	SALMON  ("salmon",  new ItemStack(Items.FISH,1,1), new ItemStack(Items.COOKED_FISH,1,1)),
	MUTTON  ("mutton",  new ItemStack(Items.MUTTON),   new ItemStack(Items.COOKED_MUTTON)),
	RABBIT  ("rabbit",  new ItemStack(Items.RABBIT),   new ItemStack(Items.COOKED_RABBIT));
	
	private final String name;
	private final ItemStack rawItem ;
	private final ItemStack cookedItem;
	
	EnumEdibleMeat(String name, ItemStack raw, ItemStack cooked) {
		this.name = name;
		this.rawItem = raw;
		this.cookedItem = cooked;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public static EnumEdibleMeat valueOf(int id) {
		for(EnumEdibleMeat meat : values()) {
			if (meat.ordinal()==id) return meat;
		}
		return BEEF; //out-of-bounds meat is Left Beef
	}
	
	public ItemStack getRawItem() {
		return rawItem;
	}
	
	public ItemStack getCookedItem() {
		return cookedItem;
	}
}