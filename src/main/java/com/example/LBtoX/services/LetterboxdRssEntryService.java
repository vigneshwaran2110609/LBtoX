package com.example.LBtoX.services;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class LetterboxdRssEntryService {
    public String extractLastParagraphText(String description) {

        if (description == null || description.isEmpty()) {
            return "";
        }

        Document doc = Jsoup.parse(description);
        Elements paragraphs = doc.select("p");

        if (paragraphs.isEmpty()) {
            return "";
        }

        Element lastParagraph = paragraphs.last();
        return lastParagraph.text().trim();
    }

    public double parseRating(String rating) {
        if (rating == null || rating.isEmpty()) {
            return 0.0;
        }

        long fullStars = rating.chars()
            .filter(ch -> ch == '★')
            .count();

        boolean hasHalf = rating.contains("½");
        System.out.println(fullStars);
        return fullStars + (hasHalf ? 0.5 : 0.0);
    }
}
