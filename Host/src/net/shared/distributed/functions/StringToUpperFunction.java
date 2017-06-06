package net.shared.distributed.functions;

import net.shared.distributed.distributor.Distributor;
import net.shared.distributed.logging.Logger;

import java.util.Map;

@DistributedFunction("String::ToUpper")
public class StringToUpperFunction extends IDistributedFunction<String, StringToUpper> {


    public StringToUpperFunction(Distributor distributor) {
        super(distributor);
    }

    @Override
    public Class<StringToUpper> GetSegmentClass() {
        return StringToUpper.class;
    }

    @Override
    public StringToUpper SegmentPayload(Distributor.DistributionInfo<String> info) {
        int blockSize = info.targetData.length() / info.max;
        String[] parts = new String[info.max];
        int processedLength = 0;
        for (int i = 0; i < info.max; i++) {
            if(i == info.max - 1)
                parts[i] = info.targetData.substring(processedLength);
            else
                parts[i] = info.targetData.substring(processedLength, processedLength + blockSize);
            processedLength += blockSize;
        }

        String substring = parts[info.current];
        return new StringToUpper(substring);
    }

    @Override
    public String ReformData(Map<Integer, Object> data) {
        final StringBuilder sb = new StringBuilder();
        data.keySet()
                .stream()
                .sorted()
                .forEach(id -> {
                    Object o = data.get(id);
                    if(o instanceof StringToUpper)
                        sb.append(((StringToUpper) o).target);
                });
        return sb.toString();
    }

    @Override
    public void UseData(String original, String data) {
        Logger.instance().Info("StringToUpper function completed");
        Logger.instance().Debug("\tOriginal: " + original);
        Logger.instance().Debug("\tProcessed: " + data);
    }

}
