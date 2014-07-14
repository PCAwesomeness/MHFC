package mhfc.heltrato.common.block;

import java.util.Random;

import mhfc.heltrato.MHFCMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockOreArmorSpherePlus extends Block{

	public BlockOreArmorSpherePlus() {
		super(Material.rock);
		setBlockName("orearmorsphereplus");
		setBlockTextureName("mhfc:orearmorsphereplus");
		setHardness(1.3F);
		setResistance(2.0F);
		setCreativeTab(MHFCMain.mhfctabs);
	}
	
	
	
	public int quantityDropped(Random random){
		return 1;
	}
	
	public void registerIcons(IIconRegister par1IconRegister){
		blockIcon = par1IconRegister.registerIcon("mhfc:orearmorsphereplus");
	}
	
}