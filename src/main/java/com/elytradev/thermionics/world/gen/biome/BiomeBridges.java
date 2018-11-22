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

import com.elytradev.thermionics.world.gen.ImageModule;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BiomeBridges extends HellCompositorBiome {
	
	public BiomeBridges() {
		super("bridges");
		
		a = 0;
		b = 0;
		
		/* Original properties:
		 * base height: 128f
		 * temperature: 0.25f
		 * rainfall: 0f
		 * rain-disabled: true
		 * 
		 * surfaceMaterial: NETHERRACK
		 * terrainFillMaterial: NETHERRACK
		 * densitySurfaceMaterial: NETHERRACK
		 * densityCoreMaterial: SOUL_SAND
		 * types: NETHER
		 */
		
		this.topBlock = Blocks.NETHERRACK.getDefaultState();
		this.fillerBlock = Blocks.SOUL_SAND.getDefaultState();
		
	}
	
	@Override
	public IBiomeChunkGenerator createChunkGenerator(World world) {
		return new IBiomeChunkGenerator() {
			BufferedImage rough = BiomeFamily.unpackTerrainImage("rough");
			ImageModule imageData = new ImageModule()
					.setImage(rough)
					.setPixelsPerUnit(1/24.0);
					//.setAmplitudeScale(1/32.0);
			
			@Override
			public double getHeightValue(int x, int z) {
				double im = (imageData.getValue(x,0,z)*0.25) + 0.2;
				return im * 16 - 8;
			}
			
			@Override
			public double getDensityValue(double x, double y, double z) {
				return imageData.getValue(x, 0, z);
			}
			
		};
	}
}
