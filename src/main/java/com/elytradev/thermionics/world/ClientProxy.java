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

package com.elytradev.thermionics.world;

import java.util.ArrayList;

import com.elytradev.thermionics.world.gen.biome.BiomeFamily;
import com.elytradev.thermionics.world.gen.biome.ICompositorBiome;
import com.elytradev.thermionics.world.item.ItemBlockEquivalentState;
import com.elytradev.thermionics.world.item.ItemBlockGemrock;
import com.elytradev.thermionics.world.item.ItemBlockVarieties;
import com.elytradev.thermionics.world.item.TWItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class ClientProxy extends Proxy {
	
	@Override
	public void preInit() {}
	
	@SubscribeEvent
	public void onRegisterModel(ModelRegistryEvent event) {
		for(Item i : TWItems.itemsForModels()) {
			registerItemModel(i);
		}
	}
	
	
	public void registerItemModel(Item item) {
		ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
		if (item instanceof ItemBlockEquivalentState) {
			
			NonNullList<ItemStack> subItems = NonNullList.create();
			item.getSubItems(ThermionicsWorld.TAB_THERMIONICS_WORLD, subItems);
			for(ItemStack stack : subItems) {
				Item stackItem = stack.getItem();
				if (stackItem!=item) continue; //The contract of getSubItems prohibits this condition.
				String keys = ((ItemBlockEquivalentState)item).getStateStringForItem(stack);
				//System.out.println("Registering model for "+loc+"#"+keys+" == meta:"+stack.getItemDamage());
				
				ModelLoader.setCustomModelResourceLocation(stackItem, stack.getItemDamage(),
						new ModelResourceLocation(loc, keys)
						);
			}
		} else if (item instanceof ItemBlockGemrock) {
			for(int i=0; i<16; i++) {
				ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(loc,"variant="+i));
			}
		} else if (item instanceof ItemBlockVarieties) {
			for(int i=0; i<16; i++) {
				ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(loc,"variant="+i));
			}
		} else {
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(loc, "inventory"));
		}
	}
	
	/**
	 * When possible, presents corrected biome information in the F3 overlay for Neo-Hell
	 * @param event
	 */
	@SubscribeEvent
	public void renderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
		if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) return;
		
		if (Minecraft.getMinecraft().world.provider.getDimension()!=-1) return;
		
		
		ArrayList<String> left = event.getLeft();
		final int LINE_BIOME = 11;
		if (left.size()<=LINE_BIOME || !left.get(LINE_BIOME).startsWith("Biome:")) return; //Someone's already tampered

		
		String biomeName = "MEMES";
		BlockPos pos = Minecraft.getMinecraft().player.getPosition();
		Chunk chunk = Minecraft.getMinecraft().world.getChunkFromBlockCoords(pos);
		byte[] biomeArray = chunk.getBiomeArray();
		int x = pos.getX() & 15;
		int z = pos.getZ() & 15;
		int biomeId = biomeArray[z*16+x];
		ICompositorBiome biome = BiomeFamily.NEO_HELL.get(biomeId);
		if (biome!=null) biomeName = biome.getName();
		
		left.set(LINE_BIOME, "Biome: "+biomeName+" ( "+biomeId+" )");
	}
}
