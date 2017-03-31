package myapp

import com.fasterxml.jackson.databind.ObjectMapper
import grails.gorm.transactions.Rollback
import grails.gorm.transactions.Transactional
import grails.test.mixin.TestFor
import org.grails.orm.hibernate.HibernateDatastore
import org.springframework.transaction.PlatformTransactionManager
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class DummyJacksonSpec extends Specification {

	@Shared @AutoCleanup HibernateDatastore hibernateDatastore
	@Shared PlatformTransactionManager transactionManager

	void setupSpec() {
		hibernateDatastore = new HibernateDatastore(Dummy)
		transactionManager = hibernateDatastore.getTransactionManager()
	}

	@Transactional
	def "json does only contain keys which relate to actual properties"(){
		Dummy dummy = new Dummy(name: "dummy").save()
		ObjectMapper objectMapper = new ObjectMapper()

		when:
		String json = objectMapper.writeValueAsString dummy
		Map<String, Object> map = objectMapper.readValue json, Map

		then:
		!map.containsKey("properties")
	}

}
