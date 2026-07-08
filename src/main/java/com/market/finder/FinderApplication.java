package com.market.finder;

import com.market.finder.dao.AppDAO;
import com.market.finder.entity.Instructor;
import com.market.finder.entity.InstructorDetail;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FinderApplication {
    static void main(String[] args) {
        SpringApplication.run(FinderApplication.class, args);
    }

	@Bean
	public CommandLineRunner commandLineRunner(AppDAO appDAO) {

		return runner -> {
			createInstructor(appDAO);
		};
	}


private void createInstructor(AppDAO appDAO) {


		// create the instructor
		Instructor tempInstructor = new Instructor("sadbinsiddique@gmail.com", "sad", "bin siddique");

		// create the instructor detail
		InstructorDetail tempInstructorDetail =  new InstructorDetail(36, "dad@gmail.com");

		// associate the objects
		tempInstructor.setInstructorDetail(tempInstructorDetail);

		// save the instructor
		//
		// NOTE: this will ALSO save the details object
		// because of CascadeType.ALL
		//
		System.out.println("Saving instructor: " + tempInstructor);
		appDAO.save(tempInstructor);

		System.out.println("Done!");
	}

}



