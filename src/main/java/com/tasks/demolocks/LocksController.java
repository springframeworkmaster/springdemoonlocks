package com.tasks.demolocks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor=@_(@Autowired))
public class LocksController {
	
	private final LockService service;

	@RequestMapping(method=RequestMethod.GET)
	public Iterable<Lock> locks() {
		return service.findAll();
	}

	@RequestMapping(value="{name}", method=RequestMethod.POST)
	public Lock create(@PathVariable String name) {
		return service.create(name);
	}

	@RequestMapping(value="{name}/{value}", method=RequestMethod.DELETE)
	public Map<String, Object> release(@PathVariable String name, @PathVariable String value) {
		if (!service.release(name, value)) {
			throw new NoSuchLockException();
		}
		return Collections.singletonMap("status", (Object) "OK");
	}

	@RequestMapping(value="{name}/{value}", method=RequestMethod.PUT)
	public Lock refresh(@PathVariable String name, @PathVariable String value) {
		return service.refresh(name, value);
	}
	
	@ExceptionHandler(LockExistsException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> lockExists() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("status", "INVALID");
		body.put("description", "Lock already exists");
		return new ResponseEntity<Map<String, Object>>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoSuchLockException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> noSuchLock() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("status", "INVALID");
		body.put("description", "Lock not found");
		return new ResponseEntity<Map<String, Object>>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(LockNotHeldException.class)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> lockNotHeld() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("status", "INVALID");
		body.put("description", "Lock not held (values do not match)");
		return new ResponseEntity<Map<String, Object>>(body, HttpStatus.NOT_FOUND);
	}

}
