package com.elytradev.thermionics.world.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class ItemBlockColored extends ItemBlockEquivalentState {

	public ItemBlockColored(Block block) {
		super(block);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		EnumDyeColor color = this.getStateForItem(stack).getValue(BlockColored.COLOR);
		String localColor = I18n.translateToLocal("color."+color.getUnlocalizedName());
		return I18n.translateToLocalFormatted(block.getUnlocalizedName()+".name", localColor);
	}
}
