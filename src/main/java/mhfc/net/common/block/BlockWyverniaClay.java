package mhfc.net.common.block;

import java.util.Random;

import mhfc.net.MHFCMain;
import mhfc.net.common.core.registry.MHFCRegItem;
import mhfc.net.common.util.lib.MHFCReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockWyverniaClay extends Block {

	public BlockWyverniaClay() {
		super(Material.clay);
		setBlockName(MHFCReference.block_wyverianclay_name);
		setBlockTextureName(MHFCReference.tex_block_wyverianclay);
		setHardness(0.9f);
		setCreativeTab(MHFCMain.mhfctabs);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return MHFCRegItem.mhfcitemwyverniaclay;
	}

	@Override
	public int quantityDropped(Random random) {
		return 2;
	}
}
