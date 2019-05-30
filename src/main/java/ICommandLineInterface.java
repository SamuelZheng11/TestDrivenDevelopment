public interface ICommandLineInterface {
    void exec(String commands);

    IBufferedReader getInputStream();
}
