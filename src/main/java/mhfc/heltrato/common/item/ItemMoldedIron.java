package mhfc.heltrato.common.item;

import mhfc.heltrato.MHFCMain;
import mhfc.heltrato.common.util.lib.MHFCReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

public class ItemMoldedIron extends Item {

	public ItemMoldedIron() {
		super();
		setUnlocalizedName(MHFCReference.item_moldediron_name);
		setCreativeTab(MHFCMain.mhfctabs);
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister
				.registerIcon(MHFCReference.item_moldediron_icon);
	}
}