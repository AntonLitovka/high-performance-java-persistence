/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.vladmihalcea.book.hpjp.hibernate.batch;

import com.vladmihalcea.book.hpjp.util.AbstractTest;
import org.jboss.logging.Logger;
import org.junit.Test;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;

/**
 * @author Vlad Mihalcea
 */
public class PersistenceContextExtendedBatchTest extends AbstractTest {

	private static final Logger log = Logger.getLogger( PersistenceContextExtendedBatchTest.class );

	@Override
	protected Class<?>[] entities() {
		return new Class<?>[] {
			Post.class
		};
	}

	@Test
	public void testScroll() {
		withBatchAndSessionManagement();
	}

	private void withBatch() {
		int entityCount = 20;
		EntityManager entityManager = null;
		EntityTransaction txn = null;
		try {
			entityManager = entityManagerFactory().createEntityManager();

			txn = entityManager.getTransaction();
			txn.begin();

			int entityManagerBatchSize = 20;

			for ( long i = 0; i < entityCount; ++i ) {
				Post person = new Post( i, String.format( "Post nr %d", i ));
				entityManager.persist( person );

				if ( i > 0 && i % entityManagerBatchSize == 0 ) {
					entityManager.flush();
					entityManager.clear();
				}
			}

			txn.commit();
		} catch (RuntimeException e) {
			if ( txn != null && txn.isActive()) {
				txn.rollback();
			}
			throw e;
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}

	private void withBatchAndSessionManagement() {
		int entityCount = 20;

		doInJPA(entityManager -> {

			for ( long i = 0; i < entityCount; ++i ) {
				Post person = new Post( i, String.format( "Post nr %d", i ));
				entityManager.persist( person );
			}
		});
	}

	private void withBatchAndResetBackToGlobalSetting() {
		EntityManager entityManager = null;
		try {
			entityManager = entityManagerFactory().createEntityManager();
			entityManager.getTransaction().begin();


		} finally {
			if (entityManager != null) {
				entityManager.getTransaction().rollback();
				entityManager.close();
			}
		}
	}

	void a() {

	};

	@Entity(name = "Post")
	public static class Post {

		@Id
		private Long id;

		private String name;

		public Post() {}

		public Post(long id, String name) {
			this.id = id;
			this.name = name;
		}

		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

}
