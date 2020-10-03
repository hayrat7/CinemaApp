package org.sid.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
	@RequestMapping(value = "/")
	public String redirect() {
		return "redirect:/films";
	}
	@RequestMapping(value = "/403")
	public String error() {
		return "403";
	}
	@RequestMapping(value = "/index")
	public String index() {
		return "films";
	}
}
