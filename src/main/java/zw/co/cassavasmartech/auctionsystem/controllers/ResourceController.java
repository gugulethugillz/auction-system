package zw.co.cassavasmartech.auctionsystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zw.co.cassavasmartech.auctionsystem.common.AuctionSystemProperties;
import zw.co.cassavasmartech.auctionsystem.common.Utils;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.CategoryService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;
import zw.co.cassavasmartech.auctionsystem.storage.StorageService;

/**
 * Created by alfred on 14 September 2020
 */
@Slf4j
@Controller
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {
    private final AuctionSystemProperties props;
    private final ResourceLoader resourceLoader;
    private final StorageService storageService;

    @GetMapping("/images/{imageName}")
    private ResponseEntity<Resource> getUserImageFromFileSystem(@PathVariable("imageName") String imageName) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-type", Utils.guessMimeType(imageName));
        final String images = props.getImages();
        final String fullPath = "file:" + images + "/" + imageName;
        Resource resource = resourceLoader.getResource(fullPath);
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }
}
