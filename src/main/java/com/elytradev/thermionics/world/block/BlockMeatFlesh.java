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

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class BlockMeatFlesh extends BlockMeat implements IItemNamer {
	public static final PropertyEnum<EnumFlesh> VARIANT = PropertyEnum.<EnumFlesh>create("variant", EnumFlesh.class);
	
	public BlockMeatFlesh() {
		super("flesh");
		this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumFlesh.ROTTEN_FLESH));
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return getMetaFromValue(state.getValue(VARIANT));
	}
	
	public static int getMetaFromValue(EnumFlesh flesh) {
		return flesh.ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, EnumFlesh.valueOf(meta));
	}
	
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(this, 1, this.getMetaFromState(state));
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(meta); //Same as superclass but just being sure. And a little more direct.
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (tab!=this.getCreativeTabToDisplayOn()) return;
		
		for(int i=0; i<EnumFlesh.values().length; i++) {
			list.add(new ItemStack(ItemBlock.getItemFromBlock(this), 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		IBlockState equivalentState = this.getStateFromMeta(stack.getItemDamage());
		EnumFlesh variant = equivalentState.getValue(VARIANT);
		
		return "tile.thermionics_world.meat.flesh."+variant.getName()+".name";
	}
	
	@Override
	public String getLocalizedName(ItemStack stack) {
		return I18n.translateToLocal(this.getUnlocalizedName(stack));
	}
}
