package mhfc.net.common.item.materials;

import java.util.List;

import mhfc.net.MHFCMain;
import mhfc.net.common.core.registry.MHFCItemRegistry;
import mhfc.net.common.util.lib.MHFCReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import mhfc.net.common.util.SubTypedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemDeviljho extends Item {
	public static enum DeviljhoSubType
			implements
				SubTypedItem.SubTypeEnum<Item> {
		SCALE (MHFCReference.item_deviljho0_name, MHFCReference.item_deviljho0_icon), //
		FANG  (MHFCReference.item_deviljho1_name , MHFCReference.item_deviljho1_icon), //
		HIDE  (MHFCReference.item_deviljho2_name , MHFCReference.item_deviljho2_icon), //
		TALON (MHFCReference.item_deviljho3_name, MHFCReference.item_deviljho3_icon), //
		SCALP (MHFCReference.item_deviljho4_name, MHFCReference.item_deviljho4_icon),
		TAIL  (MHFCReference.item_deviljho5_name , MHFCReference.item_deviljho5_icon)		;
		

		public final String name;
		public final String texture;
		private DeviljhoSubType(String name, String texture) {
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
			return MHFCItemRegistry.mhfcitemdeviljho;
		}
	}

	private final SubTypedItem<Item, DeviljhoSubType> itemPerk;

	public ItemDeviljho() {
		itemPerk = new SubTypedItem<>(DeviljhoSubType.class);
		setHasSubtypes(true);
		setUnlocalizedName(MHFCReference.item_deviljho_basename);
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
			case SCALP :
				par3List.add("Rare Drop by Deviljho");
				break;
			default :
				par3List.add("Drop by Deviljho");
				break;
		}
	}
}
