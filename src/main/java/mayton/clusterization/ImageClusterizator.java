package mayton.clusterization;

import mayton.ImageIndexer;
import mayton.ImageUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageClusterizator implements PrimeColorDetector {

    public static Logger logger = LoggerFactory.getLogger(ImageClusterizator.class);

    private BufferedImage bufferedImage;
    private int clusters;
    private int maxIterations;

    public ImageClusterizator(BufferedImage bufferedImage, int clusters, int maxIterations) {
        this.bufferedImage = bufferedImage;
        this.clusters = clusters;
        this.maxIterations = maxIterations;
    }

    @Override
    public List<CentroidCluster> detect() {
        // Euclidian distance
        // TODO: Implement R:G:B color ratio
        DistanceMeasure distance = new EuclideanDistance();

        KMeansPlusPlusClusterer kMeansPlusPlusClusterer = new KMeansPlusPlusClusterer(clusters, maxIterations, distance);
        // Generate n clusters
        Collection<DoublePoint> points = new ArrayList();
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                DoublePoint doublePoint = new DoublePoint(new double[]{
                        ImageUtils.getRPixel(rgb) / 255.0,
                        ImageUtils.getGPixel(rgb) / 255.0,
                        ImageUtils.getBPixel(rgb) / 255.0,
                });
                points.add(doublePoint);
            }
        }
        // Find most popular cluster

        List<CentroidCluster> centroidClusters = kMeansPlusPlusClusterer.cluster(points);

        centroidClusters.forEach((item) -> {
            logger.info("centroid : {}", item.getCenter());
        });

        return centroidClusters;
    }
}
