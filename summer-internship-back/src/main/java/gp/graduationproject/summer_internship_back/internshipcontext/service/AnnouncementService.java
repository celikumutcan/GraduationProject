package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Announcement;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public Announcement saveAnnouncement(Announcement announcement) {
        Announcement saved = announcementRepository.save(announcement);
        System.out.println("Saved Announcement ID: " + saved.getId()); // Debug log
        return saved;
    }

    public boolean deleteAnnouncement(Integer id) {
        announcementRepository.deleteById(id);
        return false;
    }

    public Optional<Announcement> findById(Integer id) {
        return announcementRepository.findById(id);
    }
}

