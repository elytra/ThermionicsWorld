package com.elytradev.thermionics.world.block;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;

public class BlockGroup {
	private ArrayList<IBlockState> elements = new ArrayList<>();
	
	public BlockGroup() {}
	
	public void add(IBlockState state) {
		elements.add(state);
	}
}
