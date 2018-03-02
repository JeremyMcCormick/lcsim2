package org.lcsim.service.impl;

import org.lcsim.service.api.Service;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
abstract class AbstractService implements Service {
	public String name() {
		return getClass().getSimpleName();
	}
}
