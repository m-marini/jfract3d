/**
 * 
 */
package org.mmarini.jfract3d;

import org.mmarini.fp.FPList;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public interface TransformFactory {

	/**
	 * @return
	 */
	public abstract FPList<FractalTransform> create();

}
