package mayton.clusterization;

import mayton.ImageUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageClusterizator {

    private BufferedImage bufferedImage;
    private int clusters;
    private int iterations;

    public ImageClusterizator(BufferedImage bufferedImage, int clusters, int iterations) {
        this.bufferedImage = bufferedImage;
        this.clusters = clusters;
        this.iterations = iterations;
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
    public String getMostPowerClusterCenter() {
        KMeansPlusPlusClusterer kMeansPlusPlusClusterer = new KMeansPlusPlusClusterer(clusters);
        Collection<DoublePoint> points = new ArrayList();
        for(int y = 0;y<bufferedImage.getHeight();y++) {

        }
        DoublePoint doublePoint = new DoublePoint(new double[] { 0.0, 0.0, 0.0 });

        List<CentroidCluster> centroidClusters = kMeansPlusPlusClusterer.cluster(points);


        return ImageUtils.tripleToHex(ImmutableTriple.of(0.0,0.0,0.0));
    }
}
