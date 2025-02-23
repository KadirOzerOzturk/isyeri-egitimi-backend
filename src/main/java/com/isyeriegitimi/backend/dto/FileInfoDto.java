package com.isyeriegitimi.backend.dto;

import java.util.UUID;

public record FileInfoDto(UUID id, String fileName, String fileType, String base64Data) {}
