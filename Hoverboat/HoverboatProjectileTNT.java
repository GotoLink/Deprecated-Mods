package Hoverboat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;

public class HoverboatProjectileTNT extends HoverboatProjectileType {
	@SuppressWarnings("rawtypes")
	public HoverboatProjectileTNT(Class class1, int i, String s) {
		ClassType = class1;
		name = s;
		ItemID = i;
		throwable = false;
	}

	@Override
	protected Entity CreateItem(World world, EntityLiving entityliving, double d, double d1, double d2) throws Throwable {
		EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, d, d1, d2);
		ModLoader.setPrivateValue(EntityTNTPrimed.class, entitytntprimed, (mod_Hoverboat.isMCP ? "fuse" : "a"), Integer.valueOf(mod_Hoverboat.Instance.settingIntTntTicks));
		return entitytntprimed;
	}

	@Override
	protected Entity ThrowItem(World world, EntityLiving entityliving, double d, double d1, double d2) throws Throwable {
		return CreateItem(world, entityliving, d, d1, d2);
	}
}
