package assign02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This class contains tests for LibraryGeneric.
 * 
 * @author Erin Parker, Justin Rogosienski, and Brandon Walters
 * @version January 16, 2019
 */
public class LibraryGenericTester {

	private LibraryGeneric<String> nameLib; // library that uses names to identify patrons (holders)
	private LibraryGeneric<PhoneNumber> phoneLib; // library that uses phone numbers to identify patrons
	/**
	 * library that uses booleans to identify the gender of the patron (male: true,
	 * female: false)
	 */
	private LibraryGeneric<Boolean> genderLib;

	private LibraryGeneric<String> mediumLib;

	@BeforeEach
	void setUp() throws Exception {
		nameLib = new LibraryGeneric<String>();
		nameLib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
		nameLib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
		nameLib.add(9780446580342L, "David Baldacci", "Simple Genius");

		phoneLib = new LibraryGeneric<PhoneNumber>();
		phoneLib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
		phoneLib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
		phoneLib.add(9780446580342L, "David Baldacci", "Simple Genius");

		genderLib = new LibraryGeneric<Boolean>();
		genderLib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
		genderLib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
		genderLib.add(9780446580342L, "David Baldacci", "Simple Genius");

		mediumLib = new LibraryGeneric<String>();
		mediumLib.addAll("src/assign02/Mushroom_Publishing.txt");
	}

	@Test
	public void testNameLibCheckout() {
		String patron = "Jane Doe";
		assertTrue(nameLib.checkout(9780330351690L, patron, 1, 1, 2008));
		assertTrue(nameLib.checkout(9780374292799L, patron, 1, 1, 2008));
	}

	@Test
	public void testNameLibCheckout_BAD() {
		String patron = "Jane Doe";
		assertFalse(nameLib.checkout(100330351690L, patron, 1, 1, 2008));
		assertFalse(nameLib.checkout(100374292799L, patron, 1, 1, 2008));
	}

	@Test
	public void testNameLibLookup() {
		String patron = "Jane Doe";
		nameLib.checkout(9780330351690L, patron, 1, 1, 2008);
		nameLib.checkout(9780374292799L, patron, 1, 1, 2008);
		ArrayList<LibraryBookGeneric<String>> booksCheckedOut = nameLib.lookup(patron);

		assertNotNull(booksCheckedOut);
		assertEquals(2, booksCheckedOut.size());
		assertTrue(booksCheckedOut.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
		assertTrue(booksCheckedOut.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
		assertEquals(patron, booksCheckedOut.get(0).getHolder());
		assertEquals(patron, booksCheckedOut.get(1).getHolder());
	}

	@Test
	public void testNameLibLookup_EMPTY() {
		String patron = "Jane Doe";
		nameLib.checkout(1000330351690L, patron, 1, 1, 2008);
		ArrayList<LibraryBookGeneric<String>> booksCheckedOut = nameLib.lookup(patron);

		assertNotNull(booksCheckedOut);
		assertEquals(0, booksCheckedOut.size());
	}

	@Test
	public void testNameLibCheckin() {
		String patron = "Jane Doe";
		nameLib.checkout(9780330351690L, patron, 1, 1, 2008);
		nameLib.checkout(9780374292799L, patron, 1, 1, 2008);
		assertTrue(nameLib.checkin(patron));
	}

	@Test
	public void testNameLibCheckin_BAD() {
		String patron = "Jane Doe";
		String notPatron = "Not Jane Doe";
		nameLib.checkout(9780330351690L, patron, 1, 1, 2008);
		nameLib.checkout(9780374292799L, patron, 1, 1, 2008);
		assertFalse(nameLib.checkin(notPatron));
	}

	@Test
	public void testIsbnLibCheckin() {
		String patron = "Jane Doe";
		nameLib.checkout(9780330351690L, patron, 1, 1, 2008);
		nameLib.checkout(9780374292799L, patron, 1, 1, 2008);
		assertTrue(nameLib.checkin(9780374292799L));
	}

	@Test
	public void testIsbnLibCheckin_BAD() {
		String patron = "Jane Doe";
		nameLib.checkout(9780330351690L, patron, 1, 1, 2008);
		nameLib.checkout(9780374292799L, patron, 1, 1, 2008);
		assertFalse(nameLib.checkin(1000374292799L));
	}

	@Test
	public void testPhoneLibCheckout() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		assertTrue(phoneLib.checkout(9780330351690L, patron, 1, 1, 2008));
		assertTrue(phoneLib.checkout(9780374292799L, patron, 1, 1, 2008));
	}

	@Test
	public void testPhoneLibCheckoutBad() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		assertFalse(phoneLib.checkout(1000330351690L, patron, 1, 1, 2008));
		assertFalse(phoneLib.checkout(1500374292799L, patron, 1, 1, 2008));
	}

	@Test
	public void testPhoneLibLookup() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		phoneLib.checkout(9780330351690L, patron, 1, 1, 2008);
		phoneLib.checkout(9780374292799L, patron, 1, 1, 2008);
		ArrayList<LibraryBookGeneric<PhoneNumber>> booksCheckedOut = phoneLib.lookup(patron);

		assertNotNull(booksCheckedOut);
		assertEquals(2, booksCheckedOut.size());
		assertTrue(booksCheckedOut.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
		assertTrue(booksCheckedOut.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
		assertEquals(patron, booksCheckedOut.get(0).getHolder());
		assertEquals(patron, booksCheckedOut.get(1).getHolder());
	}

	@Test
	public void testPhoneLibLookupBad() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		phoneLib.checkout(1000330351690L, patron, 1, 1, 2008);
		phoneLib.checkout(9780374292799L, patron, 1, 1, 2008);
		ArrayList<LibraryBookGeneric<PhoneNumber>> booksCheckedOut = phoneLib.lookup(patron);

		assertNotNull(booksCheckedOut);
		assertEquals(1, booksCheckedOut.size());
		assertFalse(booksCheckedOut.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
		assertTrue(booksCheckedOut.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
		assertEquals(patron, booksCheckedOut.get(0).getHolder());
	}

	@Test
	public void testPhoneLibCheckin() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		phoneLib.checkout(9780330351690L, patron, 1, 1, 2008);
		phoneLib.checkout(9780374292799L, patron, 1, 1, 2008);
		assertTrue(phoneLib.checkin(patron));
	}

	@Test
	public void testPhoneLibCheckinBad() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		phoneLib.checkout(1010330351690L, patron, 1, 1, 2008);
		phoneLib.checkout(10374292799L, patron, 1, 1, 2008);
		assertFalse(phoneLib.checkin(patron));
	}

	@Test
	public void testBoolLibCheckout() {
		boolean patron = false;
		assertTrue(genderLib.checkout(9780330351690L, patron, 1, 1, 2008));
		assertTrue(genderLib.checkout(9780374292799L, patron, 1, 1, 2008));
	}

	@Test
	public void testBoolLibCheckoutBad() {
		boolean patron = false;
		assertFalse(genderLib.checkout(1000330351690L, patron, 1, 1, 2008));
		assertFalse(genderLib.checkout(1500374292799L, patron, 1, 1, 2008));
	}

	@Test
	public void testBoolLibLookup() {
		boolean patron = false;
		genderLib.checkout(9780330351690L, patron, 1, 1, 2008);
		genderLib.checkout(9780374292799L, patron, 1, 1, 2008);
		ArrayList<LibraryBookGeneric<Boolean>> booksCheckedOut = genderLib.lookup(patron);

		assertNotNull(booksCheckedOut);
		assertEquals(2, booksCheckedOut.size());
		assertTrue(booksCheckedOut.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
		assertTrue(booksCheckedOut.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
		assertEquals(patron, booksCheckedOut.get(0).getHolder());
		assertEquals(patron, booksCheckedOut.get(1).getHolder());
	}

	@Test
	public void testBoolLibLookupBad() {
		boolean patron = false;
		genderLib.checkout(1000330351690L, patron, 1, 1, 2008);
		genderLib.checkout(9780374292799L, patron, 1, 1, 2008);
		ArrayList<LibraryBookGeneric<Boolean>> booksCheckedOut = genderLib.lookup(patron);

		assertNotNull(booksCheckedOut);
		assertEquals(1, booksCheckedOut.size());
		assertFalse(booksCheckedOut.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
		assertTrue(booksCheckedOut.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
		assertEquals(patron, booksCheckedOut.get(0).getHolder());
	}

	@Test
	public void testBoolLibCheckin() {
		boolean patron = false;
		genderLib.checkout(9780330351690L, patron, 1, 1, 2008);
		genderLib.checkout(9780374292799L, patron, 1, 1, 2008);
		assertTrue(genderLib.checkin(patron));
	}

	@Test
	public void testBoolLibCheckinBad() {
		boolean patron = false;
		genderLib.checkout(1010330351690L, patron, 1, 1, 2008);
		genderLib.checkout(10374292799L, patron, 1, 1, 2008);
		assertFalse(genderLib.checkin(patron));
	}

	@Test
	public void testMultipleLibrariesRunning() {
		String namedPatron1 = "Jon Jones";
		String namedPatron2 = "Joan Johns";
		PhoneNumber numberedPatron1 = new PhoneNumber("801.555.1234");
		PhoneNumber numberedPatron2 = new PhoneNumber("801.555.4321");
		boolean genderedPatron1 = true;
		boolean genderedPatron2 = false;

		assertTrue(nameLib.checkout(9780330351690L, namedPatron1, 1, 1, 2008));
		assertTrue(nameLib.checkout(9780374292799L, namedPatron2, 1, 1, 2008));

		assertTrue(phoneLib.checkout(9780330351690L, numberedPatron1, 1, 1, 2008));
		assertTrue(phoneLib.checkout(9780374292799L, numberedPatron2, 1, 1, 2008));

		assertTrue(genderLib.checkout(9780330351690L, genderedPatron1, 1, 1, 2008));
		assertTrue(genderLib.checkout(9780374292799L, genderedPatron2, 1, 1, 2008));
	}

	// Tests for the sorted books

	@Test
	public void testGetInventoryList() {
		ArrayList<LibraryBookGeneric<String>> sortedList = mediumLib.getInventoryList();
		assertEquals(sortedList.get(0).getTitle(), "Weapons of the Wolfhound");
		assertEquals(sortedList.get(9).getTitle(), "The War Comes to Witham Street");
		assertEquals(sortedList.get(22).getTitle(), "Transit to Scorpio");
	}
	
	@Test
	public void testGetInventoryListCheckedOut() {
		mediumLib.checkout(9781843190004L, "Some Idiot", 01, 01, 3000);
		ArrayList<LibraryBookGeneric<String>> sortedList = mediumLib.getInventoryList();
		assertEquals(sortedList.get(0).getTitle(), "Weapons of the Wolfhound");
		assertEquals(sortedList.get(9).getTitle(), "The War Comes to Witham Street");
		assertEquals(sortedList.get(22).getTitle(), "Transit to Scorpio");
	}
	
	@Test
	public void testGetOverdueList() {
		// Makes a 10 book library
		LibraryGeneric<String> customLib = new LibraryGeneric<>();
		for (long i = 0; i < 10; i++)
			customLib.add(9780330351000L + i, "Lil Uzi Vert", ("Book " + i));
		for (int i = 0; i < 5; i++)
			customLib.checkout(9780330351000L + i, "", i, 01, 1901);
		for (long i = 5; i < 10; i++)
			customLib.checkout(9780330351000L + i, "", 01, 01, 2919);
		ArrayList<LibraryBookGeneric<String>> sortedList = customLib.getOverdueList(01, 01, 2000);
		assertEquals(sortedList.size(), 5);
		assertEquals(sortedList.get(0).getTitle(), "Book 0");
		assertEquals(sortedList.get(2).getTitle(), "Book 2");
		assertEquals(sortedList.get(4).getTitle(), "Book 4");
	}
	
	@Test
	public void testGetOverdueList_NoneCheckedOut() {
		// Makes a 10 book library
		LibraryGeneric<String> customLib = new LibraryGeneric<>();
		for (long i = 0; i < 10; i++)
			customLib.add(9780330351000L + i, "Lil Uzi Vert", ("Book " + i));
		ArrayList<LibraryBookGeneric<String>> sortedList = customLib.getOverdueList(01, 01, 2000);
		assertEquals(sortedList.size(), 0);
	}

	@Test
	public void testGetTitleSortedList() {
		ArrayList<LibraryBookGeneric<String>> sortedList = mediumLib.getOrderedByTitle();
		assertEquals(sortedList.get(0).getTitle(), "An Endless Exile");
		assertEquals(sortedList.get(1).getTitle(), "Bath City Centre Street Map and Guide");
		assertEquals(sortedList.get(22).getTitle(), "Whistler");
	}
	
	@Test
	public void testGetTitleSortedListCheckedOut() {
		mediumLib.checkout(9781843191230L, "Some Idiot", 01, 01, 3000);
		ArrayList<LibraryBookGeneric<String>> sortedList = mediumLib.getOrderedByTitle();
		assertEquals(sortedList.get(0).getTitle(), "An Endless Exile");
		assertEquals(sortedList.get(1).getTitle(), "Bath City Centre Street Map and Guide");
		assertEquals(sortedList.get(22).getTitle(), "Whistler");
	}

}
