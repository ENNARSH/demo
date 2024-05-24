package com.assets.demo.services;

import com.assets.demo.dto.HomeDTO;
import com.assets.demo.dto.ProfileDTO;
import com.assets.demo.models.Profile;
import com.assets.demo.repository.HomeRepo;
import com.assets.demo.repository.ProfileRepo;
import com.assets.demo.repository.RoomRepo;
import com.assets.demo.utils.ProfileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private ProfileRepo profileRepo;

    @Mock
    private ProfileUtils profileUtils;

    @Mock
    private HomeRepo homeRepo;

    @Mock
    private RoomRepo roomRepo;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ProfileService profileService;

    private ProfileDTO profileDTO;

    @BeforeEach
    public void setUp() {
        profileDTO = new ProfileDTO();
        profileDTO.setUsername("testuser");
        profileDTO.setName("Test");
        profileDTO.setSurname("User");
        profileDTO.setPassword("password123");
    }

    @Test
    public void testCreateProfile() {
        when(profileRepo.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Profile createdProfile = profileService.createProfile(profileDTO);

        assertNotNull(createdProfile);
        assertEquals(profileDTO.getUsername(), createdProfile.getUsername());
        assertEquals(profileDTO.getName(), createdProfile.getName());
        assertEquals(profileDTO.getSurname(), createdProfile.getSurname());
        assertTrue(new BCryptPasswordEncoder().matches(profileDTO.getPassword(), createdProfile.getPassword()));
    }

    @Test
    public void testGetProfileById() {
        Profile profile = Profile.builder()
                .id("testuser")
                .username("testuser")
                .name("Test")
                .surname("User")
                .password("password123")
                .build();
        when(profileRepo.findById("testuser")).thenReturn(Optional.of(profile));

        Optional<Profile> foundProfile = profileService.getProfileById("testuser");

        assertTrue(foundProfile.isPresent());
        assertEquals("testuser", foundProfile.get().getId());
    }

    @Test
    public void testDeleteProfileById() {
        Profile profile = Profile.builder()
                .id("testuser")
                .username("testuser")
                .name("Test")
                .surname("User")
                .password("password123")
                .build();
        when(profileRepo.existsById("testuser")).thenReturn(true);
        when(profileRepo.findById("testuser")).thenReturn(Optional.of(profile));

        boolean isDeleted = profileService.deleteProfileById("testuser");

        assertTrue(isDeleted);
        verify(profileRepo, times(1)).delete(profile);
        verify(homeRepo, times(1)).deleteByUsernameID("testuser");
        verify(roomRepo, times(1)).deleteByUsernameID("testuser");
    }

    @Test
    public void testUpdateProfile_Success() {
        String id = "exampleId";
        List<HomeDTO> homes = new ArrayList<>();
        ProfileDTO profileDTO = new ProfileDTO("newUsername", "NewName", "NewSurname", "NewPassword", homes);
        Profile existingProfile = new Profile("oldId", "oldUsername", "OldName", "OldSurname", "OldPassword");

        when(profileRepo.findById(id)).thenReturn(Optional.of(existingProfile));
        when(profileRepo.save(any(Profile.class))).thenReturn(existingProfile); // Possiamo anche usare eq(existingProfile) al posto di any(Profile.class)

        Profile updatedProfile = profileService.updateProfile(id, profileDTO);

        assertEquals(profileDTO.getUsername(), updatedProfile.getUsername());
        assertEquals(profileDTO.getName(), updatedProfile.getName());
        assertEquals(profileDTO.getSurname(), updatedProfile.getSurname());
        assertEquals(profileDTO.getPassword(), updatedProfile.getPassword());

        verify(profileRepo, times(1)).findById(id);

        verify(profileRepo, times(1)).save(existingProfile);
    }

    @Test
    public void testUpdateProfile_ProfileNotFound() {
        String id = "nonExistentId";
        List<HomeDTO> homes = new ArrayList<>();
        ProfileDTO profileDTO = new ProfileDTO("newUsername", "NewName", "NewSurname", "NewPassword", homes);

        when(profileRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profileService.updateProfile(id, profileDTO));

        verify(profileRepo, times(1)).findById(id);

        verify(profileRepo, never()).save(any());
    }

    @Test
    public void testUpdateProfile_NullId() {
        String id = null;
        List<HomeDTO> homes = new ArrayList<>();

        ProfileDTO profileDTO = new ProfileDTO("newUsername", "NewName", "NewSurname", "NewPassword", homes);

        assertThrows(IllegalArgumentException.class, () -> profileService.updateProfile(id, profileDTO));

        verify(profileRepo, never()).findById(any());

        verify(profileRepo, never()).save(any());
    }


}
