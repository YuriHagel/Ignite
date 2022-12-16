package hagel.ignite.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

@AllArgsConstructor
@Getter
public class UserData {
    @QuerySqlField(index = true, notNull = true)

    private String name;
    @QuerySqlField
    private String lastName;
}
