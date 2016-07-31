package com.rf.real;

import java.util.ArrayList;

public class MainRun {
	@SuppressWarnings("static-access")
	public static void main(String args[]){

		long startTime=System.currentTimeMillis();

		ArrayList<ArrayList<DTree>> models = new ArrayList<ArrayList<DTree>>();
		ArrayList<Double> accuracy = new ArrayList<Double>();

		String traindata="Q:\\Documents\\Bitirme\\Feature Vector\\train_feature_vector_from_libsvm_";
		String testdata="Q:\\Documents\\Bitirme\\Feature Vector\\test_feature_vector_from_libsvm_";
		int numTrees=1;
		int categ=1;

		//TRAINING
		//train to 7 initial train files and create models from them
		for(int i=1; i<=7; ++i){
			//DescribeTrees DT = new DescribeTrees(traindata+i+".txt");
			//ArrayList<int[]> Input=DT.CreateInput(traindata+i+".txt");

			RandomForest RaF =new RandomForest(numTrees, null, null, ""+i);     //if 2nd and 3rd parameters are null get saved model file name from 4th parameter
			//RaF.C=categ;            //n.o. categorical columns
			//RaF.M=Input.get(0).length-1;
			//RaF.Ms=(int)Math.round(Math.log(RaF.M)/Math.log(2)+1);
			RaF.Start();

			models.add(RaF.getModel());
			RaF=null;
			System.out.println("Model "+i+"/7");
		}

		//MINI-TESTING
		/*for(int i=1; i<=3; ++i){
			DescribeTrees DTT = new DescribeTrees(traindata+i+".txt");
			ArrayList<int[]> Test=DTT.CreateInput(testdata+i+".txt");

			for(ArrayList<DTree> dt : models){              //test for all models, ensemble later
				accuracy.add(RandomForest.TestForest(dt, Test));
			}
			System.out.println("Test "+i+"/3");
		}*/

		//TESTING
		for(int i=1; i<=35; ++i){
			DescribeTrees DTT = new DescribeTrees(traindata+i+".txt");
			ArrayList<int[]> Test=DTT.CreateInput(testdata+i+".txt");

			for(ArrayList<DTree> dt : models){              //test for all models, ensemble later
				accuracy.add(RandomForest.TestForest(dt, Test));
			}
			System.out.println("Test "+i+"/35");
		}

		System.out.print("Accuracy: ");
		double average=0;
		for(Double d : accuracy){
			System.out.print(d+"%, ");
			average+=d;
		}
		average=average/accuracy.size();
		System.out.println("\nAverage accuracy: "+average+"%");

		int s=(int)(System.currentTimeMillis()-startTime)/1000;
		int h=(int)Math.floor(s/((double)3600));
		s-=(h*3600);
		int m=(int)Math.floor(s/((double)60));
		s-=(m*60);
		System.out.println("Time elapsed: "+h+"hr "+m+"m "+s+"s");

		//DescribeTrees DT = new DescribeTrees(traindata);
		//ArrayList<int[]> Input=DT.CreateInput(traindata);
        //int categ=1;

        /*for(int[] ll : Input) {
            for(int l : ll){
                System.out.print(l+",");
            }
            System.out.println();
        }*/

		//DescribeTrees DTT = new DescribeTrees(traindata);
		//ArrayList<int[]> Test=DTT.CreateInput(testdata);
		
		/*for(int k=0;k<Input.size();k++){
			if(Input.get(k)[Input.get(k).length-1]<categ)
                continue;
            else{
                categ=(int)Input.get(k)[Input.get(k).length-1];
            }
        }*/
		
		//RandomForest RaF =new RandomForest(numTrees, Input, Test);
		//RaF.C=categ;            //n.o. categorical columns
		//RaF.M=Input.get(0).length-1;
		//RaF.Ms=(int)Math.round(Math.log(RaF.M)/Math.log(2)+1);
		//RaF.Start();

	}
}
