package com.RestApi.RestfulApi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    @NotEmpty //FOR VALIDATION
    private String postTitle;
    private String content;
}
