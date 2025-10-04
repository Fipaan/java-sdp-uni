public class DocumentBuilder<T extends Document> {
    protected T doc;
    public DocumentBuilder(T doc) {
        this.doc = doc;
    }
    public DocumentBuilder<T> buildTitle(String   title)   { this.doc.setTitle(title);     return this; }
    public DocumentBuilder<T> buildAuthor(String  author)  { this.doc.setAuthor(author);   return this; }
    public DocumentBuilder<T> buildContent(String content) { this.doc.setContent(content); return this; }
    public DocumentBuilder<T> buildDate(String date) {
        this.doc.setDate(StringUtils.dateFromString(date));
        return this;
    }
    public T build() { return this.doc; }
}
