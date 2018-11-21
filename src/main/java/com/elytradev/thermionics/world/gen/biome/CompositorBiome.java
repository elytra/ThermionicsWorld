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

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class CompositorBiome extends Biome implements ICompositorBiome {
	protected String name;
	protected double densityScale = 0.5;
	protected int seed = 0;
	protected int color = 0xFF_777777;
	protected int skyColor = 0xFF_FF2687;
	protected int a = 0;
	protected int b = 0;
	
	public CompositorBiome(String id) {
		super(new BiomeProperties(I18n.translateToLocal("biome.thermionics."+id)));
		this.name = id;
		this.setRegistryName(id); //TODO: Switch to ResourceLocation
	}
	
	protected CompositorBiome(String id, BiomeProperties props) {
		super(props);
		this.name = id;
		this.setRegistryName(id); //TODO: Switch to ResourceLocation
	}
	
	public CompositorBiome setDensityScale(double d) {
		this.densityScale = d;
		return this;
	}
	
	public CompositorBiome setColor(int c) {
		this.color = c | 0xFF000000;
		return this;
	}
	
	public CompositorBiome setSkyColor(int c) {
		this.skyColor = c | 0xFF000000;
		return this;
	}
	
	
	
	//EXTENDS net.minecraft.world.biome.Biome {
		@Override
		public boolean canRain() {
			return false;
		}
		
		@Override
		public void decorate(World worldIn, Random rand, BlockPos pos) {
			//skip until we need this
		}
		
		@SideOnly(Side.CLIENT)
		@Override
		public int getSkyColorByTemp(float currentTemperature) {
			return skyColor;
		}
		
		@Override
		public TempCategory getTempCategory() {
			return Biome.TempCategory.WARM;
		}
	//}
	
	
	//IMPLEMENTS ICompositorBiome {
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public int getColor() {
			return color;
		}
		
		@Override
		public double getDensityScale() {
			return densityScale;
		}
		
		@Override
		public IBiomeChunkGenerator createChunkGenerator(World world) {
			return new IBiomeChunkGenerator() {
				@Override
				public double getHeightValue(int x, int z) {
					return 0;
				}
				
				@Override
				public double getDensityValue(double x, double y, double z) {
					return 0;
				}
				
			};
		}
		
		@Override
		public int getA() {
			return a;
		}

		@Override
		public int getB() {
			return b;
		}
	//}
}
