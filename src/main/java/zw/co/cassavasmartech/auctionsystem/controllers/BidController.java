package zw.co.cassavasmartech.auctionsystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.cassavasmartech.auctionsystem.forms.BidForm;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.Bid;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.AssetService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.BidService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.CategoryService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;

import javax.validation.Valid;
import java.util.Optional;

@Controller

@Slf4j
@RequestMapping("/bids")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;
    private final CategoryService categoryService;
    private final PersonService personService;
    private final AssetService assetService;
    //CRUD

    @GetMapping
    public String getBids(@AuthenticationPrincipal Person person,Model model) {
        Person user = personService.findByUsername(person.getUsername()).orElseThrow(() -> new RuntimeException("Person not found"));


        model.addAttribute("authUser", person);
        model.addAttribute("bids", bidService.findAllByUser((User)user));
        return "all_bids";
    }

    @GetMapping("/active")
    public String activeBids(@AuthenticationPrincipal Person person, Model model) {

        Person user = personService.findByUsername(person.getUsername()).orElseThrow(() -> new RuntimeException("Person not found"));

        model.addAttribute("authUser", person);
        model.addAttribute("message", "Welcome " + person.getFirstName());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("bids", bidService.getActiveBids(user.getId()));


        return "active_bids";
    }

    @GetMapping("/admin")
    public String viewAdminBids(Model model) {

        model.addAttribute("bids", bidService.findAllBids());
        return "admin/admin_bids";
    }

    @GetMapping("/create/{assetId}")
    public String createBid(@PathVariable("assetId") long assetId,Model model) {

        model.addAttribute("asset", assetService.findById(assetId));
        model.addAttribute("bidForm", new BidForm());

        return "create_bid";
    }

    @PostMapping("/create")
    public String createBid(@AuthenticationPrincipal Person person, @Valid BidForm bidForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        log.info("Got create bid request");

        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().toString();
            log.info(errors);
            return configureBidPage(model, bidForm, null);
        }

        Long assetId = bidForm.getAssetId();
        Asset asset = assetService.findById(assetId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + assetId));

        final Bid bid = bidForm.getBid(asset, findUser(person.getUsername()));
        bid.setCreatedBy(person.getUsername());
        bid.setLastUpdatedBy(person.getUsername());

        final Optional<Bid> bidOptional = bidService.createBid(bid);
        log.info("Creation response successful? => {}", bidOptional.isPresent());
        return bidOptional.map(createdBid -> {
            redirectAttributes.addFlashAttribute("info", "Bid created successfully");
            return "redirect:/assets/" + createdBid.getAsset().getId();
        }).orElse(configureBidPage(model, bidForm, "Could not create bid, try again later"));
    }

    private User findUser(String username) {
        return personService.findByUsername(username).map(person -> (User) person).orElse(null);
    }

    private String configureBidPage(Model model, BidForm bidForm, String errors) {
        model.addAttribute("errors", errors);
        model.addAttribute("bidForm", bidForm);
        return "create_bid";
    }

}
