//Класс, описывающий один элемент результата одного поиска.
public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;  //имя файла
    private final int page; //номер страницы
    private int count = 0; //количество раз, которое встретилось это слово на ней

    public PageEntry(String pdfName, int page) {
        this.pdfName = pdfName;
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Info{" +
                "pdfName='" + pdfName + '\'' +
                ", page='" + page + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    @Override
    public int compareTo(PageEntry o) {
        return Integer.compare(this.count, o.count);
    }
}
