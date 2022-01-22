package br.com.animal.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping
@RestController
public class MainController {

	@GetMapping("/")
	public RedirectView main() {

 	RedirectView redirectView = new RedirectView();
 	redirectView.setUrl("/docs.html");
 	return redirectView;
		
	}
}
