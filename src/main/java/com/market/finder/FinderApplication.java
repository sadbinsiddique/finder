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
			// createInstructor(appDAO);
			//findInstructor(appDAO);
			//deleteInstructor(appDAO);
			//findInstructorDetail(appDAO);
			deleteInstructorDetail(appDAO);
		};
	}

	private void deleteInstructorDetail(AppDAO appDAO) {
		int theId = 1;
		System.out.println("Deleting instructor detail id: " + theId);
		appDAO.deleteInstructorDetailById(theId);
		System.out.println("Done");
	}

	private void findInstructorDetail(AppDAO appDAO) {
		int theId = 4;
		InstructorDetail tempInstructorDetail = appDAO.findInstructorDetailById(theId);
		System.out.println("tempInstructorDetail: " + tempInstructorDetail);
		System.out.println("the associated instructor: " + tempInstructorDetail.getInstructor());
		System.out.println("Done!");
	}

	private void deleteInstructor(AppDAO appDAO) {
		int theId = 9;
		System.out.println("Delete Instructor id: " + theId);
		appDAO.deleteInstructorById(theId);
		System.out.println("Done!");
	}

	private void findInstructor(AppDAO appDAO) {
		int theId = 10;
		System.out.println("Finding instructor with id: " + theId);

		Instructor tempInstructor = appDAO.findInstructorById(theId);

		System.out.println("Temp instructor: " + tempInstructor);
		if (tempInstructor != null) {
			System.out.println("The Associated instructorDetail: " + tempInstructor.getInstructorDetail());
		} else {
			System.out.println("Instructor not found with id: " + theId);
		}
	}

	private void createInstructor(AppDAO appDAO) {


		// create the instructor
		Instructor tempInstructor = new Instructor("sadbinsiddique@gmail.com", "sad", "bin siddique");

		// create the instructor detail
		InstructorDetail tempInstructorDetail = new InstructorDetail(36, "dad@gmail.com");

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



