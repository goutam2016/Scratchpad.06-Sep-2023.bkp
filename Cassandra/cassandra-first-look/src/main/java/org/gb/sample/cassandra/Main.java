package org.gb.sample.cassandra;

import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

public class Main {

	public static void main(String[] args) {
		Cluster cluster = null;
		try {
			cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
			Session session = cluster.connect("mammals");
			System.out.println("session: " + session);
			//findPrimateByQueryBuilder(session);
			findPrimateByMapper(session);
			//directQueryString(session);
		} catch (Exception e) {
			System.err.println("Exception caught at main!");
			e.printStackTrace();
		} finally {
			if (cluster != null) {
				cluster.close();
			}
		}
	}

	private static void findPrimateByQueryBuilder(Session session) {
		Select.Where selectStmt = QueryBuilder.select().all().from("primate").where(QueryBuilder.eq("avgweight", 50.0));

		ResultSet rs = session.execute(selectStmt);
		List<Row> results = rs.all();

		for (Row row : results) {
			System.out.printf("Species: %s, Genus: %s, AvgWeight: %2.2f kg.\n", row.getString("species"),
					row.getObject("genus"), row.getDouble("avgweight"));
		}
	}

	private static void findPrimateByMapper(Session session) {
		final String species = "Hylobates lar";
		MappingManager mappingManager = new MappingManager(session);
		Mapper<Primate> mapper = mappingManager.mapper(Primate.class);
		Primate primate = mapper.get(species);
		String genusName = (primate.getGenus() == null) ? null : primate.getGenus().getName();
		String genusDescription = (primate.getGenus() == null) ? null : primate.getGenus().getDescription();
		System.out.printf("Species: %s, Genus-name: %s, Genus-description: %s, AvgWeight: %2.2f", primate.getSpecies(),
				genusName, genusDescription, primate.getAvgWeight());
	}

	private static void directQueryString(Session session) {
		ResultSet rs = session.execute("select species, genus from primate");
		List<Row> results = rs.all();

		for (Row row : results) {
			System.out.println(row.getString("species") + "<--->" + row.getString("genus"));
		}
	}

}
