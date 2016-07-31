package com.company;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;
import org.codehaus.janino.Java;
import java.util.HashMap;

import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.mllib.util.MLUtils;

import scala.Tuple2;

public class JavaRandomForestClassificationExample {

    public static void main(String[] args) {

        //create configuration options
        SparkConf sparkConf = new SparkConf().setAppName("JavaRandomForestClassificationExample").setMaster("local[4]").set("spark.executor.memory", "4g");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // Load and parse the data file.
        String datapath = "feature_vector_whole_libsvm.txt";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(jsc.sc(), datapath, 22000, 210).toJavaRDD().persist(StorageLevel.MEMORY_AND_DISK());     //trimmed'in 22000,48, whole'un 22000,210

        // Split the data into training and test sets (30% held out for testing)
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

        // Train a RandomForest model.
        // Empty categoricalFeaturesInfo indicates all features are continuous.
        Integer numClasses = 2;
        HashMap<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        Integer numTrees = 3; // Use more in practice.****
        String featureSubsetStrategy = "auto"; // Let the algorithm choose.
        String impurity = "gini";
        Integer maxDepth = 5;
        Integer maxBins = 16;
        Integer seed = 12345;

        /*final RandomForestModel model = RandomForest.trainClassifier(trainingData, numClasses,
                categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins,
                seed);*/
        final RandomForestModel model =  RandomForestModel.load(jsc.sc(), "myRandomForestClassificationModel_v1");

        // Evaluate model on test instances and compute test error
        JavaPairRDD<Double, Double> predictionAndLabel =
                data.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {       //testData
                    @Override
                    public Tuple2<Double, Double> call(LabeledPoint p) {
                        return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
                    }
                });
        Double testErr =
                1.0 * predictionAndLabel.filter(new Function<Tuple2<Double, Double>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<Double, Double> pl) {
                        return !pl._1().equals(pl._2());
                    }
                }).count() / data.count();                                          //testData
        System.out.println("Test Error: " + testErr + "Test Accuracy: " + (1-testErr));
        System.out.println("Learned classification forest model:\n" + model.toDebugString());

        // Save and load model
        //model.save(jsc.sc(), "myRandomForestClassificationModel");
        //RandomForestModel sameModel = RandomForestModel.load(jsc.sc(), "myRandomForestClassificationModel");
    }

        /*//convert file to rdd
        SparkConf sparkConf = new SparkConf().setAppName("Recommendation").setMaster("local").set("spark.driver.allowMultipleContexts", "true");

        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<String> featureVectorCsv = sc.textFile("feature_vector_trim_user_item_no_zero.txt");
        featureVectorCsv.saveAsTextFile("feature_vector_trim_user_item_no_zero");*/

        /*JavaRDD<String> productReyon = featureVectorCsv.map(new Function<String, String>() {

            @Override
            public String call(String arg0) throws Exception {
                // TODO Auto-generated method stub

                String[] urun = arg0.split("\\|");
                return urun[0];
            }
        });*/

        //productReyon.saveAsTextFile("/home/inosens/CS_ALL/TANI/Tanı_new/"+"reyon");

        /*System.out.println("reyon num :" + productReyon.distinct().count());

        JavaRDD<String> alısverıs = sc.textFile("alısveris_detay_all.txt");
        //alısverıs.saveAsTextFile("/home/inosens/CS_ALL/TANI/Tanı_new/" + "alısverıss");
        JavaPairRDD<String,String> userUrun =  alısverıs.mapToPair(new PairFunction<String, String,String>() {

            @Override
            public Tuple2<String, String> call(String arg0) throws Exception {
                String[] kullanıcıUrun = arg0.split("\\|");
                return new Tuple2<String, String> (kullanıcıUrun[2],kullanıcıUrun[7]);
            }
        });
        //userUrun.saveAsTextFile("/home/inosens/CS_ALL/TANI/Tanı_new/" + "userUrun");

        userUrun.groupByKey().saveAsTextFile("TANInew\\" + "groupByKey");


    }*/

}
