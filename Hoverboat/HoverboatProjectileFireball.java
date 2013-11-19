package Hoverboat;

import java.awt.geom.Point2D;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.world.World;

public class HoverboatProjectileFireball extends HoverboatProjectileType {
	@SuppressWarnings("rawtypes")
	public HoverboatProjectileFireball(Class class1, int i) {
		ClassType = class1;
		ItemID = i;
		throwable = true;
		usesCustomSounds = true;
	}

	@Override
	public void PlayCustomSound(boolean IsDrop, Entity ent) {
		ent.worldObj.playSoundAtEntity(ent, "fire.ignite", 3F, ent.worldObj.rand.nextFloat() * 0.4F + 0.8F);
	}

	@Override
	protected Entity CreateItem(World world, EntityLivingBase entityliving, double d, double d1, double d2) {
		EntityFireball entityfireball = new EntitySmallFireball(world);
		entityfireball.accelerationX = 0.0D;
		entityfireball.accelerationY = -0.2D * mod_Hoverboat.Instance.settingFloatFireballSpeed;
		entityfireball.accelerationZ = 0.0D;
		world.spawnEntityInWorld(entityfireball);
		return entityfireball;
	}

	@Override
	protected Entity ThrowItem(World world, EntityLivingBase entityliving, double d, double d1, double d2) {
		Point2D point2d = Hoverboat.RotatePoint(new java.awt.geom.Point2D.Double(0.0D, 1.0D), new java.awt.geom.Point2D.Double(), Hoverboat.Degreetonormalizedradian(entityliving.rotationPitch));
		double d3 = point2d.getX();
		point2d = Hoverboat.RotatePoint(new java.awt.geom.Point2D.Double(0.0D, point2d.getY()), new java.awt.geom.Point2D.Double(), Hoverboat.Degreetonormalizedradian(entityliving.rotationYaw));
		double d4 = point2d.getX();
		double d5 = point2d.getY();
		EntityFireball entityfireball = new EntitySmallFireball(world, entityliving, d4, d3, d5);
		double d6 = mod_Hoverboat.Instance.settingFloatFireballSpeed;
		entityfireball.accelerationX += d4 * d6;
		entityfireball.accelerationY += d3 * d6;
		entityfireball.accelerationZ += d5 * d6;
		return entityfireball;
	}
}
