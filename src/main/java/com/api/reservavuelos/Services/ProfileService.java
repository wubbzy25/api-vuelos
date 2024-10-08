package com.api.reservavuelos.Services;

import com.api.reservavuelos.DTO.Response.ProfileDTO;
import com.api.reservavuelos.Exceptions.UserNotFoundException;
import com.api.reservavuelos.Models.Profile_image;
import com.api.reservavuelos.Models.Usuarios;
import com.api.reservavuelos.Repositories.AuthRepository;
import com.api.reservavuelos.Repositories.ProfileImageRepository;
import com.api.reservavuelos.Security.JwtTokenProvider;
import com.api.reservavuelos.Security.getTokenForRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileService {

    private final CloudinaryService cloudinaryService;
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final getTokenForRequest getTokenForRequest;
    private final ProfileImageRepository profileImageRepository;

    private static final String JPEG = "image/jpeg";
    private static final String PNG = "image/png";

    @Autowired
    public ProfileService(CloudinaryService cloudinaryService, AuthRepository authRepository, JwtTokenProvider jwtTokenProvider, getTokenForRequest getTokenForRequest, ProfileImageRepository profileImageRepository) {
        this.cloudinaryService = cloudinaryService;
        this.authRepository = authRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.getTokenForRequest = getTokenForRequest;
        this.profileImageRepository = profileImageRepository;
    }

    public ProfileDTO getProfile(Long id) {
        Usuarios usuario = authRepository.getById(id);
       String nombre = usuario.getPrimer_nombre() + usuario.getSegundo_nombre() + usuario.getPrimer_apellido() + usuario.getSegundo_apellido();
        ProfileDTO profile = new ProfileDTO();
        profile.setUrl_imagen(usuario.getProfile_image().getImage_url());
        profile.setNombre(nombre);
        profile.setEmail(usuario.getEmail());
        profile.setTelefono(usuario.getTelefono());
        profile.setFecha_nacimiento(usuario.getFecha_nacimiento());
        profile.setGenero(usuario.getGenero());
        return profile;

    }

    public void uploadImage(MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = getTokenForRequest.getToken(request, response);
        String email = jwtTokenProvider.getEmailFromToken(token);
        Usuarios usuario = authRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        validateFileType(multipartFile.getContentType());

        Map uploadResult = cloudinaryService.upload(multipartFile);
        String url = (String) uploadResult.get("url");
        String publicId = (String) uploadResult.get("public_id");

        Profile_image profileImage = profileImageRepository.getProfileImage(email);
        if (profileImage != null) {
            cloudinaryService.delete(profileImage.getPublic_id());
            profileImage.setPublic_id(publicId);
            profileImage.setImage_url(url);
            profileImageRepository.save(profileImage);
        } else {
            Profile_image  image = new Profile_image();
            image.setImage_url(url);
            image.setPublic_id(publicId);
            usuario.setProfile_image(image);
            profileImageRepository.save(image);
            authRepository.save(usuario);
        }
    }

    private void validateFileType(String contentType) {
        if (contentType == null || (!contentType.equals(JPEG) && !contentType.equals(PNG))) {
            throw new IllegalArgumentException("El tipo de archivo no es válido. Sólo se permiten imágenes en formato JPEG o PNG.");
        }
    }
}
