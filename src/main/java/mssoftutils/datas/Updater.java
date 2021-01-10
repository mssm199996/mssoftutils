package mssoftutils.datas;

import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class Updater<T extends MSUpdatableEntity> {

	private void deleteEntity(String entityName, int entityId) {
		String request = "DELETE FROM " + entityName + " WHERE (" + this.getEntityIdName() + " = :id)";

		try (Session session = this.sessionFactory().openSession()) {
			session.beginTransaction();
			session.createNativeQuery(request).setParameter("id", entityId).executeUpdate();
			session.getTransaction().commit();
		}
	}

	private void saveEntity(T entity) {
		try (Session session = this.sessionFactory().openSession()) {
			session.beginTransaction();
			session.save(entity);
			session.getTransaction().commit();
		}
	}

	public boolean updateEntity(int originalEntityId, T newEntity) {
		newEntity.setId(originalEntityId);

		this.deleteEntity(this.getEntityName(newEntity), originalEntityId);
		this.saveEntity(newEntity);

		return true;
	}

	public String getEntityName(T entity) {
		Table table = entity.getClass().getAnnotation(Table.class);

		if (table != null) {
			return table.name();
		}

		return "NO_TABLE_NAME";
	}

	public String getEntityIdName() {
		return "ID";
	}

	public abstract SessionFactory sessionFactory();
}
