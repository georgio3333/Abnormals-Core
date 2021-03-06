package com.teamabnormals.abnormals_core.core.utils;

import java.util.Arrays;
import java.util.List;

import com.teamabnormals.abnormals_core.core.library.Modifier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IProperty;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/** 
 * @author - SmellyModder(Luke Tonon)
 */
public class BlockUtils {

	public static boolean isBlockInWater(World world, BlockPos pos) {
		if(world.getBlockState(pos).getFluidState().isTagged(FluidTags.WATER)) {
			return true;
		}
		for(Direction direction : Direction.values()) {
			if(world.getFluidState(pos.offset(direction)).isTagged(FluidTags.WATER)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean canPlace(World world, PlayerEntity player, BlockPos pos, BlockState state) {
		ISelectionContext selectionContext = player == null ? ISelectionContext.dummy() : ISelectionContext.forEntity(player);
		VoxelShape voxelshape = state.getCollisionShape(world, pos, selectionContext);
		VoxelShape offsetShape = world.getBlockState(pos).getCollisionShape(world, pos);
		return (offsetShape.isEmpty() || world.getBlockState(pos).getMaterial().isReplaceable()) && state.isValidPosition(world, pos) && world.checkNoEntityCollision(null, voxelshape.withOffset(pos.getX(), pos.getY(), pos.getZ()));
	}
	
	public static SoundEvent getPlaceSound(BlockState state, World world, BlockPos pos, PlayerEntity entity) {
		return state.getSoundType(world, pos, entity).getPlaceSound();
	}
	
	public static boolean isPosNotTouchingBlock(IWorld world, BlockPos pos, Block blockToCheck, Direction... blacklistedDirections) {
		for(Direction directions : Direction.values()) {
			List<Direction> blacklistedDirectionsList = Arrays.asList(blacklistedDirections);
			if(!blacklistedDirectionsList.contains(directions)) {
				if(world.getBlockState(pos.offset(directions)).getBlock() == blockToCheck) {
					return false;
				}
			}
		}
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static BlockState transferAllBlockStates(BlockState initial, BlockState after) {
		BlockState block = after;
		for(IProperty property : initial.getBlock().getStateContainer().getProperties()) {
			if (after.has(property) && initial.get(property) != null) {
				block = block.with(property, initial.get(property));
			}
		}
		return block;
	}
	
	public static AxisAlignedBB rotateHorizontalBB(AxisAlignedBB bb, BBRotation rotation) {
		AxisAlignedBB newBB = bb;
		return rotation.rotateBB(newBB);
	}
	
	public enum BBRotation {
		REVERSE_X((bb) -> {
			final float minX = 1.0F - (float) bb.maxX;
			return new AxisAlignedBB(minX, bb.minY, bb.minZ, bb.maxX >= 1.0F ? bb.maxX - bb.minX : bb.maxX + minX, bb.maxY, bb.maxZ);
		}),
		REVERSE_Z((bb) -> {
			final float minZ = 1.0F - (float) bb.maxZ;
			return new AxisAlignedBB(bb.minX, bb.minY, minZ, bb.maxX, bb.maxY, bb.maxZ >= 1.0F ? bb.maxZ - bb.minZ : bb.maxZ + minZ);
		}),
		RIGHT((bb) -> {
			return new AxisAlignedBB(bb.minZ, bb.minY, bb.minX, bb.maxZ, bb.maxY, bb.maxX);
		}),
		LEFT((bb) -> {
			return REVERSE_X.rotateBB(RIGHT.rotateBB(bb));
		});
		
		private final Modifier<AxisAlignedBB> modifier;
		
		BBRotation(Modifier<AxisAlignedBB> modifier) {
			this.modifier = modifier;
		}
		
		public AxisAlignedBB rotateBB(AxisAlignedBB bb) {
			return this.modifier.modify(bb);
		}
		
		public static BBRotation getRotationForDirection(Direction currentDirection, Direction startingDirection) {
			int currentIndex = currentDirection.getIndex() - 2;
			int startingIndex = startingDirection.getIndex() - 2;
			int index = (currentIndex - startingIndex) % 4;
			
			switch(index) {
				default:
				case 0:
					return BBRotation.REVERSE_X;
				case 1:
					return BBRotation.REVERSE_Z;
				case 2:
					return BBRotation.RIGHT;
				case 3:
					return BBRotation.LEFT;
			}
		}
	}
}