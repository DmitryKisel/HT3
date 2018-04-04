import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class HtmlManager {
    static StringBuilder dataFromURL = null;


    /**
     * This method reads data from incoming URL
     * @param address - incoming URL
     * @param timeOut - the time for page to be read;
     * @return
     */
    public Boolean readURL(String address, int timeOut) {
        dataFromURL = new StringBuilder();
        Boolean result = true;
        URL url = null;
        String line;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            System.out.println("[No URL have been found] " + e.toString());
            result = false;
        }

        try {
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeOut * 1000);
            connection.setReadTimeout(timeOut * 1000);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));


            while ((line = br.readLine()) != null) {
                dataFromURL.append(line);
            }
            br.close();
        } catch (UnknownHostException e) {
            System.out.println("[Unknown Host error: Page with such address \"" + address + "\" hasn't been found] "
                    + e.toString());
            result = false;

        } catch (SocketTimeoutException e) {
            System.out.println("[Timeout error: Page opening time is more then timeout time in the command file] "
                    + e.toString());
            result = false;
        } catch (IOException e) {
            System.out.println("[InputOutput error occurs] " + e.toString());
            result = false;
        } catch (NullPointerException e) {
            System.out.println(e.toString());
            result = false;
        }
        catch (IllegalArgumentException e) {
            System.out.println("[Incorrect URL \"" + address + "\" has been found. Check the URL in command file] "
                    + e.toString());
            result = false;
        }
        return result;
    }


    /**
     * This method returns data has been read from URL
     * @return
     */
    public String getData() {
        return dataFromURL.toString();
    }


    /**
     * This method checks if incoming data is present as 'href' element in page code
     * @param searchingData - incoming data
     * @return
     */
    public boolean checkHrefTag(String searchingData) {
        Boolean result;
        String fileData = getData();
       //Preparing an element of html 'href' for search in page code
        String searchingElement = "<a href=\"" + searchingData + "\"";

        if (fileData.contains(searchingElement)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    /**
     * This method checks if incoming data is present as 'href name' element in page code
     * @param searchingData - incoming data
     * @return
     */
    public boolean checkHrefName(String searchingData) {
        Boolean result;
        String fileData = getData();
        //Preparing an element of html 'href name' for search in page code
        String searchingElement = ">" + searchingData + "</a>";

        if (fileData.contains(searchingElement)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    /**
     * This method checks if incoming data is present as 'title tag' element in page code
     * @param searchingData - incoming data
     * @return
     */
    public boolean checkTitleTag(String searchingData) {
        Boolean result;
        String fileData = getData();
        //Preparing an element of html 'title tag' for search in page code
        String searchingElement = "<title>" + searchingData + "</title>";

        if (fileData.contains(searchingElement)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    /**
     * This method checks if incoming data is present in page code
     * @param searchingData - incoming data
     * @return
     */
    public boolean checkPageContains(String searchingData) {
        Boolean result;
        String fileData = getData();
        if (fileData.contains(searchingData)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }


    /**
     * This method checks is i-net connection is available.
     * @return i-net connection is available - return true, else - false
     */
    public boolean isNetAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}
