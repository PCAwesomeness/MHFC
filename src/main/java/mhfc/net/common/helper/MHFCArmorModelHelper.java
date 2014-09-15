package mhfc.net.common.helper;

import mhfc.net.client.model.armor.ModelKirinArmor;
import mhfc.net.client.model.armor.ModelKirinSArmor;
import mhfc.net.client.model.armor.ModelRathalosArmor;
import mhfc.net.client.model.armor.ModelTigrexArmor;
import mhfc.net.client.model.armor.ModelYukumoArmor;
import net.minecraft.client.model.ModelBiped;

public class MHFCArmorModelHelper {
	
	/**
	 * Param: this is where the switches for armor models are located.
	 * DN: int (id) <-- case.
	 * 
	 * 
	 * */
	
	public static final ModelTigrexArmor tigrex = new ModelTigrexArmor(1.0F);
	public static final ModelKirinArmor kirin = new ModelKirinArmor(1.0F);
	public static final ModelKirinSArmor kirinS = new ModelKirinSArmor(1.0F);
	public static final ModelYukumoArmor yukumo = new ModelYukumoArmor(1.0F);
	public static final ModelRathalosArmor rathalos = new ModelRathalosArmor(1.0F);


	
	public static ModelBiped getArmorModel(int id){
		switch(id) {
		case 0:
			return tigrex;
		case 1:
			return kirin;
		case 2:
			return kirinS;
		case 3:
			return yukumo;
		case 4:
			return rathalos;
		case 5:
			default:
				break;
		}
		return tigrex;
}
}