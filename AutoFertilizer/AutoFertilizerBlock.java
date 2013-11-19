package AutoFertilizer;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class AutoFertilizerBlock extends Block {
	public static int modelID;

	protected AutoFertilizerBlock(int i) {
		super(i, Material.circuits);
		setTickRandomly(true);
	}

	@Override
	public boolean canBlockStay(World world, int i, int j, int k) {
		return canPlaceBlockAt(world, i, j, k);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		if (world.isBlockOpaqueCube(i - 1, j, k)) {
			return true;
		}
		if (world.isBlockOpaqueCube(i + 1, j, k)) {
			return true;
		}
		if (world.isBlockOpaqueCube(i, j, k - 1)) {
			return true;
		}
		if (world.isBlockOpaqueCube(i, j, k + 1)) {
			return true;
		}
		return world.isBlockOpaqueCube(i, j - 1, k);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 vec3d, Vec3 vec3d1) {
		int l = mod_AutoFertilizer.BitGetDirection(world.getBlockMetadata(i, j, k));
		switch (l) {
		case 1:
			setBlockBounds(0.0F, 0.2F, 0.25F, 0.5F, 1F, 0.75F);
			break;
		case 2:
			setBlockBounds(0.5f, 0.2F, 0.25f, 1.0F, 1F, 0.75F);
			break;
		case 3:
			setBlockBounds(0.25f, 0.2F, 0.0F, 0.75F, 1F, 0.5f);
			break;
		case 4:
			setBlockBounds(0.25f, 0.2F, 0.5F, 0.75F, 1F, 1.0F);
			break;
		case 0:
		default:
			setBlockBounds(0.25f, 0.0F, 0.25f, 0.75F, 0.8F, 0.75F);
			break;
		}
		return super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return null;
	}

	@Override
	public int getRenderType() {
		return modelID;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
		if (mod_AutoFertilizer.GetFertBlockNeedsPower()) {
			return false;
		}
		int metadata = world.getBlockMetadata(i, j, k);
		world.setBlockMetadataWithNotify(i, j, k, mod_AutoFertilizer.BitSetOn(metadata, !mod_AutoFertilizer.BitGetOn(metadata)), 2);
		return true;
	}

	@Override
	public int onBlockPlaced(World world, int i, int j, int k, int l, float par6, float par7, float par8, int par9) {
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
		int i1 = 0;
		if ((l == 1) && world.isBlockOpaqueCube(i, j - 1, k)) {
			i1 = 5;
		}
		if ((l == 2) && world.isBlockOpaqueCube(i, j, k + 1)) {
			i1 = 4;
		}
		if ((l == 3) && world.isBlockOpaqueCube(i, j, k - 1)) {
			i1 = 3;
		}
		if ((l == 4) && world.isBlockOpaqueCube(i + 1, j, k)) {
			i1 = 2;
		}
		if ((l == 5) && world.isBlockOpaqueCube(i - 1, j, k)) {
			i1 = 1;
		}
		switch (i1) {
		case 1:
			setBlockBounds(0.0F, 0.2F, 0.25F, 0.5F, 1F, 0.75F);
			break;
		case 2:
			setBlockBounds(0.5f, 0.2F, 0.25f, 1.0F, 1F, 0.75F);
			break;
		case 3:
			setBlockBounds(0.25f, 0.2F, 0.0F, 0.75F, 1F, 0.5f);
			break;
		case 4:
			setBlockBounds(0.25f, 0.2F, 0.5F, 0.75F, 1F, 1.0F);
			break;
		case 0:
		default:
			setBlockBounds(0.25f, 0.0F, 0.25f, 0.75F, 0.8F, 0.75F);
			break;
		}
		return mod_AutoFertilizer.BitSetDirection(world.getBlockMetadata(i, j, k), i1);
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (!canBlockStay(world, i, j, k)) {
			dropBlockAsItem(world, i, j, k, blockID, 0);
			world.setBlock(i, j, k, 0);
		}
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int tickRate(World world) {
		return 21 - mod_AutoFertilizer.GetFertBlockTicksPerSecond();
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if (mod_AutoFertilizer.GetFertBlockNeedsPower() ? world.isBlockIndirectlyGettingPowered(i, j, k) : !mod_AutoFertilizer.BitGetOn(world.getBlockMetadata(i, j, k))) {
			mod_AutoFertilizer.Fert(world, i + 1, j + 1, k + 1, mod_AutoFertilizer.GetFertBlockTicksPerSecond(), 2, 3);
			world.spawnParticle("splash", (i + 0.5) + ((random.nextDouble() - 0.5D) * 0.4), (j + 0.7), (k + 0.5) + ((random.nextDouble() - 0.5D) * 0.4), (random.nextDouble() - 0.5D) * 20.0D, 20D,
					(random.nextDouble() - 0.5D) * 20.0D);
		}
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
	}
}
