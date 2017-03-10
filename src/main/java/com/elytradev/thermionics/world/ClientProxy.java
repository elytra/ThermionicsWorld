package com.elytradev.thermionics.world;

import com.elytradev.thermionics.world.item.ItemBlockVarieties;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;


public class ClientProxy extends Proxy {
	
	@Override
	public void init() {}
	
	@Override
	public void registerItemModel(Item item) {
		ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
		System.out.println("#######Registering "+loc);
		if (item instanceof ItemBlockVarieties) {
			for(int i=0; i<16; i++) {
				
				ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(loc,"variant="+i));
			}
		} else {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(loc, "inventory"));
		}
	}
}
