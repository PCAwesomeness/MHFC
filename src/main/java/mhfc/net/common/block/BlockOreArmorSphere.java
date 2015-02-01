package mhfc.net.common.block;

import java.util.Random;

import mhfc.net.MHFCMain;
import mhfc.net.common.util.lib.MHFCReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockOreArmorSphere extends Block {

	public BlockOreArmorSphere() {
		super(Material.rock);
		setBlockName(MHFCReference.block_orearmorshpere_name);
		setBlockTextureName(MHFCReference.block_orearmorsphere_tex);
		setHardness(1.3F);
		setResistance(2.0F);
		setCreativeTab(MHFCMain.mhfctabs);
	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}
}