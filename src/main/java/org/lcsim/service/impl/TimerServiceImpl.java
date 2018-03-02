package org.lcsim.service.impl;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lcsim.service.api.TimerService;

/**
 * This class implements the TimerService.  
 * 
 * All internal time calculations are performed using nanoseconds by calling {@link System#nanoTime()}.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class TimerServiceImpl extends AbstractService implements TimerService {
	
	private static class TimerStats {
		
		String name;
		long start;
		long stop;
		TimerStatus status;		
		Map<String, Long> statRecs = new HashMap<String, Long>();
		
		TimerStats(String name) {
			this.name = name;
			this.status = TimerStatus.UNKNOWN;
		}
		
		synchronized void start(long start) {
			if (status == TimerStatus.RUNNING) {
				throw new RuntimeException("Timer already running: " + name);
			}
			this.start = start;
			status = TimerStatus.RUNNING;
		}
		
		synchronized void stop(long stop) {
			if (status == TimerStatus.STOPPED) {
				throw new RuntimeException("Timer not running: " + name);
			}
			this.stop = stop;
			status = TimerStatus.STOPPED;
		}
		
		long getStartTime() {
			return start;
		}
		
		long getStopTime() {
			return stop;
		}
		
		TimerStatus status() {
			return status;
		}
		
		long delta() {
			return stop - start;
		}
		
		void setStat(String name, Long val) {
			statRecs.put(name, val);					
		}
		
		Map<String, Long> getStats() {
			return statRecs;
		}
	}
	
	Map<String, TimerStats> timerMap = new HashMap<String, TimerStats>();
	
	TimerStats getTimerStats(String name) {
		if (timerMap.get(name) == null)
			timerMap.put(name, new TimerStats(name));
		return timerMap.get(name);
	}
	
	public void start(String name) {
		getTimerStats(name).start(System.nanoTime());
	}
	
	public void stop(String name) {
		getTimerStats(name).stop(System.nanoTime());
	}

	public long delta(String name) {
		return getTimerStats(name).delta();
	}

	public TimerStatus status(String name) {
		return getTimerStats(name).status();
	}
	
	public void addStat(String name, String tag, Long val) {
		getTimerStats(name).setStat(tag, val);
	}
		
	private static Long getUnitDiv(String unit) {
		Long unitDiv = 0L;
		if (unit == "nanoseconds" || unit == "ns") {
			unitDiv = 1L;
		} else if (unit == "microseconds" || unit == "mu") {
			unitDiv = 1000L;
		} else if (unit == "milliseconds" || unit == "ms") {
			unitDiv = 1000000L;
		} else if (unit == "seconds" || unit == "s") {
			unitDiv = 1000000000L;			          
		} else {
			throw new RuntimeException("Unrecognized unit: " + unit);
		}
		return unitDiv;
	}

	public void print(String name, String unit, PrintStream ps) {
		TimerStats stats = getTimerStats(name);
		Long unitDiv = getUnitDiv(unit);
		ps.println(name);		
		ps.println("  start = " + stats.getStartTime()/unitDiv + " " + unit);
		ps.println("  stop = " + stats.getStopTime()/unitDiv + " " + unit);
		ps.println("  elapsed = " + stats.delta()/unitDiv + " " + unit);
		ps.println("  stats:");
		for (Entry<String, Long> rec : stats.getStats().entrySet()) {
			System.out.println("    " + rec.getKey() + " = " + rec.getValue()/unitDiv + " " + unit);
		}		
	}
}
