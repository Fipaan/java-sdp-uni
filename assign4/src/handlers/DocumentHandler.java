import java.text.ParseException;
import java.time.LocalDateTime;
import java.io.IOException;
import java.text.ParseException;

public abstract class DocumentHandler {
    protected Document doc;
    public Document getDocument() { return doc; }
    public void moveDocumentIn(Document doc) {
        this.doc.setTitle(doc.getTitle());
        this.doc.setAuthor(doc.getAuthor());
        this.doc.setDate(doc.getDate());
        this.doc.setContent(doc.getContent());
    }
    public DocumentHandler(Document doc) {
        this.doc = doc;
    }
    public String        getType()    { return doc.getType();    }
    public String        getTitle()   { return doc.getTitle();   }
    public String        getAuthor()  { return doc.getAuthor();  }
    public String        getContent() { return doc.getContent(); }
    public LocalDateTime getDate()    { return doc.getDate();    }
    public String        toString()   { return doc.toString();   }

    public abstract void fromString(String str) throws ParseException;
    public Document fromStringDoc(String str) throws ParseException { this.fromString(str); return doc; }
    public abstract void readFile(String filename) throws IOException, ParseException;
    public abstract void writeFile(String filename) throws IOException;
}
