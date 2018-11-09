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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

import blue.endless.libnoise.generator.Module;
import blue.endless.libnoise.generator.RidgedMulti;
import blue.endless.libnoise.generator.Spheres;
import blue.endless.libnoise.generator.Voronoi;
import blue.endless.libnoise.modifier.Multiply;
import blue.endless.libnoise.modifier.Power;

public class GenVisualizer extends JFrame {
	private static final long serialVersionUID = -6682388330686106856L;
	
	public static void main(String... args) {
		new GenVisualizer().setVisible(true);
	}
	
	
	
	public BufferedImage terrainPreview = null;
	
	
	
	public GenVisualizer() {
		Dimension d = new Dimension(3840, 2100);
		
		this.setMinimumSize(d);
		this.setSize(d);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	@Override
	public void paint(Graphics g) {
		if (terrainPreview==null) {
			terrainPreview = new BufferedImage(3840, 2100, BufferedImage.TYPE_INT_ARGB);
			generateTerrain(terrainPreview);
		}
		
		g.drawImage(terrainPreview, 0, 0, 3840, 2100, this);
	}
	
	public void generateTerrain(BufferedImage im) {
		//First, visualize a scaledNoiseField.
		
		long seed = new Random().nextInt();
		int halfWidth = im.getWidth()/2;
		int halfHeight = im.getHeight()/2;
		
		int r = 255;
		int g = 255;
		int b = 255;
		
		//ScaledNoiseField noise = new ScaledNoiseField(seed, 128f);
		//ScaledNoiseField noise2 = new ScaledNoiseField(seed+1, 64f);
		//ScaledNoiseField noise3 = new ScaledNoiseField(seed+2, 32f);
		
		Module spheres = new Power().setSources(new Spheres().setFrequency(1/1024D), (x,y,z)->2.5);
		Module ridges = new Multiply().setSources(new RidgedMulti().setFrequency(1/128D), (x,y,z)->0.9);
		//Voronoi voronoi = new Voronoi().setEnableDistance(false);
		
		double center = 1080*1080;
		double width = 600*600;
		
		for(int y=0; y<im.getHeight(); y++) {
			for(int x=0; x<im.getWidth(); x++) {
				r = 255;
				g = 255;
				b = 255;
				
				
				double dx = x - halfWidth;
				double dy = y - halfHeight;
				
				
				double cell = -1;
				double rcell = ridges.getValue(dx, 0.5, dy) - 0.4;
				
				double distsq = dx*dx + dy*dy;
				if (distsq<50*50) {
					cell = rcell;
					g = 0;
				} else {
					double distblend = 0;
					if (distsq<80*80) {
						r = 0;
						distblend = 80*80 - distsq;
						distblend /= 80*80;
						distblend *= 2;
					} else if (distsq>center-width && distsq<center+width) {
						double ringdist = Math.abs(distsq-center); //distance from center of ring
						distblend = 1 - (ringdist / width);
						
					}
					
					
				/*
				float cell = noise.get(x - halfWidth, y - halfHeight);// + (0.5f * noise2.get(x - halfWidth, y - halfHeight));
				cell += noise2.get(x - halfWidth, y - halfHeight) * 0.5f;
				cell += noise3.get(x - halfWidth, y - halfHeight) * 0.25f;
				*/
					//double scell = spheres.getValue(dx, 0, dy);
					//double scell = 0;
				
					//double rcell = ridges.getValue(dx, 0.5, dy) - 0.4;
					if (distblend > 0) {
						if (rcell + distblend > 0.6D) {
							cell = rcell;
						}// else if (scell+rcell > 0.6D) {
						//	if (scell>0) {
						//		cell = rcell;
						//	}
						//}
					}
					
				}
				//float cell = (float)(voronoi.getValue(x/1024D, 0.5D, y/1024D));
				
				//if (cell<0.9f) cell = 0.0f;
				//if (cell>0.0f) cell = 1.0f; //threshold
				
				cell = cell / 2.0 + 0.5;
				
				int cellValue = clamp((int)(cell * 255), 0, 255);
				//Color cellColor = new Color(cellValue, cellValue, cellValue);
				//im.setRGB(x, y, gray(cellValue));
				im.setRGB(x, y, rgb((int)(cell*255), r, g, b));
			}
		}
		
	}
	
	public int gray(int value) {
		int safe = value & 0xFF;
		return
			(safe <<  0) |
			(safe <<  8) |
			(safe << 16) |
			(0xFF << 24);
	}
	
	public int rgb(int value, int r, int g, int b) {
		double fr = r/255.0;
		double fg = g/255.0;
		double fb = b/255.0;
		
		int safe = value & 0xFF;
		int ir = (int)(safe*fr) & 0xFF;
		int ig = (int)(safe*fg) & 0xFF;
		int ib = (int)(safe*fb) & 0xFF;
		
		return
				(ir <<  0) |
				(ig <<  8) |
				(ib << 16) |
				(0xFF << 24);
	}
	
	public static float clamp(float value, float min, float max) {
		return Math.max(Math.min(value, max), min);
	}
	
	public static int clamp(int value, int min, int max) {
		return Math.max(Math.min(value, max), min);
	}
}
