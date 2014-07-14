package mhfc.heltrato.common.item.weapon.type;

import mhfc.heltrato.MHFCMain;
import mhfc.heltrato.common.entity.mob.EntityKirin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class LethalClass extends ItemSword{
	
	public float damageWeapon;

	public LethalClass(ToolMaterial getType) {
		super(getType);
		setCreativeTab(MHFCMain.mhfctabs);
	}
	
	public boolean isFull3D(){
        return true;
    }
	
	
	
	

}