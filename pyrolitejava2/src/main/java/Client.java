package main.java;

import com.google.gson.Gson;
import net.razorvine.pyro.PyroProxy;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    private Gson gson;
    private final PyroProxy proxy;
    private Scanner scanner;

    public Client(String host, int port, String server) throws Exception {
        init();
        proxy = new PyroProxy(host, port, server);
    }

    public static void main(String[] args) throws Exception {
        Client client = null;
        if (args.length > 0)
            client = new Client(args[0], Integer.parseInt(args[1]), args[2]);
        else
            client = new Client("localhost", 7543, "exec");

        client.mainMenu();
    }

    private void printMenu() {
        System.out.println("1: Find all files");
        System.out.println("2: Find files containing substring");
        System.out.println("3: Find files by parts of content (text)");
        System.out.println("4: Find files by parts of content (binary)");
        System.out.println("5: Find files with duplicate content");
    }

    private String getSelection() {
        System.out.println("Option: ");
        return scanner.nextLine();
    }

    private void getWhichMessagesToPrint(String option) throws IOException {
        switch (option) {
            case "1":
                findAllFiles();
                break;
            case "2":
                findFilesContainingSubstring();
                break;
            case "3":
                findFilesByContentText();
                break;
            case "4":
                findFilesByContentBinary();
                break;
            case "5":
                findFilesWithDuplicateContent();
                break;
        }
    }

    public void mainMenu() throws IOException {
        printMenu();
        String option = getSelection();

        getWhichMessagesToPrint(option);

        mainMenu();
    }

    private void init() {
        scanner = new Scanner(System.in);
        gson = new Gson();
    }

    private void printFiles(FileEntity[] files) {
        for (FileEntity file : files) {
            System.out.println("NUME: " + file.getNume() + " PATH: " + file.getPath() + " HASH: " + file.getHash());
        }
        System.out.println();
    }

    private void findAllFiles() throws IOException {
        FileEntity[] allFiles = gson.fromJson((String) proxy.call("find_all_files"), FileEntity[].class);
        printFiles(allFiles);
    }

    private void findFilesContainingSubstring() throws IOException {
        String substring = scanner.nextLine();
        FileEntity[] files = gson.fromJson((String) proxy.call("find_files_containing_substring", substring), FileEntity[].class);
        printFiles(files);
    }

    private void findFilesWithDuplicateContent() throws IOException {
        FileEntity[] files = gson.fromJson((String) proxy.call("find_files_with_duplicate_hash"), FileEntity[].class);
        printFiles(files);
    }

    private void findFilesByContentText() throws IOException {
        String text = scanner.nextLine();
        FileEntity[] files = gson.fromJson((String) proxy.call("find_files_by_content_parts_text", text), FileEntity[].class);
        printFiles(files);
    }

    private void findFilesByContentBinary() throws IOException {
        String text = scanner.nextLine();
        FileEntity[] files = gson.fromJson((String) proxy.call("find_files_by_content_parts_binary", text), FileEntity[].class);
        printFiles(files);
    }
}

class FileEntity {
    private String nume;
    private String path;
    private String hash;

    public FileEntity(){
    }

    public FileEntity(String nume, String path, String hash) {
        this.nume = nume;
        this.path = path;
        this.hash = hash;
    }

    public String getNume() {
        return nume;
    }

    public String getPath() {
        return path;
    }

    public String getHash() {
        return hash;
    }
}
