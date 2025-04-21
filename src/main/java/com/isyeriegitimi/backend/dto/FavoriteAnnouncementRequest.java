package com.isyeriegitimi.backend.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class FavoriteAnnouncementRequest {

    private UUID favoriteId;
    private UUID announcementId;
    private UUID studentId;

}
