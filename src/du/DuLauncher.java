package du;
import org.kohsuke.args4j.*;

import java.util.ArrayList;
import java.util.List;


public class DuLauncher {

    @Option(name = "-h", usage = "readable format")
    private boolean readable = false;

    @Option(name = "-c", usage = "summed size")
    private boolean summed = false;

    @Option(name = "--si", usage = "all units size comes in 1000 format instead of 1024")
    private boolean si = false;

    @Argument
    private List<String> filesList = new ArrayList<>();

    private static int result = 0; // 0 - успех, 1 - ошибка

    public static void main(String[] args) {
        new DuLauncher().launch(args);
        System.exit(DuLauncher.result);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (filesList.isEmpty()) {
                System.out.println("Error entering arguments (for correct input, see the example)");
                System.out.println("\nExample: du [-h] [-c] [--si] file1 file2 file3 ...");
                parser.printUsage(System.out);
                throw new IllegalArgumentException("");
            }
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            System.out.println("\nExample: du [-h] [-c] [--si] file1 file2 file3 ...");
            parser.printUsage(System.out);
            return;
        }
        catch (IllegalArgumentException e) {
            System.exit(0);
        }

        DiskUsage du = new DiskUsage(filesList);

        try {
            result = du.calcSizeAndWriteResult(readable, summed, si);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}