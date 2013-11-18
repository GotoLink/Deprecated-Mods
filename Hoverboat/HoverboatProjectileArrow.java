package Hoverboat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

public class HoverboatProjectileArrow extends HoverboatProjectileType {
	boolean doCrit;

	@SuppressWarnings("rawtypes")
	public HoverboatProjectileArrow(Class class1, int i, boolean critical) {
		name = critical ? "Arrow (Critical)" : "Arrow";
		ClassType = class1;
		ItemID = i;
		throwable = true;
		doCrit = critical;
		overridesPosAngs = true;
		usesCustomSounds = true;
		addMotion = 0.8;
	}

	@Override
	public void PlayCustomSound(boolean IsDrop, Entity ent) {
		ent.worldObj.playSoundAtEntity(ent, "random.bow", 1.0F, 1.0F / (ent.worldObj.rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
	}

	@Override
	protected Entity CreateItem(World world, EntityLiving entityliving, double d, double d1, double d2) throws Throwable {
		EntityArrow arrow = new EntityArrow(world, entityliving, 0F);
		arrow.setIsCritical(doCrit);
		arrow.setPosition(d, d1, d2);
		return arrow;
	}

	@Override
	protected Entity ThrowItem(World world, EntityLiving entityliving, double d, double d1, double d2) throws Throwable {
		EntityArrow arrow = new EntityArrow(world, entityliving, 1F);
		arrow.setIsCritical(doCrit);
		arrow.setPosition(d, d1, d2);
		return arrow;
	}
}
