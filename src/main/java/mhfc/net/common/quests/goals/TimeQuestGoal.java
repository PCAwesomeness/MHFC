/**
 *
 */
package mhfc.net.common.quests.goals;

import mhfc.net.common.eventhandler.MHFCDelayedJob;
import mhfc.net.common.eventhandler.MHFCJobHandler;
import mhfc.net.common.quests.QuestRunningInformation.InformationType;
import mhfc.net.common.quests.api.QuestGoal;
import mhfc.net.common.quests.api.QuestGoalSocket;

/**
 *
 */
public class TimeQuestGoal extends QuestGoal implements MHFCDelayedJob {

	protected boolean isFailed = false;
	protected boolean active;
	protected int ticksToFail;
	protected long timeOfLastUpdate;
	protected int initialTicksToFail;

	public TimeQuestGoal(QuestGoalSocket socket, int initialTime) {
		super(socket);
		this.initialTicksToFail = initialTime;
		ticksToFail = initialTime;
		active = false;
	}

	public TimeQuestGoal(int initialTime) {
		this(null, initialTime);
	}

	@Override
	public boolean isFulfilled() {
		return !isFailed;
	}

	@Override
	public boolean isFailed() {
		return isFailed;
	}

	@Override
	public void reset() {
		MHFCJobHandler.instance().remove(this);
		isFailed = false;
		ticksToFail = initialTicksToFail;
	}

	@Override
	public void setActive(boolean newActive) {
		if (newActive) {
			if (ticksToFail > 0 && !active) {
				MHFCJobHandler.instance().insert(this, ticksToFail);
			}
			timeOfLastUpdate = System.currentTimeMillis();
			active = true;
			notifyOfStatus(isFulfilled(), isFailed());
		} else {
			int delay = MHFCJobHandler.instance().getDelay(this);
			ticksToFail = delay;
			MHFCJobHandler.instance().remove(this);
			active = false;
		}
	}

	@Override
	public void executeJob() {
		isFailed = true;
		notifyOfStatus(isFulfilled(), isFailed());
	}

	@Override
	public int getInitialDelay() {
		return initialTicksToFail;
	}

	@Override
	public void questGoalFinalize() {
		MHFCJobHandler.instance().remove(this);
	}

	@Override
	public String modify(InformationType type, String current) {
		// TODO Externalize and unlocalize these strings
		int ticksToFail = MHFCJobHandler.instance().getDelay(this);
		if (ticksToFail < 0)
			ticksToFail = initialTicksToFail;
		switch (type) {
			case TimeLimit :
				current += (current.endsWith("\n") || current.matches("\\s*")
					? ""
					: "\n");
				if (active) {
					current += "{time:" + ticksToFail + "}/ "
						+ parseTimeFromTicks(initialTicksToFail);
				} else {
					current += parseTimeFromTicks(ticksToFail);
				}
				break;
			case LongStatus :
				current += (current.endsWith("\n") || current.matches("\\s*")
					? ""
					: "\n");
				current += "Finish within "
					+ (active ? " {time:" + ticksToFail + "} of " : "") + "a "
					+ parseTimeFromTicks(initialTicksToFail) + "Time Limit";
				break;
			case ShortStatus :
				current += (current.endsWith("\n") || current.matches("\\s*")
					? ""
					: "\n");
				if (active) {
					current += "{time:" + ticksToFail + "} remaining";
				} else {
					current += parseTimeFromTicks(ticksToFail) + "limit";
				}
				break;
			default :
				break;
		}
		return current;
	}

	private static String parseTimeFromTicks(long delta) {
		delta /= MHFCJobHandler.ticksPerSecond;
		return "" + (delta / 3600 > 0 ? delta / 3600 + "h " : "")
			+ ((delta % 3600) / 60 > 0 ? (delta % 3600) / 60 + "min " : "")
			+ (delta % 60 > 0 ? delta % 60 + "s " : "");
	}
}
