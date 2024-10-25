package com.api.reservavuelos.Repositories;

import com.api.reservavuelos.Models.Profile_image;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<Profile_image, Long> {
    @Query(
            value = "SELECT p.id_profile_image,p.public_id, p.image_url FROM profile_image p " +
                    "INNER JOIN usuarios u " +
                    "ON p.id_profile_image = u.id_profile_image " +
                    "WHERE u.email = :email",
            nativeQuery = true
    )
    Profile_image getProfileImage(@Param("email") String email);

}
