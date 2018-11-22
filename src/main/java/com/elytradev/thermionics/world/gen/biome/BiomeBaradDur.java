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

import java.awt.image.BufferedImage;

import com.elytradev.thermionics.world.block.TWBlocks;
import com.elytradev.thermionics.world.gen.ImageModule;

import blue.endless.libnoise.generator.Perlin;
import net.minecraft.world.World;

public class BiomeBaradDur extends HellCompositorBiome {

	public BiomeBaradDur() {
		super("barad_dur");
		
		a = 1;
		b = 1;
		
		/* Original properties:
		 * base height: 128f
		 * temperature: 0.75f
		 * rainfall: 0f
		 * rain-disabled: true
		 * 
		 * surfaceMaterial: GEMROCK_HEMATITE
		 * terrainFillMaterial: NETHERRACK
		 * densitySurfaceMaterial: GEMROCK_HEMATITE
		 * densityCoreMaterial: GEMROCK_SODALITE
		 * types: NETHER
		 */
		
		this.topBlock = TWBlocks.GEMROCK_HEMATITE.getDefaultState();
		this.fillerBlock = TWBlocks.GEMROCK_HEMATITE.getDefaultState();
	}
	
	@Override
	public IBiomeChunkGenerator createChunkGenerator(World world) {
		final int intSeed = (int)world.getWorldInfo().getSeed() ^ (int)(world.getWorldInfo().getSeed() >> 32);
		
		return new IBiomeChunkGenerator() {
			BufferedImage columns = BiomeFamily.unpackTerrainImage("columns");
			ImageModule imageData = new ImageModule()
					.setImage(columns)
					.setPixelsPerUnit(1/2.0);
			Perlin density = new Perlin()
					.setFrequency(1/128.0)
					.setOctaveCount(5)
					.setSeed(intSeed + 4);
			
			@Override
			public double getHeightValue(int x, int z) {
				double val = imageData.getValue(x, 0, z);
				if (val<0) val=0; //Shouldn't happen
				if (val>1) val=1;
				
				return (int)(val * 16);
			}

			@Override
			public double getDensityValue(double x, double y, double z) {
				//TODO: Attenuate noise near the ground?
				double d = density.getValue(x, y, z) * 1.0;
				
				return d;
			}
			
		};
	}
}
