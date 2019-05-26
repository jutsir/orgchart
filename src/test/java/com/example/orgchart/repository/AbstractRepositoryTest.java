package com.example.orgchart.repository;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration()
@DataNeo4jTest
abstract class AbstractRepositoryTest {
	@Autowired
	private SessionFactory sessionFactory;
	Session session;

	@Before
	public void setUp() {
		session = sessionFactory.openSession();
		session.beginTransaction();
	}
}
