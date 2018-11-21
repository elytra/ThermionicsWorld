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

import java.util.ArrayList;

import blue.endless.libnoise.generator.Voronoi;

public class VoronoiClusterField<E> {
	private ArrayList<E> cells = new ArrayList<E>();
	//private long epicenterBase;
	//private ScaledNoiseField a;
	//private ScaledNoiseField b;
	//public Epicenter<E>[] centers;
	//private int centerX;
	//private int centerZ;
	Voronoi base;
	public int scale;
	
	public VoronoiClusterField(long seed, int scale) {
		//Random baseRandom = new Random(seed);
		//epicenterBase = baseRandom.nextLong();
		base = new Voronoi((int)seed ^ ((int)(seed >> 32))).setFrequency(1.0/scale).setEnableDistance(false);
		//a = new ScaledNoiseField(baseRandom.nextLong(), scale);//scale);
		//b = new ScaledNoiseField(baseRandom.nextLong(), scale);//scale);
		this.scale = scale;
	}
	
	
	public void registerCell(E e) {
		cells.add(e);
	}
	
	public void replaceCell(E old, E replacement) {
		int index = cells.indexOf(old);
		if (index != -1) cells.add(index, replacement);
	}
	
	public void unregisterCell(E e) {
		cells.remove(e);
	}
	
	/*
	public E cellFor(double a, double b) {
		double bestDistance = Double.MAX_VALUE;
		Entry<E> best = null;
		for(Entry<E> entry : cells) {
			double dist = Math.abs(entry.a-a) + Math.abs(entry.b-b);
			if (best==null || dist<bestDistance) {
				best = entry;
				bestDistance = dist;
			}
		}
		
		return best.e;
	}*/
	
	/*
	public static float rescale(int coord, int scale) {
		float result = coord / (float)scale;
		if (coord<0) result -= 1;
		return result;
	}*/
	
	//TAKES RESCALED COORDINATES
	/*
	@SuppressWarnings("unchecked")
	public Epicenter<E>[] recenterOn(int x, int z) {
		int cx = (int)rescale(x, scale);
		int cz = (int)rescale(z, scale);
		//if (x<0) cx--;
		//if (z<0) cz--;
		
		//if (centers==null || centerX!=cx || centerZ!=cz) {
			Epicenter<E>[] centers = new Epicenter[9];
			//centerX = cx;
			//centerZ = cz;
			int i=0;
			for(int zi=-1; zi<=1; zi++) {
				for(int xi=-1; xi<=1; xi++) {
					Epicenter<E> cur = new Epicenter<E>(cx+xi, cz+zi, this.epicenterBase, scale);
					//float cellParamA = a.get(x+xi, z+zi);
					//float cellParamB = b.get(x+xi, z+zi);
					
					//cur.e = cellFor(cellParamA, cellParamB);
					centers[i] = cur;
					i++;
				}
			}
		//} //otherwise just leave the pregenerated data where it is.
			return centers;
	}*/
	
	
	public E get(int x, int z) {
		//float cx = rescale(x, scale);
		//float cz = rescale(z, scale);
		
		/*
		Epicenter<E>[] centers = recenterOn(x, z);
		float bestDist = Float.MAX_VALUE;
		E bestE = null;
		for(Epicenter<E> cur : centers) {
			float dist = cur.mapDistanceSquared(x, z);
			//System.out.println("DIST: "+dist);
			if (bestE==null || dist<bestDist) {
				//System.out.println("SELECTED");
				bestE = cur.e;
				bestDist = dist;
			}
		}
		
		return bestE;*/
		
		
		
		
		double cell = base.getValue(x, 0, z);
		
		int cellNumber = (int)(cell * cells.size());
		if (cellNumber<0) cellNumber = 0;
		if (cellNumber>=cells.size()) cellNumber = cells.size()-1;
		
		return cells.get(cellNumber);
		
		/*
		double bestDistance = Double.MAX_VALUE;
		Entry<E> best = null;
		for(Entry<E> entry : cells) {
			double dist = Math.abs(entry.a-cell);
			if (best==null || dist<bestDistance) {
				best = entry;
				bestDistance = dist;
			}
		}
		
		return best.e;*/
	}
	
	/*
	public HashMap<String, Double> getStats(int x, int z) {
		Epicenter<E>[] centers = recenterOn(x,z);
		HashMap<String, Double> result = new HashMap<String, Double>();
		for(Epicenter<E> cur : centers) {
			
		}
		
		return result;
	}*/
	
	public void setSeed(long newSeed) {
		//base.setSeed((int)newSeed ^ ((int)(newSeed >> 32)));
	}
	
	public boolean containsCell(E cell) {
		return cells.contains(cell);
	}
	

//	private static class Entry<E> {
//		public E e;
//		//public float a;
//		//public float b;
//		
//		public Entry(E e) {
//			this.e = e;
//			//this.a = a;
//			//this.b = b;
//		}
//	}
//	
//	public static class Epicenter<E> {
//		/** The X coordinate of this epicenter, in voronoi space */
//		public float cellX;
//		/** The Z coordinate of this epicenter, in voronoi space */
//		public float cellZ;
//		/** The X coordinate of this epicenter, in 0..1 */
//		public float fractionX;
//		/** The Z coordinate of this epicenter, in 0..1 */
//		public float fractionZ;
//		/** The cell-type picked for this voronoi cell */
//		public E e;
//		/** The number of map coordinates per voronoi cell, used in measuring distances to cell centers. */
//		public int scale;
//		
//		/**
//		 * Create an epicenter entry
//		 * @param x
//		 * @param z
//		 * @param seed
//		 * @param scale
//		 */
//		public Epicenter(int x, int z, long seed, int scale) {
//			this.scale = scale;
//			
//			long locationSeed = (seed*31) + (x*61507) + (z*54727);
//			Random rand = new Random(locationSeed);
//			this.fractionX = rand.nextFloat();
//			this.fractionZ = rand.nextFloat();
//			
//			this.cellX = x + fractionX;
//			this.cellZ = z + fractionZ;
//		}
//		
//		/**
//		 * Gets the square of the map distance from the center of this cell to the given coordinates
//		 * @param x the destination X in non-rescaled coordinates (map space)
//		 * @param z the destination Z in non-rescaled coordinates (map space)
//		 * @return the square of the distance between the cell center and the specified point
//		 */
//		public float mapDistanceSquared(int x, int z) {
//			float dx = Math.abs(this.cellX-VoronoiClusterField.rescale(x,scale));
//			float dz = Math.abs(this.cellZ-VoronoiClusterField.rescale(z,scale));
//			return dx*dx+dz*dz;
//		}
//	}
}

