package org.gb.sample.cassandra;

import org.cassandraunit.dataset.DataSetFileExtensionEnum;
import org.cassandraunit.spring.CassandraDataSet;
import org.cassandraunit.spring.CassandraUnitTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CasStringCtxConfig.class)
@TestExecutionListeners(listeners = CassandraUnitTestExecutionListener.class)
@CassandraDataSet(value = { "mammals.cql", "primate.cql" }, type = DataSetFileExtensionEnum.cql)
@EmbeddedCassandra()
public class CasStringTest {

	private static Cluster cluster;

	@BeforeClass
	public static void setupForAll() {
		cluster = Cluster.builder().addContactPoint("127.0.0.1").withPort(9142).build();
	}

	@Test
	public void findPrimate_HylobatesLar() {
		final String species = "Hylobates lar";
		Session session = cluster.connect("mammals");
		Primate primate = findPrimateByMapper(session, species);

		Boolean arboreal = primate.getArboreal();
		String genusName = primate.getGenus().getName();
		String genusDescription = primate.getGenus().getDescription();

		System.out.printf("Species: %s, Genus-name: %s, Genus-description: %s, AvgWeight: %2.2f", primate.getSpecies(),
				genusName, genusDescription, primate.getAvgWeight());

		session.close();

		Assert.assertTrue(arboreal.booleanValue());
		Assert.assertEquals("Hylobates", genusName);
		Assert.assertEquals("4 genera of gibbons", genusDescription);
	}

	private Primate findPrimateByMapper(Session session, final String species) {
		MappingManager mappingManager = new MappingManager(session);
		Mapper<Primate> mapper = mappingManager.mapper(Primate.class);
		Primate primate = mapper.get(species);
		return primate;
	}

	@AfterClass
	public static void teardownForAll() {
		cluster.close();
	}

}

@Configuration
class CasStringCtxConfig {

}
