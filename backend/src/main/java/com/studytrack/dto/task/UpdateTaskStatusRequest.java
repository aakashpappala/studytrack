package com.studytrack.dto.task;

import com.studytrack.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
public class UpdateTaskStatusRequest {
    @NotNull(message = "Status is required")
    private TaskStatus status;

    public UpdateTaskStatusRequest() {}

    public UpdateTaskStatusRequest(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }


    public static UpdateTaskStatusRequestBuilder builder() {
        return new UpdateTaskStatusRequestBuilder();
    }

    public static class UpdateTaskStatusRequestBuilder {
        private TaskStatus status;

        public UpdateTaskStatusRequestBuilder() {}

        public UpdateTaskStatusRequestBuilder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public UpdateTaskStatusRequest build() {
            return new UpdateTaskStatusRequest(this.status);
        }
    }

}
