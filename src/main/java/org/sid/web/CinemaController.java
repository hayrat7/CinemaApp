package org.sid.web;



import java.util.List;

import javax.validation.Valid;

import org.sid.dao.CinemaRepository;
import org.sid.dao.VilleRepository;
import org.sid.entities.Cinema;
import org.sid.entities.Ville;
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
public class CinemaController {
	@Autowired
	private CinemaRepository cinemaRepo;
	@Autowired
	private VilleRepository villeRepo;
	@GetMapping(path = "/cinemas")
	public String index(Model model,
			@RequestParam(name = "page",defaultValue = "0")int page,
			@RequestParam(name = "size",defaultValue = "6")int size,
			@RequestParam(name = "keyword",defaultValue = "")String mc
			) {
		Page<Cinema> cinemas=cinemaRepo.findByNameContains(mc, PageRequest.of(page, size));
				model.addAttribute("cinemas", cinemas);
		model.addAttribute("pages",new int[ cinemas.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("keyword", mc);
		model.addAttribute("size", size);
		return "cinemas/cinemaList";
	}
	@GetMapping(path = "/deleteCinema")
	public String delete(Long id) {
		cinemaRepo.deleteById(id);
		return "redirect:/cinemas";
		
	}
	@GetMapping(path = "/addCinema")
	public String add(Model model) {
		model.addAttribute("cinema", new Cinema());
		model.addAttribute("nbrSalle", new int[30]);
		List<Ville> ville =villeRepo.findAll();
		model.addAttribute("villes", ville);	
		return "cinemas/addCinema";
	}
	@PostMapping(path = "/saveCinema")
	public String saveCinema(@Valid Cinema cinema,BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("nbrSalle", new int[30]);
			List<Ville> ville =villeRepo.findAll();
			model.addAttribute("villes", ville);
			return "cinemas/addCinema";
		}
		cinemaRepo.save(cinema);
		return"redirect:/addCinema";
	}
	@GetMapping(path = "/editCinema")
	public String edit( Long id,Model model) {
		Cinema c=cinemaRepo.findById(id).get();
		model.addAttribute("nbrSalle", new int[30]);
		List<Ville> ville =villeRepo.findAll();
		model.addAttribute("villes", ville);
		model.addAttribute("cinema", c);
		
		return "cinemas/editCinema";
	}
}
