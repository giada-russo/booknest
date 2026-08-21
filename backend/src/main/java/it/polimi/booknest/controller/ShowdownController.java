package it.polimi.booknest.controller;

import it.polimi.booknest.service.ShowdownService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/showdown")
public class ShowdownController {
    final ShowdownService showdownService;

    public ShowdownController(ShowdownService showdownService) {
        this.showdownService = showdownService;
    }


}
