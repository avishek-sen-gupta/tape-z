package com.mojo.visualisation;

import com.mojo.algorithms.visualisation.GraphvizStyleScheme;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;

public class HLASMFlowchartStylePreferences {
    public static final GraphvizStyleScheme DECISION = new GraphvizStyleScheme(Color.CHOCOLATE4, Color.WHITE, Shape.DIAMOND);
    public static final GraphvizStyleScheme SECTION_START = new GraphvizStyleScheme(Color.DARKGREEN, Color.WHITE, Shape.BOX);
    public static final GraphvizStyleScheme SECTION_END = new GraphvizStyleScheme(Color.RED, Color.WHITE, Shape.BOX);
    public static final GraphvizStyleScheme PROCESSING = new GraphvizStyleScheme(Color.WHEAT, Color.BLACK);
    public static final GraphvizStyleScheme JOIN = new GraphvizStyleScheme(Color.WHEAT, Color.BLACK, Shape.POINT);
}
