package org.lcsim.service.api;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface LogService extends Service {
	<T> Logger getLogger(T object);
	Logger getLogger(String logger);
	void configLogger(Logger logger, Handler ch, Formatter formatter, Level level);
	
	static class MessageOnly extends Formatter {
		public String format(LogRecord rec) {
			StringBuilder b = new StringBuilder();
			b.append(rec.getMessage());
			b.append(System.getProperty("line.separator"));
			return b.toString();
		}
	}
	
	static class SingleLine extends Formatter {
		public String format(LogRecord rec) {			
			StringBuilder b = new StringBuilder();			
			b.append(new Date());
			b.append(" ");
			b.append(rec.getSourceClassName());
			b.append(" ");
			b.append(rec.getSourceMethodName());
			b.append(" ");
			b.append(rec.getLevel());
			b.append(" ");
			b.append(rec.getMessage());
			b.append(System.getProperty("line.separator"));
			return b.toString();
			
		}
	}	
}