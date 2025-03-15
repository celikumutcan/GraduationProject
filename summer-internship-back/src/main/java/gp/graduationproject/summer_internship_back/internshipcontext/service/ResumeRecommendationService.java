package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InternshipOfferRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ResumeRecommendationService {

    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;

    private final InternshipOfferRepository internshipOfferRepository;
    private final Path studentCvStorageLocation = Paths.get("student_cvs").toAbsolutePath().normalize();

    @Autowired
    public ResumeRecommendationService(InternshipOfferRepository internshipOfferRepository, ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository) {
        this.internshipOfferRepository = internshipOfferRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
    }

    public Set<String> extractKeywordsFromCv(String studentUsername) {
        Set<String> keywords = new HashSet<>();
        File file = new File(studentCvStorageLocation + "/" + studentUsername + "_resume.pdf");

        if (!file.exists()) {
            throw new RuntimeException("❌ CV bulunamadı: " + studentUsername);
        }

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).toLowerCase();

            List<String> keywordList = Arrays.asList("software", "backend", "frontend", "ai", "cloud", "embedded", "data science", "iot", "cybersecurity");
            for (String keyword : keywordList) {
                if (text.contains(keyword)) {
                    keywords.add(keyword);
                }
            }
            System.out.println("🔍 CV İçindeki Kelimeler: " + keywords);

        } catch (IOException e) {
            throw new RuntimeException("❌ PDF okunurken hata oluştu: " + studentUsername, e);
        }

        return keywords;
    }

    public List<InternshipOffer> recommendInternships(String studentUsername) {
        Set<String> studentKeywords = extractKeywordsFromCv(studentUsername);
        List<String> allPositions = approvedTraineeInformationFormRepository.findAllPositions();

        System.out.println("📌 CV İçindeki Kelimeler: " + studentKeywords);
        System.out.println("📌 Approved Trainee Positions: " + allPositions);

        List<InternshipOffer> recommendedInternships = new ArrayList<>();

        for (String position : allPositions) {
            for (String keyword : studentKeywords) {
                if (position.toLowerCase().contains(keyword)) {
                    System.out.println("✅ Eşleşen Pozisyon: " + position);
                    recommendedInternships.addAll(internshipOfferRepository.findByKeyword(position));
                    break;
                }
            }
        }

        return recommendedInternships;
    }


}
