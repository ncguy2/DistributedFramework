package net.shared.distributed;

public class RoutedResponse<T> {

    public T coreResponse;

    public RoutedResponse() {
    }

    public RoutedResponse(T coreResponse) {
        this.coreResponse = coreResponse;
    }
}
