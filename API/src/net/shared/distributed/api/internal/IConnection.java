package net.shared.distributed.api.internal;

/**
 * Internal Connection object
 * @param <T> The real connection object
 */
public interface IConnection<T> {

    Integer GetId();
    T GetConnection();
    <U> void Send(U data);
    <U> void SendRouted(U data);

}
