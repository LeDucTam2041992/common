package com.kpro.common.dto.base;

import com.kpro.common.utils.KproModelMapper;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;

@UtilityClass
public class PageUtils {
    private static final ModelMapper mapper = KproModelMapper.getInstance();
    public static <T> SearchResult<T> toCustomPage(Page<T> page) {
        return mapper.map(page, new TypeToken<SearchResult<T>>() {}.getType());
    }
}
