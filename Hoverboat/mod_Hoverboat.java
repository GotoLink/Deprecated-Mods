package Hoverboat;

import java.awt.Button;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.src.ModLoader;
import cpw.mods.fml.common.registry.EntityRegistry;

public class mod_Hoverboat {
	public static Item hoverboatitem = new HoverboatItem(3038).setTextureName("hoverboat").setUnlocalizedName("hoverBoat");
	public static mod_Hoverboat Instance;
	private static Vector<Class<?>> ProjectileBlacklist;
	private static Vector<HoverboatProjectileType> projectiles = new Vector<HoverboatProjectileType>();
	private static Vector<Class<?>> ProjectileWhitelist;
	private LinkedList<HoverboatProjectileType> AllowedDrops;
	private LinkedList<HoverboatProjectileType> AllowedProjectiles;
	private boolean Loaded;
	public boolean settingBoolAllowFastFov;
	public boolean settingBoolEnableArrows;
	public boolean settingBoolEnableTNT;
	public boolean settingBoolFireProof;
	public boolean settingBoolShowHUD;
	public boolean settingBoolSub;
	public boolean settingBoolUseAmmo;
	public String settingDefaultDrop;
	public String settingDefaultProjectile;
	public float settingFloatAcceleration;
	public float settingFloatAltTurnSpeed;
	public float settingFloatArrowVel;
	public float settingFloatBrakeMult;
	public float settingFloatBrakeThreshold;
	public float settingFloatCannonAccuracy;
	public float settingFloatDefaultHover;
	public float settingFloatDropAccuracy;
	public float settingFloatFireballSpeed;
	public float settingFloatMaxAltTurnSpeed;
	public float settingFloatMaxFriction;
	public float settingFloatMaxHover;
	public float settingFloatMaxJump;
	public float settingFloatMaxSpeed;
	public float settingFloatMinFriction;
	public float settingFloatMinHover;
	public float settingFloatParticles;
	public float settingFloatTNTVel;
	public float settingFloatViewShift;
	public int settingIntArrowFireRate;
	public int settingIntTNTFireRate;
	public int settingIntTntLines;
	public int settingIntTntTicks;
	public SettingKey settingKeyBrake;
	public SettingKey settingKeyFireArrow;
	public SettingKey settingKeyFireTnt;
	public SettingKey settingKeyPark;
	public SettingKey settingKeySelectBoat;
	public SettingKey settingKeySelectFlight;
	public SettingKey settingKeySelectHover;
	public SettingKey settingKeyShiftArrowLeft;
	public SettingKey settingKeyShiftArrowRight;
	public SettingKey settingKeyShiftDropLeft;
	public SettingKey settingKeyShiftDropRight;
	public SettingKey settingKeyShiftModeLeft;
	public SettingKey settingKeyShiftModeRight;
	public SettingKey settingKeySteeringLeft;
	public SettingKey settingKeySteeringRight;
	public int settingMultiBoatMode;
	public int settingMultiChestSize;
	public SettingMulti settingMultiDefKeyboardSteering;

	public void addRenderer(Map map) {
		map.put(Hoverboat.class, new HoverboatRenderer());
	}

	public HoverboatProjectileType GetDefaultDrop() {
		EnsureDrops();
		String s = settingDefaultDrop;
		HoverboatProjectileType ret = GetDropFromString(s);
		return (ret == null ? AllowedDrops.getFirst() : ret);
	}

	public HoverboatProjectileType GetDefaultProjectile() {
		EnsureProjectiles();
		String s = settingDefaultProjectile;
		HoverboatProjectileType ret = GetProjectileFromString(s);
		return (ret == null ? AllowedProjectiles.getFirst() : ret);
	}

	public HoverboatProjectileType GetDropFromString(String s) {
		for (Iterator<HoverboatProjectileType> iterator = AllowedDrops.iterator(); iterator.hasNext();) {
			HoverboatProjectileType hoverboatprojectiletype = iterator.next();
			if (hoverboatprojectiletype.GetName() == s) {
				return hoverboatprojectiletype;
			}
		}
		return null;
	}

	public HoverboatProjectileType GetNextDrop(HoverboatProjectileType hoverboatprojectiletype) {
		EnsureDrops();
		int i = AllowedDrops.indexOf(hoverboatprojectiletype);
		if ((i == -1) || (i == (AllowedDrops.size() - 1))) {
			return AllowedDrops.getFirst();
		}
		return AllowedDrops.get(i + 1);
	}

	public HoverboatProjectileType GetNextProjectile(HoverboatProjectileType hoverboatprojectiletype) {
		EnsureProjectiles();
		int i = AllowedProjectiles.indexOf(hoverboatprojectiletype);
		if ((i == -1) || (i == (AllowedProjectiles.size() - 1))) {
			return AllowedProjectiles.getFirst();
		}
		return AllowedProjectiles.get(i + 1);
	}

	public HoverboatProjectileType GetPrevDrop(HoverboatProjectileType hoverboatprojectiletype) {
		EnsureDrops();
		int i = AllowedDrops.indexOf(hoverboatprojectiletype);
		if ((i == -1) || (i == 0)) {
			return AllowedDrops.getLast();
		}
		return AllowedDrops.get(i - 1);
	}

	public HoverboatProjectileType GetPrevProjectile(HoverboatProjectileType hoverboatprojectiletype) {
		EnsureProjectiles();
		int i = AllowedProjectiles.indexOf(hoverboatprojectiletype);
		if ((i == -1) || (i == 0)) {
			return AllowedProjectiles.getLast();
		}
		return AllowedProjectiles.get(i - 1);
	}

	public HoverboatProjectileType GetProjectileFromString(String s) {
		for (Iterator<HoverboatProjectileType> iterator = AllowedProjectiles.iterator(); iterator.hasNext();) {
			HoverboatProjectileType hoverboatprojectiletype = iterator.next();
			if (hoverboatprojectiletype.GetName() == s) {
				return hoverboatprojectiletype;
			}
		}
		return null;
	}

	public boolean GetValidDrop(HoverboatProjectileType hoverboatprojectiletype) {
		EnsureDrops();
		return AllowedDrops.indexOf(hoverboatprojectiletype) != -1;
	}

	public boolean GetValidProjectile(HoverboatProjectileType hoverboatprojectiletype) {
		EnsureProjectiles();
		return AllowedProjectiles.indexOf(hoverboatprojectiletype) != -1;
	}

	public void load() {
		EntityRegistry.registerModEntity(Hoverboat.class, "HoverBoat", 0, this, 80, 3, true);
		mod_Hoverboat.ProjectileBlacklist = new Vector<Class<?>>();
		mod_Hoverboat.ProjectileBlacklist.add(EntityItem.class);
		mod_Hoverboat.ProjectileBlacklist.add(EntityPainting.class);
		mod_Hoverboat.ProjectileBlacklist.add(EntityLiving.class);
		mod_Hoverboat.ProjectileBlacklist.add(EntityMob.class);
		mod_Hoverboat.ProjectileBlacklist.add(EntityPlayerSP.class);
		mod_Hoverboat.ProjectileBlacklist.add(EntityPlayer.class);
		mod_Hoverboat.ProjectileWhitelist = new Vector<Class<?>>();
		mod_Hoverboat.ProjectileWhitelist.add(EntityMinecart.class);
		mod_Hoverboat.ProjectileWhitelist.add(EntityBoat.class);
		mod_Hoverboat.ProjectileWhitelist.add(EntityFireball.class);
		mod_Hoverboat.ProjectileWhitelist.add(EntityEgg.class);
		Loaded = false;
		InitSettings();
		AddDefaultsButton();
		mod_Hoverboat.projectiles = new Vector<HoverboatProjectileType>();
		AllowedProjectiles = new LinkedList<HoverboatProjectileType>();
		AllowedDrops = new LinkedList<HoverboatProjectileType>();
		//ModLoader.addName(mod_Hoverboat.hoverboatitem, "Hoverboat");
	}

	@Override
	public boolean OnTickInGame(float partialtick, Minecraft minecraft) {
		if (minecraft.currentScreen != null) {
			return true;
		}
		if (settingBoolShowHUD && (minecraft.thePlayer.ridingEntity != null) && (minecraft.thePlayer.ridingEntity instanceof Hoverboat)) {
			Hoverboat hoverboat = (Hoverboat) minecraft.thePlayer.ridingEntity;
			minecraft.fontRenderer.drawStringWithShadow(String.format("Hoverboat: %s Mode (%s)", new Object[] {
					hoverboat.IsParked ? "Park" : Double.isNaN(hoverboat.depth) || (hoverboat.depth <= 1.0D) || (hoverboat.CurrentMode != 0) ? settingMultiBoatMode.labelValues[hoverboat.CurrentMode]
							: "Sub", settingMultiDefKeyboardSteering.labelValues[hoverboat.KeyboardSteering] }), 2, 12, 0xffffff);
			if ((AllowedProjectiles.size() > 0) && settingBoolEnableArrows) {
				minecraft.fontRenderer.drawStringWithShadow(String.format("Hoverboat Projectile: %s Selected", new Object[] { hoverboat.SelectedArrow.GetName() }), 2, 24, 0xffffff);
			}
			if ((AllowedDrops.size() > 0) && settingBoolEnableTNT) {
				minecraft.fontRenderer.drawStringWithShadow(String.format("Hoverboat Drop: %s Selected", new Object[] { hoverboat.SelectedDrop.GetName() }), 2, 36, 0xffffff);
			}
			int i = 48;
			if (hoverboat.Debugger != "") {
				String as[];
				int k = (as = hoverboat.Debugger.split("\n")).length;
				for (int j = 0; j < k; j++) {
					String s = as[j];
					minecraft.fontRenderer.drawStringWithShadow((new StringBuilder("Hoverboat: Debugging: ")).append(s).toString(), 2, i, 0xffffff);
					i += 12;
				}
			}
		}
		return true;
	}

	@Override
	public void RegisterAnimation(Minecraft minecraft) {
		CraftingManager.getInstance().addRecipe(new ItemStack(mod_Hoverboat.hoverboatitem, 1),
				new Object[] { "#B#", "###", Character.valueOf('#'), Block.blockGold, Character.valueOf('B'), Block.blockDiamond });
		InitializeProjectiles();
		InitDropSettings();
		InitArrowSettings();
		ReloadAllowedProjectiles();
		ReloadAllowedDrops();
	}

	public void SetDefaultArrow(HoverboatProjectileType hoverboatprojectiletype) {
		if (!hoverboatprojectiletype.AllowForShoot) {
			return;
		}
		settingDefaultProjectile.set(hoverboatprojectiletype.GetName(), ModSettings.currentContext);
		GuiModScreen.clicksound();
		return;
	}

	public void SetDefaultDrop(HoverboatProjectileType hoverboatprojectiletype) {
		if (!hoverboatprojectiletype.AllowForDrops) {
			return;
		}
		settingDefaultDrop.set(hoverboatprojectiletype.GetName(), ModSettings.currentContext);
		GuiModScreen.clicksound();
		return;
	}

	private void AddDefaultsButton() {
		SimpleButtonModel simplebuttonmodel = new SimpleButtonModel();
		simplebuttonmodel.addActionCallback(new ModAction(settings, "resetAll", new Class[0]));
		Button button = new Button(simplebuttonmodel);
		button.setText("Reset all to defaults");
		modscreen.append(button);
	}

	private void EnsureDrops() {
		if (AllowedDrops.size() == 0) {
			ReloadAllowedDrops();
		}
	}

	private void EnsureProjectiles() {
		if (AllowedProjectiles.size() == 0) {
			ReloadAllowedProjectiles();
		}
	}

	private void InitArrowSettings() {
		for (Iterator<?> iterator = mod_Hoverboat.projectiles.iterator(); iterator.hasNext(); widgetsinglecolumn.add(button)) {
			HoverboatProjectileType hoverboatprojectiletype = (HoverboatProjectileType) iterator.next();
			hoverboatprojectiletype.settingBoolProjectileEnabled = settings.addSetting(widgetsinglecolumn, (new StringBuilder("Allow ")).append(hoverboatprojectiletype.GetName()).toString(),
					(new StringBuilder("Hoverboat.Projectile.")).append(hoverboatprojectiletype.GetName()).toString(), true);
			simplebuttonmodel.addActionCallback(new ModAction(hoverboatprojectiletype, "MakeDefaultArrow", new Class[0]));
			button.setText((new StringBuilder("Make ")).append(hoverboatprojectiletype.GetName()).append(" Default").toString());
		}
	}

	private void InitDropSettings() {
		for (Iterator<?> iterator = mod_Hoverboat.projectiles.iterator(); iterator.hasNext(); widgetsinglecolumn.add(button)) {
			HoverboatProjectileType hoverboatprojectiletype = (HoverboatProjectileType) iterator.next();
			hoverboatprojectiletype.settingBoolDropEnabled = settings.addSetting(widgetsinglecolumn, (new StringBuilder("Allow ")).append(hoverboatprojectiletype.GetName()).toString(),
					(new StringBuilder("Hoverboat.Drop.")).append(hoverboatprojectiletype.GetName()).toString(), true);
			simplebuttonmodel.addActionCallback(new ModAction(hoverboatprojectiletype, "MakeDefaultDrop", new Class[0]));
			button.setText((new StringBuilder("Make ")).append(hoverboatprojectiletype.GetName()).append(" Default").toString());
		}
	}

	private void InitializeProjectiles() {
		projectiles = new Vector<HoverboatProjectileType>();
		try {
			Map list = EntityList.classToStringMapping;
			projectiles.add(new HoverboatProjectileArrow(EntityArrow.class, Item.arrow.itemID, false));
			projectiles.add(new HoverboatProjectileArrow(EntityArrow.class, Item.arrow.itemID, true));
			projectiles.add(new HoverboatProjectileDefault(EntityXPOrb.class, -1, "XP Orb Spout"));
			projectiles.add(new HoverboatProjectileFireball(EntityFireball.class, -1));
			for (Iterator<?> iterator = list.keySet().iterator(); iterator.hasNext();) {
				Class<?> class1 = (Class<?>) iterator.next();
				if (!ProjectileBlacklist.contains(class1) && !Modifier.isAbstract(class1.getModifiers())) {
					HoverboatProjectileType proj = new HoverboatProjectileDefault(class1, -1, (String) list.get(class1));
					if ((EntityLiving.class).isAssignableFrom(class1) || (Material.class).isAssignableFrom(class1) || (proj.throwable || mod_Hoverboat.ProjectileWhitelist.contains(class1))) {
						projectiles.add(proj);
					}
				}
			}
			Collections.sort(projectiles);
			projectiles.add(new HoverboatProjectileTNT(EntityTNTPrimed.class, 46, "TNT Cannon"));
			projectiles.add(new HoverboatProjectileSpiderJockey(EntitySpider.class, -1, "Spider Jockey"));
			File location = new File((mod_Hoverboat.class).getProtectionDomain().getCodeSource().getLocation().toURI());
			ClassLoader classloader = (mod_Hoverboat.class).getClassLoader();
			if (location.isFile() && (location.getName().endsWith(".jar") || location.getName().endsWith(".zip"))) {
				FileInputStream fileinputstream = new FileInputStream(location);
				ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
				do {
					ZipEntry zipentry = zipinputstream.getNextEntry();
					if (zipentry == null) {
						break;
					}
					String s1 = zipentry.getName();
					if (!zipentry.isDirectory() && s1.startsWith("hoverboat_tp_") && s1.endsWith(".class")) {
						mod_Hoverboat.addProjectiles(classloader, s1);
					}
				} while (true);
				fileinputstream.close();
			} else if (location.isDirectory()) {
				Package package1 = (ModLoader.class).getPackage();
				if (package1 != null) {
					String s = package1.getName().replace('.', File.separatorChar);
					location = new File(location, s);
				}
				File afile[] = location.listFiles();
				if (afile != null) {
					for (int j = 0; j < afile.length; j++) {
						String s2 = afile[j].getName();
						if (afile[j].isFile() && s2.startsWith("hoverboat_tp_") && s2.endsWith(".class")) {
							mod_Hoverboat.addProjectiles(classloader, s2);
						}
					}
				}
			}
			if (Block.blocksList != null) {
				for (int i = 0; i < Block.blocksList.length; i++) {
					if ((Block.blocksList[i] != null) && HoverboatProjectileBlock.IsAllowed(i)) {
						mod_Hoverboat.projectiles.add(new HoverboatProjectileBlock(i));
					}
				}
			}
		} catch (Throwable throwable) {
			throw new RuntimeException("Failed to get a list of registered entities.\r\n" + throwable.getMessage(), throwable);
		}
		Loaded = true;
	}

	private void InitSettings() {
		settingMultiBoatMode =  "Default Boat Mode", "Hoverboat.DefaultMode", 1, new String[] { "Boat", "Hover", "Flight" });
		settingMultiChestSize = "Chest Size", "Hoverboat.ChestSize", 1, new String[] { "Disabled", "Regular", "Double" });
		settingMultiDefKeyboardSteering = "Default Steering", "Hoverboat.DefaultSteering", 0, new String[] { "Minecraft Standard", "Free View", "Flight Style",
				"Versatile Mode" });
		settingFloatMaxSpeed = "Max Speed", "Hoverboat.MaxSpeed", 1.1F, 0.01F, 0.01F, 3F);
		settingFloatAcceleration = "Acceleration", "Hoverboat.Acceleration", 1.4F, 0.01F, 0.01F, 10F);
		settingFloatMaxAltTurnSpeed = "Alternate Turn Speed", "Hoverboat.AlternateTurnSpeed", 5F, 0.1F, 0.1F, 10F);
		settingFloatAltTurnSpeed = "Alternate Turn Damper", "Hoverboat.AlternateTurnSpeedDamper", 0.25F, 0.01F, 0.01F, 1F);
		settingFloatMinHover = "Min Hover Height", "Hoverboat.MinHoverHeight", 1.1F, 0.1F, 0.1F, 3F);
		settingFloatDefaultHover = "Default Hover Height", "Hoverboat.DefaultHoverHeight", 2.6F, 0.5F, 0.1F, 4F);
		settingFloatMaxHover = "Max Hover Height", "Hoverboat.MaxHoverHeight", 3.7F, 1.0F, 0.1F, 6F);
		settingFloatMaxJump = "Max Jump Height", "Hoverboat.MaxJumpHeight", 9F, 1.0F, 0.1F, 14F);
		settingFloatMinFriction = "Unmanned Friction", "Hoverboat.MinFriction", 0.8F, 0.0F, 0.01F, 1F);
		settingFloatMaxFriction = "Manned Friction", "Hoverboat.MaxFriction", 0.99F, 0.0F, 0.01F, 1F);
		settingFloatViewShift = "View (Seat) Shift", "Hoverboat.SeatShift", 0.0F, -1F, 0.01F, 1.0F);
		settingFloatParticles = "Particle Multiplier", "Hoverboat.ParticleMultiplier", 1.0F, 0.0F, 0.01F, 1.0F);
		settingFloatBrakeMult =  "Brake Speed", "Hoverboat.BrakeMult", 0.9F, 0.0F, 0.01F, 1.0F);
		settingFloatBrakeThreshold = "Brake Threshold", "Hoverboat.BrakeThreshold", 0.4F, 0.0F, 0.01F, 0.6F);
		settingBoolSub = "Submarine Mode", "Hoverboat.SubEnabled", true, "Enabled", "Disabled");
		settingBoolFireProof = "Fire Proof", "Hoverboat.FireProof", true, "Yes", "No");
		settingBoolUseAmmo = "Use Ammunition", "Hoverboat.UseAmmo", false, "Yes", "No");
		settingBoolShowHUD = "On Screen Display", "Hoverboat.ShowHUD", true, "Enabled", "Disabled");
		settingIntTntTicks = "TNT Timer", "Hoverboat.TNTTicks", 80, 10, 160);
		settingBoolAllowFastFov = "Have FOV Based on speed?", "Hoverboat.FastFOV", false);
		settingBoolEnableTNT = "Enable Drops", "Hoverboat.EnableTNT", true, "Enabled", "Disabled");
		settingIntTNTFireRate = "Drop Rate", "Hoverboat.DropRate", 150, 1, 5000);
		settingIntTntLines = "Drop Lines", "Hoverboat.DropLines", 3, 0, 20);
		settingFloatDropAccuracy = "Drop Accuracy", "Hoverboat.DropAccuracy", 0.0F, 0.0F, 0.01F, 1F);
		settingBoolEnableArrows = "Cannon / Arrow Firing", "Hoverboat.EnableProjectiles", true, "Enabled", "Disabled");
		settingIntArrowFireRate = "Fire Rate", "Hoverboat.CannonFireRate", 75, 1, 5000);
		settingFloatFireballSpeed = "Fireball Speed Booster", "Hoverboat.FireballBooster", 0.7F, 0.01F, 0.01F, 3F);
		settingFloatTNTVel = "Cannon Velocity", "Hoverboat.CannonVelocity", 1.5F, 0.5F, 0.01F, 4F);
		settingFloatArrowVel = "Arrow Velocity Multiplier", "Hoverboat.ArrowMult", 1.0F, 0.5F, 0.01F, 4F);
		settingFloatCannonAccuracy = "Cannon Accuracy", "Hoverboat.CannonAccuracy", 0.0F, 0.0F, 0.1F, 40F);
		settingKeyPark = "Park", "Hoverboat.KeyPark", 54);
		settingKeyBrake = "Brake", "Hoverboat.KeyBrake", 46);
		settingKeyFireArrow = "Fire Cannon / Arrow", "Hoverboat.KeyFireProjectile", 42);
		settingKeyFireTnt = "Drop", "Hoverboat.KeyDrop", 29);
		settingKeyShiftModeLeft = "Shift Mode Left", "Hoverboat.KeySML", 24);
		settingKeyShiftModeRight = "Shift Mode Right", "Hoverboat.KeySMR", 25);
		settingKeyShiftArrowLeft = "Shift Arrow Left", "Hoverboat.KeySAL", 37);
		settingKeyShiftArrowRight = "Shift Arrow Right", "Hoverboat.KeySAR", 38);
		settingKeyShiftDropLeft = "Shift Drop Left", "Hoverboat.KeySDL", 35);
		settingKeyShiftDropRight = "Shift Drop Right", "Hoverboat.KeySDR", 36);
		settingKeySelectBoat = "Select Boat", "Hoverboat.KeySelBoat", 47);
		settingKeySelectHover = "Select Hover", "Hoverboat.KeySelHover", 48);
		settingKeySelectFlight = "Select Flight", "Hoverboat.KeySelFlight", 49);
		settingKeySteeringLeft = "Shift Steer Mode Left", "Hoverboat.KeySteerLeft", 51);
		settingKeySteeringRight = "Shift Steer Mode Right", "Hoverboat.KeySteerRight", 52);
		settingDefaultProjectile = new SettingText("Hoverboat.DefaultProjectile", "NULL");
		settingDefaultDrop = new SettingText("Hoverboat.DefaultDrop", "NULL");
	}

	private void ReloadAllowedDrops() {
		AllowedDrops.clear();
		for (Iterator<?> iterator = projectiles.iterator(); iterator.hasNext();) {
			HoverboatProjectileType hoverboatprojectiletype = (HoverboatProjectileType) iterator.next();
			if (hoverboatprojectiletype.AllowForDrops) {
				AllowedDrops.add(hoverboatprojectiletype);
				if (settingDefaultProjectile.equals(hoverboatprojectiletype.GetName())) {
					hoverboatprojectiletype.MakeDefaultArrow();
				}
			}
		}
	}

	private void ReloadAllowedProjectiles() {
		if (!Loaded) {
			return;
		}
		AllowedProjectiles.clear();
		for (Iterator<?> iterator = projectiles.iterator(); iterator.hasNext();) {
			HoverboatProjectileType hoverboatprojectiletype = (HoverboatProjectileType) iterator.next();
			if (hoverboatprojectiletype.AllowForShoot) {
				AllowedProjectiles.add(hoverboatprojectiletype);
				if (settingDefaultProjectile.equals(hoverboatprojectiletype.GetName())) {
					hoverboatprojectiletype.MakeDefaultArrow();
				}
			}
		}
	}

	private static void addProjectiles(ClassLoader classloader, String s) {
		try {
			String s1 = s.split("\\.")[0];
			if (s1.contains("$")) {
				return;
			}
			Package package1 = (mod_Hoverboat.class).getPackage();
			if (package1 != null) {
				s1 = (new StringBuilder(String.valueOf(package1.getName()))).append(".").append(s1).toString();
			}
			Class<?> class1 = classloader.loadClass(s1);
			if (class1.isAssignableFrom(Hoverboat_ThirdParty.class)) {
				return;
			}
			Hoverboat_ThirdParty hoverboat_thirdparty = (Hoverboat_ThirdParty) class1.newInstance();
			if (hoverboat_thirdparty != null) {
				if (hoverboat_thirdparty.CanLoad().booleanValue()) {
					System.out.println((new StringBuilder("Hoverboat Third Party Loaded: ")).append(hoverboat_thirdparty.toString()).toString());
					try {
						HoverboatProjectileType ahoverboatprojectiletype[] = hoverboat_thirdparty.LoadProtectiles();
						if ((ahoverboatprojectiletype == null) || (ahoverboatprojectiletype.length == 0)) {
							System.out.println((new StringBuilder("\tHoverboat Third Party Mod ")).append(hoverboat_thirdparty.toString()).append(" Failed To Load Any Projectiles.").toString());
						} else {
							for (int i = 0; i < ahoverboatprojectiletype.length; i++) {
								System.out.println((new StringBuilder("\tHoverboat Third Party Mod ")).append(hoverboat_thirdparty.toString()).append(" Loaded Projectile ")
										.append(ahoverboatprojectiletype[i].GetName()).toString());
								projectiles.add(ahoverboatprojectiletype[i]);
							}
						}
					} catch (Throwable throwable1) {
						System.out.println((new StringBuilder("\tHoverboat Third Party Mod ")).append(hoverboat_thirdparty.toString()).append(" Failed To Load Projectiles. Exception:").toString());
						throwable1.printStackTrace();
					}
				} else {
					System.out.println((new StringBuilder("Hoverboat Third Party Is Not Able To Load: ")).append(hoverboat_thirdparty.toString()).toString());
				}
			}
		} catch (Throwable throwable) {
		}
	}
}
