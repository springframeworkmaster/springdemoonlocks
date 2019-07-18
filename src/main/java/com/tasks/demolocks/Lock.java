package com.tasks.demolocks;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Lock implements Comparable<Lock> {

	private final String name;
	private final String value;
	private final Date expires;
	
	public boolean isExpired() {
		return expires.before(new Date());
	}

	@Override
	public int compareTo(Lock other) {
		return expires.compareTo(other.expires);
	}

}