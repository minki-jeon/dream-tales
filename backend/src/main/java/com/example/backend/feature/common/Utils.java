package com.example.backend.feature.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * <pre>
 * packageName    : com.example.backend.feature.common
 * fileName       : Utils
 * author         : minki-jeon
 * date           : 2025-09-10 (수)
 * description    : 공통 유틸
 * ===========================================================
 * DATE                     AUTHOR           NOTE
 * -----------------------------------------------------------
 * 2025-09-10 (수)        minki-jeon       최초 생성
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class Utils {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/10 오후 12:14
     * description    : 파일명 생성
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/10 오후 12:14     minki-jeon         최초 생성.
     *
     * </pre>
     *
     * @return fileNameArr[2] 파일명, uuid
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    public String[] createFileName() {
        String uuid = UUID.randomUUID().toString();
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = uuid + "_" + timestamp + ".png";
        String[] fileNameArr = new String[]{fileName, uuid};
        return fileNameArr;
    }

    /**
     * <pre>
     * author         : minki-jeon
     * date           : 2025/09/10 오후 2:10
     * description    : Byte(Decoding Base64) 이미지 S3 저장
     * ===========================================================
     * DATE                     AUTHOR             NOTE
     * -----------------------------------------------------------
     * 2025/09/10 오후 12:25    minki-jeon         최초 생성.
     *
     * </pre>
     *
     * @param imageBytes
     * @param objectKey
     * @author minki-jeon
     * @version 1.0
     * @since 1.0
     */
    public void s3UploadByteImage(byte[] imageBytes, String objectKey) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType("image/png")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("이미지 업로드 실패.");
        }
    }
}
