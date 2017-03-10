/**
 * MIT License
 *
 * Copyright (c) 2017 Isaac Ellingson (Falkreon) and contributors
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

public class ScaledNoiseVolume {
	private long randomSeed;
	private float scale;
	Random random = new Random(0L);
	
	private int splorchX = 0;
	private int splorchZ = 0;
	private int splorchY = 0;
	
	private int popX = 0;
	private int popZ = 0;
	private int popY = 0;
	
	private float[] data = new float[8];
	
	public ScaledNoiseVolume(long randomSeed, float scale) {
		this.randomSeed = randomSeed;
		this.scale = scale;
		random.setSeed(randomSeed);
	}
	
	private static int pos(int x, int y, int z) {
		return (y*4)+(z*2)+x;
	}
	
	public static long hash(int x, int y, int z) {
		long xi = ScaledNoiseField.swizzle(x) * 452930477L;
		long yi = ScaledNoiseField.swizzle(y) * 633910099L;
		long zi = ScaledNoiseField.swizzle(z) * 715225741L;
		long interleave = 3*xi | (3*zi + 1) | (3*yi + 2);
		
		return interleave;
	}
	
	
	/**
	 * Recenters on the indicated absolute (block) coordinates
	 */
	public void recenter(int x, int y, int z) {
		int baseX = (int) (x/scale);  if (x<0) baseX-=1;
		int baseZ = (int) (z/scale);  if (z<0) baseZ-=1;
		int baseY = (int) (y/scale);  if (y<0) baseY-=1;
		
		if (baseX==popX && baseZ==popZ && baseY==popY) return;
		
		
		if (data==null) data = new float[2 * 2 * 2];
		for(int zi=0; zi<2; zi++) {
			for(int xi=0; xi<2; xi++) {
				for(int yi=0; yi<2; yi++) {
					long hash = randomSeed ^ hash(baseX+xi, baseY+yi, baseZ+zi);
					
					random.setSeed(hash);
					float element = (float) random.nextFloat();
					data[pos(xi,yi,zi)] = element;
				}
			}
		}
		
		popX = baseX;
		popZ = baseZ;
		popY = baseY;
	}
	

	/**
	 * Gets the interpolated noise value at the indicated absolute (block) coordinates
	 * @return a normally distributed value with mean 0 and standard deviation 1.
	 */
	public float get(int x, int y, int z) {
		int baseX = x + splorchX;
		int baseZ = z + splorchZ;
		int baseY = y + splorchY;
		
		recenter(baseX,baseY,baseZ);

		float xProgress = baseX % scale / scale; if (baseX<0) xProgress+=1;
		float zProgress = baseZ % scale / scale; if (baseZ<0) zProgress+=1;
		float yProgress = baseY % scale / scale; if (baseY<0) yProgress+=1;

		//interpolate Z
		float uwest =ScaledNoiseField.weightedAverage(data[pos(0,1,0)], data[pos(0,1,1)], zProgress);
		float ueast =ScaledNoiseField.weightedAverage(data[pos(1,1,0)], data[pos(1,1,1)], zProgress);
		
		float lwest =ScaledNoiseField.weightedAverage(data[pos(0,0,0)], data[pos(0,0,1)], zProgress);
		float least =ScaledNoiseField.weightedAverage(data[pos(1,0,0)], data[pos(1,0,1)], zProgress);
		
		//interpolate Y
		float west = ScaledNoiseField.weightedAverage(lwest, uwest, yProgress);
		float east = ScaledNoiseField.weightedAverage(least, ueast, yProgress);
		
		//interpolate X and return the result
		return ScaledNoiseField.weightedAverage(west, east, xProgress);
	}
}