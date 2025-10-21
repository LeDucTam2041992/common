// package com.kpro.common.procedure;
//
// import java.util.Collections;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import lombok.experimental.UtilityClass;
// import org.jetbrains.annotations.NotNull;
// import org.springframework.jdbc.core.BeanPropertyRowMapper;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
// import org.springframework.jdbc.core.simple.SimpleJdbcCall;
//
// @UtilityClass
// public class CallStoredRepositoryHelpers {
//
//  public static void execute(JdbcTemplate jdbcTemplate, Map<String, Object> mapParams,
//      String procedureName) {
//    SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
//        .withProcedureName(procedureName);
//    MapSqlParameterSource parameters = new MapSqlParameterSource();
//    if (mapParams != null && !mapParams.isEmpty()) {
//      mapParams.forEach(parameters::addValue);
//    }
//    simpleJdbcCall.execute(parameters);
//  }
//
//  public static <T> List<T> executeReturnCollection(
//      JdbcTemplate jdbcTemplate,
//      Map<String, Object> mapParams,
//      String procedureName,
//      String resultKey,
//      Class<T> mappedClass) {
//    Map<String, Object> rsMap = execute(jdbcTemplate, mapParams, procedureName, resultKey,
//        mappedClass);
//    return Optional.ofNullable(rsMap.get(resultKey))
//        .filter(List.class::isInstance)
//        .map(list -> (List<T>) list)
//        .orElse(Collections.emptyList());
//  }
//
//  public static <T> Optional<T> executeReturnSingle(
//      JdbcTemplate jdbcTemplate,
//      Map<String, Object> mapParams,
//      String procedureName,
//      String resultKey,
//      Class<T> mappedClass) {
//    var rs = executeReturnCollection(jdbcTemplate, mapParams, procedureName, resultKey,
//        mappedClass);
//    return Optional.ofNullable(rs)
//        .filter(ls -> !ls.isEmpty())
//        .map(ls -> ls.get(0));
//  }
//
//  /**
//   * @return The execute method will always return a non-null Map. It may be empty if the
// procedure
//   * returns nothing, but never null.
//   */
//  @NotNull
//  private static <T> Map<String, Object> execute(
//      JdbcTemplate jdbcTemplate,
//      Map<String, Object> mapParams,
//      String procedureName,
//      String resultKey,
//      Class<T> mappedClass) {
//    MapSqlParameterSource parameters = new MapSqlParameterSource();
//    if (mapParams != null && !mapParams.isEmpty()) {
//      mapParams.forEach(parameters::addValue);
//    }
//    SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
//        .withProcedureName(procedureName)
//        .returningResultSet(resultKey, new BeanPropertyRowMapper<>(mappedClass));
//    return simpleJdbcCall.execute(parameters);
//  }
// }
