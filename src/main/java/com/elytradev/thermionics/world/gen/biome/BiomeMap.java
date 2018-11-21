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

/**
 * Fresh biome hardware for fractal hell and fractal sky
 */
public class BiomeMap<E> {
	protected E fallback;
	protected int cellsHigh = 5;
	protected int cellsWide = 5;
	@SuppressWarnings("unchecked")
	protected E[] data = (E[])new Object[cellsWide*cellsHigh];
	
	public BiomeMap() {
		
	}
	
	public BiomeMap(E fallback) {
		this.fallback = fallback;
	}
	
	
	public BiomeMap<E> setFallback(E e) {
		this.fallback = e;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public BiomeMap<E> setSize(int width, int height) {
		if (width==cellsHigh && height==cellsWide) return this;
		
		cellsWide = width;
		cellsHigh = height;
		data = (E[])new Object[cellsWide*cellsHigh];
		
		return this;
	}
	
	public BiomeMap<E> setBiome(int x, int y, E biome) {
		if (x<0 || y<0 || x>=cellsWide || y>=cellsHigh) return this;
		data[y*cellsWide + x] = biome;
		
		if (fallback==null) fallback = biome;
		
		return this;
	}
	
	public E getValue(double x, double y) {
		if (x<0) x=0; if (x>1) x=1;
		if (y<0) y=0; if (y>1) y=1;
		
		int cellX = (int)(x * cellsWide);
		int cellY = (int)(y * cellsHigh);
		int idx = cellY*cellsWide + cellX;
		if (idx>=data.length) idx = data.length-1;
		E result = data[idx];
		return (result!=null) ? result : fallback;
	}
	
	public E getValueInt(int x, int y) {
		if (x<0) x=0; if (x>=cellsWide) x=cellsWide-1;
		if (y<0) y=0; if (y>=cellsHigh) y=cellsHigh-1;
		int idx = y*cellsWide + x;
		if (idx>=data.length) idx = data.length-1;
		E result = data[idx];
		return (result!=null) ? result : fallback;
	}
}
