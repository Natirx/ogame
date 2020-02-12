package domain.web.featuresimpl;

import utils.AllureUtils;
import utils.StringUtilInternal;

import java.util.List;

public class FeatureExecutor {
    private final List<Feature> features;

    public FeatureExecutor(List<Feature> features) {
        this.features = features;
    }

    public void execute() {
        features.forEach(f -> {
            String name = StringUtilInternal.beatifyCamelCaseString(f.getClass().getSimpleName());
            performFeature(name, f);
        });
    }

    private void performFeature(String name, Feature feature) {
        feature.init();
        feature.execute();
        AllureUtils.takeScreenshot(name);
    }
}