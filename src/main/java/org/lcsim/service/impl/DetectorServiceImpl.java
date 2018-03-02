package org.lcsim.service.impl;

import org.lcsim.detector.IDetectorElement;
import org.lcsim.geometry.Detector;
import org.lcsim.geometry.util.DetectorLocator;
import org.lcsim.service.api.DetectorService;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class DetectorServiceImpl extends AbstractService implements DetectorService {

	IDetectorElement top = null;
	Detector detector = null;
	
	public void loadDetector(String detectorName) {
		Detector detector = DetectorLocator.findDetector(detectorName);
		top = detector.getDetectorElement();
	}

	public IDetectorElement getDetectorElement(String path) {
		if (path == null || path.equals("/")) {
			return top;
		} else {
			return top.findDetectorElement(path);
		}
	}
	
	public Detector getDetector() {
		return detector;
	}	
}
