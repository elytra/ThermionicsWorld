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

import com.elytradev.thermionics.world.ThermionicsWorld;

import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBasalt extends BlockObsidian {
	public static PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 15);
	
	public BlockBasalt() {
		super();
		this.setCreativeTab(ThermionicsWorld.TAB_THERMIONICS_WORLD);
		this.setHardness(2.5f);
		this.setResistance(2000.0f);
		this.setHarvestLevel("pickaxe",2);
		this.setRegistryName("obsidian");
		this.setUnlocalizedName("thermionics_world.basalt");
		
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
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i=0; i<16; i++) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
    	return EnumPushReaction.BLOCK;
    }
}
