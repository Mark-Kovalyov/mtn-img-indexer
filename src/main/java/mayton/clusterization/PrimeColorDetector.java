package mayton.clusterization;

import org.apache.commons.math3.ml.clustering.CentroidCluster;

import java.util.List;

public interface PrimeColorDetector {

    List<CentroidCluster> detect();

}
