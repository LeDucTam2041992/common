//package com.kpro.common.function;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.SqlParameter;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.simple.SimpleJdbcCall;
//
//public class JdbcCallStoreExecutor<T> {
//  private static final Logger log = LoggerFactory.getLogger(JdbcCallStoreExecutor.class);
//  private final JdbcTemplate jdbcTemplate;
//  private String schemaName;
//  private String catalogName;
//  private String procedureName;
//  private String keyResult;
//  private SqlParameter[] declareParams;
//  private Map<String, Object> paramMap;
//  private Class<T> mappedClass;
//
//  private JdbcCallStoreExecutor(JdbcTemplate jdbcTemplate) {
//    this.jdbcTemplate = jdbcTemplate;
//  }
//
//  public static <T> JdbcCallStoreExecutor<T> builder(JdbcTemplate jdbcTemplate) {
//    return new JdbcCallStoreExecutor<>(jdbcTemplate);
//  }
//
//  public JdbcCallStoreExecutor<T> withSchemaName(String schemaName) {
//    this.schemaName = schemaName;
//    return this;
//  }
//
//  public JdbcCallStoreExecutor<T> withCatalogName(String catalogName) {
//    this.catalogName = catalogName;
//    return this;
//  }
//
//  public JdbcCallStoreExecutor<T> withProcedureName(String procedureName) {
//    this.procedureName = procedureName;
//    return this;
//  }
//
//  public JdbcCallStoreExecutor<T> withKeyResult(String keyResult) {
//    this.keyResult = keyResult;
//    return this;
//  }
//
//  public JdbcCallStoreExecutor<T> withDeclareParams(SqlParameter... declareParams) {
//    this.declareParams = declareParams;
//    return this;
//  }
//
//  public JdbcCallStoreExecutor<T> withParamMap(Map<String, Object> paramMap) {
//    this.paramMap = paramMap;
//    return this;
//  }
//
//  public JdbcCallStoreExecutor<T> withMappedClass(Class<T> mappedClass) {
//    this.mappedClass = mappedClass;
//    return this;
//  }
//
//  public Optional<T> executeAsSingle() {
//    return executeAsList().stream().findFirst();
//  }
//
//  public List<T> executeAsList() {
//    Map<String, Object> result = execute();
//    Object list = result.get(keyResult);
//    if (list instanceof List<?>) {
//      return ((List<?>) list).stream().map(mappedClass::cast).toList();
//    }
//    return Collections.emptyList();
//  }
//
//  public Map<String, Object> execute() {
//    SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate);
//    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
//    if (paramMap != null && !paramMap.isEmpty()) {
//      paramMap.forEach(mapSqlParameterSource::addValue);
//    }
//
//    if (schemaName != null) {
//      jdbcCall.withSchemaName(schemaName);
//    }
//
//    if (catalogName != null) {
//      jdbcCall.withCatalogName(catalogName);
//    }
//
//    if (procedureName != null) {
//      jdbcCall.withProcedureName(procedureName);
//    }
//
//    // nên dùng withoutProcedureColumnMetaDataAccess trên production để tránh làm giảm hiệu năng
//    // do cần query lấy metadata trước khi execute
//    if (declareParams != null && declareParams.length > 0) {
//      jdbcCall.withoutProcedureColumnMetaDataAccess().declareParameters(declareParams);
//    }
//
//    // Spring có cơ chế cache nội bộ cho các Mapper được tạo qua newInstance. Nó giúp tiết kiệm
//    // CPU đáng kể nếu procedure đó được gọi liên tục. -> không nên dùng new BeanPropertyRowMapper<>(mappedClass)
//    if (keyResult != null) {
//      jdbcCall.returningResultSet(keyResult, BeanPropertyRowMapper.newInstance(mappedClass));
//    }
//
//    return jdbcCall.execute(mapSqlParameterSource);
//  }
//}
