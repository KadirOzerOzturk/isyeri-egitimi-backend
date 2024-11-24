package com.isyeriegitimi.backend.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPhotosDto {
    private String postId;
    private String photo;

}
