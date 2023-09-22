package main.java;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Client {
    public Client(String urlServ) throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(urlServ));
        XmlRpcClient proxy = new XmlRpcClient();
        proxy.setConfig(config);

        boolean finish = true;

        while (finish) {
            System.out.println("1. Find all files");
            System.out.println("2. Find files containing substring");
            System.out.println("3. Find files by parts of content (text)");
            System.out.println("4. Find files by parts of content (binary)");
            System.out.println("5. Find files with duplicate content");
            System.out.println("0. Close");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int option = Integer.parseInt(reader.readLine());
            switch (option) {
                case 1:
                    String resp = (String) proxy.execute("find_all_files", new Object[0]);
                    printFiles(resp);
                    break;
                case 2:
                    String substring = reader.readLine();
                    String resp1 = (String) proxy.execute("find_files_containing_substring", new Object[]{substring});
                    printFiles(resp1);
                    break;
                case 3:
                    String contentText = reader.readLine();
                    String resp3 = (String) proxy.execute("find_files_by_content_parts_text", new Object[]{contentText});
                    printFiles(resp3);
                    break;
                case 4:
                    String contentBinary = reader.readLine();
                    String resp4 = (String) proxy.execute("find_files_by_content_parts_binary", new Object[]{contentBinary});
                    printFiles(resp4);
                    break;
                case 5:
                    String resp5 = (String) proxy.execute("find_files_with_duplicate_hash", new Object[0]);
                    printFiles(resp5);
                    break;
                case 0:
                    finish = false;
                    break;
                default:
                    System.out.println("Optiunea nu exista!");
                    break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0)
            new Client(args[0]);
        else
            new Client("http://localhost:8080/XMLRPC");
    }

    private void printFiles(String files) {
        String[] fileArray = files.split("}");
        for (String file : fileArray) {
            System.out.println(file);
        }
        System.out.println();
    }
}
