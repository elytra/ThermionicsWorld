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

package com.elytradev.thermionics.world.block;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidSimple extends BlockFluidFinite {
	
	public static final int SCALDING = 322; //322 degrees kelvin is about 120 degrees fahrenheit; scalding temperature.
	
	private DamageSource fluidDamage;
	private boolean liftsItems = false;
	//private float damageAmount = 0.0f;
	
	public BlockFluidSimple(Fluid fluid, String name) {
		super(fluid,
				//ThermionicsWorld.MATERIAL_PAIN);
				Material.WATER);
		//this.opaque = true;
		//this.icons = icons;
		this.quantaPerBlock = 4;
		this.quantaPerBlockFloat = 4.0f;
		this.tickRate = 10; //density-based tickrate winds up really high
		
		this.setRegistryName(name);
		this.setUnlocalizedName("thermionics_world.fluid."+name);
		
		this.fluidDamage = new DamageSource("fluid."+fluid.getName()).setDamageBypassesArmor().setDamageIsAbsolute();
		//if (fluid.getTemperature() > SCALDING) damageAmount = 1 + (fluid.getTemperature() - SCALDING) / 20f;
		
		this.setDefaultState(blockState.getBaseState().withProperty(BlockFluidBase.LEVEL, 3));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks) {
		return new Vec3d(0.4, 0.03, 0.1);
	}
	
	public BlockFluidSimple setLiftsItems(boolean shouldLiftItems) {
		this.liftsItems = shouldLiftItems;
		return this;
	}
	
	@Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
		if (!world.isAreaLoaded(pos, 2)) return;
		//if (!world.doChunksNearChunkExist(x, y, z, 2)) return;
		super.updateTick(world, pos, state, rand);
	}
	
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		super.onEntityCollidedWithBlock(world, pos, state, entity);
		
		if (entity instanceof EntityBoat) {
			entity.motionY += 0.007;
			//entity.motionY -= 0.2;
			//entity.setVelocity(entity.motionX, 0.01, entity.motionZ);
			return;
		}
		
		if (!world.isRemote) {
			//Immune creatures
			if (entity instanceof EntityPigZombie) return;
			if (entity instanceof EntityMagmaCube) return;
			if (entity instanceof EntityGhast) return;
		}
		if (entity instanceof EntityItem) {
			if (liftsItems) {
				if (entity.motionY < 0) entity.motionY *= 0.98;
				entity.motionY += 0.08 + 0.02;
			}
			return;
		} else {
			//slows everything else down - disables because atm it messes with upwards swimming and creative flight
			/*
			if (entity instanceof EntityPlayer) {
				//Do the slowdown clientside-only
				if (world.isRemote) {
					applyFluidPhysics(entity);
				}
			} else {
				applyFluidPhysics(entity);
			}*/
		}
		
		if (world.isRemote) return;
		if (fluidDamage!=null && !entity.isRiding()) {
			entity.attackEntityFrom(fluidDamage, 1.0f);
			entity.velocityChanged = false;
		}
	}
	
	public void applyFluidPhysics(Entity entity) {
		entity.onGround = true;
		entity.fallDistance = 0;
		if (entity.motionY < -0.11d) {
			entity.motionY += 0.1d;
		}// else if (entity.motionY > 0.41d){
		//	entity.motionY -= 0.1d;
		//}
	}
}
