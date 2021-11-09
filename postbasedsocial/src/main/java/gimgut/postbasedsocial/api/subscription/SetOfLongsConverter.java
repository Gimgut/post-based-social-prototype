package gimgut.postbasedsocial.api.subscription;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashSet;
import java.util.Set;

@Converter
public class SetOfLongsConverter implements AttributeConverter<Set<Long>, String> {

    private final String SPLITTER = ",";


    @Override
    public String convertToDatabaseColumn(Set<Long> longs) {
        //toString transforms "[1, 2]" into "1,2"
        return longs.toString().replaceAll("]|\\[| ", "");
    }

    @Override
    public Set<Long> convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return new HashSet<>();
        }
        String[] split = s.split(SPLITTER);
        Set<Long> resultSet = new HashSet<>(split.length);
        for (String setElem : split) {
            resultSet.add(Long.valueOf(setElem));
        }
        return resultSet;
    }
}
