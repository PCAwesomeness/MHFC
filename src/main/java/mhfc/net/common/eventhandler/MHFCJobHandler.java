package mhfc.net.common.eventhandler;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;

public class MHFCJobHandler {
	public static final int ticksPerSecond = 50;
	public static final MHFCJobHandler instance = new MHFCJobHandler();

	private abstract class JobListElement {
		public abstract JobListElement insert(MHFCDelayedJob job, int delay);

		public abstract JobListElement tick();

		public abstract int getDelay(MHFCDelayedJob job);

		public abstract void remove(MHFCDelayedJob job);

		public abstract boolean containsJob(MHFCDelayedJob job);

		public abstract int queueLength();

		public abstract int maxQueue();
	}

	private class DelayedJob extends JobListElement {

		private volatile List<MHFCDelayedJob> jobs;
		private volatile JobListElement following;
		private volatile int ticksToExecution;

		public DelayedJob(MHFCDelayedJob initialJob, int delay,
			JobListElement following) {
			if (initialJob == null)
				throw new IllegalArgumentException(
					"Initial job may not be null");
			jobs = new Vector<MHFCDelayedJob>();
			ticksToExecution = delay;
			jobs.add(initialJob);
			this.following = following;
		}

		@Override
		synchronized public JobListElement insert(MHFCDelayedJob job, int delay) {
			if (delay > ticksToExecution) {
				// System.out.println("Inserting after me " + ticksToExecution+
				// "+" + delay);
				following = following.insert(job, delay - ticksToExecution);
				return this;
			} else if (delay == ticksToExecution) {
				// System.out.println("Inserting into " + ticksToExecution +
				// "+"+ delay);
				jobs.add(job);
				return this;
			} else {
				ticksToExecution -= delay;
				// System.out.println("Inserting before " + ticksToExecution +
				// "+"+ delay);
				return new DelayedJob(job, delay, this);
			}
		}

		@Override
		synchronized public JobListElement tick() {
			--ticksToExecution;
			if (ticksToExecution == 0) {
				Iterator<MHFCDelayedJob> iter = jobs.iterator();
				while (iter.hasNext()) {
					iter.next().executeJob();
				}
				jobs.clear();
				return following.tick();
			} else if (ticksToExecution < 0)
				return following.tick();
			return this;
		}

		@Override
		public int getDelay(MHFCDelayedJob job) {
			if (jobs.contains(job))
				return ticksToExecution;
			int a = following.getDelay(job);
			return a < 0 ? -1 : ticksToExecution + a;
		}

		@Override
		synchronized public void remove(MHFCDelayedJob job) {
			jobs.remove(job);
			following.remove(job);
		}

		@Override
		public boolean containsJob(MHFCDelayedJob job) {
			return jobs.contains(job) || following.containsJob(job);
		}

		@Override
		public int queueLength() {
			return 1 + following.queueLength();
		}

		@Override
		public int maxQueue() {
			return ticksToExecution + following.maxQueue();
		}
	}

	private class JobListEnd extends JobListElement {

		@Override
		public JobListElement insert(MHFCDelayedJob job, int delay) {
			return new DelayedJob(job, delay, this);
		}

		@Override
		public JobListElement tick() {
			return this;
		}

		@Override
		public int getDelay(MHFCDelayedJob job) {
			return -1;
		}

		@Override
		public void remove(MHFCDelayedJob job) {
		}

		@Override
		public boolean containsJob(MHFCDelayedJob job) {
			return false;
		}

		@Override
		public int queueLength() {
			return 0;
		}

		@Override
		public int maxQueue() {
			return 0;
		}
	}

	public static MHFCJobHandler instance() {
		return instance;
	}

	private volatile JobListElement startOfList;
	private List<MHFCDelayedJob> listOfRemoves = new Vector<MHFCDelayedJob>();

	private MHFCJobHandler() {
		startOfList = new JobListEnd();
	}

	@SubscribeEvent
	public void receiveTick(ClientTickEvent tick) {
		synchronized (this) {
			cleanUp();
			startOfList = startOfList.tick();
		}
	}

	@SubscribeEvent
	public void receiveTick(ServerTickEvent tick) {
		synchronized (this) {
			cleanUp();
			if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
				startOfList = startOfList.tick();
			}
		}
	}

	private synchronized void cleanUp() {
		for (MHFCDelayedJob job : listOfRemoves) {
			startOfList.remove(job);
		}
		listOfRemoves.clear();
	}

	public void insert(MHFCDelayedJob job, int delay) {
		if (delay >= 0)
			startOfList = startOfList.insert(job, delay);
	}

	public void remove(MHFCDelayedJob job) {
		listOfRemoves.add(job);
	}

	public boolean containsJob(MHFCDelayedJob job) {
		return startOfList.containsJob(job);
	}

	public int getDelay(MHFCDelayedJob job) {
		return startOfList.getDelay(job);
	}

	public int getMaxDelay() {
		return startOfList.maxQueue();
	}
}
