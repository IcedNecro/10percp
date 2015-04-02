package com.checkpoint.test;

import java.util.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyMap<K,V> implements Map<K, V> {

	
	private Thread t;
	private final static float MULTIPLIER = 1.25f;
	private final static int INITIAL_SIZE = 10;
	private long expirationTime = 10000;
	
	private int topPosition = 0;
	
	private Object[] map;
	
	public MyMap() {
		this.allocate();
		this.t = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){

					for(Entry<K,V> e:MyMap.this.entrySet()) {
						MyEntry<K,V> entry = (MyEntry<K, V>)e;
						if(entry.isOld(expirationTime)) {
							MyMap.this.remove(entry.getKey());
						}
					}
				}
			}
			
			
		});
		this.t.start();
	}
	
	public void allocate() {
		if(map == null)
			this.map = new Object[INITIAL_SIZE];
		else {
			int newSize = (int) (this.map.length * MULTIPLIER);
			Object buffer[] = new Object[newSize]; 
			
			for(int i=0; i<topPosition; i++) {
				buffer[i] = map[i];
			}
			this.map = buffer;
		}
	}
	
	@Override
	public void clear() {
		this.map = null;
		this.allocate();
		this.topPosition = 0;
	}

	@Override
	public boolean containsKey(Object arg0) {
		for(Object o : this.map){
			MyEntry<K,V> entry = (MyEntry<K,V>) o;
			if(entry.getKey().equals(arg0))
				return true;
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		for(Object o : this.map){
			MyEntry<K,V> entry = (MyEntry<K,V>) o;
			if(entry.getValue().equals(value))
				return true;
		}
		return false;
	}

	@Override
	public synchronized Set<java.util.Map.Entry<K, V>> entrySet() {
		HashSet<Entry<K,V>> set = new HashSet<Entry<K,V>>();
		for(int i=0; i<topPosition; i++) {
			Entry<K,V> entry = (Entry<K,V>) this.map[i];
			set.add(entry);
		}
		return set;
	}

	@Override
	public V get(Object key) {
		for(int i=0; i < this.topPosition; i++){
			MyEntry<K,V> entry = (MyEntry<K,V>) map[i];
			if(entry.getKey().equals(key))
				return entry.getValue();
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		
		return this.topPosition==0 ? true : false;
	}

	@Override
	public Set<K> keySet() {
		HashSet<K> set = new HashSet<K>();
		
		for(int i=0; i<topPosition; i++) {
			Entry<K,V> entry = (Entry<K,V>) this.map[i];
			set.add(entry.getKey());
		}
		return set;
	}

	@Override
	public synchronized V put(K key, V value) {
		if(this.topPosition==this.map.length-1)
			allocate();
		
		MyEntry<K,V> entry = new MyEntry<K,V>(key, value);
		
		V exValue = this.get(entry.getKey());
		if(exValue==null)
			this.map[topPosition++] = entry;
		else{
			for(int i = 0; i<this.topPosition; i++) {
				MyEntry<K,V> innerEntry = (MyEntry<K,V>) this.map[i];
				if(innerEntry.getKey().equals(key)){
					innerEntry.setValue(value);
					break;
				}
			}
		}
		
		return exValue;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(Entry<? extends K,? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public synchronized V remove(Object key) {
		V exValue = null;
		
		for(int i = 0; i<this.topPosition; i++) {
			MyEntry<K,V> innerEntry = (MyEntry<K,V>) this.map[i];
			if(innerEntry.getKey().equals(key)){
				exValue = innerEntry.getValue();
				for(int j = i+1; j<this.topPosition;j++)
					this.map[j-1] = this.map[j];
				this.map[this.topPosition-1] = null;
				this.topPosition--;
				break;
			}
		}
		return exValue;
	}

	@Override
	public int size() {
		return this.topPosition;
	}

	@Override
	public Collection<V> values() {
		HashSet<V> set = new HashSet<V>();
		
		for(int i=0; i<topPosition; i++) {
			Entry<K,V> entry = (Entry<K,V>) this.map[i];
			set.add(entry.getValue());
		}
		return set;
	}

	private class MyEntry<K,V> implements Map.Entry<K, V> {

		private K key;
		private V value;
		private Date dateCreated;
		
		public MyEntry(K key, V value) {
			this.key = key;
			this.value = value;
			this.dateCreated = new Date();
		}
		
		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.value;
		}

		@Override
		public V setValue(V arg0) {
			V exValue = this.value;
			this.value = arg0;
			return exValue;
		}	
		
		@Override
		public String toString() {
			return this.key+"="+this.value;
		}
		
		public boolean isOld(long expirationTime) {
			Date time = new Date();
			return 
					time.getTime() - this.dateCreated.getTime()
						<expirationTime ? false: true;
		}
	}
}
