package net.shared.distributed.api.internal;

/**
 * Data required for the segmentation on the host
 * @param <T> The data type
 */
public class DistributionInfo<T> {
    public int current;
    public int max;
    public T targetData;
}