import java.util.List;
//Интерфейс, описывающий поисковый движок.
public interface SearchEngine {
    List<PageEntry> search(String word);
    //содержит только запрос из слова отвечает списком элементов результата ответа
}
