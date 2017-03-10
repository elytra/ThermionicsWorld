package com.elytradev.thermionics.world.block;

import com.elytradev.thermionics.world.ThermionicsWorld;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
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
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i=0; i<16; i++) {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

	public String getBaseVariant() {
		// TODO Auto-generated method stub
		return null;
	}
}
