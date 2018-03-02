package org.lcsim.service.impl;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogramFactory;
import hep.aida.ITree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.lcsim.service.api.HistogramService;

/**
 * Provides basic access to AIDA trees and factories.  
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public final class HistogramServiceImpl extends AbstractService implements HistogramService {
	
	private IAnalysisFactory af = IAnalysisFactory.create();
	private String compressFormat = System.getProperty(aidaTreeCompressProperty, "gzip");
	public static String aidaTreeCompressProperty = "org.lcsim.util.aida.CompressOption";
	
	Map<String, ITree> trees = new HashMap<String, ITree>();
	Map<ITree, File> tempFiles = new HashMap<ITree, File>();
	Map<ITree, IHistogramFactory> factories = new HashMap<ITree, IHistogramFactory>();
	
	/**
	 * Check if the service has a named tree.
	 * @param name The name of the tree.
	 * @return True if tree exists; false if not.
	 */
	public boolean hasTree(String name) {
		return trees.get(name) != null;
	}
	
	/**
	 * Create tree called <code>name</code> or get existing tree if already exists.
	 * @param name The name of the tree to create or get.
	 * @return The tree.
	 */
	public ITree tree(String name) {
		
		if (trees.get(name) != null) {
			return trees.get(name);
		}
		
		File tempFile;
		try {
			tempFile = File.createTempFile("aida",".aida");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
		tempFile.deleteOnExit();

		ITree tree;
		try {
			tree = af.createTreeFactory().create(tempFile.getAbsolutePath(), "xml", false, true, "compress="+compressFormat);
		} catch (Exception e) {
			throw new RuntimeException("Error creating tree: " + name);
		}
		IHistogramFactory fac = af.createHistogramFactory(tree);
		
		trees.put(name, tree);
		tempFiles.put(tree, tempFile);
		factories.put(tree, fac);
		
		return tree;
	}
	
	/**
	 * Remove a named tree from the service.
	 * @param name The name of the tree to remove.
	 */
	public void removeTree(String name) {
		if (!hasTree(name)) {
			throw new RuntimeException("No tree exists with name: " + name);
		}
		ITree tree = tree(name);
		try {
			tree.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		trees.remove(name);
		File tempFile = tempFiles.get(tree);
		tempFile.delete();
		tempFiles.remove(tree);
		factories.remove(tree);
	}
	
	/**
	 * Save a named ITree to default path, which is the current working directory plus the tree's name.
	 * @param name The name of the tree.
	 */
	public final void saveTree(String name) {
		ITree tree = trees.get(name);
		if (tree == null) {
			throw new RuntimeException("No tree found with name: " + name);
		}
		File saveFile = new File(name + ".aida");
		saveTree(tree, saveFile);
	}
	
	/**
	 * Save an ITree to a specific output path named in File <code>path</code>.
	 * @param name
	 * @param path
	 */
	public void saveTree(String name, File path) {
		saveTree(trees.get(name), path);
	}
	
	/**
	 * Get the HistogramFactory for a specific named tree.
	 * @param tree
	 * @return
	 */
	public IHistogramFactory getHistogramFactory(ITree tree) {
		return factories.get(tree);
	}
				  
	/**
	 * Utility method for saving AIDA files.  This is taken from the AIDA util class in lcsim.
	 * @param tree
	 * @param dest
	 * @param useZip
	 * @throws IOException
	 */
	private void saveTree(ITree tree, File dest) {
		File tempFile = tempFiles.get(tree);
		if (tempFile == null) {
			throw new RuntimeException("No temp file found for tree.");
		}
		try {
			tree.commit();
			if (dest.exists()) dest.delete();
			boolean rc = tempFile.renameTo(dest);
			if (!rc) {
				byte[] buffer = new byte[32768];
				OutputStream out = new FileOutputStream(dest);
				InputStream in = new FileInputStream(tempFile);
				try {
					for (;;) {
						int l = in.read(buffer);
						if (l<0) break;
						out.write(buffer,0,l);
					}
				} finally {
					out.close();
					in.close();
					tempFile.delete();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	} 	
}
