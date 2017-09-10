package net.shared.distributed.functions;

import net.shared.distributed.api.DistributedFunction;
import net.shared.distributed.api.IDistributedFunction;
import net.shared.distributed.api.internal.DistributionInfo;
import net.shared.distributed.distributor.Distributor;

import java.util.Map;

@DistributedFunction(StringToLower.class)
public class StringToLowerFunction extends IDistributedFunction<String, StringToLower> {

    public StringToLowerFunction(Distributor distributor) {
        super(distributor);
    }

    @Override
    public Class<StringToLower> GetSegmentClass() {
        return StringToLower.class;
    }

    @Override
    public StringToLower SegmentPayload(DistributionInfo<String> info) {
        int blockSize = info.targetData.length() / info.max;
        String[] parts = new String[info.max];
        int processedLength = 0;
        for (int i = 0; i < info.max; i++) {
            if(i == info.max - 1) parts[i] = info.targetData.substring(processedLength);
            else parts[i] = info.targetData.substring(processedLength, processedLength + blockSize);
            processedLength += blockSize;
        }
        String substr = parts[info.current];
        return new StringToLower(substr);
    }

    @Override
    public String ReformData(Map<Integer, Object> data) {
        return null;
    }

}
