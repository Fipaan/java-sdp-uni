package fipaan.com.console;

public class ConsoleBufferHandler implements IConsoleBuffer {
    private int[][] storedScreen = new int[0][];
    private ArrayList<ArrayList<Integer>> screen = new ArrayList<>();

    private Console console;
    public Console getConsole() { return console; }
    public ConsoleBufferHandler getConsole(Console c) { console = c; return this; }

    public ConsoleBufferHandler(Console c) { console = c; }
}
