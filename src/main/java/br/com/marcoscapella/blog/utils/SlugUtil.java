package br.com.marcoscapella.blog.utils;

import java.text.Normalizer;

public class SlugUtil {
    /* Solution found online for helping out with the slug creation. */
    public static String toSlug(String input) {
        if (input == null) {
            return null;
        }

        String slug = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .replaceAll("[^\\w\\s-]", "")
                .replaceAll("-", " ")
                .trim()
                .replaceAll("\\s+", "-")
                .toLowerCase();

        return slug;
    }
}
