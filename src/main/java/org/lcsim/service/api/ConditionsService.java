package org.lcsim.service.api;

import org.lcsim.conditions.CachedConditions;
import org.lcsim.conditions.ConditionsManager.ConditionsSetNotFoundException;
import org.lcsim.conditions.ConditionsSet;
import org.lcsim.conditions.RawConditions;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface ConditionsService {
	ConditionsSet getConditions(String name) throws ConditionsSetNotFoundException;
	<T> CachedConditions<T> getCachedConditions(Class<T> type, String name) throws ConditionsSetNotFoundException;
	RawConditions getRawConditions(String name) throws ConditionsSetNotFoundException;
}
