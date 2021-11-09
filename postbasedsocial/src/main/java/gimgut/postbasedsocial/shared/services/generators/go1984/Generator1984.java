package gimgut.postbasedsocial.shared.services.generators.go1984;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class Generator1984 {

    private final List<String> pseudoPages = new ArrayList<>();

    public Generator1984() {
        File resource = null;
        try {
            resource = new ClassPathResource("1984.txt").getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> lines = null;
        try {
            lines = Files.readAllLines(resource.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Group rows into pages with paragraphs
        int nLines = lines.size() - 1;
        int charsPerPage = 2000;
        int pagesLimit = 100;
        StringBuilder currentPage = new StringBuilder();
        String lineSeparator = "<br>";//System.lineSeparator();
        for (int line = 0; line < nLines; line++) {
            if (currentPage.length() < charsPerPage || lines.get(line).length() > 0) {
                currentPage.append(lines.get(line) + " ");

                if (lines.get(line).length() > 0 && lines.get(line + 1).length() < 1)
                    currentPage.append(lineSeparator + lineSeparator);
            } else {
                pseudoPages.add(currentPage.toString());
                currentPage = new StringBuilder();
                line -= 1;
            }

            if (pseudoPages.size() >= pagesLimit)
                break;
        }
    }

    public List<String> getPseudoPages() {
        return pseudoPages;
    }
}
