package bachelorthesis.clustering.data;

import java.util.ArrayList;
import java.util.List;

public class ShapeGenerator {

    private int dim;
    private ArbitraryShape shape;
    private DataGenerator generator;

    public ShapeGenerator(ArbitraryShape shape) {
        this.shape = shape;
        dim = shape.getDim();
        generator = new DataGenerator(dim);
    }

    public List<DataPoint> generateShape(int dataSetSize) {

        List<DataPoint> dataPoints = new ArrayList<>();
        int numberSegments = shape.getSegments().size();
        int modulus;
        Segment segment;
        for (int i = 0; i < dataSetSize; ++i) {

            modulus = i % numberSegments;
            segment = shape.getSegment(modulus);
            dataPoints.add(generator.generateDataPoint(segment.getMean(), segment.getDeviation()));
        }
        return dataPoints;
    }
}
