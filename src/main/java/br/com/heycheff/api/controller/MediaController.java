package br.com.heycheff.api.controller;

import br.com.heycheff.api.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private FileService service;

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody Resource getMedia(@RequestParam String path) {
        return service.getMedia(path);
    }
}
