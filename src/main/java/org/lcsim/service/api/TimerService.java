package org.lcsim.service.api;

import java.io.PrintStream;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface TimerService extends Service {
	void start(String name);
	void stop(String name);
	long delta(String name);
	TimerStatus status(String name);
	void addStat(String name, String tag, Long val);
	void print(String name, String unit, PrintStream ps);
	enum TimerStatus {
		UNKNOWN, RUNNING, STOPPED
	}	
}