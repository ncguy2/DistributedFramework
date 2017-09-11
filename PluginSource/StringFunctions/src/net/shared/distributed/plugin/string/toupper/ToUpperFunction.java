package net.shared.distributed.plugin.string.toupper;

import net.shared.distributed.api.DistributedFunction;
import net.shared.distributed.api.IDistributedFunction;
import net.shared.distributed.api.internal.DistributionInfo;
import net.shared.distributed.api.internal.IDistributor;
import net.shared.distributed.plugin.string.common.StringSegmenter;

import java.util.Map;

@DistributedFunction(ToUpper.class)
public class ToUpperFunction extends IDistributedFunction<String, ToUpper> {

    public ToUpperFunction(IDistributor distributor) {
        super(distributor);
    }

    @Override
    public Class<ToUpper> GetSegmentClass() {
        return ToUpper.class;
    }

    @Override
    public ToUpper SegmentPayload(DistributionInfo<String> info) {
        return new ToUpper(StringSegmenter.Segment(info));
    }

    @Override
    public String ReformData(Map<Integer, Object> data) {
        return StringSegmenter.Reform(data);
    }
}
