/**
 * This class allows to create an operation unit with defined parameters
 */
public class Operation {
    OperationTypes operationType;
    String target;
    Long operationTime;
    Boolean operationResult;
    int timeOut;
    int operationCase;


    public Operation(OperationTypes operationType, String target, int timeOut,
                     int operationCase) {
        this.operationType = operationType;
        this.target = target;
        this.timeOut = timeOut;
        this.operationCase = operationCase;
    }


    public Operation(OperationTypes operationType, String target, Long operationTime,
                     Boolean operationResult, int timeOut) {
        this.operationType = operationType;
        this.target = target;
        this.operationTime = operationTime;
        this.operationResult = operationResult;
        this.timeOut = timeOut;
    }


    public String getTarget() {
        return target;
    }


    public Long getOperationTime() {
        return operationTime;
    }


    public Boolean getOperationResult() {
        return operationResult;
    }


    public int getTimeOut() {
        return timeOut;
    }


    public OperationTypes getOperationType() {
        return operationType;
    }


    public int getOperationCase() {
        return operationCase;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (operationResult == true) {
            sb.append("+");
        } else {
            sb.append("-");
        }

        sb.append(" [")
                .append(operationType)
                .append(" \"")
                .append(target)
                .append("\" ");

        if(timeOut !=0){
            sb.append("\"")
                    .append(timeOut)
                    .append("\"");
        }

            sb.append("] ")
            .append(timeFormatter(operationTime))
            .append("\n");
        return sb.toString();
    }


    /**
     * This method convert milliseconds to seconds or minutes and returns a sting
     * @param millis - incoming time in milliseconds
     * @return
     */
    public String timeFormatter(Long millis){
        StringBuilder stringBuilder = new StringBuilder();
        double seconds = (double)millis/1000;
        if(seconds >=60) {
            seconds = seconds%60;
            int minutes = (int) ((millis/ (1000*60)) % 60);
            stringBuilder.append(minutes)
                    .append(" min. ")
                    .append(String.format("%.3f", seconds))
                    .append("sec.");
        }
        else{

            stringBuilder.append(String.format("%.3f", seconds))
                    .append(" sec.");
        }
        return stringBuilder.toString();
    }
}
