package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipOffer;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Resume;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InternshipOfferRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ResumeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ResumeRecommendationService {

    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    private final InternshipOfferRepository internshipOfferRepository;
    private final ResumeRepository resumeRepository;

    @Autowired
    public ResumeRecommendationService(InternshipOfferRepository internshipOfferRepository,
                                       ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository,
                                       ResumeRepository resumeRepository) {
        this.internshipOfferRepository = internshipOfferRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.resumeRepository = resumeRepository;
    }

    private Optional<File> writeByteDataToTempFile(byte[] fileData, String studentUsername) {
        try {
            File tempFile = File.createTempFile(studentUsername + "_resume", ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileData);
            }
            return Optional.of(tempFile);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Set<String> extractKeywordsFromCv(String studentUsername) {
        Set<String> keywords = new HashSet<>();

        Optional<Resume> resumeOptional = resumeRepository.findTopByUserName_UserNameOrderByIdDesc(studentUsername);
        if (resumeOptional.isEmpty() || resumeOptional.get().getFileData() == null) {
            throw new RuntimeException("❌ Resume not found in database for user: " + studentUsername);
        }

        byte[] fileData = resumeOptional.get().getFileData();
        Optional<File> tempFile = writeByteDataToTempFile(fileData, studentUsername);

        if (tempFile.isEmpty() || !tempFile.get().exists()) {
            throw new RuntimeException("❌ Temporary file could not be created for user: " + studentUsername);
        }

        try (PDDocument document = PDDocument.load(tempFile.get())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document).toLowerCase();

            List<String> keywordList = Arrays.asList(
                    "ai", "cloud", "software", "backend", "frontend","aı",
                    "embedded", "cybersecurity", "iot", "data science"
            );
            System.out.println("🔍 CV metni:\n" + text);
            for (String keyword : keywordList) {
                String regex = "(?<![a-zA-Z])" + Pattern.quote(keyword.toLowerCase()) + "(?![a-zA-Z])";
                if (Pattern.compile(regex).matcher(text).find()) {
                    keywords.add(keyword);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("❌ Error reading PDF for user: " + studentUsername, e);
        }

        return keywords;
    }

    public List<String> recommendInternships(String studentUsername) {
        Set<String> studentKeywords = extractKeywordsFromCv(studentUsername);
        Set<String> matchedPositions = new HashSet<>();

        List<String> allPositions = approvedTraineeInformationFormRepository.findAllPositions();

        for (String keyword : studentKeywords) {
            for (String position : allPositions) {
                if (position != null && position.toLowerCase().contains(keyword)) {
                    System.out.println("✅ Eşleşen Pozisyon: " + position);
                    matchedPositions.add(position);

                }
            }
        }

        System.out.println("📌 Postman'a Gönderilen Pozisyonlar: " + matchedPositions);
        return new ArrayList<>(matchedPositions);
    }
}
