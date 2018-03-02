package org.lcsim.service.api;

import hep.aida.IHistogramFactory;
import hep.aida.ITree;

import java.io.File;

/**
 * Provides basic access to AIDA trees and factories.  
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface HistogramService extends Service {	
	boolean hasTree(String name);
	ITree tree(String name);
	void removeTree(String name);
	void saveTree(String name);
	void saveTree(String name, File path);
	IHistogramFactory getHistogramFactory(ITree tree);
}
