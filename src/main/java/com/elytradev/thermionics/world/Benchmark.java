/*
 * MIT License
 *
 * Copyright (c) 2017-2018 Isaac Ellingson (Falkreon) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * MIT License
 *
 * Copyright (c) 2017 Isaac Ellingson (Falkreon) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
