import java.util.ArrayList;
import java.io.IOException;

public class MessengerAdapter implements Messenger {
    public static final int USERNAME_LENGTH_CAP = 64;
    public static final int MESSAGE_LENGTH_CAP  = 512;
    public static final int MESSAGE_HISTORY_CAP = 10;
    private IOInstance io = new IOInstance();
    private StringBuilder sb = new StringBuilder();
    ArrayList<User> users = new ArrayList<>();
    User currentUser;
    private boolean isExiting = false;
    private boolean isReading = false;
    private Message[] messageHistory = new Message[MESSAGE_HISTORY_CAP]; // TODO: maybe use Queue
    private int messageHistoryCount = 0;
    private int lastHistoryRead = 0;

    private void newUser(User user) throws IOException {
        users.add(user);
        currentUser = user;
        io.writeStr(String.format("User `%s` succesfully created!\n", user.getName()));
    }
    public boolean createUser() {
        try {
            io.writeStr("Enter your name (empty for anonymous): ");
            io.writeFlush();
            sb.setLength(0);
            io.readUntilCharSync(sb, '\n', 1024, 0.05);
            if (sb.length() > USERNAME_LENGTH_CAP) {
                io.setOut(System.err);
                io.writeStr(String.format("Username `%s` is too long.\n", sb.toString()));
                io.setOut(System.out);
                return false;
            }
            String name = sb.toString();
            User user = name.length() == 0 ? new User() : new User(name);
            newUser(user);
        } catch (IOException e) {
            System.out.println("IO ERROR: " + e.getMessage());
            return false;
        }
        return true;
    }
    public User getCurrentUser() { return currentUser; }

    public boolean shouldExit() { return isExiting; }
    public boolean readRequested() {
        boolean result = isReading;
        isReading = false;
        return result;
    }
    public void readMessage() {
        // TODO: be dynamic -> 
        //         TODO: use `\x0D` when TTY doesn't force flushing on '\n'
        try {
            for (int i = lastHistoryRead; i < messageHistoryCount; ++i) {
                Message msg = messageHistory[i];
                io.writeStr(String.format("[%s] %s: %s\n",
                                msg.getTimestamp(),
                                msg.username,
                                msg.message));
            }
            lastHistoryRead = messageHistoryCount;
        } catch (IOException e) {
            System.out.println("IO ERROR: " + e.getMessage());
        }
    }
    User findUser(String name) {
        for (int i = 0; i < users.size(); ++i) {
            User user = users.get(i);
            if (name.equals(user.getName())) return user;
        }
        return null;
    }
    public boolean sendMessage() {
        try {
            io.writeStr(String.format("%s >> ", currentUser.getName()));
            io.writeFlush();
            sb.setLength(0);
            io.readUntilCharSync(sb, '\n', 1024, 0.05);
            String msg = sb.toString();
            if (msg.equals("exit")) { isExiting = true; return true; }
            String[] msgParts = msg.trim().split("\\s+");
            if (msgParts.length >= 1 && msgParts[0].startsWith("/")) {
                String command = msgParts[0];
                if (command.equals("/switchUser")) {
                    if (msgParts.length == 2) {
                        String name = msgParts[1];
                        User user = findUser(name);
                        if (user == null) {
                            io.setOut(System.err);
                            io.writeStr(String.format("User `%s` does not exist.\n", name));
                            io.setOut(System.out);
                            return false;
                        }
                        currentUser = user;
                        return true;
                    } else {
                        io.setOut(System.err);
                        io.writeStr(String.format("Command `%s` expects 1 argument\n", "/switchUser"));
                        io.setOut(System.out);
                        return false;
                    }
                } else if (command.equals("/addUser")) {
                    User user;
                    if (msgParts.length == 1) {
                        user = new User();
                    } else if (msgParts.length == 2) {
                        String name = msgParts[1];
                        User findUser = findUser(name);
                        if (findUser != null) {
                            io.setOut(System.err);
                            io.writeStr(String.format("User `%s` already exists!\n", name));
                            io.setOut(System.out);
                            return false;
                        }
                        user = new User(name);
                    } else {
                        io.setOut(System.err);
                        io.writeStr(String.format("Command `%s` expects 0 or 1 arguments\n", "/addUser"));
                        io.setOut(System.out);
                        return false;
                    }
                    newUser(user);
                    return true;
                } else if (command.equals("/readHistory")) {
                    isReading = true;
                    return true;
                } else if (command.equals("/help")) {
                    io.writeStr("COMMANDS:\n");
                    io.writeStr("  /switchUser <username> - changes current user to specified name\n");
                    io.writeStr("  /addUser [username]    - adds new user and switches to it\n");
                    io.writeStr("  /readHistory           - shows message history\n");
                    io.writeStr("  /help                  - shows this message\n");
                } else {
                    io.setOut(System.err);
                    io.writeStr(String.format("Unknown command `%s`. See /help for more info.\n", command));
                    io.setOut(System.out);
                    return false;
                }
            }
            String message = sb.toString();
            if (message.length() > MESSAGE_LENGTH_CAP) {
                io.setOut(System.err);
                io.writeStr(String.format("Message `%s` is too long.\n", message));
                io.setOut(System.out);
                return false;
            }
            if (messageHistoryCount == MESSAGE_HISTORY_CAP) {
                for (int i = 1; i < MESSAGE_HISTORY_CAP; ++i) {
                    messageHistory[i - 1] = messageHistory[i];
                }
                if (lastHistoryRead > 0) lastHistoryRead -= 1;
            } else {
                messageHistoryCount += 1;
            }
            messageHistory[messageHistoryCount - 1] = new Message(currentUser.getName(), message);
        } catch (IOException e) {
            System.out.println("IO ERROR: " + e.getMessage());
            return false;
        }
        return true;
    }
}
