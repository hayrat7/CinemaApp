package org.sid;

import org.sid.dao.SalleRepository;
import org.sid.entities.Film;
import org.sid.entities.Salle;
import org.sid.entities.Ticket;
import org.sid.services.IcinemaInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CinemaProjectApplication implements CommandLineRunner {
	@Autowired
	private IcinemaInitService cinemaInitService;
	@Autowired
	private RepositoryRestConfiguration restConfig;
	
	
	public static void main(String[] args) {
		SpringApplication.run(CinemaProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		restConfig.exposeIdsFor(Film.class,Salle.class,Ticket.class);
		cinemaInitService.initVilles();
		cinemaInitService.initCinemas();
		cinemaInitService.initSalles();
		cinemaInitService.initPlaces();
		cinemaInitService.initSeances();
		cinemaInitService.initCategories();
		cinemaInitService.initFilms();
		cinemaInitService.initProjections();
		cinemaInitService.initTickets();
		
		
	}

}
