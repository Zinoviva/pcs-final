import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

//	Реализация поискового движка
public class BooleanSearchEngine implements SearchEngine {
    protected HashMap<String, ArrayList<PageEntry>> idxMap;

    private void addToIdxMap(HashMap<String, ArrayList<PageEntry>> map, String[] words, String fn, int page){
        for (String word: words) {
            String lowerWord = word.toLowerCase();
            map.computeIfAbsent(lowerWord, k -> new ArrayList<>());
            ArrayList <PageEntry> array = map.get(lowerWord);
            List <PageEntry> filteredArray = array.stream().filter(pg -> pg.getPdfName().equals(fn) && pg.getPage() == page).collect(Collectors.toList());
                if (!filteredArray.isEmpty()){
                PageEntry curPe = filteredArray.get(0);
                curPe.setCount(curPe.getCount() + 1);
            }else{
                var newPe = new PageEntry(fn, page);
                newPe.setCount(1);
                array.add(newPe);
                map.put(lowerWord, array);
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        ArrayList <PageEntry> array = this.idxMap.get(word.toLowerCase());
        if (array == null || array.isEmpty()){
            return Collections.emptyList();
        }
        else{
            sortArray(array);
            return array;
        }
    }

    public  void sortArray (ArrayList<PageEntry> array){
        Collections.sort(array);
        Collections.reverse(array);
    }

    public BooleanSearchEngine(File directory) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
        idxMap = new HashMap<>();
        //загрузка документа
        if (directory.isDirectory()){
            for (File f : Objects.requireNonNull(directory.listFiles())) {
                try(PDDocument pdd = PDDocument.load(f)){
                    pdd.setAllSecurityToBeRemoved(true);
                    PDFTextStripper ts = new PDFTextStripper();
                    int count = pdd.getNumberOfPages();
                    for (int i = 1; i < count ; i++) {
                        ts.setStartPage(i);
                        ts.setEndPage(i);
                        String text = ts.getText(pdd);
                        String [] words = text.split("\\P{IsAlphabetic}+");
                        addToIdxMap(this.idxMap, words, f.getName(), i);
                    }
                }
            }
        }
    }
}
