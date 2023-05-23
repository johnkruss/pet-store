package com.chewy.osn.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePetRequest {
    private String petName;
    private Integer cuteness;
}
