package com.checkpoint.test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyLinkedList<E> implements List<E> {

	private ListNode<E> header=null;
	private ListNode<E> end=null;
	
	@Override
	public boolean add(E e) {

		ListIterator<E> iter = new InnerIterator(this.end);
		iter.add(e);
		return true;
	}

	@Override
	public void add(int index, E element) {
		ListIterator<E> iter = this.listIterator(index);
		iter.add(element);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for(E elem : c)
			this.add(elem);
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		int b = index;
		for(E elem : c){
			this.add(b++,elem);
		}
		return true;
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
		return this.listIterator(index).next();
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
	public ListIterator<E> listIterator(int index) {
		return new InnerIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		for(Iterator<E> iter = this.iterator(); iter.hasNext();)
			if(iter.next().equals(o)) {
				iter.remove();
				return true;
			}
		return false;
	}

	@Override
	public E remove(int index) {
		ListIterator<E> iter = this.listIterator(index);
		E value = iter.next();
		return value;
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
		ListIterator<E> iter = this.listIterator(index);
		E value = iter.next();
		iter.set(element);
		return value;
	}

	@Override
	public int size() {
		int count = 0;
		for(Iterator<E> iter = this.iterator(); iter.hasNext(); count++, iter.next());
		return count;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
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
				//System.out.println(elemToAdd);
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

	private static class ListNode<E> {

		private ListNode<E> previous;
		private ListNode<E> next;
		private E value;

		public ListNode(E value) {
			this.value = value;
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
	
	
	@Override 
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		for(Iterator<E> iter = this.iterator(); iter.hasNext();) {
			strBuf.append(iter.next()+",");
		}
		return strBuf.toString();
	}
}