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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.elytradev.thermionics.world.gen.biome.ICompositorBiome;
import com.elytradev.thermionics.world.gen.biome.BiomeMap;
import com.elytradev.thermionics.world.gen.biome.BiomeModule;
import com.elytradev.thermionics.world.gen.biome.NeoBiome;

import blue.endless.libnoise.Module;
import blue.endless.libnoise.NoiseQuality;
import blue.endless.libnoise.generator.Checkerboard;
import blue.endless.libnoise.generator.Noise;
import blue.endless.libnoise.generator.Perlin;
import blue.endless.libnoise.generator.RidgedMulti;
import blue.endless.libnoise.generator.Spheres;
import blue.endless.libnoise.generator.Voronoi;
import blue.endless.libnoise.modifier.Bandpass;
import blue.endless.libnoise.modifier.Multiply;
import blue.endless.libnoise.modifier.Power;
import blue.endless.libnoise.modifier.RotatePoint;
import blue.endless.libnoise.modifier.ScaleBias;
import blue.endless.libnoise.modifier.ScalePoint;
import blue.endless.libnoise.modifier.Turbulence;
import net.minecraft.world.biome.Biome.BiomeProperties;

public class GenVisualizer extends JFrame {
	private static final long serialVersionUID = -6682388330686106856L;
	
	public static void main(String... args) {
		new GenVisualizer().setVisible(true);
	}
	
	
	
	public BufferedImage terrainPreview = null;
	
	
	
	public GenVisualizer() {
		Dimension d = new Dimension(3200, 2048);
		
		this.setMinimumSize(d);
		this.setSize(d);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	@Override
	public void paint(Graphics g) {
		if (terrainPreview==null) {
			terrainPreview = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_ARGB);
			generateTerrainBiomes(terrainPreview);
		}
		int scale = 2;
		g.drawImage(terrainPreview, 0, 0, 2048*scale, 2048*scale, this);
	}
	
	public void generateTerrainBiomes(BufferedImage im) {
		/*
		BufferedImage blebs = null;
		try {
			blebs = ImageIO.read(new File("blobs.png"));
		} catch (IOException e) {
			blebs = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}
		
		//int seed = new Random().nextInt();
		int seed = 42;
		
		int halfWidth = im.getWidth()/2;
		int halfHeight = im.getHeight()/2;
		
		//int r = 255;
		//int g = 255;
		//int b = 255;
		//BiomeData bridges = new BiomeData("bridges").setModule(
		//		new ImageModule()
		//			.setImage(blebs).setPixelsPerUnit(1/4.0)
					//.setImage(2, 2, new int[] {-1, 1, 1, -1}).setPixelsPerUnit(1/4.0)
				
				//new ScaleBias().setSources(new Perlin().setFrequency(1/128.0))
				//.setScale(0.4)
				//.setBias(-0.5)
				//(x,y,z)->-1
		//		).setColor(0xFF_FF4477).setDensity(0.7); //magenta-pink
		ICompositorBiome strata = new ICompositorBiome("strata").setModule(
				new ImageModule().setImage(2, 2, new int[] {-1, 1, 1, -1}).setPixelsPerUnit(1/4.0)
				//new ScaleBias().setSources(
				//		new RidgedMulti().setFrequency(1/256.0)
				//).setScale(1.5).setBias(0.5)
				//(x,y,z)->1
				).setColor(0xFF_44FFB8).setDensity(0.5); //BUBBLE MOUNTAIN - cyan
		ICompositorBiome cold = new ICompositorBiome("cold").setModule(
				new ScaleBias().setSources(
						new Perlin().setFrequency(1/32.0).setOctaveCount(1)
				).setScale(0.125).setBias(-0.5)
				//(x,y,z)->1
				).setColor(0xFF_4CC9FF); //blue
		ICompositorBiome baradDur = new ICompositorBiome("barad_dur").setModule(
				new ScaleBias().setSources(
						new Bandpass().setSources(
						new Power().setSources(new Perlin().setFrequency(1/8.0), (x,y,z)->7)
						).setBounds(0.7, 256.0)
					).setScale(1/1.0).setBias(0.3)
				//(x,y,z)->1
				).setColor(0xFF_999999).setDensity(0.8); //LightGray
		ICompositorBiome heartsblood = new ICompositorBiome("heartsblood").setModule(
				(x,y,z)->-1
				).setColor(0xFF_FFA444); //orange
		ICompositorBiome sulfur = new ICompositorBiome("sulfur").setModule(
				
				new ScaleBias().setSources(
						new Bandpass().setSources(
								new Power().setSources(
										new Perlin().setFrequency(1/16.0), (x,y,z)->5
								),
								
								(x,y,z)->0.7
						).setBounds(-256, -0.25)
					).setScale(1/1.0).setBias(-0.3)
				
				
				//(x,y,z)->-1
				).setColor(0xFF_FFE644).setDensity(0.0); //yellow
		ICompositorBiome nocturne = new ICompositorBiome("nocturne").setModule(
				(x,y,z)->-1
				).setColor(0xFF_9256FF); //purple
		ICompositorBiome doom = new ICompositorBiome("doom").setModule(
				(x,y,z)->-1
				).setColor(0xFF_FF9999).setDensity(0.8);//red-pink
		
		BiomeMap<ICompositorBiome> map = new BiomeMap<>(bridges).setSize(3, 3);
		map.setBiome(0, 0, bridges);
		map.setBiome(0, 1, strata);
		map.setBiome(1, 0, cold);
		map.setBiome(1, 1, baradDur);
		map.setBiome(2, 0, heartsblood);
		map.setBiome(2, 1, sulfur);
		map.setBiome(2, 2, nocturne);
		map.setBiome(1, 2, doom);
		
		Perlin biomeA = new Perlin().setSeed(seed)  .setFrequency(1/379.0).setOctaveCount(7);
		Perlin biomeB = new Perlin().setSeed(seed+1).setFrequency(1/367.0).setOctaveCount(6);
		
		BiomeModule module = new BiomeModule(biomeA, biomeB).setBiomeMap(map);
		
		//Module imageModule = //new ImageModule().setImage(2, 2, new int[] {0, 1, 1, 0}).setPixelsPerUnit(1/4.0);
				//new ImageModule().setImage(blebs).setPixelsPerUnit(1/3.0);
		
		double scale = 1/1.0;
		for(int y=0; y<im.getHeight(); y++) {
			for(int x=0; x<im.getWidth(); x++) {
				//r = 255;
				//g = 255;
				//b = 255;
				
				
				double dx = x - halfWidth;
				double dy = y - halfHeight;
				dx *= scale;
				dy *= scale;
				
				double cell = -1;
				
				ICompositorBiome data = module.biomeAt(dx, 0, dy);
				int rgb = data.getColor();
				
				cell = module.getValue(dx, 0, dy) / 2.0 + 0.5;
				//cell = imageModule.getValue(dx, 0, dy) / 2.0 + 0.5;
				
				if (cell<0) cell = 0;
				if (cell>1) cell = 1;
				
				
				
				//cell = data.getModule().getValue(dx, 0, dy) / 2.0 + 0.5;
				//cell = clamp(cell, 0, 1);
				rgb = color(rgb, cell);
				
				
				
				//float cell = (float)(voronoi.getValue(x/1024D, 0.5D, y/1024D));
				
				//if (cell<0.9f) cell = 0.0f;
				//if (cell>0.0f) cell = 1.0f; //threshold
				
				//cell = clamp(cell, -1, 1) / 2.0 + 0.5;
				
				//int cellValue = clamp((int)(cell * 255), 0, 255);
				//im.setRGB(x, y, rgb((int)(cell*255), r, g, b));
				
				//im.setRGB(x, y, color(rgb, cell));
				im.setRGB(x, y, rgb);
			}
		}*/
		
	}
	
	public void generateTerrainSpikes(BufferedImage im) {
		int halfWidth = im.getWidth()/2;
		int halfHeight = im.getHeight()/2;
		
		int r = 255;
		int g = 255;
		int b = 255;
		
		Module spikes = new ScaleBias().setSources(
				new Power().setSources(new Perlin().setFrequency(1/32.0), (x,y,z)->6)
			).setScale(1/4.0).setBias(-1);
		
		for(int y=0; y<im.getHeight(); y++) {
			for(int x=0; x<im.getWidth(); x++) {
				r = 255;
				g = 255;
				b = 255;
				
				
				double dx = x - halfWidth;
				double dy = y - halfHeight;
				
				
				double cell = -1;
				
				cell = spikes.getValue(dx, 0.5, dy);
				
				cell = clamp(cell, -1, 1) / 2.0 + 0.5;
				
				int cellValue = clamp((int)(cell * 255), 0, 255);
				
				im.setRGB(x, y, rgb((int)(cell*255), r, g, b));
			}
		}
		
	}
	
	
	
	public void generateTerrainSky(BufferedImage im) {
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
		
		//Module spheres = new Power().setSources(new Spheres().setFrequency(1/1024D), (x,y,z)->2.5);
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
				(ir << 16) |
				(ig <<  8) |
				(ib <<  0) |
				(0xFF << 24);
	}
	
	public int rgbDouble(double r, double g, double b) {
		if (r>1) r=1; if (g>1) g=1; if (b>1) b=1;
		if (r<0) r=0; if (g<0) g=0; if (b<0) b=0;
		
		int ir = (int)(r*255.0); ir &= 0xFF;
		int ig = (int)(g*255.0); ig &= 0xFF;
		int ib = (int)(b*255.0); ib &= 0xFF;
		
		return
				(ir << 16) |
				(ig <<  8) |
				(ib <<  0) |
				(0xFF << 24);
	}
	
	public int color(int color, double intensity) {
		if (intensity < 0) intensity = 0; if (intensity > 1) intensity = 1;
		int r = (color >> 16) & 0xFF;
		int g = (color >>  8) & 0xFF;
		int b = (color >>  0) & 0xFF;
		
		r *= intensity;
		g *= intensity;
		b *= intensity;
		
		return 0xFF000000 |
			((r & 0xFF) << 16) |
			((g & 0xFF) <<  8) |
			((b & 0xFF) <<  0);
	}
	
	public static double clamp(double value, double min, double max) {
		return Math.max(Math.min(value, max), min);
	}
	
	public static int clamp(int value, int min, int max) {
		return Math.max(Math.min(value, max), min);
	}
}
