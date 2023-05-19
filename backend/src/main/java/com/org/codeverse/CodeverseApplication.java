package com.org.codeverse;

import com.org.codeverse.service.S3Service;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class CodeverseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeverseApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(S3Service s3Service) {
		return args -> {
//			testS3(s3Service);
		};
	}

	private static void testS3(S3Service s3Service) {
		s3Service.putObject("aws-mydemo", "foo", "Hello World!".getBytes());
		byte[] object = s3Service.getObject("aws-mydemo", "foo");
		System.out.println("Hooray: " + new String(object));
	}


	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.addMappings(new PropertyMap<QuestionResponseDTO, Question>() {
//			@Override
//			protected void configure() {
//				skip(destination.getTestCases());
//			}
//		});

		return modelMapper;
	}

}
