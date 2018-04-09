package com.troy.test;

import java.util.*;

public class BigObject {

	private HashMap<Object, Object> o;

	public BigObject() {
		o = new HashMap<Object, Object>();

		for (int i = 0; i < 1000; i++) {
			if ((i % 10) == 0)
				o.put("32r4e", "23ew" + Math.random());
			ArrayList<Double> d = new ArrayList<Double>();
			for (int j = 0; j < 10_000; j++)
				d.add(new Double(j));

			o.put(Integer.valueOf(i), d);
		}

	}
}
