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
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO file_info (id,file_name, file_type, owners, data, barcode_number,upload_date,delete_date) " +
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
    @Modifying
    @Transactional
    @Query(value = "UPDATE file_info SET file_name = :fileName, file_type = :fileType, owners = CAST(:owners AS jsonb), data = :data, barcode_number = :barcodeNumber ,upload_date = :uploadDate,delete_date = :deleteDate WHERE id = :id",
            nativeQuery = true)
    void updateFile(@Param("id") UUID id,
                    @Param("fileName") String fileName,
                    @Param("fileType") String fileType,
                    @Param("owners") String owners,
                    @Param("data") String data,
                    @Param("barcodeNumber") String barcodeNumber,
                    @Param("uploadDate") Date uploadDate,
                    @Param("deleteDate") Date deleteDate);

    @Query(value = """
            SELECT
                                         *\s
                                     FROM
                                         file_info\s
                                     WHERE
                                         EXISTS (
                                             SELECT
                                                 1    \s
                                             FROM
                                                 jsonb_array_elements(owners) AS owner    \s
                                             WHERE
                                                 owner @> CAST(? AS jsonb)
                                         )
                                         AND file_name = ?
            """, nativeQuery = true)
    Optional<FileInfo> findByOwnersContainingAndFileName(@Param("owner") String owner, @Param("fileName") String fileName);
    @Query(value = """
        SELECT * 
        FROM file_info 
        WHERE owners @> CAST(:ownerRoleJson AS jsonb)
        """, nativeQuery = true)
    List<FileInfo> findByOwner(@Param("ownerRoleJson") String ownerRoleJson);

    List<FileInfo> findAllByFileName(String  fileName);

    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM file_info
    WHERE owners @> CAST(CONCAT('[{\"id\": \"', :ownerId, '\"}]') AS jsonb)
    """, nativeQuery = true)
    void deleteByOwnerId(@Param("ownerId") String ownerId);


}