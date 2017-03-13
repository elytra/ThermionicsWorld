package com.elytradev.thermionics.world.block;

import com.elytradev.thermionics.world.ThermionicsWorld;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGemrock extends Block {
	public BlockGemrock(String name, EnumDyeColor dyeColor) {
		super(Material.ROCK, dyeColor.getMapColor());
		
		this.setRegistryName("gemrock."+name);
		this.setUnlocalizedName("thermionics_world.gemrock."+name);
		
		this.setHarvestLevel("pickaxe", 1);
		this.setHardness(1.5f);
		this.setResistance(25f);
		
		this.setCreativeTab(ThermionicsWorld.TAB_THERMIONICS_WORLD);
		
		this.setDefaultState(blockState.getBaseState().withProperty(BlockVarieties.VARIANT, 0));
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockVarieties.VARIANT);
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockVarieties.VARIANT, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockVarieties.VARIANT);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(BlockVarieties.VARIANT);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i=0; i<16; i++) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    	return getDefaultState().withProperty(BlockVarieties.VARIANT, meta);
    }
}
