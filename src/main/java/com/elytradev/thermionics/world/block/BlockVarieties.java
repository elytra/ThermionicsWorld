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

package com.elytradev.thermionics.world.block;

import com.elytradev.thermionics.world.ThermionicsWorld;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVarieties extends Block {
	public static PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 15);
	
	private String baseVariant = "variant=0";
	
	public BlockVarieties(String id, Material material, MapColor color) {
		super(material, color);
		
		this.setCreativeTab(ThermionicsWorld.TAB_THERMIONICS_WORLD);
		this.setHardness(1.5f);
		this.setResistance(10.0f);
		this.setRegistryName("thermionics_world", id);
		this.setUnlocalizedName("thermionics_world."+id);
		
		this.setDefaultState(blockState.getBaseState());
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (tab!=CreativeTabs.SEARCH && tab!=this.getCreativeTabToDisplayOn()) return;

		for (int i=0; i<16; i++) {
			list.add(new ItemStack(ItemBlock.getItemFromBlock(this), 1, i));
		}
	}

	public String getBaseVariant() {
		return baseVariant;
	}
}
