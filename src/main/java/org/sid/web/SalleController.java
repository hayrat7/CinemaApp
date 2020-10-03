package org.sid.web;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.sid.dao.CinemaRepository;
import org.sid.dao.PlaceRepository;
import org.sid.dao.SalleRepository;
import org.sid.entities.Cinema;
import org.sid.entities.Place;
import org.sid.entities.Salle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SalleController {
	@Autowired
	private SalleRepository salleRepo;
	@Autowired
	private CinemaRepository cinemaRepo;
	@Autowired
	PlaceRepository placeRepository;
	
	Collection<Place>places[];
	
	@GetMapping(path = "/salles")
	public String index(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "6") int size,
			@RequestParam(name = "keyword", defaultValue = "") String mc) {
		Page<Salle> salle = salleRepo.findByNameContains(mc, PageRequest.of(page, size));
		model.addAttribute("salles", salle);
		model.addAttribute("pages", new int[salle.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("keyword", mc);
		model.addAttribute("size", size);
		return "salle/salleList";
	}

	@GetMapping(path = "/deleteSalle")
	public String delete(Long id) {
		salleRepo.deleteById(id);
		return "redirect:/salles";

	}

	@GetMapping(path = "/addSalle")
	public String add(Model model) {
		model.addAttribute("salle", new Salle());
		List<Cinema> cinemas=cinemaRepo.findAll();
		model.addAttribute("cinemas", cinemas);
		model.addAttribute("nbrPlaces", new int[100]);
		return "salle/addSalle";
	}

	@PostMapping(path = "/saveSalle")
	public String saveSalle(@Valid Salle salle, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			List<Cinema> cinemas=cinemaRepo.findAll();
			model.addAttribute("cinemas", cinemas);
			model.addAttribute("nbrPlaces", new int[100]);
			return "salle/addSalle";
		}
		salleRepo.save(salle);
		
		
		return "redirect:/salles";

	}

	@GetMapping(path = "/editSalle")
	public String edit(Long id, Model model) {
		Salle s = salleRepo.findById(id).get();
		model.addAttribute("salle", s);
		List<Cinema> cinemas=cinemaRepo.findAll();
		model.addAttribute("cinemas", cinemas);
		model.addAttribute("nbrPlaces", new int[100]);
		return "salle/editSalle";
	}

}
