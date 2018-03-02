package org.lcsim.service.impl;

import hep.physics.particle.properties.ParticlePropertyManager;
import hep.physics.particle.properties.ParticlePropertyProvider;
import hep.physics.particle.properties.ParticleType;

import org.lcsim.service.api.ParticlePropertiesService;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class ParticlePropertiesServiceImpl extends AbstractService implements ParticlePropertiesService {
	
	ParticlePropertyProvider mgr = ParticlePropertyManager.getParticlePropertyProvider();
	
	public ParticleType getParticleType(int pdgid) {
		return mgr.get(pdgid);
	}
}
