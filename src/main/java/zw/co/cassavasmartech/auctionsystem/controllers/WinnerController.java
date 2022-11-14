package zw.co.cassavasmartech.auctionsystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.Winner;
import zw.co.cassavasmartech.auctionsystem.repository.WinnerRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.WinnerService;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/winner")
@RequiredArgsConstructor
public class WinnerController {


    private final WinnerService winnerService;



    @GetMapping("winner")
    public String multiplePage(Model model) {
        return "winner";
    }


    @GetMapping("multiple")
    public String getWinners(Model model) {
        model.addAttribute("winners", winnerService.findAllWinners());
        return "multiple";
    }


    @GetMapping("winner/{id}")
    public String viewAsset(@PathVariable("id") Long id, Model model) {
        Winner winner = winnerService.findById(id).orElseThrow(() -> new RuntimeException("Asset not found for ID: " + id));
        model.addAttribute("winner", winner);
        return "winner";
    }



    @GetMapping("/{name}")
    public List<Winner> getWinnerByUsername(@PathVariable("name") String username){
        return winnerService.findByUsername(username);
    }


}
