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

import java.awt.Color;

import com.elytradev.thermionics.world.ThermionicsWorld;

import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNorfairite extends BlockColored {
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
	
	private Color[] equivalents = {
		Color.WHITE,
		Color.ORANGE,
		Color.MAGENTA,
		new Color(0x6B8AC9), //Light Blue
		Color.YELLOW,
		new Color(0x41AE38), //Lime
		new Color(0xD08499), //Pink
		new Color(0x404040), //Gray
		new Color(0x9AA1A1), //LightGray, which is now apparently called "silver"
		new Color(0x2E6E89), //Cyan
		new Color(0x7E3DB5), //Purple
		new Color(0x7E3DB5), //Purple
		new Color(0x7E3DB5), //Purple
		new Color(0x7E3DB5), //Purple
		new Color(0x7E3DB5), //Purple
		Color.RED,
		
			
	};
	@Override
	public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
		Color equivalent = equivalents[this.getMetaFromState(state)];
		return new float[]{equivalent.getRed()/256f, equivalent.getGreen()/256f, equivalent.getBlue()/256f};
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
