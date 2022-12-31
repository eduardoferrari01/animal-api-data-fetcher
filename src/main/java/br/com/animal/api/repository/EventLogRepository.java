package br.com.animal.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.animal.api.domain.EventLog;

public interface EventLogRepository extends MongoRepository<EventLog,String> {

}
