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

import java.text.NumberFormat;
import java.util.HashMap;

import blue.endless.libnoise.Interpolate;
import blue.endless.libnoise.Module;
import blue.endless.libnoise.generator.Perlin;
import net.minecraft.world.World;

public class BiomeModule {
	protected World world;
	private BiomeMap<ICompositorBiome> biomeMap;
	protected Module biome1 = null;
	protected Module biome2 = null;
	protected Perlin xTurb = new Perlin().setSeed(0).setFrequency(1/1.1);
	protected Perlin zTurb = new Perlin().setSeed(4).setFrequency(1/1.1);
	NumberFormat fmt = NumberFormat.getInstance();
	
	protected HashMap<ICompositorBiome, IBiomeChunkGenerator> spawnedGenerators = new HashMap<>();
	
	public BiomeModule(Module a, Module b) {
		this.biome1 = a;
		this.biome2 = b;
		
		fmt.setMaximumFractionDigits(2);
		fmt.setMinimumFractionDigits(2);
	}
	
	public BiomeModule setBiomeMap(BiomeMap<ICompositorBiome> map) {
		this.biomeMap = map;
		return this;
	}
	
	public BiomeModule setWorld(World world) {
		this.world = world;
		return this;
	}
	
	
	public double getHeightValue(int x, int z) {
		double a = biomeA(x, 0, z);
		double b = biomeB(x, 0, z);
		
		double scaledA = a * (getBiomeMap().cellsWide) - 0.5;
		double scaledB = b * (getBiomeMap().cellsHigh) - 0.5;
		
		int a1 = (int)scaledA;
		int a2 = a1 + 1; if (a2>=getBiomeMap().cellsWide) a2 = getBiomeMap().cellsWide - 1;
		int b1 = (int)scaledB;
		int b2 = b1 + 1; if (b2>=getBiomeMap().cellsHigh) b2 = getBiomeMap().cellsHigh - 1;
		
		double fa = scaledA - a1;
		double fb = scaledB - b1;
		
		double sw = getOrCreateGenerator(getBiomeMap().getValueInt(a1,b1)).getHeightValue(x, z); //sw = clamp(sw);
		double se = getOrCreateGenerator(getBiomeMap().getValueInt(a2,b1)).getHeightValue(x, z); //se = clamp(se);
		double nw = getOrCreateGenerator(getBiomeMap().getValueInt(a1,b2)).getHeightValue(x, z); //nw = clamp(nw);
		double ne = getOrCreateGenerator(getBiomeMap().getValueInt(a2,b2)).getHeightValue(x, z); //ne = clamp(ne);
		
		//double sw = biomeMap.getValueInt(a1,b1).getColumnHeight((int)x, (int)z); sw = clamp(sw);
		//double se = biomeMap.getValueInt(a2,b1).getColumnHeight((int)x, (int)z); se = clamp(se);
		//double nw = biomeMap.getValueInt(a1,b2).getColumnHeight((int)x, (int)z); nw = clamp(nw);
		//double ne = biomeMap.getValueInt(a2,b2).getColumnHeight((int)x, (int)z); ne = clamp(ne);
		double s = Interpolate.linear(sw, se, fa);
		double n = Interpolate.linear(nw, ne, fa);
		
		return Interpolate.linear(s, n, fb);
	}
	
	public double getDensityValue(int x, int y, int z) {
		double a = biomeA(x, 0, z);
		double b = biomeB(x, 0, z);
		
		double scaledA = a * (getBiomeMap().cellsWide) - 0.5;
		double scaledB = b * (getBiomeMap().cellsHigh) - 0.5;
		
		int a1 = (int)scaledA;
		int a2 = a1 + 1; if (a2>=getBiomeMap().cellsWide) a2 = getBiomeMap().cellsWide - 1;
		int b1 = (int)scaledB;
		int b2 = b1 + 1; if (b2>=getBiomeMap().cellsHigh) b2 = getBiomeMap().cellsHigh - 1;
		
		double fa = scaledA - a1;
		double fb = scaledB - b1;
		
		double sw = getOrCreateGenerator(getBiomeMap().getValueInt(a1,b1)).getDensityValue(x, y, z);
		double se = getOrCreateGenerator(getBiomeMap().getValueInt(a2,b1)).getDensityValue(x, y, z);
		double nw = getOrCreateGenerator(getBiomeMap().getValueInt(a1,b2)).getDensityValue(x, y, z);
		double ne = getOrCreateGenerator(getBiomeMap().getValueInt(a2,b2)).getDensityValue(x, y, z);
		
		double s = Interpolate.linear(sw, se, fa);
		double n = Interpolate.linear(nw, ne, fa);
		
		return Interpolate.linear(s, n, fb);
	}
	
	public IBiomeChunkGenerator getGenerator(int x, int z) {
		ICompositorBiome biome = biomeAtTurbulent(x,z);
		return getOrCreateGenerator(biome);
	}
	
	public IBiomeChunkGenerator getOrCreateGenerator(ICompositorBiome biome) {
		if (!spawnedGenerators.containsKey(biome)) {
			IBiomeChunkGenerator gen = biome.createChunkGenerator(this.world);
			spawnedGenerators.put(biome, gen);
			return gen;
		}
		
		return spawnedGenerators.get(biome);
	}
	
	public double getDensityScale(int x, int z) {
		double a = biomeA(x, 0, z);
		double b = biomeB(x, 0, z);
		
		double scaledA = a * (getBiomeMap().cellsWide) - 0.5;
		double scaledB = b * (getBiomeMap().cellsHigh) - 0.5;
		
		int a1 = (int)scaledA;
		int a2 = a1 + 1; if (a2>=getBiomeMap().cellsWide) a2 = getBiomeMap().cellsWide - 1;
		int b1 = (int)scaledB;
		int b2 = b1 + 1; if (b2>=getBiomeMap().cellsHigh) b2 = getBiomeMap().cellsHigh - 1;
		
		double fa = scaledA - a1;
		double fb = scaledB - b1;
		
		double sw = getBiomeMap().getValueInt(a1,b1).getDensityScale(); sw = clamp(sw);
		double se = getBiomeMap().getValueInt(a2,b1).getDensityScale(); se = clamp(se);
		double nw = getBiomeMap().getValueInt(a1,b2).getDensityScale(); nw = clamp(nw);
		double ne = getBiomeMap().getValueInt(a2,b2).getDensityScale(); ne = clamp(ne);
		double s = Interpolate.linear(sw, se, fa);
		double n = Interpolate.linear(nw, ne, fa);
		
		return Interpolate.linear(s, n, fb);
	}
	
	public double getRepresentativeValue(double x, double y, double z) {
		int boxSize = 4;
		
		//return a representative value for visualization
		int xplane = (int)Math.round(biomeA(x,y,z)*boxSize);
		int zplane = (int)Math.round(biomeB(x,y,z)*boxSize);
		//int xplane = (int)(biomeA(x, y, z) * boxSize);
		//int zplane = (int)(biomeB(x, y, z) * boxSize);
		int result = xplane * boxSize + zplane;
		return result / (double)(boxSize*boxSize);
	}
	
	public ICompositorBiome biomeAtClear(int x, int z) {
		double a = biomeA(x, 0, z);
		double b = biomeB(x, 0, z);
		
		double scaledA = a * (getBiomeMap().cellsWide) - 0.5;
		double scaledB = b * (getBiomeMap().cellsHigh) - 0.5;
		
		int a1 = (int)Math.round(scaledA);
		int b1 = (int)Math.round(scaledB);
		
		return getBiomeMap().getValueInt(a1, b1);
	}
	
	public ICompositorBiome biomeAtTurbulent(int x, int z) {
		double turbPower = 6.0;
		double dx = xTurb.getValue(x, 0, z)*turbPower;
		double dz = zTurb.getValue(x, 0, z)*turbPower;
		double a = biomeA(x+dx, 0, z+dz);
		double b = biomeB(x+dx, 0, z+dz);
		
		double scaledA = a * (getBiomeMap().cellsWide) - 0.5;
		double scaledB = b * (getBiomeMap().cellsHigh) - 0.5;
		
		int a1 = (int)Math.round(scaledA);
		int b1 = (int)Math.round(scaledB);
		
		return getBiomeMap().getValueInt(a1, b1);
	}
	
	public double biomeA(double x, double y, double z) {
		double d = biome1.getValue(x, y, z) / 2.0 + 0.5;
		if (d<0) d=0;
		if (d>1) d=1;
		return d;
	}
	
	public double biomeB(double x, double y, double z) {
		double d = biome2.getValue(x, y, z) / 2.0 + 0.5;
		if (d<0) d=0;
		if (d>1) d=1;
		return d;
	}
	
	private static double clamp(double value) {
		if (value<0) return 0;
		if (value>1) return 1;
		return value;
	}
	
	public BiomeMap<ICompositorBiome> getBiomeMap() {
		return biomeMap;
	}
}
