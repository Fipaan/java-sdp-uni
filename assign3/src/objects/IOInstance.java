import java.io.*;
import java.nio.charset.*;

public class IOInstance {
    private InputStream in;
    private BufferedInputStream bufIn;
    public InputStream getIn() { return in; }
    public void setIn(InputStream in) {
        this.in    = in;
        this.bufIn = new BufferedInputStream(in);
    }
    
    private PrintStream out;
    public PrintStream getOut() { return out; }
    public void setOut(PrintStream out) { this.out = out; }

    public IOInstance(InputStream in, PrintStream out) {
        this.in    = in;
        this.bufIn = new BufferedInputStream(in);
        this.out   = out;
    }
    public IOInstance() { this(System.in, System.out); }
    public IOInstance(InputStream in) { this(in, System.out); }
    public IOInstance(PrintStream out) { this(System.in, out); }
    
    public boolean readBytes(byte[] buf) throws IOException {
        return in.read(buf) >= 0;
    }
    public void writeBytes(byte[] buf, int offset, int length) throws IOException {
        out.write(buf, offset, length);
    }
    public void writeBytes(byte[] buf) throws IOException {
        out.write(buf);
    }
    public void writeStr(String str) throws IOException {
        out.write(str.getBytes(StandardCharsets.UTF_8));
    }
    public void writeFlush() {
        out.flush();
    }

    public boolean readUntilChar(StringBuilder sb, char ch, int chunkSize) throws IOException {
        int charLength = StringUtils.charLength(ch);

        byte[] buf         = new byte[chunkSize];
        byte[] charBuf     = new byte[charLength];

        boolean found = false;
        int lastIndex = -1;
        while (true) {
            in.mark(chunkSize);
            int n = in.read(buf);
            if (n <= 0) break; // buf.length() == 0 or EOF
            StringUtils.IndexResult ir = StringUtils.indexOf(buf, ch, n);
            int     index      = ir.index();
            boolean isFullChar = ir.isFullChar();
            if (index < 0) index = n;
            else if (!isFullChar) {
                in.reset();
                in.mark(index + charBuf.length);
                in.read(buf, 0, index);
                in.read(charBuf);
                in.reset(); // If we found, we can properly skip it later
                in.mark(chunkSize); // If we didn't found we need to check next chunk accordingly
                StringUtils.IndexResult new_ir = StringUtils.indexOf(charBuf, ch);
                if (new_ir.index() == 0 && new_ir.isFullChar()) {
                    isFullChar = true;
                } else {
                    in.read(buf, 0, chunkSize);
                }
            }
            if (!StringUtils.isValidCharset(buf, 0, index, StandardCharsets.UTF_8)) {
                throw new CharacterCodingException();
            }
            sb.append(new String(buf, 0, index, StandardCharsets.UTF_8));
            if (index < n && isFullChar) {
                found = true;
                lastIndex = index;
                in.reset();
                in.read(buf, 0, lastIndex + charLength);
                break;
            }
        }

        return found;
    }
    public void readUntilCharSync(StringBuilder sb, char ch, int chunkSize, double tryTime) throws IOException {
        while (!readUntilChar(sb, ch, chunkSize)) {
            SystemUtils.sleep(tryTime);
        }
    }

}
