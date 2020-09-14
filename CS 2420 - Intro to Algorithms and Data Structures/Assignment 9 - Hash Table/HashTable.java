package assign09;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class creates a HashTable data structure, implementing the Map
 * interface. This specific HashTable utilizes a separate chaining method, which
 * means that the backing array of this HashTable consists of an array of
 * LinkedLists which contain the entries of the map.
 * 
 * @author Brandon Walters and Brandon Ernst
 */
public class HashTable<K, V> implements Map<K, V> {

	private int size = 0;

	private ArrayList<LinkedList<MapEntry<K, V>>> table;

	private int capacity = 10;

	public HashTable() {
		table = new ArrayList<LinkedList<MapEntry<K, V>>>();
		for (int i = 0; i < capacity; i++)
			table.add(new LinkedList<MapEntry<K, V>>());
	}

	@Override
	public void clear() {
		for (int i = 0; i < capacity; i++) {
			table.get(i).clear();
		}
		size = 0;
	}

	@Override
	public boolean containsKey(K key) {
		LinkedList<MapEntry<K, V>> listToCheck = table.get(key.hashCode() % capacity);
		for (MapEntry<K, V> item : listToCheck) {
			if (item.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(V value) {
		for (int i = 0; i < capacity; i++) {
			LinkedList<MapEntry<K, V>> listToCheck = table.get(i);
			for (MapEntry<K, V> item : listToCheck) {
				if (item.getValue().equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<MapEntry<K, V>> entries() {
		List<MapEntry<K, V>> listToReturn = new ArrayList<MapEntry<K, V>>();
		for (int i = 0; i < capacity; i++) {
			LinkedList<MapEntry<K, V>> listToCheck = table.get(i);
			listToReturn.addAll(listToCheck);
		}
		return listToReturn;
	}

	@Override
	public V get(K key) {
		LinkedList<MapEntry<K, V>> listToCheck = table.get(key.hashCode() % capacity);
		for (int i = 0; i < listToCheck.size(); i++) {
			if (listToCheck.get(i).getKey().equals(key))
				return listToCheck.get(i).getValue();
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public V put(K key, V value) {
		// This is the rehashing check. If the load factor of this HashTable hits 3, the
		// backing array is doubled in size and then the original array's elements are
		// re-added and rehashed to the new array.
		if (size / capacity == 3) {
			size = 0;
			capacity = capacity * 2;
			ArrayList<LinkedList<MapEntry<K, V>>> temp = table;
			table = new ArrayList<LinkedList<MapEntry<K, V>>>();
			for (int i = 0; i < capacity; i++) {
				table.add(new LinkedList<MapEntry<K, V>>());
			}
			for (int i = 0; i < capacity / 2; i++) {
				if (!temp.get(i).isEmpty()) {
					for (int j = 0; j < temp.get(i).size(); j++) {
						this.put(temp.get(i).get(j).getKey(), temp.get(i).get(j).getValue());
					}
				}
			}
			temp = null;
		}

		MapEntry<K, V> newEntry = new MapEntry<K, V>(key, value);
		LinkedList<MapEntry<K, V>> listToAdd = table.get(key.hashCode() % capacity);
		if (this.containsKey(key)) {
			for (int i = 0; i < listToAdd.size(); i++) {
				if (listToAdd.get(i).getKey().equals(key)) {
					listToAdd.get(i).setValue(newEntry.getValue());
				}
			}
			return value;
		}
		listToAdd.add(newEntry);
		size++;
		return value;
	}

	@Override
	public V remove(K key) {
		LinkedList<MapEntry<K, V>> listToCheck = table.get(key.hashCode() % capacity);
		for (int i = 0; i < listToCheck.size(); i++) {
			if (listToCheck.get(i).getKey().equals(key)) {
				size--;
				return listToCheck.remove(i).getValue();
			}
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

}
