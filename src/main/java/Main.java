import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
//    public static void main(String[] args) throws Exception {
//        engine.search("согласовать");
//        BooleanSearchEngine engine = new BooleanSearchEngine(new File("C:\\Games\\java\\pcs-final-diplom\\pdfs"));
//        var searchedSting = engine.search("бизнес");
//        var jsonArray = new JSONArray();
//        for(PageEntry pe: searchedSting){
//            var jo = new JSONObject(pe);
//            jsonArray.put(jo);
////        }
////        System.out.println("");
//
////использования поискового движка
//        try (ServerSocket serverSocket = new ServerSocket(8989);) { // стартуем сервер один(!) раз на порту 8989
//            BooleanSearchEngine engine = new BooleanSearchEngine(new File("C:\\Games\\java\\pcs-final-diplom\\pdfs"));
//            while (true) { // в цикле(!) принимаем подключения
//                try (
//                        Socket socket = serverSocket.accept(); // становимся в ожидание подключения к сокету под именем - "client" на серверной стороне
//                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // канал чтения из сокета
//                        PrintWriter out = new PrintWriter(socket.getOutputStream());  // канал записи в сокет
//                ) {
////                    BooleanSearchEngine engine = new BooleanSearchEngine(new File("C:\\Games\\java\\pcs-final-diplom\\pdfs"));
//
//
//
//                    // обработка одного подключения
//                    var searchedWord = in.readLine();
//                    var searchedString = engine.search(searchedWord);
//                    var jsonArray = new JSONArray(searchedString);
//
//                    out.write(jsonArray.toString());
//
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Не могу стартовать сервер");
//            e.printStackTrace();
//        }
//    }

// Запуск сервера, обслуживающего поисковые запросы
    static final int SERVER_PORT = 8989;

    public static void main (String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT);) {
            System.out.println("Старт подключения к " + SERVER_PORT + "...");

            BooleanSearchEngine engine = new BooleanSearchEngine(new File ("C:\\\\Games\\\\java\\\\pcs-final-diplom\\\\pdfs"));
            System.out.println(engine.search("бизнес"));

            Gson gson = new GsonBuilder().create();

            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {
                    String  word= in.readLine();
                    List<PageEntry> pageEntryList = engine.search(word);

                    if (pageEntryList == null) {
                        out.println("Не найдено ни одного совпадения");
                    }

                    String json = gson.toJson(pageEntryList);
                    out.println(json);

                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
