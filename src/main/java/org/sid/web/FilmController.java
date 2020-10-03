package org.sid.web;


import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.sid.dao.CategorieRepository;
import org.sid.dao.FilmRepository;
import org.sid.entities.Categorie;
import org.sid.entities.Cinema;
import org.sid.entities.Film;
import org.sid.entities.Salle;
import org.sid.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@Controller


@CrossOrigin("*")
public class FilmController {
	
	

	@Autowired
	FilmRepository filmRepository;
	@Autowired
	CategorieRepository categorieRepository;
	
	@GetMapping(path = "/films")
	public String index(Model model,
			@RequestParam(name = "page",defaultValue = "0")int page,
			@RequestParam(name = "size",defaultValue = "6")int size,
			@RequestParam(name = "keyword",defaultValue = "")String mc
			) {
		Page<Film> films=filmRepository.findByTitreContains(mc, PageRequest.of(page, size));
				model.addAttribute("films", films);
		model.addAttribute("pages",new int[ films.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("keyword", mc);
		model.addAttribute("size", size);
		return "films/filmList";
	}
	
	@RequestMapping(value = "/saveFilm", method = RequestMethod.POST)
	public String saveFilm(Film film, @RequestParam(name = "picture") MultipartFile file) throws Exception {
		
		
		
		if (!(file.isEmpty())) {
			film.setPhoto(file.getOriginalFilename());
			file.transferTo(new File(System.getProperty("user.home")+"/cinema/images/"+file.getOriginalFilename()));
			
		}
		filmRepository.save(film);
	
		return "redirect:/films";
	}
	//@GetMapping(path ="/filmPicture/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
	@RequestMapping(value="/filmPicture", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] image(Long id) throws Exception{
		Film f=filmRepository.findById(id).get();
		String photoName=f.getPhoto();
		File file=new File(System.getProperty("user.home")+"/cinema/images/"+photoName);
		Path path=Paths.get(file.toURI());
		return Files.readAllBytes(path);
	}

	@RequestMapping(value = "/addFilm", method = RequestMethod.GET)
	public String formFilm(Model model) {
		model.addAttribute("film", new Film());
        
		List<Categorie> list = categorieRepository.findAll();
		model.addAttribute("categories", list);

		return "films/addFilm";
	}
//	@GetMapping(value = "/allfilms" )
//	public String list(Model model) {
//		List<Film> list = filmRepository.findAll();
//		model.addAttribute("films", list);
//		return "films/films";
//		
//	}
	@GetMapping(path = "/deleteFilm")
	public String delete(Long id) {
		filmRepository.deleteById(id);
		return "redirect:/films";
	}
	@GetMapping(path = "/editFilm")
	public String edit( Long id,Model model) {
		Film f=filmRepository.findById(id).get();
		model.addAttribute("nbrSalle", new int[30]);
		List<Categorie> category =categorieRepository.findAll();
		model.addAttribute("categories", category);
		model.addAttribute("film", f);
		
		return "films/editFilm";
	}
}
