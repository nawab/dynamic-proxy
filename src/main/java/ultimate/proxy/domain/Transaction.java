package ultimate.proxy.domain;

import lombok.Getter;

import java.time.Instant;

@Getter
public class Transaction {
    private Long startTime;
    private Long endTime;
    private RequestModel requestModel;
    private ResponseModel responseModel;

    private Transaction(long startTime) {
        this.startTime = startTime;
    }

    public static Transaction start() {
        return new Transaction(Instant.now().toEpochMilli());
    }

    public Transaction stop(RequestModel requestModel, ResponseModel responseModel) {
        this.requestModel = requestModel;
        this.responseModel = responseModel;
        endTime = Instant.now().toEpochMilli();
        return this;
    }

    public long getExecutionTime() {
        return endTime - startTime;
    }
}
