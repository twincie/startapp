package com.example.startapp;

import com.example.startapp.entity.Role;
import com.example.startapp.entity.Users;
import com.example.startapp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
//@ComponentScan(basePackages = "com.example.startup.repository")
public class StartappApplication implements CommandLineRunner {

	@Autowired
	private UsersRepository usersRepository;



	public static void main(String[] args) {
		SpringApplication.run(StartappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Users adminAccount = usersRepository.findByRole(Role.ADMIN);
		if(null == adminAccount){
			Users users = new Users();

			users.setEmail("admin@gmail.com");
			users.setName("admin");
			users.setRole(Role.ADMIN);
			users.setPassword(new BCryptPasswordEncoder().encode("admin"));
			usersRepository.save(users);
		}
	}
}
