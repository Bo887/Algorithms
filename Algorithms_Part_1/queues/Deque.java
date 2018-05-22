import java.lang.Iterable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Deque<Item> implements Iterable<Item>{

	private class Node{

		public Node(Item item, Node prev, Node next){
			this.item = item;
			this.next = next;
			this.prev = prev;
		}

		public Item item;
		public Node next;
		public Node prev;
	}
	
	private class DequeIterator implements Iterator<Item>{
	
		private Node current;
	
		public DequeIterator(){
			current = first;
		}
	
		@Override
		public void remove(){
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasNext(){
			return current != null;
		}

		@Override
		public Item next(){
			if (!hasNext()) throw new NoSuchElementException();
			Item curr = current.item;
			current = current.next;
			return curr;
		}
	}
	
	private Node first;
	private Node last;
	private int m_size;

	public Deque(){
		m_size = 0;
	}
	
	public boolean isEmpty(){
		return size() == 0;
	}
	
	public int size(){
		return m_size;
	}

	public void addFirst(Item item){
		//System.out.println("Adding " + item + " in front");
		if (item == null) throw new IllegalArgumentException();
		if (isEmpty()){
			first = last = new Node(item, null, null);
			m_size++;
			return;
		}
		Node curr_first = first;
		Node new_first = new Node(item, null, curr_first);
		curr_first.prev = new_first;
		first = new_first;
		m_size++;
	}
	
	public void addLast(Item item){
		//System.out.println("Adding " + item + " in back");
		if (item == null) throw new IllegalArgumentException();
		if (isEmpty()){
			first = last = new Node(item, null, null);
			m_size++;
			return;
		}
		Node curr_last = last;
		Node new_last = new Node(item, curr_last, null);
		curr_last.next = new_last;
		last = new_last;
		m_size++;
	}
	
	public Item removeFirst(){
		if (isEmpty()) throw new NoSuchElementException();
		Node curr_first = first;
		//System.out.println(first.next + "\t" + first.prev + "\t" + first.item);
		first = curr_first.next != null ? curr_first.next : null;
		if (first != null) first.prev = null;
		m_size--;
		if (isEmpty()){
			first = last = null;
		}
		//System.out.println("Removing " + curr_first.item + " in front");
		return curr_first.item;
	}
	
	public Item removeLast(){
		if (isEmpty()) throw new NoSuchElementException();
		Node curr_last = last;
		//System.out.println(last.next + "\t" + last.prev + "\t" + last.item);
		last = curr_last.prev != null ? curr_last.prev : null;
		if (last != null) last.next = null;
		m_size--;
		if (isEmpty()){
			first = last = null;
		}
		//System.out.println("Removing " + curr_last.item + " in back");
		return curr_last.item;
	}

	public Iterator<Item> iterator(){
		return new DequeIterator();	
	}

	private void print(){
		System.out.println("Size: " + size());
		System.out.println("isEmpty: " + isEmpty());
		System.out.print("Deque: "); 
		for(Item i : this){
			System.out.print(i + " ");
		}
		System.out.println("\n");
	}	
	
	public static void main(String[] args){
		Deque<Integer> dq = new Deque<Integer>();	
		dq.print();
		dq.addFirst(2);
		dq.print();
		dq.removeLast();
		dq.print();
	}
}
