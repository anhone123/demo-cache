package cache.demo.repository;

import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomPostgresDialect extends PostgreSQL81Dialect {

  public CustomPostgresDialect() {
    super();
    registerFunction("STRING_AGG",
        new SQLFunctionTemplate(StandardBasicTypes.STRING, "STRING_AGG(?1, ?2)"));
  }
}
