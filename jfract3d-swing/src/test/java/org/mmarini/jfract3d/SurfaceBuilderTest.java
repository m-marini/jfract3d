/**
 * 
 */
package org.mmarini.jfract3d;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author US00852
 * 
 */
public class SurfaceBuilderTest {

	@Test
	public void test() {
		final SurfaceBuilder b = new SurfaceBuilder(3,
				0.5, new Randomizer<Double>() {

					@Override
					public Double next() {
						return 1.0;
					}
				});
		assertThat(b, notNullValue());
	}
}
