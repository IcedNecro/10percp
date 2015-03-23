package com.checkpoint;

import java.util.Stack;

public class MyOwnThreadPool {
	private int n, nOfThreads;
	private ManagerThread manager;
	
	public MyOwnThreadPool(int n,int nOfThreads) {
		this.n=n;
		this.nOfThreads = nOfThreads;
		manager = new ManagerThread(n, nOfThreads);
	}
	
	public int getAnswer() {
		this.manager.start();
		/*try {
			this.manager.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		
		return 0;//this.manager.getResult();
	}
	
	private class ManagerThread extends Thread {
		private Stack<Thread> stack;
			
		private Integer result = 0;
		
		public ManagerThread(int n, int nOfThreads) {
			this.stack = new Stack<>();
			for(int i=0; i<nOfThreads; i++) {
				Thread t = new LocalThread();
				this.stack.add(t);
				t.start();
			}
		}
		
		@Override 
		public void run() {
			int i=0;
			while(i<MyOwnThreadPool.this.n) {
			//	synchronized(stack) {
					if(!stack.isEmpty()) {
						LocalThread lt = (LocalThread) stack.remove(0);
						synchronized (lt) {
							lt.setData(i++, result);
							lt.notify();							
						}
					}
				//}
			}
		}
		
		public int getResult() {
			return this.result;
		}
		
		private class LocalThread extends Thread {
			private Integer total;
			private int i;
			
			public LocalThread() {
				this.total = total;
			}
			
			public void setData(int i, Integer total) {
				this.total = total;
				this.i = i;
			}
			
			@Override
			public void run() {
				while(true){
					System.out.println("running");
					synchronized (this) {
						try {
							System.out.println("waiting");
							this.wait();
							System.out.println("after");
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						int buffer = i%2==0?1:-1;
						this.total += (int) Math.pow(2, (i-buffer));
						stack.add(this);
					}
				}
			}
		}

	}
	
	public static void main(String[] args) {
		MyOwnThreadPool tp = new MyOwnThreadPool(9,5);
		tp.getAnswer();
	}
}
