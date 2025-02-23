package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FileInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface FileInfoRepository extends JpaRepository<FileInfo, UUID> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO file_info (id,file_name, file_type, owners, signed_by, data, barcode_number) " +
            "VALUES (:id,:fileName, :fileType, CAST(:owners AS jsonb), CAST(:signedBy AS jsonb), :data, :barcodeNumber)",
            nativeQuery = true)
    void insertFile(@Param("id") UUID id,
            @Param("fileName") String fileName,
                    @Param("fileType") String fileType,
                    @Param("owners") String owners,
                    @Param("signedBy") String signedBy,
                    @Param("data") String data,
                    @Param("barcodeNumber") String barcodeNumber);

}