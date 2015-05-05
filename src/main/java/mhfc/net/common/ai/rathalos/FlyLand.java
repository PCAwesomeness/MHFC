package mhfc.net.common.ai.rathalos;

import mhfc.net.common.ai.AttackAdapter;
import mhfc.net.common.entity.mob.EntityRathalos;
import mhfc.net.common.entity.mob.EntityRathalos.Stances;
import net.minecraft.entity.EntityLiving;

public class FlyLand extends AttackAdapter<EntityRathalos> {

	public static final float WEIGHT = 1.0f;
	private static final int LAND_LAST_FRAME = 10;

	boolean hasTouchedDown;

	public FlyLand() {
		setLastFrame(LAND_LAST_FRAME);
	}

	@Override
	public float getWeight() {
		EntityRathalos rathalos = this.getEntity();
		if (rathalos.getAttackManager().getCurrentStance() != Stances.FLYING)
			return DONT_SELECT;
		return WEIGHT;
	}

	@Override
	public void beginExecution() {
		super.beginExecution();
		EntityRathalos rathalos = this.getEntity();
		rathalos.getAttackManager().setNextStance(Stances.GROUND);
		hasTouchedDown = false;
	}

	@Override
	public void update() {
		super.update();
		if (!hasTouchedDown) {
			EntityLiving entity = getEntity();
			entity.moveFlying(0, -0.2f, 0);
			if (!entity.isAirBorne) {
				hasTouchedDown = true;
				setToNextFrame(-1);
			}
		} else {

		}
	}

	@Override
	public boolean shouldContinue() {
		return hasTouchedDown && super.shouldContinue();
	}

}