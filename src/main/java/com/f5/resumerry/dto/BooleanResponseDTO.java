package com.f5.resumerry.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BooleanResponseDTO {
    private Boolean result;
}
