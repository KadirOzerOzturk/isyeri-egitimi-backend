package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FileInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileInfoRepository extends JpaRepository<FileInfo, UUID> {

    Optional<FileInfo> findByFileNameAndOwnerIdAndOwnerRole(String fileName, UUID ownerId, String ownerRole);
    Optional<FileInfo> findByOwnerIdAndOwnerRole(UUID ownerId, String ownerRole);
    List<FileInfo> findAllByOwnerIdAndOwnerRole(UUID userId, String userRole);
    List<FileInfo> findAllByFileName(String fileName);
    List<FileInfo> findAllByFileNameAndOwnerRole(String fileName, String ownerRole);
    void deleteByOwnerIdAndOwnerRole(UUID ownerId, String ownerRole);
    void deleteByOwnerId(UUID ownerId);

}