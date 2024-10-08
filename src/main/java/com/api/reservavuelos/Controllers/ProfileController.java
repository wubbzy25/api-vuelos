package com.api.reservavuelos.Controllers;

import com.api.reservavuelos.DTO.Response.ProfileDTO;
import com.api.reservavuelos.DTO.Response.ResponseDTO;
import com.api.reservavuelos.Services.ProfileService;
import com.api.reservavuelos.Utils.DateFormatter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;


@RequestMapping("api/v1/profile")
@RestController
public class ProfileController {
    private final DateFormatter dateFormatter;
    private final ProfileService profileService;

    @Autowired
    public ProfileController(DateFormatter dateFormatter, ProfileService profileService) {
        this.dateFormatter = dateFormatter;
        this.profileService = profileService;
    }
    private final Date tiempoactual = new Date();

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id){
        System.out.println("a");
        return new ResponseEntity<>(profileService.getProfile(id), HttpStatus.OK);

    }

    @PostMapping("/upload-image")
    public ResponseEntity<ResponseDTO> getProfileImage(@RequestParam("Image") MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
            profileService.uploadImage(multipartFile, request, response);
        return new ResponseEntity<>(new ResponseDTO(dateFormatter.formatearFecha(tiempoactual), "P-200", "La imagen se subio correctamente", request.getRequestURI()), HttpStatus.OK);
    }


}
