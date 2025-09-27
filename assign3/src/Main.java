import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Messenger adapter = new MessengerAdapter();
        
        while (!adapter.createUser());
        while (!adapter.shouldExit()) {
            if (!adapter.sendMessage()) continue;
            if (adapter.readRequested()) {
                adapter.readMessage();
            }
        }
    }
}
