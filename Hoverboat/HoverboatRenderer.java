package Hoverboat;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class HoverboatRenderer extends Render {
	protected ModelBase model;
	private static final ResourceLocation hoverboat = new ResourceLocation("/item/hoverboat.png");

	public HoverboatRenderer() {
		shadowSize = 0.5F;
		model = new ModelBoat();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		Hoverboat hoverboat = (Hoverboat) entity;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
		float f2 = hoverboat.TimeSinceHit - f1;
		float f3 = hoverboat.CurrentDamage - f1;
		float f4 = 0.0F;
		if (f3 < 0.0F) {
			f3 = 0.0F;
		}
		if (f2 > 0.0F) {
			f4 = ((MathHelper.sin(f2) * f2 * f3) / 10F) * hoverboat.RockDirection;
		}
		f4 += hoverboat.Lean;
		if (f4 != 0.0F) {
			GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
		}
		float f5 = 0.75F;
		GL11.glScalef(f5, f5, f5);
		GL11.glScalef(1.0F / f5, 1.0F / f5, 1.0F / f5);
		bindEntityTexture(entity);
		GL11.glScalef(-1F, -1F, 1.0F);
		model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return hoverboat;
	}
}
