package org.sid.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.sid.dao.CategorieRepository;
import org.sid.dao.CinemaRepository;
import org.sid.dao.FilmRepository;
import org.sid.dao.PlaceRepository;
import org.sid.dao.ProjectionRepository;
import org.sid.dao.SalleRepository;
import org.sid.dao.SeanceRepository;
import org.sid.dao.TicketRepository;
import org.sid.dao.VilleRepository;
import org.sid.entities.Categorie;
import org.sid.entities.Cinema;
import org.sid.entities.Film;
import org.sid.entities.Place;
import org.sid.entities.Projection;
import org.sid.entities.Salle;
import org.sid.entities.Seance;
import org.sid.entities.Ticket;
import org.sid.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CinemaServiceImpl implements IcinemaInitService {
	@Autowired
	private VilleRepository villeRepo;
	@Autowired
	private CinemaRepository cinemaRepo;
	@Autowired
	private SalleRepository salleRepo;
	@Autowired
	private PlaceRepository placeRepo;
	@Autowired
	private SeanceRepository seanceRepo;
	@Autowired
	private FilmRepository filmRepo;
	@Autowired
	private ProjectionRepository projectionRepo;
	@Autowired
	private TicketRepository ticketRepo;
	@Autowired
	private CategorieRepository categorieRepo;

	@Override
	public void initVilles() {
		Stream.of("Casablanca", "Tanger", "Marrakech", "Rabat").forEach(nameVille -> {
			Ville ville = new Ville();
			ville.setName(nameVille);
			villeRepo.save(ville);
		});
	}

	@Override
	public void initCinemas() {
		villeRepo.findAll().forEach(v -> {
			Stream.of("MegaRama", "Imax", "Founon", "Saada").forEach(nameCinema -> {
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setVille(v);
				cinema.setNbrSalles(3 + (int) (Math.random() * 7));
				cinemaRepo.save(cinema);
				
			});
		});
	}

	@Override
	public void initSalles() {
		cinemaRepo.findAll().forEach(cinema -> {
			for (int i = 0; i < cinema.getNbrSalles(); i++) {
				Salle salle = new Salle();
				salle.setName("Salle " + i);
				salle.setCinema(cinema);
				salle.setNbrPlaces(15 + (int) (Math.random() * 20));
				salleRepo.save(salle);
			}
		});

	}

	@Override
	public void initPlaces() {
		salleRepo.findAll().forEach(salle -> {
			for (int i = 0; i < salle.getNbrPlaces(); i++) {
				Place place = new Place();
				place.setNumero(i + 1);
				place.setSalle(salle);
				placeRepo.save(place);
			}
		});

	}

	@Override
	public void initSeances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("12:00", "15:00", "17:00", "19:00", "21:00").forEach(s -> {
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepo.save(seance);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void initCategories() {
		Stream.of("Histoire", "Actions", "Fiction", "Drama").forEach(cat -> {
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepo.save(categorie);
		});
	}

	@Override
	public void initFilms() {
		double[] durees = new double[] { 1, 1.5, 2, 2.5, 3 };
		List<Categorie> categories = categorieRepo.findAll();
		Stream.of("the shawshank redemption", "A Beautifulmind", "Cast Away", "The God Father", "Zodiac")
				.forEach(titreFilm -> {
					Film film = new Film();
					film.setTitre(titreFilm);
					film.setDuree(durees[new Random().nextInt(durees.length)]);
					film.setPhoto(titreFilm.replaceAll(" ", "") + ".jpg");
					film.setCategorie(categories.get(new Random().nextInt(categories.size())));
					filmRepo.save(film);
				});

	}

	@Override
	public void initProjections() {
		double[] prices = new double[] { 30, 50, 60, 70, 90, 100 };
		List<Film>films=filmRepo.findAll();
		villeRepo.findAll().forEach(ville -> {
			ville.getCinemas().forEach(cinema -> {
				cinema.getSalles().forEach(salle -> {
					int index=new Random().nextInt(films.size());
					Film film=films.get(index);
						seanceRepo.findAll().forEach(seance -> {
							Projection projection = new Projection();
							projection.setDateDeProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepo.save(projection);
						});
				});
			});
		});
	}

	@Override
	public void initTickets() {
		projectionRepo.findAll().forEach(p -> {
			p.getSalle().getPlaces().forEach(place -> {
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepo.save(ticket);
			});
		});
	}

}
