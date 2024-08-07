package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.Student;
import lombok.Data;

@Data
public class FavoriteAnnouncementDto {
    private Long favoriId;

    private Student ogrenci;

    private Announcement ilan;

}
