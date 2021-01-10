package mssoftutils.datas;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class UseExistingOrGenerateIdGenerator extends SequenceStyleGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		Serializable id = session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);

		if (id == null) {
			id = super.generate(session, object);

			if (id instanceof Integer) {
				Integer integerId = (Integer) id;
				integerId += 1;

				return integerId;
			}
		}

		return id;
	}
}