package com.isyeriegitimi.backend.repository;

import com.isyeriegitimi.backend.model.FileLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.UUID;

public interface FileLogRepository extends JpaRepository<FileLog, UUID> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO file_log (id,file_name, file_type, owners, data, barcode_number,upload_date,delete_date) " +
            "VALUES (:id,:fileName, :fileType, CAST(:owners AS jsonb), :data, :barcodeNumber,:uploadDate,:deleteDate)",
            nativeQuery = true)
    void insertFile(@Param("id") UUID id,
                    @Param("fileName") String fileName,
                    @Param("fileType") String fileType,
                    @Param("owners") String owners,
                    @Param("data") String data,
                    @Param("barcodeNumber") String barcodeNumber,
                    @Param("uploadDate") Date uploadDate,
                    @Param("deleteDate") Date deleteDate);
}
