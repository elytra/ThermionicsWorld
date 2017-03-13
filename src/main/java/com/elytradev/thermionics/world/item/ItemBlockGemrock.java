package com.elytradev.thermionics.world.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class ItemBlockGemrock extends ItemBlock {

	public ItemBlockGemrock(Block block) {
		super(block);
		this.setUnlocalizedName(block.getUnlocalizedName());
		this.setRegistryName(block.getRegistryName());
		this.hasSubtypes = true;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String localizedRockName = I18n.translateToLocal(block.getUnlocalizedName()+".name");
		return I18n.translateToLocalFormatted("thermionics_world.brickvariety."+stack.getItemDamage(), localizedRockName);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> items) {
		for(int i=0; i<5; i++) {
			items.add(new ItemStack(this,1,i)); //TODO: Add more varieties as textures are filled in
		}
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
