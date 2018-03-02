package org.lcsim.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lcsim.service.api.LogService;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public final class LogServiceImpl extends AbstractService implements LogService {
	
	Map<Object,Logger> logMap = new HashMap<Object,Logger>();
		
	public <T> Logger getLogger(T object) {			
	    if (!logMap.containsKey(object)) {			
			Logger logger = Logger.getLogger(object.getClass().getName() + "#" + object.hashCode());
			logMap.put(object, logger);
		}
	    return logMap.get(object);
	}
	
	public Logger getLogger(String name) {
		if (!logMap.containsKey(name)) {		
			logMap.put(name, Logger.getLogger(name));
		}
		return logMap.get(name);
	}
	
	public void configLogger(Logger logger, Handler ch, Formatter formatter, Level level) {
		logger.setLevel(level);
		ch.setFormatter(formatter);
		logger.setUseParentHandlers(false);
		logger.addHandler(ch);
	}	
}