package br.com.animal.api.repository;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import br.com.animal.api.domain.Animal;

@Repository
public class AnimalDALRepository implements AnimalDAL {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Page<Animal> findAnimalByDescription(Pageable pagination, String description) {

		Query query = new Query().with(pagination);

		if (description != null) {

			Criteria nameCriteria = new Criteria().orOperator(
					Criteria.where("label").regex(Pattern.compile(description, Pattern.CASE_INSENSITIVE)),
					Criteria.where("family").regex(Pattern.compile(description, Pattern.CASE_INSENSITIVE)),
					Criteria.where("genre").regex(Pattern.compile(description, Pattern.CASE_INSENSITIVE)),
					Criteria.where("species").regex(Pattern.compile(description, Pattern.CASE_INSENSITIVE)),
					Criteria.where("popularNames").regex(Pattern.compile(description, Pattern.CASE_INSENSITIVE)));

			query.addCriteria(nameCriteria);

			List<Animal> animais = mongoTemplate.find(query, Animal.class);

			long count = mongoTemplate.count(query, Animal.class);

			return new PageImpl<Animal>(animais, pagination, count);
		}

		return  Page.empty();
	
	}

}
