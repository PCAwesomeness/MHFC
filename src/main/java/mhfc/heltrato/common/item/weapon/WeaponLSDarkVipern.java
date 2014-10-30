package mhfc.heltrato.common.item.weapon;

import java.util.List;

import mhfc.heltrato.common.entity.mob.EntityKirin;
import mhfc.heltrato.common.item.weapon.type.LethalClass;
import mhfc.heltrato.common.util.lib.MHFCReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class WeaponLSDarkVipern extends LethalClass {

	private float weaponDamage;

	public WeaponLSDarkVipern(ToolMaterial getType) {
		super(getType);
		setUnlocalizedName(MHFCReference.weapon_ls_darkvipern_name);
		setFull3D();
		weaponDamage = getType.getDamageVsEntity() - 4;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer,
			@SuppressWarnings("rawtypes") List par3List, boolean par4) {
		par3List.add("Longsword Class");
		par3List.add("\u00a79Poison-Element");
		par3List.add("\u00a72Lethal Damage");
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister
				.registerIcon(MHFCReference.weapon_ls_darkvipern_icon);
	}

	public float getDamageVsEntity(Entity entity) {

		return weaponDamage;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase ent,
			EntityLivingBase player) {

		float damage = 0.0f;
		player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 60, 3));
		ent.addPotionEffect(new PotionEffect(Potion.poison.id, 40, 2));
		if (ent instanceof EntityKirin) {
			damage = (weaponDamage - 12);
		}

		DamageSource dmgSource = DamageSource
				.causePlayerDamage((EntityPlayer) player);
		ent.attackEntityFrom(dmgSource, damage);

		return true;
	}

}