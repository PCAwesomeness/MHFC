package mhfc.net.common.item.materials;

import java.util.List;

import mhfc.net.MHFCMain;
import mhfc.net.common.core.registry.MHFCItemRegistry;
import mhfc.net.common.util.SubTypedItem;
import mhfc.net.common.util.lib.MHFCReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemRemobra extends Item {
	public static enum RemobraSubType
			implements
				SubTypedItem.SubTypeEnum<Item> {
		SKIN(MHFCReference.item_remobra0_name,
				MHFCReference.item_remobra0_icon), //
		STRIPE(MHFCReference.item_remobra1_name,
				MHFCReference.item_remobra1_icon), //
		SKULL(MHFCReference.item_remobra2_name,
				MHFCReference.item_remobra2_icon), //
		WING(MHFCReference.item_remobra3_name,
				MHFCReference.item_remobra3_icon),;//
			

		public final String name;
		public final String texture;
		private RemobraSubType(String name, String texture) {
			this.name = name;
			this.texture = texture;
		}
		@Override
		public String getName() {
			return this.name;
		}
		@Override
		public String getTexPath() {
			return this.texture;
		}
		@Override
		public Item getBaseItem() {
			return MHFCItemRegistry.mhfcitemremobra;
		}
	}

	private final SubTypedItem<Item, RemobraSubType> itemPerk;

	public ItemRemobra() {
		itemPerk = new SubTypedItem<>(RemobraSubType.class);
		setHasSubtypes(true);
		setUnlocalizedName(MHFCReference.item_remobra_basename);
		setCreativeTab(MHFCMain.mhfctabs);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return super.getUnlocalizedName(itemStack) + "."
				+ itemPerk.getSubType(itemStack).name;
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return itemPerk.getIcons()[meta];
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemPerk.registerIcons(iconRegister);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getSubItems(Item base, CreativeTabs tab,
			@SuppressWarnings("rawtypes") List list) {
		itemPerk.getSubItems(base, list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer,
			@SuppressWarnings("rawtypes") List par3List, boolean par4) {
		switch (itemPerk.getSubType(par1ItemStack)) {
			case WING :
				par3List.add("Rare Drop by Remobra");
				break;
			default :
				par3List.add("Drop by Remobra");
				break;
		}
	}
}
