package com.chewy.osn.response;

import com.chewy.osn.domain.Species;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CutenessResponse {
    private Species species;
    private List<String> names = new ArrayList<>();
    private Integer totalCuteness = 0;
}
