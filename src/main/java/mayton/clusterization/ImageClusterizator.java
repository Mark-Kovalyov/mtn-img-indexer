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

public class ImageClusterizator {

    public static Logger logger = LoggerFactory.getLogger(ImageClusterizator.class);

    private BufferedImage bufferedImage;
    private int clusters;
    private int maxIterations;

    public ImageClusterizator(BufferedImage bufferedImage, int clusters, int maxIterations) {
        this.bufferedImage = bufferedImage;
        this.clusters = clusters;
        this.maxIterations = maxIterations;
    }

    /**
     * public static void main(String[] args) {
     *         KMeansPlusPlusClusterer<DoublePoint> algorithm = new KMeansPlusPlusClusterer<>(2);
     *         List<DoublePoint> doublePoints = new ArrayList<>();
     *         Random random = new Random();
     *         for (int i = 0; i < 500; i++) {
     *             DoublePoint doublePoint = new DoublePoint(new double[] {100.0 + 30 * random.nextGaussian(),30 * random.nextGaussian()});
     *             doublePoints.add(doublePoint);
     *             DoublePoint doublePoint2 = new DoublePoint(new double[] {30 * random.nextGaussian(), 30 * random.nextGaussian() + 100.0});
     *             doublePoints.add(doublePoint2);
     *         }
     *         Collections.shuffle(doublePoints);
     *         List<CentroidCluster<DoublePoint>> result = algorithm.cluster(doublePoints);
     *         for(CentroidCluster resItem : result) {
     *             Arrays.stream(resItem.getCenter().getPoint()).forEach(v -> {
     *                 System.out.printf("%f ", v);
     *             });
     *             System.out.println();
     *         }
     *     }
     * @return
     */
    public List<CentroidCluster> clusterize() {
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
