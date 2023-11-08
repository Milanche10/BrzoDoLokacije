package com.look4.demo;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import com.cloudinary.utils.ObjectUtils;
import com.look4.demo.security.JWTFilter;
import com.look4.demo.security.JwtUtil;
import com.look4.demo.security.PasswordEncoder;
import com.look4.demo.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

public class Look4Application {

	public static void main(String[] args) {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", "dgra88cxm", // insert here you cloud name
				"api_key", "255914247957623", // insert here your api code
				"api_secret", "xHH8kISDAQtrHbPxZS6bqrOsUms")); // insert here your api secret
		SingletonManager manager = new SingletonManager();
		manager.setCloudinary(cloudinary);
		manager.init();

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.refresh();

		SpringApplication.run(Look4Application.class, args);

	}

}
