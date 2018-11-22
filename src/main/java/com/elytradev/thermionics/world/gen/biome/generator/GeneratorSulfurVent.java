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

package com.elytradev.thermionics.world.gen.biome.generator;

import java.util.ArrayList;
import java.util.Random;

import com.elytradev.thermionics.world.block.TWBlocks;
import com.elytradev.thermionics.world.gen.ChunkProviderCompositor;
import com.elytradev.thermionics.world.gen.NeoHellGenerators;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GeneratorSulfurVent extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {
		BlockPos cur = NeoHellGenerators.findSurface(worldIn, position);
		if (cur==null) return false;
		cur = cur.up(4); //punch through a certain amount of incline, too.
		
		ArrayList<BlockPos> buffer = new ArrayList<BlockPos>();
		//int depth = 20 + rand.nextInt(20);
		for(int i=0; i<30; i++) {
			float radius = 3f + rand.nextFloat()*4;
			
			NeoHellGenerators.cylinderAround(cur, radius, buffer);
			cur = cur.down();
		}
		
		for(BlockPos pos : buffer) {
			if (pos.getY()<ChunkProviderCompositor.SEA_LEVEL) {
				worldIn.setBlockState(pos, TWBlocks.FLUID_PAIN.getDefaultState());
			} else {
				worldIn.setBlockToAir(pos); //TODO: randomy spawn sulfurgas pockets
			}
		}
		
		return true;
	}
	
}
