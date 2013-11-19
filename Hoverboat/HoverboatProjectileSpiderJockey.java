package Hoverboat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.World;

public class HoverboatProjectileSpiderJockey extends HoverboatProjectileType {
	@SuppressWarnings("rawtypes")
	public HoverboatProjectileSpiderJockey(Class class1, int i, String s) {
		ClassType = class1;
		name = s;
		ItemID = i;
		throwable = false;
	}

	@Override
	protected Entity CreateItem(World world, EntityLivingBase entityliving, double d, double d1, double d2) {
		EntitySpider entityspider = new EntitySpider(world);
		entityspider.setPosition(d, d1, d2);
		EntitySkeleton entityskeleton = new EntitySkeleton(world);
		entityskeleton.setPosition(d, d1, d2);
		entityskeleton.mountEntity(entityspider);
		return entityspider;
	}

	@Override
	protected Entity ThrowItem(World world, EntityLivingBase entityliving, double d, double d1, double d2) {
		return CreateItem(world, entityliving, d, d1, d2);
	}
}
