package com.elytradev.thermionics.world.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockVarieties extends ItemBlock {
	public ItemBlockVarieties(Block block) {
		super(block);
		System.out.println("#####REGISTRY NAME: "+block.getRegistryName());
		this.setRegistryName(block.getRegistryName());
	}
	
	public ItemBlockVarieties(Block block, String nameOverride) {
		super(block);
		this.setRegistryName(nameOverride);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.block.getUnlocalizedName()+"."+stack.getItemDamage();
	}
}
