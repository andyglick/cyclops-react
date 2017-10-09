package com.aol.cyclops2.control;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cyclops.control.Option;
import cyclops.control.Xor;
import org.junit.Before;
import org.junit.Test;

public class XorSecondaryTest {

	Xor<FileNotFoundException,Integer> failure;
	FileNotFoundException error = new FileNotFoundException();
	@Before
	public void setup(){
		failure = Xor.secondary(error);
	}


	@Test
	public void testOf() {
		assertNotNull(failure);
	}



	@Test
	public void testMap() {
		assertThat(failure.map(x->x+1),equalTo(failure));
	}

	@Test
	public void testFlatMap() {
		assertThat(failure.flatMap(x->Xor.primary(10)),equalTo(failure));
	}

	@Test
	public void testFilter() {
		assertThat(failure.filter(x->x==10),equalTo(Option.some(failure)));
	}

	

	
	@Test
	public void testOrElse() {
		assertThat(failure.orElse(10),equalTo(10));
	}

	@Test
	public void testOrElseGet() {
		assertThat(failure.orElseGet(()->10),equalTo(10));
	}

	@Test
	public void testToOptional() {
		assertThat(failure.toOptional(),equalTo(Optional.empty()));
	}

	@Test
	public void testToStream() {
		assertThat(failure.stream().collect(Collectors.toList()),
				equalTo(Stream.of().collect(Collectors.toList())));
	}

	@Test
	public void testIsSuccess() {
		assertThat(failure.isPrimary(),equalTo(false));
	}

	@Test
	public void testIsFailure() {
		assertThat(failure.isSecondary(),equalTo(true));
	}

	Integer value = null;
	@Test
	public void testForeach() {
		
		failure.forEach(v -> value = v);
		assertThat(value,is(nullValue()));
	}

	
	Object errorCaptured;
	@Test
	public void testForeachFailed() {
		errorCaptured = null;
		failure.secondaryPeek(e -> errorCaptured =e);
		assertThat(error,equalTo(errorCaptured));
	}

	

}
