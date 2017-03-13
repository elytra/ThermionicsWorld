package com.elytradev.thermionics.world;

import java.util.HashMap;
import java.util.Map;

public class Benchmark {
	private static final long MILLIS_PER_NANO = 1_000_000L;
	
	private HashMap<String, Long> savedTimes = new HashMap<>();
	
	private long frameStart = 0L;
	private long sectionStart = 0L;
	private long elapsed = 0L;
	
	public Benchmark() {
		
	}
	
	public void startFrame() {
		savedTimes.clear();
		sectionStart = System.nanoTime() / MILLIS_PER_NANO;
		frameStart = sectionStart;
	}
	
	public void endSection(String sectionName) {
		long now = System.nanoTime() / MILLIS_PER_NANO;
		savedTimes.put(sectionName, now - sectionStart);
		sectionStart = now;
	}
	
	public void endFrame() {
		long now = System.nanoTime() / MILLIS_PER_NANO;
		elapsed = now - frameStart;
		sectionStart = 0L;
		frameStart = now;
	}
	
	public long getTotalTime() {
		return elapsed;
	}
	
	public void printDebug() {
		StringBuilder result = new StringBuilder();
		result.append("TOTAL: ");
		result.append(elapsed);
		
		for(Map.Entry<String,Long> entry : savedTimes.entrySet()) {
			result.append(' ');
			result.append(entry.getKey());
			result.append(':');
			result.append(entry.getValue());
		}
		
		
		
		System.out.println(result);
	}
}
