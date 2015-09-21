/*
 * Writen by Jin Xi 09/20/2015
 * jinxi@andrew.cmu.edu
 * */

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/*
 * Junit Test of the LongestNameChain algorithm, applying mockito.
 */
public class LongestNameChainTest {
	private LongestNameChain mockLongestNameChain;

	@Before
	public void initializeMockObject() throws IOException {
		final LongestNameChain longestNameChain = new LongestNameChain(
				"not_exist_file");
		mockLongestNameChain = spy(longestNameChain);

		doNothing().when(mockLongestNameChain).initializeWordsList();
		doNothing().when(mockLongestNameChain).initializeAdjacentList();
		doReturn(new ArrayList<Integer>(Arrays.asList(1, 2))).when(
				mockLongestNameChain).topologicalSort();
		doNothing().when(mockLongestNameChain)
				.generateLongestPathWhenExistsCycle();
		doNothing().when(mockLongestNameChain)
				.generateLongestPathWhenNotExistCycle(new ArrayList<Integer>(Arrays.asList(1, 2)));
	}

	@Test
	public void shouldBeInitialize() {
		mockLongestNameChain.size = 1;

		mockLongestNameChain.initializeLongestNameChain();
		assertTrue(mockLongestNameChain.initialized);
	}

	@Test
	public void verifyWorkFlowWhenNoCycle() throws IOException {
		mockLongestNameChain.size = 1;

		mockLongestNameChain.initializeLongestNameChain();
		verify(mockLongestNameChain, times(1)).initializeWordsList();
		verify(mockLongestNameChain, times(1)).initializeAdjacentList();
		verify(mockLongestNameChain, times(1)).topologicalSort();
		verify(mockLongestNameChain, times(1))
				.generateLongestPathWhenExistsCycle();
	}

	@Test
	public void verifyWorkFlowWhenExistsCycle() throws IOException {
		mockLongestNameChain.size = 2;

		mockLongestNameChain.initializeLongestNameChain();
		verify(mockLongestNameChain, times(1)).initializeWordsList();
		verify(mockLongestNameChain, times(1)).initializeAdjacentList();
		verify(mockLongestNameChain, times(1)).topologicalSort();
		verify(mockLongestNameChain, times(1))
				.generateLongestPathWhenNotExistCycle(
						new ArrayList<Integer>(Arrays.asList(1, 2)));
	}
}
