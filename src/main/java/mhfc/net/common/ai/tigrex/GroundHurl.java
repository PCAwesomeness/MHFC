package mhfc.net.common.ai.tigrex;

import mhfc.net.common.ai.AttackAdapter;
import mhfc.net.common.entity.mob.EntityTigrex;
import mhfc.net.common.entity.projectile.EntityTigrexBlock;
import mhfc.net.common.util.world.WorldHelper;
import net.minecraft.util.Vec3;

public class GroundHurl extends AttackAdapter<EntityTigrex> {
	private static final float MIN_DIST = 10f;
	private static final int LAST_FRAME = 60;
	private static final int THROW_FRAME = 21;
	private static final int TURN_FRAMES = 14;

	private static final double SPLIT_MULTIPLIER = 0.125;
	private static final double THROW_HEIGHT = 0.35;
	private static final float TURN_RATE = 2;

	private static final double MAX_ANGLE = 0.155;

	private boolean thrown;
	private int weightFactor;

	public GroundHurl() {
		setAnimation("mhfc:models/Tigrex/dirtthrow.mcanm");
		setLastFrame(LAST_FRAME);
		weightFactor = 1;
	}

	@Override
	public float getWeight() {
		EntityTigrex tigrex = this.getEntity();
		target = tigrex.getAttackTarget();
		if (target == null)
			return DONT_SELECT;
		Vec3 toTarget = WorldHelper.getVectorToTarget(tigrex, target);
		if (toTarget.normalize().dotProduct(tigrex.getLookVec()) < MAX_ANGLE)
			return DONT_SELECT;
		double dist = toTarget.lengthVector();
		int weight = weightFactor;
		if (weightFactor > 1)
			weightFactor--;
		return (float) (dist - MIN_DIST) / (weight);
	}

	@Override
	public void beginExecution() {
		super.beginExecution();
		thrown = false;
		weightFactor *= 3;
	}

	@Override
	public void update() {
		if (thrown)
			return;
		EntityTigrex tigrex = getEntity();
		if (getRecentFrame() < THROW_FRAME) {
			if (getRecentFrame() < TURN_FRAMES) {
				tigrex.getTurnHelper().updateTurnSpeed(TURN_RATE);
				tigrex.getTurnHelper().updateTargetPoint(
					tigrex.getAttackTarget());
			} else if (getRecentFrame() == TURN_FRAMES) {
				Vec3 look = tigrex.getLookVec();
				tigrex.getMoveHelper().setMoveTo(tigrex.posX + look.xCoord,
					tigrex.posY + look.yCoord, tigrex.posZ + look.zCoord, 0.2);
			}
			return;
		}
		Vec3 look = tigrex.getLookVec();
		Vec3 lookVec = tigrex.getLookVec();
		Vec3 rightSide = lookVec.crossProduct(Vec3.createVectorHelper(0, 1, 0));
		for (int i = 0; i < 3; i++) {
			EntityTigrexBlock block = new EntityTigrexBlock(tigrex.worldObj,
				tigrex);
			double xCo = look.xCoord;
			double yCo = look.yCoord + THROW_HEIGHT;
			double zCo = look.zCoord;
			if (i == 0) {
				xCo += rightSide.xCoord * SPLIT_MULTIPLIER;
				zCo += rightSide.zCoord * SPLIT_MULTIPLIER;
			} else if (i == 2) {
				xCo -= rightSide.xCoord * SPLIT_MULTIPLIER;
				zCo -= rightSide.zCoord * SPLIT_MULTIPLIER;
			}
			block.setThrowableHeading(xCo, yCo, zCo, 1f, 0.5f);
			tigrex.worldObj.spawnEntityInWorld(block);
		}
		thrown = true;
	}

}
