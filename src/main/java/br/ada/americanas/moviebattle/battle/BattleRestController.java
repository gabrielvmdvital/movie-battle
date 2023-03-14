package br.ada.americanas.moviebattle.battle;

import br.ada.americanas.moviebattle.movie.Movie;
import br.ada.americanas.moviebattle.player.Player;
import br.ada.americanas.moviebattle.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/battles")
public class BattleRestController {

    private BattleService battleService;
    private PlayerService playerService;

    @Autowired
    public BattleRestController(
            BattleService battleService,
            PlayerService playerService
    ) {
        this.battleService = battleService;
        this.playerService = playerService;
    }

    @GetMapping("/create")
    public String create(Model model) {
        List<Player> players = (List<Player>) playerService.list();
        //Ordenada a lista baseado no nome do jogador
        Collections.sort(players, Comparator.comparing(Player::getName));

        model.addAttribute("players", players);
        model.addAttribute("player", new Player());
        return "battle/form";
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Iterable<Battle> list() {
        return battleService.list();
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public Battle get(@PathVariable("id") Long id) {
        return this.battleService.find(id).get();
    }

    @DeleteMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public Battle delete(@PathVariable("id") Long id) {
        return this.battleService.delete(id).get();
    }
    @PostMapping
    public String save(
            @ModelAttribute Player player,
            Model model
    ) {
        Battle battle = battleService.create(player);
        model.addAttribute("battle", battle);
        model.addAttribute("selected_movie", new Movie());
        return "battle/play";
    }

    @PostMapping("/{id}/answer")
    public String answer(
            @PathVariable("id") Long id,
            @ModelAttribute Movie selected,
            Model model
    ) {
        Battle battle = battleService.find(id).get();
        battleService.answer(battle, selected);
        boolean hit = battle.getHit();
        model.addAttribute("player", battle.getPlayer());
        return hit ? "battle/congrats" : "battle/wasted";
    }

}
