import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void test1() throws IOException {
        GenericDocument poem = new Poem<GenericDocument>().build(new GenericDocument());
        AnyDocumentHandler dh = new AnyDocumentHandler(poem);
        System.out.println("==================================================");
        System.out.println("================= WRITE EXAMPLE ==================");
        System.out.println("==================================================");
        System.out.println("       ====================================");
        System.out.println("       =========== HTML VERSION ===========");
        System.out.println("       ====================================");
        dh.convertDoc(new HTMLDocument());
        System.out.println(dh.toString());
        dh.writeFile("files/poem.html");
        System.out.println("       ====================================");
        System.out.println("       ========= MARKDOWN VERSION =========");
        System.out.println("       ====================================");
        dh.convertDoc(new MarkdownDocument());
        System.out.println(dh.toString());
        dh.writeFile("files/poem.md");
    }
    public static void test2() throws IOException, ParseException {
        System.out.println("==================================================");
        System.out.println("================== READ EXAMPLE ==================");
        System.out.println("==================================================");
        AnyDocumentHandler dh = new AnyDocumentHandler(new GenericDocument());
        System.out.println("       ====================================");
        System.out.println("       =========== HTML VERSION ===========");
        System.out.println("       ====================================");
        dh.readFile("files/poem.html");
        System.out.println(dh.toString());
        System.out.println("       ====================================");
        System.out.println("       ========= MARKDOWN VERSION =========");
        System.out.println("       ====================================");
        dh.readFile("files/poem.md");
        System.out.println(dh.toString());
    }
    public static void main(String[] args) throws IOException, ParseException {
        test1();
        test2();
    }
}
