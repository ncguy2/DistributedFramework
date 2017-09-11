package net.shared.distributed.plugin.string.tolower;

import net.shared.distributed.api.DistributedFunction;
import net.shared.distributed.api.IDistributedFunction;
import net.shared.distributed.api.internal.DistributionInfo;
import net.shared.distributed.api.internal.IDistributor;
import net.shared.distributed.plugin.string.common.StringSegmenter;

import java.util.Map;

@DistributedFunction(ToLower.class)
public class ToLowerFunction extends IDistributedFunction<String, ToLower> {

    public ToLowerFunction(IDistributor distributor) {
        super(distributor);
    }

    @Override
    public Class<ToLower> GetSegmentClass() {
        return ToLower.class;
    }

    @Override
    public ToLower SegmentPayload(DistributionInfo<String> info) {
        return new ToLower(StringSegmenter.Segment(info));
    }

    @Override
    public String ReformData(Map<Integer, Object> data) {
        return StringSegmenter.Reform(data);
    }
}
