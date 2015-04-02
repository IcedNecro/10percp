package com.checkpoint.test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyLinkedList<E> implements List<E> {

	private long MAX_TIME = 10000;
	private ListNode<E> header=null;
	private ListNode<E> end=null;
	private TimeListener listener;
	
	public MyLinkedList() {
		this.listener = new TimeListener();
		this.listener.start();
	}
	
	private synchronized Object[] getNodesArray() {
		Object nodes[] = new Object[this.size()];
		ListNode<E> current = this.header;

		for(int i=0; i<nodes.length; i++) {
			nodes[i] = current.copy();
			current = current.next;
		}
		return nodes;
	}
	
	@Override
	public boolean add(E e) {

		ListIterator<E> iter = new InnerIterator(this.end);
		iter.add(e);
		return true;
	}

	@Override
	public void add(int index, E element) {
		if(index>=this.size() || index<0) throw new IndexOutOfBoundsException();
		else {
			ListIterator<E> iter = this.listIterator(index);
			iter.add(element);
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for(E elem : c)
			this.add(elem);
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if(index>=this.size() || index<0) throw new IndexOutOfBoundsException();
		else {
			int b = index;
			for(E elem : c){
				this.add(b++,elem);
			}
			return true;
		}
	}

	@Override
	public void clear() {
		this.header = null;
	}

	@Override
	public boolean contains(Object o) {
		for(Iterator<E> iter = this.iterator(); iter.hasNext();)
			if(iter.next().equals(o))
				return true;
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object obj : c)
			if(!this.contains(obj))
				return false;
		return true;
	}

	@Override
	public E get(int index) {
		if(index>=this.size() || index<0) throw new IndexOutOfBoundsException();
		else {
			return this.listIterator(index).next();
		}
	}

	@Override
	public int indexOf(Object o) {
		int count = 0;
		
		for(Iterator<E> iter = this.iterator(); iter.hasNext(); count++)
			if(iter.next().equals(o))
				return count;
		return -1;
	}

	@Override
	public boolean isEmpty() {
		if (this.header!=null)
			return false;
		else 
			return true;
	}

	@Override
	public Iterator<E> iterator() {
		return new InnerIterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		ListIterator<E> iter = new InnerIterator(this.end);
		int count = this.size()-1;
		while (iter.hasPrevious()) {
			
			if(iter.previous().equals(o))
				return count;
			count--;
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		return new InnerIterator();
	}

	@Override
	public synchronized ListIterator<E> listIterator(int index) {
		if(index>=this.size() || index<0) throw new IndexOutOfBoundsException();
		else {
			return new InnerIterator(index);
		}
	}

	@Override
	public synchronized boolean remove(Object o) {
		for(Iterator<E> iter = this.iterator(); iter.hasNext();)
			if(iter.next().equals(o)) {
				iter.remove();
				return true;
			}
		return false;
	}

	@Override
	public synchronized E remove(int index) {
		if(index>=this.size() && index<0) throw new IndexOutOfBoundsException();
		else {
			ListIterator<E> iter = this.listIterator(index);
			E value = iter.next();
			iter.remove();
			return value;
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean b=false;
		for (Object o: c)
			while(this.contains(o)) {
				b=true;
				this.remove(o);
			}
		return b;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean b=false;
		for (E o: this)
			if(!c.contains(o))
				while(this.contains(o)) {
					b=true;
					this.remove(o);
				}
		return b;
	}

	@Override
	public E set(int index, E element) {
		if(index>=this.size() || index<0) throw new IndexOutOfBoundsException();
		else {
		
			ListIterator<E> iter = this.listIterator(index);
			E value = iter.next();
			iter.set(element);
			return value;
		}
	}

	@Override
	public int size() {
		int count = 0;
		for(Iterator<E> iter = this.iterator(); iter.hasNext(); count++, iter.next());
		return count;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		
		return null;
	}

	@Override
	public Object[] toArray() {
		Object[] arr = new Object[this.size()];
		int i = 0;
		for(Iterator<E> iter = this.listIterator(); iter.hasNext();)
			arr[i++]=iter.next();
		return arr;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return null;
	}

	private class InnerIterator implements ListIterator<E> {

		private ListNode<E> pointer;
		private int index;
		private boolean nextInvoked = true;
		private ListNode<E> lastReturned = null;
		
		public InnerIterator() {
			this.pointer = MyLinkedList.this.header;
		}
		
		public InnerIterator(ListNode<E> node) {
			if(node == MyLinkedList.this.end) this.nextInvoked = false;
			this.pointer = node;
		}
		
		public InnerIterator(int index) {
			if(index == MyLinkedList.this.size()-1)
				this.nextInvoked = false;
			else 
				this.nextInvoked = true;
			this.index = index;
			this.pointer = MyLinkedList.this.header;
			
			int b = index;
			while(b-->0)
				this.pointer=this.pointer.getNext();
		}
		
		@Override
		public void add(E e) {
			ListNode<E> elemToAdd = new ListNode<E>(e);
			if(MyLinkedList.this.header==null) {
				MyLinkedList.this.header=elemToAdd;
				MyLinkedList.this.end = MyLinkedList.this.header;
				return;
			}
			
			if(this.nextInvoked){
				elemToAdd.setNext(this.pointer);
				elemToAdd.setPrevious(this.pointer.getPrevious());
				if(this.pointer.getPrevious()!=null)
					this.pointer.getPrevious().setNext(elemToAdd);
				else
					MyLinkedList.this.header = elemToAdd;
				this.pointer.setPrevious(elemToAdd);
					
			} else {
				
				elemToAdd.setPrevious(this.pointer);
				elemToAdd.setNext(this.pointer.getNext());
				if(this.pointer.getNext()!=null)
					this.pointer.getNext().setPrevious(elemToAdd);
				else
					MyLinkedList.this.end = elemToAdd;
				this.pointer.setNext(elemToAdd);
				
			}
		}

		@Override
		public boolean hasNext() {
			if(this.pointer == null)
				return false;
			else return true;
		}

		@Override
		public boolean hasPrevious() {
			if(this.pointer == null)
				return false;
			else return true;
		}

		@Override
		public E next() {
			E value = this.pointer.getValue();
			this.index++;
			this.lastReturned = this.pointer;
			this.pointer = this.pointer.getNext();
			this.nextInvoked=true;
			return value;
		}

		@Override
		public int nextIndex() {
			return this.index+1;
		}

		@Override
		public E previous() {
			E value = this.pointer.getValue();
			this.index--;
			this.lastReturned = this.pointer;
			this.pointer = this.pointer.getPrevious();
			this.nextInvoked=false;
			return value;
		}

		@Override
		public int previousIndex() {
			return this.index-1;
		}

		@Override
		public void remove() {
			if(this.lastReturned.getPrevious()!=null)
				this.lastReturned.getPrevious().setNext(this.lastReturned.getNext());
			else 
				MyLinkedList.this.header = this.lastReturned.getNext();
			if(this.lastReturned.getNext()!=null)
				this.lastReturned.getNext().setPrevious(this.lastReturned.getPrevious());
			else 
				MyLinkedList.this.end = this.lastReturned.getPrevious();

		}

		@Override
		public void set(E e) {
			this.lastReturned.setValue(e);
		}

	}

	private class ListNode<E> {

		private ListNode<E> previous;
		private ListNode<E> next;
		private E value;
		private Long timeCreated;
		
		public ListNode(E value) {
			this.value = value;
			this.timeCreated = System.currentTimeMillis();
		}
		
		public ListNode<E> copy() {
			ListNode<E> c = new ListNode<E>(value);
			c.timeCreated = timeCreated;
			c.next = next;
			c.previous = previous;
			return c;
		}

		public ListNode<E> getPrevious() {
			return previous;
		}

		public void setPrevious(ListNode<E> previous) {
			this.previous = previous;
		}

		public ListNode<E> getNext() {
			return next;
		}

		public void setNext(ListNode<E> next) {
			this.next = next;
		}

		public Long getTime() {
			return this.timeCreated;
		}
		
		public boolean isOld() {
			return (System.currentTimeMillis()-this.timeCreated)>MyLinkedList.this.MAX_TIME;
		}
		
		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			String s1="NULL";
			String s2="NULL";
			if(this.next!=null) s1 = this.next.value+"";
			if(this.previous!=null) s2 = this.previous.value+"";

			return s2 +":"+s1;
		}
	}
	
	
	private class TimeListener extends Thread {
		private boolean toStop=false;
		
		public void stopThread() {
			this.toStop = true;
		}
		
		@Override 
		public void run() {
			while(!toStop) {

				Object[] nodes = MyLinkedList.this.getNodesArray();

				for(int i = 0, j=0; i<nodes.length && i<MyLinkedList.this.size();i++, j++ )
					if(((ListNode<E>)nodes[i]).isOld()){
						MyLinkedList.this.remove((int)j--);
					} 
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.listener.stopThread();
	};

	@Override
	public boolean equals(Object o) {
		if(o instanceof MyLinkedList) {
			if(((MyLinkedList)o).size()==this.size()) {
				for(int i=0; i<this.size(); i++)
					if(!((MyLinkedList)o).get((int)i).equals(this.get(i)))
						return false;
				return true;
			}
		}
		return false;
	}
	
	@Override 
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		for(Iterator<E> iter = this.iterator(); iter.hasNext();) {
			strBuf.append(iter.next()+",");
		}
		return strBuf.toString();
	}
}