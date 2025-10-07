package io.hexlet.spring.controller;

import io.hexlet.spring.dto.guest.GuestCreateDTO;
import io.hexlet.spring.dto.guest.GuestDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.GuestMapper;
import io.hexlet.spring.repository.GuestRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestsController {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private GuestMapper guestMapper;

    @GetMapping(path = "")
    public List<GuestDTO> index() {
        var products = guestRepository.findAll();
        return products.stream()
                .map(p -> guestMapper.map(p))
                .toList();
    }

    @GetMapping(path = "/{id}")
    public GuestDTO show(@PathVariable long id) {

        var guest =  guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest with id " + id + " not found"));
        var guestDto = guestMapper.map(guest);
        return guestDto;
    }

    // BEGIN
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public GuestDTO create(@Valid @RequestBody GuestCreateDTO guestData) {
        var guest = guestMapper.map(guestData);
        guestRepository.save(guest);
        var guestDto = guestMapper.map(guest);
        return guestDto;
    }
    // END
}