package com.cookBook.CookBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CookBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookBookApplication.class, args);
	}

}


/** !!!!!!!!!!!!!!!!!!!!INFO!!!!!!!!!!!!!!!!!!
 *
 * After first run use SQL in DB:
 * INSERT INTO recipes.category (name)
 * VALUES ('breakfast'), ('lunch'), ('dinner'), ('snack');
 * */