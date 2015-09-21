/*
 * Writen by Jin Xi 09/20/2015
 * jinxi@andrew.cmu.edu
 * */

/*
 * Functional Test of the LongestNameChain algorithm, File IO operation included.
 */
public class LongestNameChainFunctionalTest {
	private static LongestNameChain circleChain;
	private static LongestNameChain celebrity;
	private static LongestNameChain noConsecutiveNames;
	private static LongestNameChain binaryTree;
	private static LongestNameChain empty;

	public static void main(final String[] args) {
		initalize();
		displayResults(celebrity, "celebrity");
		displayResults(circleChain, "circleChain");
		displayResults(noConsecutiveNames, "noConsecutiveNames");
		displayResults(binaryTree, "binaryTree");
		displayResults(empty, "empty");
	}

	/*
	 * For five different type of name graph, try to apply the algorithm to find
	 * longest name chain.
	 */
	public static void initalize() {
		celebrity = new LongestNameChain("test/celebrity.txt");
		celebrity.initializeLongestNameChain();

		circleChain = new LongestNameChain("test/circleChain.txt");
		circleChain.initializeLongestNameChain();

		noConsecutiveNames = new LongestNameChain("test/noConsecutiveNames.txt");
		noConsecutiveNames.initializeLongestNameChain();

		binaryTree = new LongestNameChain("test/binaryTree.txt");
		binaryTree.initializeLongestNameChain();

		empty = new LongestNameChain("test/empty.txt");
		empty.initializeLongestNameChain();
	}

	/*
	 * Display the result generated from the algorithm for specific name list.
	 */
	public static void displayResults(final LongestNameChain longestNameChain,
			final String type) {
		if (!longestNameChain.initialized) {
			System.out.println("Initialized " + type + " failed. No name found.");
		} else {
			System.out.println("Initialized " + type + " success.");
			if (longestNameChain.getHasCycle()) {
				System.out.println("\tThere exists cycle in this name list.");
			} else {
				System.out
						.println("\tThere exists no cycle in this name list.");
			}
			System.out.println("\tThe highest word count in a name chain is "
					+ longestNameChain.getMaxLen());
			System.out.println("\tBelow is the longest name chain:");
			System.out.println("\t\t" + longestNameChain.getLongestChain()
					+ "\n");
		}
	}
}
