package org.lcsim.service;

import hep.aida.ICloud1D;
import hep.aida.IHistogramFactory;
import hep.aida.ITree;
import hep.physics.particle.properties.ParticleType;

import java.util.Random;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.lcsim.detector.IDetectorElement;
import org.lcsim.service.api.DetectorService;
import org.lcsim.service.api.HistogramService;
import org.lcsim.service.api.ServiceLocator;
import org.lcsim.service.api.TimerService;
import org.lcsim.service.api.LogService;
import org.lcsim.service.api.ParticlePropertiesService;
import org.lcsim.service.impl.ServiceLocatorFactory;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class ServicesTest extends TestCase {
	
	// Get the ServiceLocator for this runtime.
	ServiceLocator locator = ServiceLocatorFactory.createServiceLocator();
	
	// LogService.
	public void testLogService() {
		LogService logService = locator.findService(LogService.class);
		Logger logger = logService.getLogger(this);
		logger.info("Hello there!");
	}
	
	// HistogramService.
	public void testHistogramService() {			
		String cloudName = "cloud";
		String treeName = "Histograms";
		HistogramService histService = locator.findService(HistogramService.class);
		Random random = new Random();
		
		// First file of histograms.
		ITree tree = histService.tree(treeName);
		IHistogramFactory fac = histService.getHistogramFactory(tree);
		ICloud1D cloud = fac.createCloud1D(cloudName);
		for (int i=0; i<1000; i++) {
			cloud.fill(random.nextGaussian()*1000);
		}
		histService.saveTree(treeName);
		
		// Second file of histograms.
		String treeName2 = "SomeMoreHistograms";
		ITree tree2 = histService.tree(treeName2);
		IHistogramFactory fac2 = histService.getHistogramFactory(tree2);
		ICloud1D cloud2 = fac2.createCloud1D(cloudName);
		for (int i=0; i<1000; i++) {
			cloud2.fill(random.nextGaussian()*1000);
		}
		histService.saveTree(treeName2);
	}
	
	// ParticlePropertiesService.
	public void testParticlePropertiesService() {
		int pdgid = 22;
		ParticlePropertiesService pp = locator.findService(ParticlePropertiesService.class);
		ParticleType p = pp.getParticleType(pdgid);
		System.out.println("found particle type:" + p.getName());
	}
	
	// TimerService.
	public void testTimerService() {
		TimerService timerService = locator.findService(TimerService.class);
		String timerName = "MyTimer";
		timerService.start(timerName);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timerService.stop(timerName);
		Long val = timerService.delta(timerName);
		timerService.addStat(timerName, "Something", val);
		
		timerService.print(timerName, "ns", System.out);	
		timerService.print(timerName, "mu", System.out);
		timerService.print(timerName, "ms", System.out);
		timerService.print(timerName, "s", System.out);
	}
	
	public void testDetectorService() {
		DetectorService detService = locator.findService(DetectorService.class);
		detService.loadDetector("sidloi3");
		IDetectorElement det = detService.getDetectorElement("/");
		System.out.println("got top DetectorElement: " + det.getName());
	}
}
