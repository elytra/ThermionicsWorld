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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.elytradev.thermionics.world.ThermionicsWorld;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockShrubBone extends Block {
	public static final PropertyEnum<BoneType> VARIANT = PropertyEnum.create("variant", BoneType.class);
	
	public BlockShrubBone() {
		super(Material.ROCK, MapColor.SAND); //Same as BlockBone
		this.setRegistryName("shrub.bone");
		this.setUnlocalizedName("thermionics_world.shrub.bone");
		this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, BoneType.FEMUR));
		this.setCreativeTab(ThermionicsWorld.TAB_THERMIONICS_WORLD);
		
		this.setHardness(0.5f);
		this.setResistance(10f);
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, BoneType.values()[meta % BoneType.values().length]);
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		if (random.nextBoolean()) {
			return 1+fortune;
		} else {
			return 0+(fortune/2);
		}
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return quantityDroppedWithBonus(fortune, random);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		HashSet<Integer> validMetas = new HashSet<Integer>();
		for(IBlockState state : blockState.getValidStates()) {
			validMetas.add(getMetaFromState(state));
		}
		
		for(int meta : validMetas) {
			list.add(new ItemStack(ItemBlock.getItemFromBlock(this), 1, meta));
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		super.getDrops(world, pos, state, fortune);
		
		ItemStack bone = new ItemStack(Items.BONE);
		ArrayList<ItemStack> result = new ArrayList<>();
		result.add(bone);
		return result;
	}
	
	public static enum BoneType implements IStringSerializable {
		FEMUR("femur"),
		CLAW("claw");
		
		private final String name;
		
		BoneType(String name) { this.name = name; }
		
		@Override
		public String getName() {
			return name;
		}
	}
}
