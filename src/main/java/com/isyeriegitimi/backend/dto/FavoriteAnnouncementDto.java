package com.isyeriegitimi.backend.dto;

import com.isyeriegitimi.backend.model.Announcement;
import com.isyeriegitimi.backend.model.Student;
import lombok.Data;

import java.util.UUID;

@Data
public class FavoriteAnnouncementDto {
    private UUID favoriteId;

    private Student student;

    private Announcement announcement;

}
