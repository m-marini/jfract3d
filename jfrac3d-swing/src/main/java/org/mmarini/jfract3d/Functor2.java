/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author US00852
 * 
 */
public interface Functor2<R, T1, T2> {
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public abstract R apply(T1 p1, T2 p2);
}
