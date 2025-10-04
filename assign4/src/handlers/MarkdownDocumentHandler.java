import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.text.ParseException;

public class MarkdownDocumentHandler extends DocumentHandler {
    public MarkdownDocumentHandler() {
        super(new MarkdownDocument());
    }
    public MarkdownDocumentHandler(MarkdownDocument doc) {
        super(doc);
    }
    public MarkdownDocumentHandler(String str) throws ParseException {
        this();
        this.fromString(str);
    }
    public void fromString(String str) throws ParseException {
        if (!doc.fromString(str)) throw new ParseException("Invalid Markdown document", 0);
    }
    public void readFile(String filename) throws IOException, ParseException {
        try {
            Path path = Path.of(filename);
            this.fromString(Files.readString(path, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IOException("Couldn't read from Markdown file");
        }
    }
    public void writeFile(String filename) throws IOException {
        try {
            Path path = Path.of(filename);
            Files.writeString(path, this.toString(), StandardCharsets.UTF_8);
            System.out.println(String.format("Written doc into %s", filename));
        } catch (IOException e) {
            throw new IOException("Couldn't write into Markdown file");
        }
    }
}
