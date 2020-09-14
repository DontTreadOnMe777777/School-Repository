package assign04;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

class AnagramCheckerTestCases {

	@Test
	void testSort() {
		assertEquals("abc", AnagramChecker.sort("cab"));
		assertEquals("abekst", AnagramChecker.sort("basket"));
		assertEquals("abdnnor", AnagramChecker.sort("Brandon"));
		assertEquals("aaacccdeefgiiiiiiillloopprrssstuux", AnagramChecker.sort("supercalifragilisticexpialidocious"));
	}

	@Test
	void testAreAnagrams() {
		assertEquals(true, AnagramChecker.areAnagrams("auction", "caution"));
		assertEquals(true, AnagramChecker.areAnagrams("emigrants", "Mastering"));
		assertEquals(true, AnagramChecker.areAnagrams("ENLIST", "tinsel"));
		assertEquals(true, AnagramChecker.areAnagrams("aabbggttyy", "bagytbagyt"));
		assertEquals(false, AnagramChecker.areAnagrams("nope", "not"));
		assertEquals(false, AnagramChecker.areAnagrams("prettyclose", "preatyclose"));
	}
	

	@Test
	void testInsertionSort() {
		Integer[] arr = new Integer[]{ 5, 3, 8, 77, 89, -9, 544, -1009};
		Integer[] sortedArr = new Integer[] {-1009, -9, 3, 5, 8, 77, 89, 544};
		Comparator<Integer> comp = new compareInteger();
		AnagramChecker.insertionSort(arr, comp);
		assertArrayEquals(sortedArr, arr);
		
		String[] arr2 = new String[] {"zebra", "xylaphone", "tarantino", "Hippie", "Aardvark"};
		String[] sortedArr2 = new String[] {"Aardvark", "Hippie", "tarantino", "xylaphone", "zebra"};
		Comparator<String> comp2 = new compareString();
		AnagramChecker.insertionSort(arr2,  comp2);
		assertArrayEquals(sortedArr2, arr2);
		
	}
	
	@Test
	void testGetLargestAnagramGroup() {
		String[] input = new String[] {"emits", "enlarge", "drapes", "padres", "dowry", "items", "mites", "parsed", "general", "rasped", "spared", "mastering", "spread"};
		String[] finalArray = new String[] {"drapes", "padres", "parsed", "rasped", "spared", "spread"};
		assertArrayEquals(finalArray, AnagramChecker.getLargestAnagramGroup(input));
	}
	
	@Test
	void testGetLargestAnagramGroupFromFile() {
		String[] input = AnagramChecker.getLargestAnagramGroup("sample_word_list.txt");
		String[] testAgainst = new String[] {"carets", "Caters", "caster", "crates", "Reacts", "recast", "traces"};
		assertArrayEquals(testAgainst, input);
		
		String[] input2 = AnagramChecker.getLargestAnagramGroup("TesterDoc.txt");
		String[] testAgainst2 = new String[] {"elohl", "hello", "olleh"};
		assertArrayEquals(testAgainst2, input2);
	}
}

class compareInteger implements Comparator<Integer> {

	@Override
	public int compare(Integer o1, Integer o2) {
		 if(o1 > o2) {
			return 1;
		}
		
		else if(o1 < o2) {
			return -1;
		}
		else {
			return 0;
		}
	}

	
	
}