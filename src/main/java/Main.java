import org.json.JSONArray;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
//        engine.search("согласовать");
//        BooleanSearchEngine engine = new BooleanSearchEngine(new File("C:\\Games\\java\\pcs-final-diplom\\pdfs"));
//        var searchedSting = engine.search("бизнес");
//        var jsonArray = new JSONArray();
//        for(PageEntry pe: searchedSting){
//            var jo = new JSONObject(pe);
//            jsonArray.put(jo);
//        }
//        System.out.println("");

//использования поискового движка
        try (ServerSocket serverSocket = new ServerSocket(8989);) { // стартуем сервер один(!) раз
            BooleanSearchEngine engine = new BooleanSearchEngine(new File("C:\\Games\\java\\pcs-final-diplom\\pdfs"));
            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    // обработка одного подключения
                    var searchedWord = in.readLine();
                    var searchedString = engine.search(searchedWord);
                    var jsonArray = new JSONArray(searchedString);

                    out.write(jsonArray.toString());
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}