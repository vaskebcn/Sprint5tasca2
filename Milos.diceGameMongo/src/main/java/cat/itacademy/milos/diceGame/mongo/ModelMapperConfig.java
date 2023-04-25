package cat.itacademy.milos.diceGame.mongo;

import cat.itacademy.milos.diceGame.mongo.model.domain.ObjectIdToStringConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper(ObjectIdToStringConverter objectIdToStringConverter) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(objectIdToStringConverter);
        return modelMapper;
    }
}
