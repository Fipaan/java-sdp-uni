public interface Messenger {
    boolean createUser();
    User getCurrentUser();

    boolean shouldExit();
    boolean readRequested();
    void readMessage();
    boolean sendMessage();
}
