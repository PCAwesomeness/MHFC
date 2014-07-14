package mhfc.heltrato.common.item.weapon.type;

import mhfc.heltrato.MHFCMain;
import mhfc.heltrato.common.entity.mob.EntityKirin;
import mhfc.heltrato.common.entity.mob.EntityTigrex;
import mhfc.heltrato.common.entity.type.EntityWyvernHostile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class SemiLethalClass extends ItemSword{
	

	public SemiLethalClass(ToolMaterial getType) {
		super(getType);
		setCreativeTab(MHFCMain.mhfctabs);
		setFull3D();
		
	}
	
	public boolean isFull3D(){
        return true;
    }
	
	
	
	
	
	
	
	

}