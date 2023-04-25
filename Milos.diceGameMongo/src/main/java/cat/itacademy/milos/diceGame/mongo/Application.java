package cat.itacademy.milos.diceGame.mongo;

import cat.itacademy.milos.diceGame.mongo.model.domain.ObjectIdToStringConverter;
import com.mongodb.client.MongoClient;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;


@SpringBootApplication
@EnableMongoAuditing
public class Application {

//	Custom modelMapper to solve ObjectId to String problem
@Bean
public ModelMapper modelMapper(ObjectIdToStringConverter objectIdToStringConverter) {
	ModelMapper modelMapper = new ModelMapper();
	modelMapper.addConverter(objectIdToStringConverter);
	return modelMapper;
}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public MongoTemplate mongoTemplate(MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, "MongoDiceGame");
	}

}

//	@EnableMongoAuditing - enable automatic setting of the creation and modification dates in Spring Data MongoDB
//   MongoTemplate - it's a bean that Spring Data MongoDB will use to interact with the database
