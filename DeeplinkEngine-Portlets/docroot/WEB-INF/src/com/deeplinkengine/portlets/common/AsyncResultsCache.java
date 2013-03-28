package com.deeplinkengine.portlets.common;

public class AsyncResultsCache {

	public enum STATUS {WAITING, OK};
	
	private String id;
	private Object results;
	private STATUS state = STATUS.WAITING;
	private Thread worker;
	
	public AsyncResultsCache(String _id){
		this.id = _id;
		this.results = null;
	}
	
	public AsyncResultsCache(String _id, Thread _worker){
		this(_id);
		this.worker = _worker;
	}
	
	public String getId() {
		return id;
	}
	
	public Object getResults() {
		return results;
	}
	
	public void setResults(Object results) {
		this.results = results;
		this.state = STATUS.OK;
	}
	public STATUS getState() {
		return state;
	}
	
	public Thread getWorker(){
		return worker;
	}
}
