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
package com.elytradev.thermionics.world;

import com.elytradev.thermionics.world.gen.WorldProviderNeoHell;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "thermionics_world", name="Thermionics|World", version="@VERSION@")
public class ThermionicsWorld {
	
	public static CreativeTabs TAB_THERMIONICS_WORLD = new CreativeTabs("thermionics_world") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.NETHERRACK);
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		System.out.println("Unregistering dimension!");
		DimensionManager.unregisterDimension(-1);
		DimensionType neohellType = DimensionType.register("Neo-Hell", "_neohell", -1, WorldProviderNeoHell.class, false);
		
		DimensionManager.registerDimension(-1, neohellType);
		WorldProvider provider = DimensionManager.createProviderFor(-1);
		System.out.println(provider.getDimensionType());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
}
