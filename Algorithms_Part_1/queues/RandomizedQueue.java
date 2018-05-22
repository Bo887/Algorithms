import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item>{

	private class RQIterator implements Iterator<Item>{

		Item[] copy;
		private int i = 0;

		public RQIterator(){
			copy = (Item[]) new Object[N];
			for(int i=0; i<N; i++){
				copy[i] = storage[i];
			}
			StdRandom.shuffle(copy);
		}

		@Override
		public void remove(){
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasNext(){
			return i < copy.length;	
		}

		@Override
		public Item next(){
			if (!hasNext()) throw new NoSuchElementException();
			return copy[i++];
		}
	}

	private	int N = 0;
	private Item[] storage;
	
	//@SuppressWarnings("unchecked")
	public RandomizedQueue(){
		storage = (Item[]) new Object[10];
	}
	
	public boolean isEmpty(){
		return N == 0;
	}

	public int size(){
		return N;
	}
	
	//@SuppressWarnings("unchecked")
	public void enqueue(Item item){
		if (item == null) throw new IllegalArgumentException();
		//System.out.println(item);
		if (N == storage.length){
			//System.out.println("resizing");
			Item[] new_storage = (Item[]) new Object[2*storage.length];	
			for(int i=0; i<N; i++){
				new_storage[i] = storage[i];
			}
			storage = new_storage;
		}
		//System.out.println("N: " + N + "\tlength: " + storage.length);
		storage[N++] = item;
	}
	
	//@SuppressWarnings("unchecked")
	public Item dequeue(){
		if (isEmpty()) throw new NoSuchElementException();
		//System.out.println(N + "\t" + storage.length);
		int random = StdRandom.uniform(0, N);
		Item item = storage[random];
		storage[random] = storage[--N];
		if (N == storage.length/4){
			Item[] new_storage = (Item[]) new Object[storage.length/2];
			for(int i=0; i<N; i++){
				new_storage[i] = storage[i];
			}
			storage = new_storage;
		}
		//System.out.println(N + "\t" + storage.length);
		//System.out.println(N);
		storage[N] = null;
		return item;
	}

	public Item sample(){
		if (isEmpty()) throw new NoSuchElementException();
		int random = StdRandom.uniform(0, N);
		return storage[random];
	}
	
	@Override
	public Iterator<Item> iterator(){
		return new RQIterator();
	}	

	private void print(){
		System.out.println();
		System.out.println("N: " + N);
		System.out.println("Len: " + storage.length);
		System.out.print("RQ: ");
		for(Item i : this){
			System.out.print(i + " ");
		}
		System.out.println("\n");
	}
	
	public static void main(String[] args){
		RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
		for(int i=0; i<10; i++){
			rq.enqueue(i+1);
		}
		rq.print();
	}
}
	
