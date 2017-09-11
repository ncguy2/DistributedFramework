package net.shared.distributed.plugin.string.common;

import net.shared.distributed.api.internal.DistributionInfo;
import net.shared.distributed.plugin.string.toupper.ToUpper;

import java.util.Map;

public class StringSegmenter {

    public static String Segment(DistributionInfo<String> info) {

        int blockSize = info.targetData.length() / info.max;
        String[] parts = new String[info.max];

        int processedLength = 0;
        for (int i = 0; i < info.max; i++) {

            if(i == info.max - 1)
                parts[i] = info.targetData.substring(processedLength);
            else parts[i] = info.targetData.substring(processedLength, processedLength + blockSize);

            processedLength += blockSize;
        }

        return parts[info.current];
    }

    public static String Reform(Map<Integer, Object> data) {
        final StringBuilder sb = new StringBuilder();
        data.keySet()
                .stream()
                .sorted()
                .forEach(id -> {
                    Object o = data.get(id);
                    if(o instanceof ToUpper)
                        sb.append(((ToUpper) o).target);
                });
        return sb.toString();
    }

}
