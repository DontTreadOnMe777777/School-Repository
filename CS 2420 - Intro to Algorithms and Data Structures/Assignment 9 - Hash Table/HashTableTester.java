package assign09;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashTableTester {
	
	HashTable<StudentMediumHash, Double> GPATable, StudentMilesRunTable;
	HashTable<StudentMediumHash, String> FavoriteIceCreamTable, FearTable, NewTable;
	StudentMediumHash philip, dan, dave, alan, brandon, bran, adam, xavier;
	StudentBadHash avery;

	@BeforeEach
	void setUp() {
		philip = new StudentMediumHash(5922068, "Philip", "Hogs");
		dan  = new StudentMediumHash(7182655, "Dan", "Hamman");
		dave = new StudentMediumHash(9817266, "Dave", "Grahaven");
		alan = new StudentMediumHash(8276452, "Alan", "Gavers");
		brandon = new StudentMediumHash(2203976, "Brandon", "Walters");
		bran = new StudentMediumHash(1155068, "Bran", "Ernst");
		adam = new StudentMediumHash(6524389, "Adam", "Adams");
		xavier = new StudentMediumHash(9999556, "Xavier", "Ulttimaze");
		avery = new StudentBadHash(8822347, "Avery", "Reynolds");
		
		GPATable = new HashTable<StudentMediumHash, Double>();
		StudentMilesRunTable = new HashTable<StudentMediumHash, Double>();
		
		FavoriteIceCreamTable = new HashTable<StudentMediumHash, String>();
		FearTable = new HashTable<StudentMediumHash, String>();
		
		NewTable = new HashTable<StudentMediumHash, String>();
		
		NewTable.put(philip, "Yeet");
		NewTable.put(dan, "Yeet");
		NewTable.put(dave, "Yeet");
		NewTable.put(alan, "Yeet");
		NewTable.put(brandon, "Yeet");
		NewTable.put(bran, "Yeet");
		NewTable.put(adam, "Yeet");
		NewTable.put(xavier, "Yeet");
		
		GPATable.put(philip, 3.6);
		GPATable.put(dan, 2.5);
		GPATable.put(dave, 1.0);
		GPATable.put(alan, 4.0);
		GPATable.put(brandon, 0.0);
		GPATable.put(bran, 5.0);
		
		
		FavoriteIceCreamTable.put(philip, "Bannana");
		FavoriteIceCreamTable.put(xavier, "Rocky Road");
		FavoriteIceCreamTable.put(adam, "Strawberry");
		FavoriteIceCreamTable.put(brandon, "Chocolate");
		FavoriteIceCreamTable.put(dave, "Mango");
		
		
		
	}
	
	
	@Test
	void testClearAndIsEmpty() {
		assertFalse(GPATable.isEmpty());
		GPATable.clear();
		assertTrue(GPATable.isEmpty());
		
		assertTrue(FearTable.isEmpty());
		assertTrue(StudentMilesRunTable.isEmpty());
		
		assertFalse(FavoriteIceCreamTable.isEmpty());
		FavoriteIceCreamTable.clear();
		assertTrue(FavoriteIceCreamTable.isEmpty());
	}
	
	@Test
	void testContainsContainValueAndRemove() {
		assertTrue(GPATable.containsKey(philip));
		assertTrue(GPATable.containsValue(3.6));
		assertFalse(GPATable.containsKey(xavier));
		assertFalse(GPATable.containsValue(2.9));
		GPATable.remove(philip);
		assertFalse(GPATable.containsKey(philip));
		assertFalse(GPATable.containsValue(3.6));
		
		assertTrue(FavoriteIceCreamTable.containsKey(xavier));
		assertTrue(FavoriteIceCreamTable.containsValue("Rocky Road"));
		assertTrue(FavoriteIceCreamTable.containsKey(philip));
		assertTrue(FavoriteIceCreamTable.containsValue("Bannana"));
		assertFalse(FavoriteIceCreamTable.containsValue("Green Tea"));
		FavoriteIceCreamTable.remove(xavier);
		FavoriteIceCreamTable.remove(philip);
		assertFalse(FavoriteIceCreamTable.containsKey(xavier));
		assertFalse(FavoriteIceCreamTable.containsValue("Rocky Road"));
		assertFalse(FavoriteIceCreamTable.containsKey(philip));
		assertFalse(FavoriteIceCreamTable.containsValue("Bannana"));
		
		
	}

	@Test
	void TestSize() {
		assertEquals(0, FearTable.size());
		assertEquals(0, StudentMilesRunTable.size());
		assertEquals(6, GPATable.size());
		assertEquals(5, FavoriteIceCreamTable.size());
		
		GPATable.put(philip, 4.8);
		assertEquals(6, GPATable.size());
	}
	
	@Test
	void TestGetAndPut() {
		assertEquals(3.6, GPATable.get(philip), 1E-10);
		assertEquals(null, GPATable.get(adam));
		GPATable.put(philip, 4.5);
		assertEquals(4.5, GPATable.get(philip), 1E-10);
		
		assertEquals("Rocky Road", FavoriteIceCreamTable.get(xavier));
		assertEquals("Bannana", FavoriteIceCreamTable.get(philip));
		assertEquals("Strawberry", FavoriteIceCreamTable.get(adam));
		assertEquals(null, FavoriteIceCreamTable.get(bran));
	}
	
	
}
