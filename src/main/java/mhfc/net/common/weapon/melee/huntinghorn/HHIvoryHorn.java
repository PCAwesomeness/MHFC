package mhfc.net.common.weapon.melee.huntinghorn;

import java.util.List;

import mhfc.net.common.helper.MHFCWeaponMaterialHelper;
import mhfc.net.common.util.Cooldown;
import mhfc.net.common.util.lib.MHFCReference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class HHIvoryHorn extends HuntingHornClass {

	public HHIvoryHorn() {
		super(MHFCWeaponMaterialHelper.HHIvoryHorn, 2000);
		setUnlocalizedName(MHFCReference.weapon_hh_ivoryhorn_name);
		getWeaponDescription("No Element", 1);
		elementalType(false, false);
		enableCooldownDisplay = true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack iStack, World world,
			EntityPlayer player) {
		weapon.onItemRightClick(iStack, world, player);
		if (Cooldown.canUse(iStack, cooldown)) {
			@SuppressWarnings("unchecked")
			List<Entity> list = player.worldObj
					.getEntitiesWithinAABBExcludingEntity(
							player.getLastAttacker(),
							player.boundingBox.expand(12D, 8D, 12D));
			for (Entity entity : list) {
				if (entity != null) {
					if (entity instanceof EntityPlayer
							|| entity instanceof EntityVillager)
						((EntityLivingBase) entity).heal(8F);
				}
			}
		}
		// player.motionY++;
		return iStack;
	}
}
