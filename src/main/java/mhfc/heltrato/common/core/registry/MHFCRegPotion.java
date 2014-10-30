package mhfc.heltrato.common.core.registry;

import mhfc.heltrato.common.helper.MHFCReflectionHelper;
import mhfc.heltrato.common.potion.PotionAttackUpLow;
import mhfc.heltrato.common.potion.PotionKirinBless;
import mhfc.heltrato.common.potion.PotionParalyze;
import net.minecraft.potion.Potion;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLCommonHandler;

public class MHFCRegPotion 
{
	public static Potion mhfcpotionshock;
	public static Potion mhfcpotionkirinbless;
	public static Potion mhfcpotionattackuplow;
	
	
	public static int potionOffset;
	
	private static final int MAXNEWPOTIONS = 16;

	public static void init()
	{
		extendPotionsArray();
		intializePotions();
	}
	
	private static void extendPotionsArray()
	{
		FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[MHFC] Extending Potions Array.");
		potionOffset = Potion.potionTypes.length;
		
		Potion[] potionTypes = new Potion[potionOffset + MAXNEWPOTIONS];
		System.arraycopy(Potion.potionTypes, 0, potionTypes, 0, potionOffset);

		MHFCReflectionHelper.setPrivateFinalValue(Potion.class, null, potionTypes, "potionTypes", "field_76425_a");
	}

	private static void intializePotions()
	{
		mhfcpotionshock = new PotionParalyze(getNextID(),true,1684929);
		mhfcpotionkirinbless = new PotionKirinBless(getNextID(), false, 591932);
		mhfcpotionattackuplow = new PotionAttackUpLow(getNextID(), false, 493491);
	}
	
	public static int getNextID()
	{
		return potionOffset++ - 1;
	}
}