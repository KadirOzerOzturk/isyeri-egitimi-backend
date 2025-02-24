package com.isyeriegitimi.backend;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.isyeriegitimi.backend.aws.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@SpringBootApplication
@Slf4j
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
//	@Bean
//	public ApplicationRunner applicationRunner(S3Service s3Service) {
//		return args -> {
//			log.info("Spring Boot AWS S3 integration...");
//
//			try {
//				var s3Object = s3Service.getFile("jvm.png");
//				log.info(s3Object.getKey());
//			} catch (AmazonS3Exception e) {
//				log.error(e.getMessage());
//			}
//		};
//	}

}
