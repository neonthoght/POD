package pod;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class GuideFiller {

    public static void main(String[] args) throws IOException {
        String ifPath = "";
        String ofPath = "";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-if") ) {
                ifPath = args[++i];
            } else if (args[i].equals("-of") ) {
            ofPath = args[++i];
            }
        }

        System.out.println("date " + ifPath + " " + ofPath );
        searchModel(ifPath, ofPath);
    }

    public static void searchModel(String ifPath, String ofPath) throws IOException {
        String delimiter = ";";

        // получить список  наименований марок машин из справочника
        Path guidePath = Paths.get(ofPath);
        List<String> guideLines = Files.readAllLines(guidePath, Charset.forName("UTF-8"));
        HashSet<List> carNameBrandSet = new HashSet<List>();

        for (String guideLine : guideLines) {
            List<String> CarBrandArr = new ArrayList<String>();
            CarBrandArr.add(guideLine.split(";")[1]); // Код модели
            CarBrandArr.add(guideLine.split(";")[2]); // Наименование модели
            CarBrandArr.add(guideLine.split(";")[5]); // Синоним модели
            carNameBrandSet.add(CarBrandArr);
        }
        System.out.println(carNameBrandSet);

        // получить список моделей из файла с ошибками, марки которых есть в справочнике
        Path errPath = Paths.get(ifPath);
        List<String> errLines = Files.readAllLines(errPath, Charset.forName("WINDOWS-1251"));
        HashSet<List> carErrorSet = new HashSet<List>();

        int coincidenceCount = 0;
        int elementNumber = 0;
    
            // поиск марки машины в справочнике
            for (List carNameBrandSetElement : carNameBrandSet) {
                elementNumber++; // счетчик строк в errors.csv
                coincidenceCount = 0; // кол-во совпадений моделей у определённой марки
                for (String errLine : errLines) {
                // если в справочнике есть такая марка машины, то проверяем модель
                if ( 
                    errLine.split(";")[2].contains((String) carNameBrandSetElement.get(0)) // код марки
                    || errLine.split(";")[2].contains((String) carNameBrandSetElement.get(1)) // наименование марки
                ) {
                    // Проверка модели. Если в справочнике нет такой модели, то добавляем её
                    if (errLine.split(";")[3].contains((String) carNameBrandSetElement.get(2))) {
                        //System.out.println(carNameBrandSetElement.get(0) + "/" + carNameBrandSetElement.get(1) + " | " + errLine.split(";")[2] + " | " + errLine.split(";")[3]);
                    coincidenceCount++;
                }
                if (coincidenceCount == 0) {
                    System.out.println(carNameBrandSetElement.size() + " " + carNameBrandSetElement + "  " + carNameBrandSetElement.get(1) + "  " + carNameBrandSetElement.get(0));
                }
                
            }
            System.out.println("num: " + elementNumber);
        }
        //System.out.println(carNameBrandSet);
    }
    }
}