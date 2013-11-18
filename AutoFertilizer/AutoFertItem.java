package AutoFertilizer;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class AutoFertItem extends Item {
	static Icon fertboaticon;
	static Icon fertcarticon;

	public AutoFertItem(int i) {
		super(i);
		maxStackSize = 1;
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public Icon getIconFromDamage(int i) {
		switch (i) {
		case 1:
			return fertcarticon;
		default:
		case 0:
			return fertboaticon;
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		switch (itemstack.getItemDamage()) {
		case 1:
			return "item.afertcart";
		default:
		case 0:
			return "item.afertboat";
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		float f = 1.0F;
		float f1 = entityplayer.prevRotationPitch + ((entityplayer.rotationPitch - entityplayer.prevRotationPitch) * f);
		float f2 = entityplayer.prevRotationYaw + ((entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f);
		double d = entityplayer.prevPosX + ((entityplayer.posX - entityplayer.prevPosX) * f);
		double d1 = (entityplayer.prevPosY + ((entityplayer.posY - entityplayer.prevPosY) * f) + 1.6200000000000001D) - entityplayer.yOffset;
		double d2 = entityplayer.prevPosZ + ((entityplayer.posZ - entityplayer.prevPosZ) * f);
		Vec3 vec3d = world.getWorldVec3Pool().getVecFromPool(d, d1, d2);
		float f3 = MathHelper.cos((-f2 * 0.01745329F) - 3.141593F);
		float f4 = MathHelper.sin((-f2 * 0.01745329F) - 3.141593F);
		float f5 = -MathHelper.cos(-f1 * 0.01745329F);
		float f6 = MathHelper.sin(-f1 * 0.01745329F);
		float f7 = f4 * f5;
		float f8 = f6;
		float f9 = f3 * f5;
		double d3 = 5D;
		Vec3 vec3d1 = vec3d.addVector(f7 * d3, f8 * d3, f9 * d3);
		MovingObjectPosition movingobjectposition = world.clip(vec3d, vec3d1, true);
		if (movingobjectposition == null) {
			return itemstack;
		}
		if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
			int i = movingobjectposition.blockX;
			int j = movingobjectposition.blockY;
			int k = movingobjectposition.blockZ;
			if (!world.isRemote) {
				switch (itemstack.getItemDamage()) {
				case 1: {
					world.spawnEntityInWorld(new AutoFertCartEntity(world, i + 0.5F, j + 1.5F, k + 0.5F));
					break;
				}
				default:
				case 0: {
					world.spawnEntityInWorld(new AutoFertBoatEntity(world, i + 0.5F, j + 1.5F, k + 0.5F));
					break;
				}
				}
			}
			itemstack.stackSize--;
		}
		return itemstack;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		fertcarticon = reg.registerIcon("/gui/autofertcart.png");
		fertboaticon = reg.registerIcon("/gui/autofertboat.png");
	}
}
