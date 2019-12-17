package com.example.buddii.data;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class result<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    private result() {
    }

    @Override
    public String toString() {
        if (this instanceof result.Success) {
            result.Success success = (result.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof result.Error) {
            result.Error error = (result.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends result {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends result {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
