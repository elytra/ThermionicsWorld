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

package com.elytradev.thermionics.world.gen;

import java.util.Random;

public class ScaledNoiseField {
	private long randomSeed;
	private float scale;
	Random random = new Random(0L);
	//private BufferedImage sampler;
	//private Image rescaled;
	
	private int splorchX = 0;
	private int splorchZ = 0;
	
	//private int popX = 0;
	//private int popZ = 0;
	
	//private float[] data = new float[4];
	
	public ScaledNoiseField(long randomSeed, float scale) {
		this.randomSeed = randomSeed;
		this.scale = scale;
		random.setSeed(randomSeed);
		//this.splorchX = random.nextInt(0x0000FFFF);
		//this.splorchZ = random.nextInt(0x0000FFFF);
	}
	
	public static long swizzle(long i) {
		if (i >= 0) {
			return 2 * i;
		} else {
			return -2 * i - 1;
		}
	}
	
	public static long hash(int x, int z) {
		long xi = swizzle(x) * 452930477L;
		long zi = swizzle(z) * 715225741L;
		long interleave = 2*xi | (2*zi + 1);
		
		return interleave;
	}
	
	
	/**
	 * Recenters on the indicated absolute (block) coordinates
	 */
	public float[] recenter(int x, int z) {
		int baseX = (int) (x/scale); if (x<0) baseX-=1;
		int baseZ = (int) (z/scale); if (z<0) baseZ-=1;
		
		//if (baseX==popX && baseZ==popZ) return;
		
		float[] data = new float[2 * 2];
		for(int zi=0; zi<2; zi++) {
			for(int xi=0; xi<2; xi++) {
				random.setSeed((randomSeed*31L) + hash(baseX+xi, baseZ+zi));
				
				float element = (float) random.nextFloat();
				data[zi*2+xi] = element;
			}
		}
		
		return data;
		//popX = baseX;
		//popZ = baseZ;
	}
	

	public static float weightedAverage(float a, float b, float weight) {
		//return a;
		
		if (weight<=0) return a;
		if (weight>=1) return b;
		return a*(1-weight) + b*weight;
	}
	
	public static float planarAverage(float nw, float ne, float sw, float se, float xProgress, float zProgress) {
		float west = weightedAverage(nw, sw, zProgress);
		float east = weightedAverage(ne, se, zProgress);

		return weightedAverage(west, east, xProgress);
	}
	
	/**
	 * Takes a value between -1 and 1, and turns it into a value between -1 and 1.
	 * The difference, however, is that dynamic range is compressed and renormalized
	 * so that features have more of a rounded, parabolic field.
	 */
	public static float filter(float a) {
		if (a<-1) a=-1;
		if (a>1) a=1;
		//Rescale the 0..1 number so that it's 0..PI
		float theta = a*(float)Math.PI;
		float result = (float)Math.cos(theta + Math.PI);
		
		//re-rescale to 0..1
		return (result + 1) / 2;
	}
	
	public float normalizeAgainstScale(float f) {
		int integerPart = (int)(f/scale);
		integerPart*=scale;
		float fractionalPart = f - integerPart;
		fractionalPart = fractionalPart / scale;
		if (fractionalPart<0) fractionalPart = 0;
		if (fractionalPart>0.9999f) fractionalPart = 0.9999f;
		
		
		return fractionalPart;
	}
	
	/**
	 * Gets the interpolated noise value at the indicated absolute (block) coordinates
	 * @return a normally distributed value with mean 0 and standard deviation 1.
	 */
	public float get(int x, int z) {
		
		int baseX = x + splorchX + (int)(scale/2);
		int baseZ = z + splorchZ + (int)(scale/2);
		
		float[] data = recenter(baseX,baseZ);
		float xProgress = (baseX % scale) / scale;
		if (baseX<0) xProgress+=1;
		float zProgress = (baseZ % scale) / scale;
		if (baseZ<0) zProgress+=1;
		
		//interpolate vertically
		float west = weightedAverage(data[0], data[2], zProgress);
		float east = weightedAverage(data[1], data[3], zProgress);
		
		//interpolate horizontally and return the result
		return weightedAverage(west, east, xProgress);
	}
	
	public float getFiltered(int x, int z) {
		return filter(get(x,z));
	}
}
