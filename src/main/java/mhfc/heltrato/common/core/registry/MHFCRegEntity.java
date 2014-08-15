package mhfc.heltrato.common.core.registry;

import mhfc.heltrato.MHFCMain;
import mhfc.heltrato.common.entity.mob.EntityAptonoth;
import mhfc.heltrato.common.entity.mob.EntityKirin;
import mhfc.heltrato.common.entity.mob.EntityPopo;
import mhfc.heltrato.common.entity.mob.EntityRathalos;
import mhfc.heltrato.common.entity.mob.EntityTigrex;
import mhfc.heltrato.common.entity.part.EntityTigrexTail;
import mhfc.heltrato.common.entity.projectile.EntityRathalosFireball;
import mhfc.heltrato.common.entity.projectile.EntityTigrexBlock;
import cpw.mods.fml.common.registry.EntityRegistry;

public class MHFCRegEntity {
	
	public static int mobID = 300;
	public static int projID = 200;
	private static MHFCMain mod = MHFCMain.instance;
	private static EntityRegistry entity;

	public static void render(){
		
		registerMonster();
		registerOthers();
		
	}
	
	public static void registerMonster() {
		
		getMobID(EntityAptonoth.class, "aptonoth");
		getMobID(EntityPopo.class, "popo");
		getMobID(EntityRathalos.class, "rathalos");
		getMobID(EntityTigrex.class, "tigrex");
		getMobID(EntityKirin.class, "kirin");
		
	}
	
	public static void registerOthers() {
		
		getEntityID(EntityTigrexBlock.class , "TigrexBlock");
		getEntityID(EntityRathalosFireball.class, "Fireball");
	}
	
	private static void getMobID(Class entityClass, String name) {
		int monsterID = getMobID();
		entity.registerModEntity(entityClass, name, monsterID, mod, 64, 1, true);
		
	}
	
	private static void getEntityID(Class clazz, String name) {
		int projectileID = getProjID();
		entity.registerModEntity(clazz, name, projectileID, mod, 64, 10, true);
	}
	
	public static int getMobID(){
		return mobID++;
	}
	
	public static int getProjID(){
		return projID++;
	}

}