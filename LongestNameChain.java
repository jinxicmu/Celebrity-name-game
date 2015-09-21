/*
 * Writen by Jin Xi 09/20/2015
 * jinxi@andrew.cmu.edu
 * */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/*
 * A class that generates a name-chain from the first and last names of people.
 */
public class LongestNameChain {
	int size;
	boolean initialized = false;

	private List<List<Integer>> outDegrees;
	private List<List<Integer>> inDegrees;
	private List<String> words;
	private String longestChain;
	private String nameFileLocation;
	private int maxLen;
	private int leaf;
	private int[] parent;
	private boolean hasCycle;
	private final String SPLIT_BY = " ";

	private LongestNameChain() {
	}

	public LongestNameChain(final String fileLocation) {
		this.nameFileLocation = fileLocation;
	}

	/*
	 * Generate the longest name chain for each specific name list.
	 */
	public void initializeLongestNameChain() {
		try {
			initializeWordsList();
		} catch (final IOException e) {
			initialized = false;
			System.out.println("Failed when load names from file.");
			return;
		}
		if (size == 0) {
			longestChain = "";
			initialized = false;
			return;
		}

		initializeAdjacentList();
		final List<Integer> topoOrder = topologicalSort();
		if (topoOrder.size() == size) {
			hasCycle = false;
			generateLongestPathWhenNotExistCycle(topoOrder);
		} else {
			hasCycle = true;
			generateLongestPathWhenExistsCycle();
		}
		initialized = true;
	}

	/*
	 * Return hasCycle.
	 */
	public boolean getHasCycle() {
		return hasCycle;
	}

	/*
	 * Return longest name chain.
	 */
	public String getLongestChain() {
		return longestChain;
	}

	/*
	 * Return length of the longest name chain.
	 */
	public int getMaxLen() {
		return maxLen;
	}

	/*
	 * Generate a list of names from the input file.
	 */
	void initializeWordsList() throws IOException {
		words = new ArrayList<String>();
		for (final String line : Files
				.readAllLines(Paths.get(nameFileLocation))) {
			if (validName(line)) {
				words.add(line.trim().toLowerCase());
			}
		}
		size = words.size();
	}

	/*
	 * Generate a directed graph to represent the relationship between names.
	 * Utilize Adjacent list.
	 */
	void initializeAdjacentList() {
		final Map<String, List<Integer>> firstNameMap = new HashMap<String, List<Integer>>();
		final Map<String, List<Integer>> lastNameMap = new HashMap<String, List<Integer>>();

		for (int i = 0; i < size; i++) {
			final String[] name = words.get(i).split(SPLIT_BY);
			final String lastName = name[name.length - 1].trim();
			final String firstName = name[0].trim();
			if (firstNameMap.containsKey(firstName)) {
				firstNameMap.get(firstName).add(i);
			} else {
				firstNameMap.put(firstName,
						new ArrayList<Integer>(Arrays.asList(i)));
			}
			if (lastNameMap.containsKey(lastName)) {
				lastNameMap.get(lastName).add(i);
			} else {
				lastNameMap.put(lastName,
						new ArrayList<Integer>(Arrays.asList(i)));
			}
		}

		outDegrees = new ArrayList<List<Integer>>();
		inDegrees = new ArrayList<List<Integer>>();
		for (int i = 0; i < size; i++) {
			final String[] name = words.get(i).split(SPLIT_BY);
			final String firstName = name[0].trim();
			final String lastName = name[name.length - 1].trim();
			final List<Integer> outDegree = new ArrayList<Integer>();
			final List<Integer> inDegree = new ArrayList<Integer>();
			if (firstNameMap.containsKey(lastName)) {
				outDegree.addAll(firstNameMap.get(lastName));
			}
			outDegrees.add(outDegree);
			if (lastNameMap.containsKey(firstName)) {
				inDegree.addAll(lastNameMap.get(firstName));
			}
			inDegrees.add(inDegree);
		}
	}

	/*
	 * Generate a topological order of nodes. Also detect whether there exists
	 * cycle or not.
	 */
	List<Integer> topologicalSort() {
		final int[] inDegreeCount = new int[size];
		final Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < size; i++) {
			inDegreeCount[i] = inDegrees.get(i).size();
			if (inDegreeCount[i] == 0) {
				stack.add(i);
			}
		}
		final List<Integer> topoOrder = new ArrayList<Integer>();
		while (!stack.isEmpty()) {
			final int st = stack.pop();
			topoOrder.add(st);
			for (final int end : outDegrees.get(st)) {
				inDegreeCount[end]--;
				if (inDegreeCount[end] == 0) {
					stack.add(end);
				}
			}
		}
		return topoOrder;
	}

	/*
	 * When there is no cycle in name graph, according the topological order,
	 * apply dynamic programming to get longest name chain.
	 */
	void generateLongestPathWhenNotExistCycle(final List<Integer> topoOrder) {
		final int[] len = new int[size];
		parent = new int[size];

		for (int i = 0; i < size; i++) {
			if (inDegrees.get(i).isEmpty()) {
				len[i] = 1;
				parent[i] = -1;
			}
		}

		for (int i = 0; i < size; i++) {
			final int end = topoOrder.get(i);
			for (final int st : inDegrees.get(end)) {
				if (len[st] + 1 >= len[end]) {
					len[end] = len[st] + 1;
					parent[end] = st;
				}
			}
			if (len[end] >= maxLen) {
				maxLen = len[end];
				leaf = end;
			}
		}

		connectLongestChain();
	}
	
	/*
	 * Check whether the input line has at least two word seperated by space.
	 */
	private boolean validName(String line) {
		final String[] name = line.split(SPLIT_BY);
		if (name != null && name.length >= 2) {
			return true;		
		}
		return false;
	}

	/*
	 * Build longest name chain through parent pointer step by step.
	 */
	private void connectLongestChain() {
		final String[] name = words.get(leaf).split(SPLIT_BY);
		final StringBuilder stringBuilder = new StringBuilder(SPLIT_BY
				+ name[name.length - 1].trim());
		int cur = leaf;
		while (cur != -1) {
			stringBuilder.insert(0, SPLIT_BY
					+ words.get(cur).split(SPLIT_BY)[0]);
			cur = parent[cur];
		}
		longestChain = stringBuilder.toString().trim();
	}

	/*
	 * When there exist cycle in that name graph, for this NP problem, apply
	 * brute-force solution through depth-first-search.
	 */
	void generateLongestPathWhenExistsCycle() {
		final boolean[] visited = new boolean[size];
		parent = new int[size];
		for (int i = 0; i < size; i++) {
			final int[] prev = new int[size];
			Arrays.fill(prev, -1);
			dfs(i, 1, visited, prev);
		}
		connectLongestChain();
	}

	/*
	 * Depth-first-search to find the longest name chain.
	 */
	private void dfs(final int st, final int depth, final boolean[] visited,
			final int[] prev) {
		if (depth >= maxLen) {
			maxLen = depth;
			parent = Arrays.copyOf(prev, size);
			leaf = st;
		}
		visited[st] = true;
		for (final int end : outDegrees.get(st)) {
			if (!visited[end]) {			
				prev[end] = st;
				dfs(end, depth + 1, visited, prev);
				prev[end] = -1;
			}
		}
		visited[st] = false;
	}
}
