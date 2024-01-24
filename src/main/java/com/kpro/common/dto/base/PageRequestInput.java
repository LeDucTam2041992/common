package com.kpro.common.dto.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageRequestInput {
  @Min(1)
  @Max(1000)
  @Schema(example = "10", defaultValue = "10", required = true)
  private Integer pageSize;

  @Schema(example = "0", defaultValue = "0", required = true)
  @Min(0)
  private Integer pageNo;

  @Pattern(regexp = "^[a-z0-9]+:(asc|desc)$", flags = Pattern.Flag.CASE_INSENSITIVE)
  @Schema(example = "createdAt:desc", defaultValue = "createdAt:desc")
  private String sort;

  public Pageable pageable() {
    var orders = this.sort();
    return org.springframework.data.domain.PageRequest.of(
            this.pageNo, this.pageSize, Sort.by(orders));
  }

  public List<Sort.Order> sort() {
    List<Sort.Order> orders = new ArrayList<>();

    if(this.sort!= null) {
      String[] part = this.sort.split("_");
      for (String s : part) {
        String[] tmp = s.split(":");
        if (tmp.length == 2) {
          orders.add(new Sort.Order(Sort.Direction.fromString(tmp[1]), tmp[0]));
        }
      }
    }
    return orders;
  }
}
