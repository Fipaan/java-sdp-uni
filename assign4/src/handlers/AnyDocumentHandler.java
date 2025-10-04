import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.text.ParseException;

public class AnyDocumentHandler extends DocumentHandler {
    HTMLDocumentHandler     htmlDH = null;
    MarkdownDocumentHandler mdDH   = null;
    public AnyDocumentHandler() {
        super(null);
        htmlDH = new HTMLDocumentHandler();
        mdDH   = new MarkdownDocumentHandler();
    }
    public AnyDocumentHandler(Document doc) {
        super(doc);
        htmlDH = new HTMLDocumentHandler();
        mdDH   = new MarkdownDocumentHandler();
    }
    public AnyDocumentHandler(HTMLDocument doc) {
        super(null);
        htmlDH   = new HTMLDocumentHandler(doc);
        this.doc = htmlDH.getDocument();
    }
    public AnyDocumentHandler(MarkdownDocument doc) {
        super(null);
        mdDH     = new MarkdownDocumentHandler(doc);
        this.doc = mdDH.getDocument();
    }
    public AnyDocumentHandler(String str) throws ParseException {
        super(null);
        this.fromString(str);
    }

    public void convertDoc(Document container) {
        if (container.getType().equals(htmlDH.getType())) {
            htmlDH.moveDocumentIn(doc);
            doc = htmlDH.getDocument();
        } else if (container.getType().equals(mdDH.getType())) {
            mdDH.moveDocumentIn(doc);
            doc = mdDH.getDocument();
        } else {
            assert "Unknown type" == null;
        }
    }
    public void fromString(String str) throws ParseException {
        Document htmlDoc = htmlDH.getDocument();
        Document mdDoc   = mdDH.getDocument();
        if (htmlDoc.fromString(str)) {
            doc = htmlDoc;
        } else if (mdDoc.fromString(str)) {
            doc = mdDoc;
        } else throw new ParseException("Unknown document format", 0);
    }
    public void readFile(String filename) throws IOException, ParseException {
        try {
            Path path = Path.of(filename);
            String str = Files.readString(path, StandardCharsets.UTF_8);
            String pathS = path.toString();
            if (pathS.endsWith(".html")) {
                doc = htmlDH.fromStringDoc(str);
            } else if (pathS.endsWith(".md")) {
                doc = mdDH.fromStringDoc(str);
            } else this.fromString(str);
        } catch (IOException e) {
            throw new IOException("Couldn't read from file");
        }
    }
    public void writeFile(String filename) throws IOException {
        try {
            Path path = Path.of(filename);
            Files.writeString(path, this.toString(), StandardCharsets.UTF_8);
            System.out.println(String.format("Written doc into %s", filename));
        } catch (IOException e) {
            throw new IOException("Couldn't write into file");
        }
    }
}
