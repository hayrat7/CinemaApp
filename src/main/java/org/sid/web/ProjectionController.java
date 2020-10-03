package org.sid.web;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.sid.dao.FilmRepository;
import org.sid.dao.PlaceRepository;
import org.sid.dao.ProjectionRepository;
import org.sid.dao.SalleRepository;
import org.sid.dao.SeanceRepository;
import org.sid.dao.TicketRepository;
import org.sid.entities.Categorie;
import org.sid.entities.Cinema;
import org.sid.entities.Film;
import org.sid.entities.Place;
import org.sid.entities.Projection;
import org.sid.entities.Salle;
import org.sid.entities.Ticket;
import org.sid.entities.Ville;
import org.sid.entities.Seance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProjectionController {
	
	@Autowired
	ProjectionRepository projectionRepository;
	@Autowired
	SalleRepository salleRepository;
	@Autowired
	FilmRepository filmRepository;
	@Autowired 
	SeanceRepository seanceRepository;
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	PlaceRepository placeRepository;
	@PostMapping(path = "/saveProjection")
	public String saveCinema( Projection projection, Model model) {
		
		projectionRepository.save(projection);
		projection.getSalle();
		int nbrPlace= projection.getSalle().getNbrPlaces();
		for(int i = 1 ;i<=nbrPlace;i++) {
			Place place  = new Place(null,i,0, 0, 0, projection.getSalle(),null);
			placeRepository.save(place);
			
			
			ticketRepository.save(new Ticket(null,null,projection.getPrix(),null,false,place,projection));
		}
		return"redirect:/addProjection";
	}
	
	
	@RequestMapping(value = "/addProjection", method = RequestMethod.GET)
	public String formProjection(Model model) {
		model.addAttribute("projection", new Projection());
        
		List<Salle> listSalles = salleRepository.findAll();
		model.addAttribute("salles", listSalles);
		List<Film> listFilms = filmRepository.findAll();
		model.addAttribute("films", listFilms);
		List<Seance> listSeances = seanceRepository.findAll();
		model.addAttribute("seances", listSeances);

		return "projection/addProjection";
	}
	@GetMapping(path = "/projections")
	public String list(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "7") int size,
			@RequestParam(name = "keyword", defaultValue = "0") Long motCle) {
		Page<Projection> pageProjections = projectionRepository.findBydoubleContains(motCle, PageRequest.of(page, size));
//	    List<Projection> projections = projectionRepository.findAll();
		model.addAttribute("projections", pageProjections);
		model.addAttribute("pages", new int[pageProjections.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("keyword", motCle);
		return "projection/projectionList";
	}
	@GetMapping(path = "/deleteProjection")
	public String delete(Long id) {
		projectionRepository.deleteById(id);
		return "redirect:/projections";
	}
	

}
