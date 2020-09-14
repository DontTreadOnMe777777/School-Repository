package assign02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This class contains tests for Library.
 * 
 * @author Erin Parker, Brandon Walters, and Justin Rogosienski
 * @version January 16, 2019
 */
public class LibraryTester {

	private Library emptyLib, smallLib, mediumLib;
	
	@BeforeEach
	void setUp() throws Exception {
		emptyLib = new Library();
		
		smallLib = new Library();
		smallLib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
		smallLib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
		smallLib.add(9780446580342L, "David Baldacci", "Simple Genius");

		mediumLib = new Library();
		mediumLib.addAll("src/assign02/Mushroom_Publishing.txt");
	}

	@Test
	public void testEmptyLookupISBN() {
		assertNull(emptyLib.lookup(978037429279L));
	}
	
	@Test
	public void testEmptyLookupHolder() {
		ArrayList<LibraryBook> booksCheckedOut = emptyLib.lookup("Jane Doe");
		assertNotNull(booksCheckedOut);
		assertEquals(0, booksCheckedOut.size());
	}
	
	@Test
	public void testEmptyCheckout() {
		assertFalse(emptyLib.checkout(978037429279L, "Jane Doe", 1, 1, 2008));
	}

	@Test
	public void testEmptyCheckinISBN() {
		assertFalse(emptyLib.checkin(978037429279L));
	}
	
	@Test
	public void testEmptyCheckinHolder() {
		assertFalse(emptyLib.checkin("Jane Doe"));
	}

	@Test
	public void testSmallLibraryLookupISBN() {
		assertNull(smallLib.lookup(9780330351690L));
	}
	
	@Test
	public void testSmallLibraryLookupHolder() {
		smallLib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008);
		ArrayList<LibraryBook> booksCheckedOut = smallLib.lookup("Jane Doe");
		
		assertNotNull(booksCheckedOut);
		assertEquals(1, booksCheckedOut.size());
		assertEquals(new Book(9780330351690L, "Jon Krakauer", "Into the Wild"), booksCheckedOut.get(0));
		assertEquals("Jane Doe", booksCheckedOut.get(0).getHolder());
	}

	@Test
	public void testSmallLibraryCheckout() {
		assertTrue(smallLib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008));
	}

	@Test
	public void testSmallLibraryCheckinISBN() {
		smallLib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008);
		assertTrue(smallLib.checkin(9780330351690L));
	}

	@Test
	public void testSmallLibraryCheckinHolder() {
		assertFalse(smallLib.checkin("Jane Doe"));
	}
	
	@Test
	public void testMediumLibraryLookupISBN() {
		assertNull(mediumLib.lookup(0000000000000));
	}
	
	@Test
	public void testMediumLibraryLookupHolder() {
		mediumLib.checkout(9781843190998L, "Jane Doe", 1, 22, 2019);
		ArrayList<LibraryBook> booksCheckedOut = mediumLib.lookup("Jane Doe");
		
		assertNotNull(booksCheckedOut);
		assertEquals(1, booksCheckedOut.size());
		assertEquals(new Book(9781843190998L, "Helen K Barker", "Iceni"), booksCheckedOut.get(0));
		assertEquals("Jane Doe", booksCheckedOut.get(0).getHolder());
	}

	@Test
	public void testMediumLibraryCheckout() {
		assertTrue(mediumLib.checkout(9781843190998L, "Jane Doe", 1, 22, 2019));
	}

	@Test
	public void testMediumLibraryCheckinISBN() {
		mediumLib.checkout(9781843190998L, "Jane Doe", 1, 22, 2019);
		assertTrue(mediumLib.checkin(9781843190998L));
	}

	@Test
	public void testMediumLibraryCheckinHolder() {
		assertFalse(mediumLib.checkin("Jane Doe"));
	}
	
	@Test
	public void testMediumLibraryMultipleHolders() {
		mediumLib.checkout(9781843190998L, "Jane Doe", 1, 22, 2019); // Iceni
		mediumLib.checkout(9781843191230L, "Jane Doe", 1, 22, 2019); // Endless Exile
		mediumLib.checkout(9781843192022L, "Jane Doe", 1, 22, 2019); // Whistler
		assertFalse(mediumLib.checkout(9781843190998L, "Bob Ross", 1, 22, 2019));
		mediumLib.checkout(9781843190004L, "Bob Ross", 1, 22, 2019); // Wolfhound
		mediumLib.checkout(9781843190011L, "Bob Ross", 1, 22, 2019); // Callanish
		ArrayList<LibraryBook> booksCheckedOutJane = mediumLib.lookup("Jane Doe");
		ArrayList<LibraryBook> booksCheckedOutBob = mediumLib.lookup("Bob Ross");
		
		assertNotNull(booksCheckedOutJane);
		assertNotNull(booksCheckedOutBob);
		assertEquals(3, booksCheckedOutJane.size());
		assertEquals(2, booksCheckedOutBob.size());
		assertEquals(new Book(9781843190998L, "Helen K Barker", "Iceni"), booksCheckedOutJane.get(0));
		assertEquals(new Book(9781843191230L, "Mary Lancaster", "An Endless Exile"), booksCheckedOutJane.get(1));
		assertEquals(new Book(9781843192022L, "Roger Taylor", "Whistler"), booksCheckedOutJane.get(2));
		assertEquals("Jane Doe", booksCheckedOutJane.get(0).getHolder());
		assertEquals("Jane Doe", booksCheckedOutJane.get(1).getHolder());
		assertEquals("Jane Doe", booksCheckedOutJane.get(2).getHolder());
		assertEquals(new Book(9781843190004L, "Moyra Caldecott", "Weapons of the Wolfhound"), booksCheckedOutBob.get(0));
		assertEquals(new Book(9781843190011L, "Moyra Caldecott", "The Eye of Callanish"), booksCheckedOutBob.get(1));
		assertEquals("Bob Ross", booksCheckedOutBob.get(0).getHolder());
		assertEquals("Bob Ross", booksCheckedOutBob.get(1).getHolder());
		assertTrue(mediumLib.checkin("Jane Doe"));
		assertFalse(mediumLib.checkin(9781843190998L));
		assertTrue(mediumLib.checkin(9781843190004L));
	}
}