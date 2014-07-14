package mhfc.heltrato.common.item.armor;

import java.util.List;

import mhfc.heltrato.MHFCMain;
import mhfc.heltrato.common.core.registry.MHFCRegItem;
import mhfc.heltrato.common.helper.MHFCArmorModelHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RathalosArmor extends ItemArmor{
	
	private Item item;

	public RathalosArmor(ArmorMaterial p_i45325_1_, int p_i45325_2_,
			int p_i45325_3_) {
		super(p_i45325_1_, p_i45325_2_, p_i45325_3_);
		setCreativeTab(MHFCMain.mhfctabs);
	}
	
	 @SideOnly(Side.CLIENT)
	  public void registerIcons(IIconRegister iconRegister) {
	     this.itemIcon = iconRegister.registerIcon("mhfc:mhf_" + this.armorType);
	  }
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot,
	String type) {
		if(stack.getItem() == MHFCRegItem.mhfcitemrathaloshelm || stack.getItem() == MHFCRegItem.mhfcitemrathaloschest || stack.getItem() == MHFCRegItem.mhfcitemrathalosboots){
			return "mhfc:textures/armor/rathalos_layer_1.png";
		}if(stack.getItem() == MHFCRegItem.mhfcitemrathaloslegs){
			return "mhfc:textures/armor/rathalos_layer_2.png";
		}
		return null;
		
		
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Attack Up H");
		par3List.add("Poison D[+4%]");
		par3List.add("+ 10 Fire");
		par3List.add("+ 15 Thunder");
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving,
			ItemStack itemStack, int armorSlot) {

		ModelBiped armorModel = null;

		if(itemStack != null){
			int type = ((ItemArmor)itemStack.getItem()).armorType;

			if(type == 1 || type == 3 || type == 0){
				armorModel = MHFCArmorModelHelper.getArmorModel(4);
			}
			if(armorModel != null){
				armorModel.bipedHead.showModel = armorSlot == 0;
				armorModel.bipedHeadwear.showModel = armorSlot == 0;
				armorModel.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
				armorModel.bipedRightArm.showModel = armorSlot == 1;
				armorModel.bipedLeftArm.showModel = armorSlot == 1;
				armorModel.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
				armorModel.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;

				armorModel.isSneak = entityLiving.isSneaking();
				armorModel.isRiding = entityLiving.isRiding();
				armorModel.isChild = entityLiving.isChild();
				armorModel.heldItemRight = entityLiving.getEquipmentInSlot(0) != null ? 1 :0;
				if(entityLiving instanceof EntityPlayer){
					armorModel.aimedBow =((EntityPlayer)entityLiving).getItemInUseDuration() > 2;
				}
				return armorModel;
			}	
		}
		return null;
	}

	}