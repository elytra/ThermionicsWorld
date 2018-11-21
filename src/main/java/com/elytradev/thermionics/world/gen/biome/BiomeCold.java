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

package com.elytradev.thermionics.world.gen.biome;

import com.elytradev.thermionics.world.block.TWBlocks;

import blue.endless.libnoise.generator.Perlin;
import net.minecraft.world.World;

public class BiomeCold extends HellCompositorBiome {

	public BiomeCold() {
		super("cold");
		
		a = 1;
		b = 0;
		
		/* Original properties:
		 * base height: 128f
		 * temperature: 0.625f
		 * rainfall: 0f
		 * rain-disabled: true
		 * 
		 * surfaceMaterial: GEMROCK_MAGNESITE
		 * terrainFillMaterial: GEMROCK_SAPPHIRE
		 * densitySurfaceMaterial: GEMROCK_OPAL
		 * densityCoreMaterial: PACKED_ICE
		 * types: NETHER, COLD
		 */
		
		this.topBlock = TWBlocks.GEMROCK_MAGNESITE.getDefaultState();
		this.fillerBlock = TWBlocks.GEMROCK_SAPPHIRE.getDefaultState();
		
	}
	
	@Override
	public IBiomeChunkGenerator createChunkGenerator(World world) {
		final int intSeed = (int)world.getWorldInfo().getSeed() ^ (int)(world.getWorldInfo().getSeed() >> 32);
		
		return new IBiomeChunkGenerator() {
			private Perlin rolling = new Perlin().setFrequency(1/100.0).setOctaveCount(3).setSeed(intSeed + 3);
			
			@Override
			public double getHeightValue(int x, int z) {
				double h = rolling.getValue(x, -512, z) / 2.0 + 0.5;
				if (h<0) h=0;
				if (h>1) h=1;
				
				return (int)(h * 32) - 8;
			}

			@Override
			public double getDensityValue(double x, double y, double z) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};
	}
}
