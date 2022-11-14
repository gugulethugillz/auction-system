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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.cassavasmartech.auctionsystem.forms.AssetForm;
import zw.co.cassavasmartech.auctionsystem.forms.BidForm;
import zw.co.cassavasmartech.auctionsystem.forms.FileResourceForm;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.repository.CategoryRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.AssetService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.BidService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.CategoryService;
import zw.co.cassavasmartech.auctionsystem.storage.StorageService;

import javax.validation.Valid;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;
    private final CategoryService categoryService;
    private final BidService bidService;
    private final CategoryRepository categoryRepository;
    private final StorageService storageService;

    @GetMapping("/assets/category/{id}")
    public String displayAssetsByCategory(@AuthenticationPrincipal Person person, @PathVariable("id") Long id, Model model) {
        model.addAttribute("authUser", person);
        model.addAttribute("message", "Welcome " + person.getFirstName());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("assets", assetService.findAllAssets());
        model.addAttribute("assets", assetService.findByCategoryId(id));
        return "assets_user";
    }

    @GetMapping("/assets")
    public String displayAssets(@AuthenticationPrincipal Person person, Model model, String keyWord, RedirectAttributes
            redirectAttributes) {
        model.addAttribute("authUser", person);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("message", "Welcome " + person.getFirstName());
        model.addAttribute("categories", categoryService.findAll());
        if (keyWord != null) {
            model.addAttribute("assets", assetService.searchByKeyWord(keyWord));
            if (assetService.searchByKeyWord(keyWord).isEmpty()) {
                redirectAttributes.addFlashAttribute("info", "No Assets found like {} " + keyWord);
            }
        } else {
            model.addAttribute("assets", assetService.findAllAssets());
        }

        return "assets_user";
    }

    @GetMapping("/admin/assets")
    public String displayAssetsAdmin(@AuthenticationPrincipal Person person, Model model) {
        model.addAttribute("authUser", person);
        model.addAttribute("assets", assetService.findAllAssets());
        return "admin/assets_admin";
    }

    @GetMapping
    public String viewAssets(Model model) {
        return viewAssetsHelper(assetService.findAllAssets(), model);
    }

    @GetMapping("/named/{name}")
    public String getAssetByName(@PathVariable("name") String name, Model model) {
        return viewAssetsHelper(assetService.findByName(name), model);
    }

    private String viewAssetsHelper(List<Asset> assets, Model model) {
        model.addAttribute("assets", assets);
        return "assets_user";
    }

    //Delete Post
    @GetMapping("/admin/asset/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        if (assetService.findById(id).isPresent()) {
            try {
                assetService.deleteAssetById(id);
                redirectAttributes.addFlashAttribute("info", "Successful Delete");
                return "redirect:/admin/assets";
            } catch (Exception ex) {
                log.error("First Delete assets under the category");
                log.error(ex.getMessage());
                return "redirect:/admin/assets";
            }

        } else {
            log.error("Asset does not exist with ID : {}" + id);
            return "redirect:/admin/assets";
        }

    }

    @GetMapping("/admin/asset/edit/{id}")
    public String editAsset(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Asset asset = new Asset();
        if (!assetService.findById(id).isPresent()) {
            log.error("Asset does not exist with ID" + id);
            redirectAttributes.addFlashAttribute("Error", "Asset does not exist wih ID" + id);
            return "redirect:/admin";
        } else {
            try {
                asset = assetService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid asset Id" + id));
            } catch (IllegalArgumentException ex) {
                log.error("Invalid asset Id " + id);
                redirectAttributes.addFlashAttribute("Error", "Invalid asset");
                return "redirect:/admin";
            }
            AssetForm assetForm = new AssetForm();
            assetForm.setId(asset.getId());
            assetForm.setCategoryId(asset.getCategory().getId());
            assetForm.setName(asset.getName());
            assetForm.setDescription(asset.getDescription());
            assetForm.setBidEndDate(LocalDateTime.now());
            assetForm.setBidStartDate(LocalDateTime.now());
            assetForm.setBidStartPrice(asset.getBidStartPrice());
            model.addAttribute("assetForm", assetForm);
            return "edit_asset";
        }
    }

    @PostMapping("/admin/assets/update/{id}")
    public String updateAsset(@AuthenticationPrincipal Person person, @PathVariable("id") Long id, @Valid AssetForm assetForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("Errors", bindingResult.getAllErrors());
            log.error("Errors in form :{}", bindingResult.getAllErrors());
            model.addAttribute("assetForm", assetForm);
            return "edit_asset";
        }
        Asset assetToUpdate = new Asset();
        try {
            assetToUpdate = assetService.findById(id).orElseThrow(() -> new Exception("Asset does not exist"));
        } catch (Exception ex) {
            log.error("Asset does not exist with D: " + id);
            redirectAttributes.addFlashAttribute("error", "Asset does not exist");
            return "redirect:/admin/assets";
        }
        assetToUpdate.setName(assetForm.getName());
        assetToUpdate.setDescription(assetForm.getDescription());
        //assetToUpdate.setBidStartDate(assetForm.getBidStartDate(l));
        //assetToUpdate.setBidEndDate(assetForm.getBidEndDate());
        assetToUpdate.setBidStartPrice(assetForm.getBidStartPrice());
        final Optional<Asset> assetOptional = assetService.updateAsset(assetToUpdate, "");
        if (assetOptional.isPresent()) {
            Asset updatedAsset = assetOptional.get();
            redirectAttributes.addFlashAttribute("info", "succesful update");
            log.info("Updated successfully of asset " + assetToUpdate.getName());
        } else {
            redirectAttributes.addFlashAttribute("error", "could not peform update, try again later");
            log.error("Could not Update Asset");
        }
        return "redirect:/admin/assets";
    }

    @GetMapping("/assets/{id}")
    public String viewAsset(@AuthenticationPrincipal Person person, @PathVariable("id") Long id, Model model) {
        Asset asset = assetService.findById(id).orElseThrow(() -> new RuntimeException("Asset not found for ID: " + id));
        model.addAttribute("authUser", person);
        model.addAttribute("bidForm", new BidForm());
        model.addAttribute("bids", bidService.findByAssetIdOrderByValueDesc(id));
        model.addAttribute("asset", asset);
        return "view_asset";
    }

    @GetMapping("/admin/assets/{id}")
    public String viewAssetAdmin(@AuthenticationPrincipal Person person, @PathVariable("id") Long id, Model model) {
        Asset asset = assetService.findById(id).orElseThrow(() -> new RuntimeException("Asset not found for ID: " + id));
        log.info("Asset images: {}", asset.getImageNames());
        model.addAttribute("authUser", person);
        model.addAttribute("asset", asset);
        model.addAttribute("fileResourceForm", new FileResourceForm());
        return "admin/view_asset";
    }

//    @GetMapping("/{name}")
//    public List<Asset> getAssetsByName(@PathVariable("name") String name){
//        return assetService.findAllAssetsByName(name);
//
//    }
//    @GetMapping("/create")
//    public String createAsset(Model model){
//        AssetForm assetForm =  new AssetForm();
//        model.addAttribute("assetForm",assetForm);
//        return "asset_create";
//    }

    @GetMapping("/admin/asset_create")
    public String createAsset(@AuthenticationPrincipal Person person, Model model) {
        AssetForm assetForm = new AssetForm();
        model.addAttribute("authUser", person);
        model.addAttribute("assetForm", assetForm);
        model.addAttribute("categorylist", categoryService.findAll());

        return "admin/asset_create";
    }

    @PostMapping("/admin/asset_create")
    public String createAsset(@AuthenticationPrincipal Person person, @Valid AssetForm assetForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        log.info("Got create asset request");

        if (bindingResult.hasErrors()) {
            log.error("Errors in form: {}", bindingResult.getAllErrors());
            model.addAttribute("assetForm", assetForm);
            return "redirect:/admin/assets";
        }

        final Asset asset = assetForm.getAsset(categoryRepository.findById(assetForm.getCategoryId()).orElseThrow(() -> {
            throw new RuntimeException("Category not found.");
        }));
//        final Asset asset = assetForm.getAsset(null);
        asset.setCreatedBy(person.getUsername());
        asset.setLastUpdatedBy(person.getUsername());

        if (assetForm.getImageFile() != null) {
            Path path = storageService.storeImage(assetForm.getImageFile());
            if (asset.getImageNames() == null) {
                asset.setImageNames(new ArrayList<>());
            }
            asset.getImageNames().add(path.getFileName().toString());
        }

        final Optional<Asset> assetOptional = assetService.createAsset(asset);
        if (assetOptional.isPresent()) {
            Asset createdAsset = assetOptional.get();
            redirectAttributes.addFlashAttribute("info", "Created successfully");
            return "redirect:/admin/assets";
        } else {
            //TODO delete image because asset creation has failed
            redirectAttributes.addFlashAttribute("errors", "Could not create asset, try again later");
            return "redirect:/admin/assets";
        }
    }

    @PostMapping("/admin/asset/add/image")
    public String addAssetImage(@AuthenticationPrincipal Person person, @Valid FileResourceForm fileResourceForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.error("Errors in form: {}", bindingResult.getAllErrors());
            model.addAttribute("fileResourceForm", fileResourceForm);
            return "redirect:/admin/assets/" + fileResourceForm.getId();
        }

        if (fileResourceForm.getImageFile() != null) {
            return assetService.findById(fileResourceForm.getId()).map(asset -> {
                if (asset.getImageCount() >= 10) {
                    redirectAttributes.addFlashAttribute("errors", "Could not add asset image, max reached");
                    return "redirect:/admin/assets/" + asset.getId();
                } else {
                    Path path = storageService.storeImage(fileResourceForm.getImageFile());
                    asset.addImageName(path.getFileName().toString());
                    assetService.updateAsset(asset, "");
                    redirectAttributes.addFlashAttribute("info", "Image added successfully");
                    return "redirect:/admin/assets/" + asset.getId();
                }
            }).orElseGet(() -> {
                redirectAttributes.addFlashAttribute("errors", "Could not add asset image, asset not found");
                return "redirect:/admin/assets/" + fileResourceForm.getId();
            });
        } else {
            redirectAttributes.addFlashAttribute("errors", "Could not add empty image to asset");
            return "redirect:/admin/assets/" + fileResourceForm.getId();
        }
    }

    @PostMapping("/admin/asset/delete/image/{fileName}")
    public String deleteAssetImage(@PathVariable("fileName") String fileName, Model model,
                                   RedirectAttributes redirectAttributes,
                                   @Valid FileResourceForm fileResourceForm, BindingResult bindingResult,
                                   @AuthenticationPrincipal Person person) {
        Asset asset = new Asset();
        if (assetService.findById(fileResourceForm.getId()).isPresent()) {
            try {
                asset = assetService.findById(fileResourceForm.getId()).orElseThrow(() -> new Exception("Asset does not exist"));
                assetService.updateAsset(asset, fileName);
                redirectAttributes.addFlashAttribute("info", "Image Deleted successfully");
                return "redirect:/admin/assets/" + asset.getId();
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("error", "Could not delete Asset does not exist");
                return "redirect:/admin/assets/";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Asset not present");
            return "redirect:/admin/assets/";
        }
    }
}
