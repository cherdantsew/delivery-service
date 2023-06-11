package com.deliverengine.deliver;

import com.deliverengine.core.enumeration.AuthoritiesConstants;
import com.deliverengine.core.enumeration.RolesConstants;
import com.deliverengine.core.security.jwt.TokenProvider;
import com.deliverengine.deliver.config.TestBeansConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.apache.kafka.test.TestUtils.randomString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(TestBeansConfig.class)
public abstract class SpringBootApplicationTest extends TestPostgresContainer {

//    @BeforeEach
//    public void clearTables(){
//        jdbcTemplate.execute("delete from parcel_delivery_order");
//        jdbcTemplate.execute("delete from couriers");
//        jdbcTemplate.execute("delete from users");
//    }

    private static final String INSERT_INTO_TEMPLATE = """
        insert into %s(%s) values(%s)
        """;

    @Autowired
    protected MockMvc mockmvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected TokenProvider tokenProvider;
    @Autowired
    protected PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected String authenticateAndReturnToken(String username, RolesConstants currentRole, List<AuthoritiesConstants> authorities) {
        TokenProvider.JwtTokenConfiguration jwtTokenConfiguration = new TokenProvider.JwtTokenConfiguration(username, authorities.stream().map(AuthoritiesConstants::name).collect(Collectors.joining()), currentRole.name());
        String jwt = tokenProvider.createToken(jwtTokenConfiguration);
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return String.format("Bearer %s", jwt);
    }

    protected String authenticateAndReturnToken(RolesConstants currentRole, List<AuthoritiesConstants> authorities) {
        return authenticateAndReturnToken(randomString(10), currentRole, authorities);
    }

    protected void insert(Object entity) {
        jdbcTemplate.update(buildUpdateQuery(extractTableName(entity), extractInsertFields(entity)));
    }

    private List<ImmutablePair<String, Object>> extractInsertFields(Object entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
            .filter(field -> field.getAnnotation(Column.class) != null || field.getAnnotation(JoinColumn.class) != null)
            .filter(field -> {
                try {
                    field.setAccessible(true);
                    return field.get(entity) != null;
                } catch (IllegalAccessException e) {
                    return false;
                }
            })
            .map(field -> {
                    try {
                        if (field.getAnnotation(JoinColumn.class) != null &&
                            StringUtils.isNotEmpty(field.getAnnotation(JoinColumn.class).name())
                        ) {
                            String fieldName = field.getAnnotation(JoinColumn.class).name();
                            String referencedColumnName = field.getAnnotation(JoinColumn.class).referencedColumnName();
                            Object referenceObject = field.get(entity);
                            Object value = Arrays.stream(referenceObject.getClass().getDeclaredFields())
                                .filter(refObjectField -> {
                                    try {
                                        refObjectField.setAccessible(true);
                                        return refObjectField.get(referenceObject) != null;
                                    } catch (IllegalAccessException e) {
                                        return false;
                                    }
                                })
                                .filter(referencedObjectField -> referencedObjectField.getAnnotation(Column.class) != null
                                    && referencedObjectField.getAnnotation(Column.class).name().equals(referencedColumnName))
                                .map(foundField -> {
                                    try {
                                        return foundField.get(referenceObject);
                                    } catch (IllegalAccessException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).findFirst().orElseThrow();
                            return new ImmutablePair<>(fieldName, value);
                        } else /*if (StringUtils.isNotEmpty(field.getAnnotation(Column.class).name()))*/
                        return new ImmutablePair<>(
                            StringUtils.isEmpty(field.getAnnotation(Column.class).name()) ? field.getName() : field.getAnnotation(Column.class).name(),
                            field.get(entity)
                        );
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            )
            .collect(Collectors.toList());
    }

    private String extractTableName(Object entity) {
        return entity.getClass().getAnnotation(Table.class).name();
    }

    private String buildUpdateQuery(String tableName, List<ImmutablePair<String, Object>> fieldNamesToValues) {
        return String.format(
            INSERT_INTO_TEMPLATE,
            tableName,
            fieldNamesToValues.stream().map(ImmutablePair::getLeft).collect(Collectors.joining(",")),
            fieldNamesToValues.stream()
                .map(ImmutablePair::getRight)
                .map(object -> {
                    if (ClassUtils.isPrimitiveOrWrapper(object.getClass()) || BigDecimal.class == object.getClass()) {
                        return object.toString();
                    }
                    return String.format("'%s'", object);
                })
                .collect(Collectors.joining(","))
        );
    }

    public <T> T resultAsObject(MvcResult mvcResult, Class<T> clazz) throws Exception {
        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> resultAsList(MvcResult mvcResult, Class<T> clazz) throws Exception {
        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, ArrayList.class);
    }
}
