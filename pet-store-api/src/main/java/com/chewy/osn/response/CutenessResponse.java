package com.chewy.osn.response;

import com.chewy.osn.domain.Species;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CutenessResponse {
    private Species species;
    private List<String> names = new ArrayList<>();
    private Integer totalCuteness = 0;
}
