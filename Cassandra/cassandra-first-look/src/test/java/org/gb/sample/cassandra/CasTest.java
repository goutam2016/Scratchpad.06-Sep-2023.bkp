package org.gb.sample.cassandra;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Before;
import org.junit.Test;

public class CasTest {
	
	//@Before
	public void before() throws Exception {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();
	}

	//@Test
	public void work() {
		System.out.println("Hello!");
	}

}

