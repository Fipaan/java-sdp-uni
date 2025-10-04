import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HTMLDocument implements Document {
    public String getType() { return "HTML"; }
    
    protected String title = null;
    public String getTitle()           { return title;       }
    public void setTitle(String title) { this.title = title; }
    
    protected String author = null;
    public String getAuthor()            { return author;        }
    public void setAuthor(String author) { this.author = author; }
    
    protected String content = null;
    public String getContent()             { return content;         }
    public void setContent(String content) { this.content = content; }
    
    protected LocalDateTime date = null;
    public LocalDateTime getDate()          { return date;      }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String toString() {
        return String.format(
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <title>%s</title>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <h1>%s</h1>\n" +
            "        <h2>%s</h2>\n" +
            "        <p>%s</p>\n" +
            "    </body>\n" +
            "</html>",
            title, author, StringUtils.dateToString(date), content
        );
    }
    public boolean fromString(String str) {
        StringParser spFile = new StringParser(str);
        if (!spFile.trim().extractStarts("<!DOCTYPE html>")) return false;
        if (!spFile.trim().extractWrapper("<html>", "</html>")) return false;
        
        if (!spFile.trim().extractStartsContainer("<head>", "</head>")) return false;
        StringParser spHead = new StringParser(spFile.getContainer());
        
        if (!spFile.trim().extractStartsContainer("<body>", "</body>")) return false;
        StringParser spBody = new StringParser(spFile.getContainer());
        
        if (!spFile.trim().isStrEmpty()) return false;

        if (!spHead.trim().extractStarts("<meta charset=\"UTF-8\">")) return false;
        if (!spHead.trim().extractStartsContainer("<title>", "</title>")) return false;
        String title = spHead.getContainer();
        if (!spHead.trim().isStrEmpty()) return false;
        
        if (!spBody.trim().extractStartsContainer("<h1>", "</h1>")) return false;
        String author = spBody.getContainer().trim();
        if (!spBody.trim().extractStartsContainer("<h2>", "</h2>")) return false;
        String date = spBody.getContainer().trim();
        if (!spBody.trim().extractStartsContainer("<p>", "</p>")) return false;
        String content = spBody.getContainer().trim();
        if (!spBody.trim().isStrEmpty()) return false;
        
        try {
            this.date = StringUtils.dateFromString(date);
        } catch (DateTimeParseException e) { return false; }
        this.title   = title;
        this.author  = author;
        this.content = content;
        return true;
    }
}
