package org.lcsim.service.api;

import org.lcsim.detector.IDetectorElement;
import org.lcsim.geometry.Detector;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface DetectorService extends Service {
	
	// Load a detector by name
	void loadDetector(String detectorName);
	
	// Access to org.lcsim.detector API
	IDetectorElement getDetectorElement(String name);
	
	// Access to compact API
	Detector getDetector();
}
