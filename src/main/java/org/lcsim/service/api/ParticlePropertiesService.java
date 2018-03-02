package org.lcsim.service.api;

import hep.physics.particle.properties.ParticleType;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface ParticlePropertiesService extends Service {
	ParticleType getParticleType(int pdgid);
}