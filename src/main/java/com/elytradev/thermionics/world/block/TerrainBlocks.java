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
package com.elytradev.thermionics.world.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder("thermionics_world")
public class TerrainBlocks {
	@ObjectHolder("pain")
	public static final BlockFluidSimple FLUID_PAIN = null;
	@ObjectHolder("soylent")
	public static final BlockFluidSimple FLUID_SOYLENT = null;
	
	public static List<BlockGemrock> GROUP_GEMROCK = null;
	
	@ObjectHolder("gemrock.magnesite")
	public static final BlockGemrock GEMROCK_MAGNESITE = null;
	@ObjectHolder("gemrock.garnet")
	public static final BlockGemrock GEMROCK_GARNET = null;
	@ObjectHolder("gemrock.tourmaline")
	public static final BlockGemrock GEMROCK_TOURMALINE = null;
	@ObjectHolder("gemrock.sapphire")
	public static final BlockGemrock GEMROCK_SAPPHIRE = null;
	@ObjectHolder("gemrock.heliodor")
	public static final BlockGemrock GEMROCK_HELIODOR = null;
	@ObjectHolder("gemrock.peridot")
	public static final BlockGemrock GEMROCK_PERIDOT = null;
	@ObjectHolder("gemrock.rosequartz")
	public static final BlockGemrock GEMROCK_ROSE_QUARTZ = null;
	@ObjectHolder("gemrock.hematite")
	public static final BlockGemrock GEMROCK_HEMATITE = null;
	@ObjectHolder("gemrock.opal")
	public static final BlockGemrock GEMROCK_OPAL = null;
	@ObjectHolder("gemrock.chrysoprase")
	public static final BlockGemrock GEMROCK_CHRYSOPRASE = null;
	@ObjectHolder("gemrock.amethyst")
	public static final BlockGemrock GEMROCK_AMETHYST = null;
	@ObjectHolder("gemrock.sodalite")
	public static final BlockGemrock GEMROCK_SODALITE = null;
	@ObjectHolder("gemrock.pyrite")
	public static final BlockGemrock GEMROCK_PYRITE = null;
	@ObjectHolder("gemrock.emerald")
	public static final BlockGemrock GEMROCK_EMERALD = null;
	@ObjectHolder("gemrock.spinel")
	public static final BlockGemrock GEMROCK_SPINEL = null;
	@ObjectHolder("gemrock.cassiterite")
	public static final BlockGemrock GEMROCK_CASSITERITE = null;
	@ObjectHolder("meat.edible")
	public static final BlockMeat MEAT_EDIBLE = null;
	@ObjectHolder("meat.flesh")
	public static final BlockMeat MEAT_FLESH = null;
}
