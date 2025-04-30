package internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Announcement;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AnnouncementRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.AnnouncementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit test for AnnouncementService.
 * Tests the saveAnnouncement() method using mocked repository.
 */
@ExtendWith(MockitoExtension.class)
public class AnnouncementServiceTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private AnnouncementService announcementService;

    /**
     * Test if saveAnnouncement() returns the saved announcement correctly.
     */
    @Test
    public void testSaveAnnouncement_ReturnsSavedAnnouncement() {
        // Arrange
        Announcement announcement = new Announcement();
        announcement.setContent("Important Announcement");

        Announcement saved = new Announcement();
        saved.setId(1);
        saved.setContent("Important Announcement");

        when(announcementRepository.save(announcement)).thenReturn(saved);

        // Act
        Announcement result = announcementService.saveAnnouncement(announcement);

        // Assert
        assertEquals(1, result.getId());
        assertEquals("Important Announcement", result.getContent());
    }
}