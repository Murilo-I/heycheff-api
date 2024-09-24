package br.com.heycheff.api.app.controller;

import br.com.heycheff.api.app.service.FileService;
import br.com.heycheff.api.util.constants.CacheNames;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media")
public class MediaController {

    final FileService service;

    public MediaController(FileService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Cacheable(value = CacheNames.MEDIA, unless = "#path.endsWith('.mp4')")
    public Resource getMedia(@RequestParam String path) {
        return service.getMedia(path);
    }
}
