package com.rd.account;

import org.junit.Test;

public class SerializationOptimizerImplTest {
	@Test
	public void testAll() {
		SerializationOptimizerImpl serialization = new SerializationOptimizerImpl();
		serialization.getSerializableClasses();
	}
}
