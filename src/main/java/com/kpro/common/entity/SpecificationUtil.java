// package com.kpro.common.entity;
//
// import org.springframework.data.jpa.domain.Specification;
//
// import java.util.Collection;
// import java.util.List;
// import java.util.Objects;
// import java.util.stream.Collectors;
//
// public final class SpecificationUtil {
//    private SpecificationUtil() {
//        throw new IllegalStateException("Utility class");
//    }
//
//    public static <T> Specification<T> buildANDSpecification(Collection<Specification<T>>
// conditions) {
//        List<Specification<T>> nullFilteredConditionList =
// conditions.stream().filter(Objects::nonNull).collect(Collectors.toList());
//        if (nullFilteredConditionList.isEmpty()) {
//            return null;
//        }
//
//        Specification<T> spec = Specification.where(nullFilteredConditionList.get(0));
//
//        for(int i = 1; i < nullFilteredConditionList.size(); ++i) {
//            spec = spec.and(nullFilteredConditionList.get(i));
//        }
//
//        return spec;
//    }
// }
