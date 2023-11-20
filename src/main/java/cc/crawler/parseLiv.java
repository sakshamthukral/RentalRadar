package cc.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class parseLiv {
    public static void main(String[] args){
//        String htmlFolderPath = "E:/Semester-1/ACC/final_project/compute_champions/pageSources";
//        String txtFolderPath = "E:/Semester-1/ACC/final_project/compute_champions/htmlParsedPages";
//
//        createFolderIfNotExists(htmlFolderPath);
//        createFolderIfNotExists(txtFolderPath);
//
//        processHtmlFiles(htmlFolderPath, txtFolderPath);

    }

    public static void processHtmlFiles(String sourceFolderPath, String destinationFolderPath){
        File sourceFolder = new File(sourceFolderPath);
        File[] htmlFiles = sourceFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".html"));

        if(htmlFiles != null){
          for(File htmlFile : htmlFiles){
              String txtFileName = htmlFile.getName().replace(".html",".txt");
              File txtFile = new File(destinationFolderPath, txtFileName);

              try{
                  String textContent = parseHtmlToText(htmlFile);
                  saveTextToFile(textContent, txtFile);
                  System.out.println("Converted "+htmlFile.getName()+" to " + txtFile.getName());
              } catch(IOException e){
                  e.printStackTrace();
              }
          }
        }
    }
    private static String parseHtmlToText(File htmlFile) throws IOException {
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        return doc.text();
    }

    private static void saveTextToFile(String textContent,File txtFile) throws IOException {
        try (FileWriter fileWriter = new FileWriter(txtFile)) {
            fileWriter.write(textContent);
        }
    }

}
