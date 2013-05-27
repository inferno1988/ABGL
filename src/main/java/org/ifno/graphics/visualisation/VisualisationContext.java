package org.ifno.graphics.visualisation;

import android.content.Context;
import org.ifno.graphics.visualisation.strategies.SimpleSpectrumVisualisationStrategy;
import org.ifno.graphics.visualisation.strategies.SpectrogramVisualisationStrategy;
import org.ifno.graphics.visualisation.strategies.VisualisationStrategy;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: inferno
 * Date: 21.04.13
 * Time: 21:00
 * Palamarchuk Maksym © 2013
 */
public class VisualisationContext {
    private final ConcurrentHashMap<Class<? extends VisualisationStrategy>, VisualisationStrategy> visualisationStrategyMap = new ConcurrentHashMap<Class<? extends VisualisationStrategy>, VisualisationStrategy>();
    private final Context context;

    public VisualisationContext(Context context) {
        this.context = context;
    }

    public void registerStrategy(VisualisationStrategy visualisationStrategy) {
        visualisationStrategyMap.put(visualisationStrategy.getClass(), visualisationStrategy);
    }

    public VisualisationStrategy getVisualisationStrategy(Class<? extends VisualisationStrategy> strategy) {
        VisualisationStrategy visualisationStrategy = visualisationStrategyMap.get(strategy);
        if (visualisationStrategy != null)
            return visualisationStrategy;
        else
            throw new IllegalArgumentException("Requested strategy is not registered.");
    }
}
