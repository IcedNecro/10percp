package com.checkpoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyArrayList<E> implements List<E>{

	private static Long MAX_DELAY = new Long(10000);
	private static float MULTIPLIER = 1.5f;
	private Object[][] buffer;
	private static int INITIAL_SIZE = 10;
	private int listSize = 0;
	
	private synchronized void allocate() {
		if(buffer == null)
			this.buffer = new Object[INITIAL_SIZE][2];
		else {
			Object[][] arr = new Object[(int)(this.buffer.length*MULTIPLIER)][2];
			for(int i=0; i<listSize; i++){
				arr[i] = this.buffer[i];
			}
			this.buffer = arr;
		}
	}
	
	public MyArrayList() {
		allocate();
		new Thread(new Runnable(){

			@Override
			public void run() {
				while(true) {
					
					synchronized(MyArrayList.this) {
						for(int i=0; i<MyArrayList.this.size(); i++) {
							if(System.currentTimeMillis()-(long)MyArrayList.this.buffer[i][1]>MAX_DELAY) {
								MyArrayList.this.remove((int)i);
								i--;
							}
						}
					}
				}
			}
			
		}).start();
	}
	
	@Override
	public int size() {
		return listSize;
	}

	@Override
	public boolean isEmpty() {
		return (this.buffer == null) || (this.listSize == 0);
	}

	@Override
	public boolean contains(Object o) {
		for(int i=0; i<this.listSize; i++)
			if(this.buffer[i][0].equals(o))
				
				return true;
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		Object[] arr = new Object[this.listSize];
		for(int i=0; i<this.listSize; i++)
			arr[i] = this.buffer[i][0];
		return arr;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(E e) {
		if(this.buffer==null || this.listSize == this.buffer.length) {
			this.allocate();
		}
		this.buffer[this.listSize][0] = e;
		this.buffer[this.listSize][1] = new Long(System.currentTimeMillis());
		this.listSize++;
		return true;
	}

	@Override
	public boolean remove(Object o) {
		for(int i=0; i<this.listSize; i++) {
			if(this.buffer[i][0].equals(o)) {
				this.remove(i);
			}
		}
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c)
			if(!this.contains(o))
				return false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for(Object e : c)
			this.add((E)e);
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		int i=index;
		for(Object o:c)
			this.add(i++,(E)o);
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		for(Object o:c){
			while(this.indexOf(o)!=-1) {
				this.remove(this.indexOf(o));
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = false;
		for(int i=0; i<this.listSize; i++) {
			if(!c.contains(this.buffer[i][0])) {
				this.remove(this.buffer[i][0]);
				i--;
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public void clear() {
		this.listSize = 0;
		this.buffer = null;
	}

	@Override
	public E get(int index) {
		if(index>this.size() || index<0) throw new IndexOutOfBoundsException();
		
		return (E)this.buffer[index][0];
	}

	@Override
	public E set(int index, E element) {
		if(index>this.size() || index<0) throw new IndexOutOfBoundsException();
		
		E previous = (E)this.buffer[index];
		this.buffer[index][0] = element;
		return previous;
	}

	@Override
	public void add(int index, E element) {
		if(index>this.size() || index<0) throw new IndexOutOfBoundsException();
		
		if(this.buffer==null || this.listSize == this.buffer.length)
			allocate();
		
		for(int i=this.listSize; i>index; i--){
			this.buffer[i] = this.buffer[i-1].clone();
		}
		this.buffer[index][0] = element;
		this.buffer[index][1] = new Long(System.currentTimeMillis());
		this.listSize++;
	}

	@Override
	public synchronized E remove(int index) {
		if(index>this.size() || index<0) throw new IndexOutOfBoundsException();
		
		E previous = (E)this.buffer[index][0];
		for(int j=index+1; j<this.listSize; j++)
			this.buffer[j-1]=this.buffer[j].clone();
		this.buffer[this.listSize-1][0]=null;
		this.buffer[this.listSize-1][1]=null;
		this.listSize--;
		return previous;
	}

	@Override
	public int indexOf(Object o) {
		for(int i=0; i<this.listSize; i++)
			if(this.buffer[i][0].equals(o))
				return i;
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		for(int i=this.listSize-1; i>=0; i--)
			if(this.buffer[i][0].equals(o))
				return i;
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if(toIndex>=this.size() || fromIndex<0) throw new IndexOutOfBoundsException();
		
		ArrayList<E> sublist = new ArrayList<E>();
		for(int i=fromIndex; i<toIndex; i++)
			sublist.add((E)this.buffer[i][0]);
		return null;
	}

	@Override 
	public boolean equals(Object o) {
		if(o instanceof MyArrayList)
			if(((MyArrayList)o).size() == this.size())
				for(int i=0; i<this.size(); i++) {
					if(!((MyArrayList)o).get(i).equals(this.buffer[i][0]))
						return false;
					return true;
				}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		
		for(int i=0; i<this.listSize; i++)
			strBuf.append(this.buffer[i][0]+", ");
		return strBuf.toString();
	}
}
