import java.util.ArrayList;
import java.util.List;

public class Manager {
    //    final String example = "Example: open \"https://www.tut.by/\" \"2\" \n        " +
//            "checkPageTitle \"Google Search Page\"";
    int timeOut;
    long startTime;
    long endTime;
    long operationTime;
    Operation operation = null;
    List<Operation> operationList = new ArrayList<Operation>();
    StringBuilder statistic = new StringBuilder();
    StringBuilder errors = new StringBuilder();
    HtmlManager htmlManager = new HtmlManager();


    /**
     * This method is application core. It integrates all classes and  methods
     *
     * @param incomingFileName
     * @param outputFileName
     */
    public void run(String incomingFileName, String outputFileName) {

        if (!htmlManager.isNetAvailable()) {
            System.out.println("[Check internet connection]");
            System.exit(0);
        }

        FileManager fileManager = new FileManager();
        List<String> operationFromFile = fileManager.readFile(incomingFileName);
        for (String item : operationFromFile) {
            Operation i = splitOperation(item);
            //According to operation in file this switch runs operation execution
            switch (i.getOperationCase()) {
                case 1: {
                    operationList.add(openUrlOperation(i.getTarget(), i.getTimeOut()));
                    break;
                }
                case 2: {
                    operationList.add(checkHrefTagOperation(i.getTarget()));
                    break;
                }
                case 3: {
                    operationList.add(checkHrefNameOperation(i.getTarget()));
                    break;
                }
                case 4: {
                    operationList.add(checkTitleTagOperation(i.getTarget()));
                    break;
                }
                case 5: {
                    operationList.add(checkPageContainsOperation(i.getTarget()));
                    break;
                }
                default: {
                    break;
                }
            }
        }
        fileManager.writeFile(outputFileName, statisticForLog(operationList));
    }


    /**
     * This method runs, calculate execution time and create a new 'checkPageContains' operation
     * @param target element to check
     * @return new Operation
     */
    public Operation checkPageContainsOperation(String target) {
        startTime = System.currentTimeMillis();
        Boolean openResult = htmlManager.checkPageContains(target);
        endTime = System.currentTimeMillis();
        operationTime = endTime - startTime;
        timeOut = 0;
        operation = new Operation(OperationTypes.checkPageContains, target,
                operationTime, openResult, timeOut);
        return operation;
    }


    /**
     * This method runs, calculate execution time and create a new 'checkPageTitle' operation
     * @param target element to check
     * @return new Operation
     */
    public Operation checkTitleTagOperation(String target) {
        startTime = System.currentTimeMillis();
        Boolean openResult = htmlManager.checkTitleTag(target);
        endTime = System.currentTimeMillis();
        operationTime = endTime - startTime;
        timeOut = 0;
        operation = new Operation(OperationTypes.checkPageTitle, target,
                operationTime, openResult, timeOut);
        return operation;
    }


    /**
     * This method runs, calculate execution time and create a new 'checkLinkPresentByName' operation
     * @param target element to check
     * @return new Operation
     */
    public Operation checkHrefNameOperation(String target) {
        startTime = System.currentTimeMillis();
        Boolean openResult = htmlManager.checkHrefName(target);
        endTime = System.currentTimeMillis();
        operationTime = endTime - startTime;
        timeOut = 0;
        operation = new Operation(OperationTypes.checkLinkPresentByName, target,
                operationTime, openResult, timeOut);
        return operation;
    }


    /**
     * This method runs, calculate execution time and create a new 'checkLinkPresentByHref' operation
     * @param target element to check
     * @return new Operation
     */
    public Operation checkHrefTagOperation(String target) {
        startTime = System.currentTimeMillis();
        Boolean openResult = htmlManager.checkHrefTag(target);
        endTime = System.currentTimeMillis();
        operationTime = endTime - startTime;
        timeOut = 0;
        operation = new Operation(OperationTypes.checkLinkPresentByHref, target,
                operationTime, openResult, timeOut);
        return operation;
    }


    /**
     * This method runs, calculate execution time and create a new 'open' operation
     * @param target element to check
     * @return new Operation
     */
    public Operation openUrlOperation(String target, int timeOut) {
        startTime = System.currentTimeMillis();

        Boolean openResult = htmlManager.readURL(target, timeOut);
        endTime = System.currentTimeMillis();
        operationTime = endTime - startTime;
        if(operationTime > timeOut*1000){
            openResult = false;
        }
        operation = new Operation(OperationTypes.open, target,
                operationTime, openResult, timeOut);
        return operation;
    }


    /**
     * This method splits an incoming line to operation, timeout and check target
     * @param line
     * @return
     */
    public Operation splitOperation(String line) {
        OperationTypes operationType;
        String target = null;
        int timeOut = 0;
        int operationCase;
        String[] array = line.split("\"");

        if (array[0].trim().equalsIgnoreCase("open")) {
            operationType = OperationTypes.open;
            operationCase = 1;
        } else if (array[0].trim().equalsIgnoreCase("checkLinkPresentByHref")) {
            operationType = OperationTypes.checkLinkPresentByHref;
            operationCase = 2;
        } else if (array[0].trim().equalsIgnoreCase("checkLinkPresentByName")) {
            operationType = OperationTypes.checkLinkPresentByName;
            operationCase = 3;
        } else if (array[0].trim().equalsIgnoreCase("checkPageTitle")) {
            operationType = OperationTypes.checkPageTitle;
            operationCase = 4;
        } else if (array[0].trim().equalsIgnoreCase("checkPageContains")) {
            operationType = OperationTypes.checkPageContains;
            operationCase = 5;
        } else {
            operationType = null;
            operationCase = 0;
        }
        try {
            if (!array[1].equals(null)) {
                target = array[1];
            } else {
                target = null;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            errors.append("[The source data must be an shown after operation ")
                    .append(line)
                    .append("]")
                    .append(e.toString());
        }

        if(array[0].trim().equalsIgnoreCase("open")) {
            try {
                //Array element with index 3, was used
                // because line was split off by ". So array element with index 2 wil be equals " "
                //open "http://www.google.com" "3"
                timeOut = Integer.parseInt(array[3]);
            } catch (ArrayIndexOutOfBoundsException e) {
                timeOut = 0;
            } catch (NumberFormatException e) {
                System.out.println("[The timeout must be an integer number in shows in file " + e.toString());
            }
        }
        return new Operation(operationType, target, timeOut, operationCase);
    }


    /**
     * This method create statistic information for writing to log file
     * @param list
     * @return
     */
    public String statisticForLog(List<Operation> list) {
        long totalTime = 0;
        int positiveTests = 0;

        for (Operation tst : list) {
            statistic.append(tst.toString());
            totalTime = totalTime + tst.getOperationTime();
            if (tst.getOperationResult() == true) {
                positiveTests++;
            }
        }
        long averageTime = totalTime / list.size();
        statistic.append("Total tests: ")
                .append(list.size())
                .append("\n")
                .append("Passed/Failed: ")
                .append(positiveTests)
                .append("/")
                .append(list.size() - positiveTests)
                .append("\n")
                .append("Total time: ")
                .append(operation.timeFormatter(totalTime))
                .append("\n")
                .append("Average time: ")
                .append(operation.timeFormatter(averageTime))
                .append("\n");

        return statistic.toString();
    }


    /**
     * This method check the extension of txt - file
     *
     * @param arg
     * @return
     */
    static boolean isCorrect(String arg) {
        if (arg.endsWith(".txt")) {
            return true;
        } else {
            return false;
        }
    }
}
