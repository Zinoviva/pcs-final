import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

//	Реализация поискового движка
public class BooleanSearchEngine implements SearchEngine {
    HashMap<String, ArrayList<PageEntry>> idxMap;

    private void addToIdxMap(HashMap<String, ArrayList<PageEntry>> map, String[] words, String fn, int page){
        for (String word: words) {
            String lowerWord = word.toLowerCase();
//            var lowerWord = word.toLowerCase();  //на 18  джаву
            map.computeIfAbsent(lowerWord, k -> new ArrayList<>());
            ArrayList <PageEntry> array = map.get(lowerWord);
           // var array = map.get(lowerWord);  //на 18  джаву
            List <PageEntry> filteredArray = array.stream().filter(pg -> pg.getPdfName().equals(fn) && pg.getPage() == page).collect(Collectors.toList());
//            var filteredArray = array.stream().filter(pg -> pg.getPdfName().equals(fn) && pg.getPage() == page).collect(Collectors.toList());  //на 18  джаву
            if (filteredArray.size() > 0){
                PageEntry curPe = filteredArray.get(0);
//                var curPe = filteredArray.get(0);  //на 18  джаву
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
//        var array = this.idxMap.get(word.toLowerCase()); //на 18  джаву
        if (array == null || array.isEmpty()){
            return Collections.emptyList();
        }else{
            Collections.sort(array);
            Collections.reverse(array);
//            var sortedArray = array.stream().sorted().collect(Collectors.toList());
            return array;
        }
    }

    public BooleanSearchEngine(File directory) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
        idxMap = new HashMap<>();
        // TODO чтение файла
        //загрузка документа
        if (directory.isDirectory()){
            for (File f : Objects.requireNonNull(directory.listFiles())) {
                try(PDDocument pdd = PDDocument.load(f)){
                    pdd.setAllSecurityToBeRemoved(true);
                    PDFTextStripper ts = new PDFTextStripper();
//                    String text = ts.getText(pdd);
//                    for (String str : ) text.split(" ")
//                    System.out.println("");
//                    String textFromPage = PdfTextExtractor.getTextFromPage(reader, 1);
                    int count = pdd.getNumberOfPages();
                    for (int i = 1; i < count ; i++) {
                        ts.setStartPage(i);
                        ts.setEndPage(i);
                        String text = ts.getText(pdd);
                        String [] words = text.split("\\P{IsAlphabetic}+");
//                        var text = ts.getText(pdd);  //на 18  джаву
//                        var words = text.split("\\P{IsAlphabetic}+");  //на 18  джаву
                        addToIdxMap(this.idxMap, words, f.getName(), i);
                    }
                    //определение количества страниц (нужно ниже)//получить объект одной страницы документа (нужно)
                    //var text = PdfTextExtractor.getTextFromPage(page); //получить текст со страницы
                    //var words = text.split("\\P{IsAlphabetic}+");
                }
            }
        }
    }
}
