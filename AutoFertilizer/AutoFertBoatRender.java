package AutoFertilizer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class AutoFertBoatRender extends Render {
	protected ModelBase modelBoat;
	private ResourceLocation autofert = new ResourceLocation("autofert.png");

	public AutoFertBoatRender() {
		shadowSize = 0.5F;
		modelBoat = new ModelBoat();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		AutoFertBoatEntity ent = (AutoFertBoatEntity) entity;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
		float f2 = ent.TimeSinceHit - f1;
		float f3 = ent.CurrentDamage - f1;
		if (f3 < 0.0F) {
			f3 = 0.0F;
		}
		if (f2 > 0.0F) {
			GL11.glRotatef(((MathHelper.sin(f2) * f2 * f3) / 10F) * ent.RockDirection, 1.0F, 0.0F, 0.0F);
		}
		float f4 = 0.75F;
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
		bindEntityTexture(entity);
		GL11.glScalef(-1F, -1F, 1.0F);
		modelBoat.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return autofert;
	}
}
