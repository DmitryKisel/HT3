import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {


    /**
     * This method reads file with fileName
     * @param fileName - the name of file to read
     * @return
     */
    public List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                lines.add(sCurrentLine);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("[Command file read error] " + e.toString());
        }
        return lines;
    }


    /**
     * This method writes to file incoming data
     * @param outputFileName - the name of file to write
     * @param dataToWrite - the data to be written in to the file
     */
    public void writeFile(String outputFileName, String dataToWrite) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFileName), "utf-8"));
            writer.write(dataToWrite);
            System.out.println("Log has been saved successfully in " + outputFileName);
        } catch (IOException e) {
            System.out.println("[Log file write error] " + e.toString());
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                System.out.println("[Unable to close the output stream] " + e.toString());
            }
        }
    }
}
