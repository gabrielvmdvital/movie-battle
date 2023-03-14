package br.ada.americanas.moviebattle.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/app/players")
public class PlayerAppController {
    private PlayerService service;

    @Autowired
    public PlayerAppController(PlayerService service) {
        this.service = service;
    }

    @GetMapping
    public String get(Model model) {
        model.addAttribute("players", service.list());
        return "player/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("player", new Player());
        return "player/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        Player player = service.findById(id).get();
        model.addAttribute("player", player);

        return "player/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        service.delete(id);
        return "redirect:/app/players";
    }
    @PostMapping
    public String save(@ModelAttribute Player player) {
        service.add(player);
        return "redirect:/app/players";
    }
}
