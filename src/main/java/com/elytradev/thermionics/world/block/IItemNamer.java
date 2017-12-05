package com.elytradev.thermionics.world.block;

import net.minecraft.item.ItemStack;

public interface IItemNamer {
	public String getUnlocalizedName(ItemStack stack);
	public String getLocalizedName(ItemStack stack);
}
