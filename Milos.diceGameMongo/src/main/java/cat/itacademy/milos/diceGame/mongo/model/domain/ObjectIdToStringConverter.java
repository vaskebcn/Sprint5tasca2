package cat.itacademy.milos.diceGame.mongo.model.domain;

import org.bson.types.ObjectId;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdToStringConverter implements Converter<ObjectId, String> {
    @Override
    public String convert(MappingContext<ObjectId, String> context) {
        ObjectId objectId = context.getSource();
        return objectId != null ? objectId.toHexString() : null;
    }
}
