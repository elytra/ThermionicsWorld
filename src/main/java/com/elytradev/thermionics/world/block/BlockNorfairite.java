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

import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockNorfairite extends BlockColored implements IItemNamer {
	public BlockNorfairite(String variety) {
		super(Material.GLASS);
		this.setRegistryName("norfairite."+variety);
		this.setUnlocalizedName("thermionics_world.norfairite."+variety);
		this.setCreativeTab(ThermionicsWorld.TAB_THERMIONICS_WORLD);
		this.setSoundType(ThermionicsWorld.SOUNDTYPE_SQUISH);
		
		this.setHardness(1.0f);
		this.setResistance(15.0f);
		this.setHarvestLevel("axe", 0);
		this.setLightLevel(1.0f);
		this.setLightOpacity(0);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			BlockBeacon.updateColorAsync(worldIn, pos);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
	
	@Override
	public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {
		world.setBlockState(pos, this.getDefaultState().withProperty(BlockColored.COLOR, color));
		return true;
	}
	
	//Correct as of "World of Color"
	private int[] equivalents = {
		0xE9ECEC, //White
		0xF07613, //Orange
		0xBD44B3, //Magenta
		0x3AAFD9, //Light Blue
		0xF8C627, //Yellow
		0x70B919, //Lime
		0xED8DAC, //Pink
		0x3E4447, //Gray
		0x8E8E86, //Silver (LightGray)
		0x158991, //Cyan
		0x792AAC, //Purple
		0x35399D, //Blue
		0x724728, //Brown
		0x546D1B, //Green
		0xA12722, //Red
		0x141519, //Black
	};
	
	@Override
	public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
		int equivalent = equivalents[this.getMetaFromState(state)];
		int red   = (equivalent >> 16) & 0xFF;
		int green = (equivalent >>  8) & 0xFF;
		int blue  = (equivalent >>  0) & 0xFF;
		return new float[]{red/256f, green/256f, blue/256f};
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
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName();
	}
	
	@Override
	public String getLocalizedName(ItemStack stack) {
		EnumDyeColor color = this.getStateFromMeta(stack.getItemDamage()).getValue(BlockColored.COLOR);
		String localColor = I18n.translateToLocal("color."+color.getUnlocalizedName());
		return I18n.translateToLocalFormatted(getUnlocalizedName()+".name", localColor);
	}
}
