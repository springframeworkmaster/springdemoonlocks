package com.tasks.demolocks;

public interface LockService {

	Iterable<Lock> findAll();

	Lock create(String name) throws LockExistsException;

	boolean release(String name, String value) throws LockNotHeldException;

	Lock refresh(String name, String value) throws LockNotHeldException;

}
