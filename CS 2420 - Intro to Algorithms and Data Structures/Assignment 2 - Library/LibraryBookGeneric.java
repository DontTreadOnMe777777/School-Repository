package assign02;

import java.util.GregorianCalendar;

/**
 * This class represents a library book, which is a book that has the additional
 * features of a holder, due date, and whether or not it is checked out.
 * 
 * @author Erin Parker, Brandon Walters, and Justin Rogosienski
 * @version January 16, 2019
 */
public class LibraryBookGeneric<Type> extends Book {

	/** Some identification of the holder */
	private Type holder;

	private GregorianCalendar dueDate;

	private boolean isCheckedOut;

	/**
	 * Creates a library book from the given ISBN, author, and title. The book is
	 * initially not checked out.
	 * 
	 * @param isbn
	 * @param author
	 * @param title
	 */
	public LibraryBookGeneric(long isbn, String author, String title) {
		super(isbn, author, title);
		holder = null;
		dueDate = null;
		isCheckedOut = false;
	}

	/**
	 * Checks the book out to the given holder and sets the due date.
	 * 
	 * @param holder - identification of person to check out the book
	 * @param dueDate - the new due date of the library book
	 */
	public void checkOut(Type holder, GregorianCalendar dueDate) {
		this.dueDate = dueDate;
		this.holder = holder;
		isCheckedOut = true;
	}

	/**
	 * Checks the book in (no due date and no holder)
	 */
	public void checkIn() {
		this.dueDate = null;
		this.holder = null;
		isCheckedOut = false;
	}

	/**
	 * returns the holder for the library book
	 * @return Type
	 */
	public Type getHolder() {
		return holder;
	}

	/**
	 * returns the due date for the library book
	 * @return GregorianCalendar
	 */
	public GregorianCalendar getDueDate() {
		return dueDate;
	}

	/**
	 * returns if the book is checked out
	 * @return boolean
	 */
	public boolean isCheckedOut() {
		return isCheckedOut;
	}

}
