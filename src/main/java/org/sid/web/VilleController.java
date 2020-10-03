package org.sid.web;




import javax.validation.Valid;

import org.sid.dao.VilleRepository;
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
public class VilleController {
	@Autowired
	private VilleRepository villeRepo;
	@GetMapping(path = "/allVilles")
	public String index(Model model,
			@RequestParam(name = "page",defaultValue = "0")int page,
			@RequestParam(name = "size",defaultValue = "6")int size,
			@RequestParam(name = "keyword",defaultValue = "")String mc
			) {
		Page<Ville> villes=villeRepo.findByNameContains(mc, PageRequest.of(page, size));
				model.addAttribute("villes", villes);
		model.addAttribute("pages",new int[ villes.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("keyword", mc);
		model.addAttribute("size", size);
		return "ville/villeList";
	}
	@GetMapping(path = "/deleteVille")
	public String delete(Long id) {
		villeRepo.deleteById(id);
		return "redirect:/villes";
		
	}
	@GetMapping(path = "/addVille")
	public String add(Model model) {
		model.addAttribute("ville", new Ville());
		return "ville/addVille";
	}
	@PostMapping(path = "/saveVille")
	public String saveCinema(@Valid Ville ville,BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "ville/addVille";
		}
		villeRepo.save(ville);
		return"redirect:/addVille";
		
	}
	@GetMapping(path = "/editVille")
	public String edit( Long id,Model model) {
		Ville v=villeRepo.findById(id).get();
		model.addAttribute("ville", v);
		return "ville/editVille";
	}
}
