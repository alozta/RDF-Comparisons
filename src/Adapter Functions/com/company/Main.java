/*
 * This program's methods used as adapter functions to fit in to the data analytics platforms.
 * So it's not consistent or good looking. This program includes bunch of code fragments to used in different places for different actions.
 */

package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
* Fix the TANI data into Skytree format
*/
public class Main {

    public static void main(String[] args) {

        System.setProperty( "line.separator", "\n" );                   // prepare to produce Unix-format text files on a Windows machine
        //newTANIWithoutZero();
        //convertToLIBSVM();
        libsvmToCsv();
    }


    //format:   classID,att1,att2,...attn
    public static void libsvmToCsv(){
        int rowCount=0, columnIndex=0, maxColumn=22700;
        try {
            BufferedReader urun = new BufferedReader(new FileReader("feature_vector_whole_libsvm.txt"));
            String line;
	        int k=4;
	        PrintWriter writer = new PrintWriter("test_feature_vector_from_libsvm_"+k+".txt", "UTF-8");
	        for (int i = 0; i < maxColumn - 1; ++i) {
		        writer.print("V" + i + ",");
	        }
	        writer.print("Result\n");

	        while ((line = urun.readLine()) != null) {

		        if (rowCount >= 150001) {
			        String[] lineSplit1 = line.split(" ");
			        String result = lineSplit1[0];            //class

			        if (lineSplit1.length > 1) {                  //do not take customers with no items
				        int j = 1;
				        String[] lineSplit2 = lineSplit1[j].split(":");
				        for (int i = 0; i < maxColumn - 1; ++i) {
					        if (columnIndex == Integer.parseInt(lineSplit2[0])) {
						        writer.print(lineSplit2[1] + ",");
						        if (++j < lineSplit1.length) {
							        lineSplit2 = lineSplit1[j].split(":");
						        }
					        } else {
						        writer.print("0,");
					        }

					        /*if(Integer.parseInt(lineSplit2[0]) > maxColumn){        //set max column count
                                maxColumn=Integer.parseInt(lineSplit2[0]);
                            }*/
					        ++columnIndex;
				        }
				        writer.print(result + "\n");
				        columnIndex = 0;
				        if (rowCount % 1000 == 0)
					        System.out.println(rowCount + "/1150000");    //v1 user count 329638
			        }
			        if(rowCount%15000==0){
				        ++k;
				        writer.close();
				        writer = new PrintWriter("test_feature_vector_from_libsvm_"+k+".txt", "UTF-8");
				        for (int i = 0; i < maxColumn - 1; ++i) {
					        writer.print("V" + i + ",");
				        }
				        writer.print("Result\n");
			        }
			        if (rowCount == 1050000){
				        System.out.println("SUMMARY:\nrow/column: "+rowCount+"/"+maxColumn);
				        break;
			        }
		        }
		        ++rowCount;
	        }
            writer.close();

            //System.out.println("SUMMARY:\nrow/column: "+rowCount+"/"+maxColumn);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void convertToLIBSVM(){
        try {
            PrintWriter writer = new PrintWriter("feature_vector_whole_libsvm.txt", "UTF-8");
            BufferedReader urun = new BufferedReader(new FileReader("feature_vector_whole_user_trim_item_no_zero.txt"));

            String line=urun.readLine();        //ignore first line
            String [] headSplit = line.split(",");
            int features=headSplit.length-1;
            int regularCustomer=0, totalCustomers=0;

            while((line=urun.readLine())!=null){
                int itemCount=0;
                String [] lineSplit = line.split(",");
                for(int i=1; i<lineSplit.length; ++i){
                    if(!lineSplit[i].equals("")){
                        ++itemCount;
                    }
                }
                if(itemCount>10){
                    ++regularCustomer;
                    writer.print(0+" ");        //regular
	                for(int i=1; i<lineSplit.length; ++i){
		                if(!lineSplit[i].equals("")) {
			                writer.print(i + ":" + lineSplit[i] + " ");
		                }
	                }
	                writer.print("\n");
                }else if(itemCount>1){
                    writer.print(1+" ");        //not regular
	                for(int i=1; i<lineSplit.length; ++i){
		                if(!lineSplit[i].equals("")) {
			                writer.print(i + ":" + lineSplit[i] + " ");
		                }
	                }
	                writer.print("\n");
                }
                ++totalCustomers;
                if(totalCustomers%1000==0) System.out.println(totalCustomers+"/1150000");    //v1 user count 329638
                //if(totalCustomers==50000) break;
            }

            System.out.println("SUMMARY:\nRegular/Total: "+regularCustomer+"/"+totalCustomers+"\nFeatures: "+features);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //create feature vector
    public static void newTANIWithoutZero(){
        try {
            String line;
            //PrintWriter writer = new PrintWriter("feature_vector_trim_user_no_zero.txt", "UTF-8");
            PrintWriter writer = new PrintWriter("feature_vector_whole_user_trim_item_no_zero.txt", "UTF-8");
            BufferedReader urun = new BufferedReader(new FileReader("feature_vector_without_zero.txt"));

            int i=0,j=0,columnCount=0;

            line=urun.readLine();
            //writer.print(line+"\n");          //first line
            //ArrayList<Integer> items = new ArrayList<Integer>();
            String [] lineSplit = line.split(",");
            for(String s : lineSplit){
                //items.add(0);               //initialize column counts to 0
                ++columnCount;
            }
            columnCount/=2;     //get half of it
            for(String s : lineSplit){
                if(j>columnCount){
                    break;
                }
                writer.print(s+",");
                ++j;
            }
            writer.print("\n");

            while((line=urun.readLine())!=null){
                lineSplit = line.split(",");
                //int itemCount=-1;
                int k=0;
                for(String s : lineSplit){
                    if(k>columnCount){
                        break;
                    }
                    /*if(!s.equals("")){
                        //items.add(k, items.get(k)+1);
                        ++itemCount;
                        //writer.print(",");
                    }else{
                        //writer.print(s+",");
                    }*/
                    writer.print(s+",");
                    ++k;
                }
                writer.print("\n");
                /*if(itemCount>6) {
                    writer.print(line+"\n");
                    ++j;
                }*/
                ++i;
                if(i%1000==0){
                    System.out.println(j+" of 1000 users selected, "+(1170000-i)+" users left.");
                    //j=0;
                }
            }
            //i=0;
            /*for(Integer integer : items){
                System.out.print(integer+" ");
                if(i%200==0){
                    System.out.println();
                }
                ++i;
            }*/
            writer.close();
            urun.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //create feature vector
    public static void newTANI(){
        try {
            List<Long> urun_id = new ArrayList<Long>();
            List<UserItemIds> tuketici_item_id1 = new ArrayList<UserItemIds>();
            List<UserItemIds> tuketici_item_id2 = new ArrayList<UserItemIds>();
            String tuketiciLine, urunLine, detay1Line, detay2Line;
            PrintWriter writer = new PrintWriter("test_feature_vector_2.txt", "UTF-8");

            BufferedReader urun = new BufferedReader(new FileReader("urun.txt"));
            writer.print("0,");
            urunLine=urun.readLine();       //ignore first line
            while((urunLine=urun.readLine())!=null){                                                                                    //get urun_id file into dynamic
                String [] urunSplit = urunLine.split("\\|");      //ARTIKEL(BARKOD) @0 ITEM_ID
                writer.print(urunSplit[0]+",");
                try
                {
                    urun_id.add(Long.parseLong(urunSplit[0].trim()));
                }
                catch (NumberFormatException nfe){
                    System.out.println("<"+urunSplit[0].trim()+">");
                }
            }
            writer.print("0\n");
            urun.close();

            BufferedReader tuketici = new BufferedReader(new FileReader("tuketici.txt"));
            int i=0,tuketiciSize=-1;
            while((tuketiciLine=tuketici.readLine())!=null){++tuketiciSize;}
            tuketici.close();

            BufferedReader detay1 = new BufferedReader(new FileReader("alisveris_detay1.txt"));      //get detay1 file into dynamic
            detay1Line=detay1.readLine();                               //ignore first line
            while((detay1Line=detay1.readLine())!=null){
                String [] detay1Split = detay1Line.split("\\|");      //DWH_PARO_KOD @2, BARKOD @7 ITEM_ID
                tuketici_item_id1.add(new UserItemIds(Long.parseLong(detay1Split[2]),Long.parseLong(detay1Split[7])));
            }
            detay1.close();

            BufferedReader detay2 = new BufferedReader(new FileReader("alisveris_detay2.txt"));      //get detay2 file into dynamic
            detay2Line=detay2.readLine();           //ignore first line
            while((detay2Line=detay2.readLine())!=null){
                String [] detay2Split = detay2Line.split("\\|");      //DWH_PARO_KOD @2, BARKOD @7 ITEM_ID
                tuketici_item_id2.add(new UserItemIds(Long.parseLong(detay2Split[2]),Long.parseLong(detay2Split[7])));
            }
            detay2.close();

            Long tuketici_id=null;
            tuketici = new BufferedReader(new FileReader("tuketici.txt"));
            tuketiciLine=tuketici.readLine();           //ignore first line
            while((tuketiciLine=tuketici.readLine())!=null){
                List<Long> barcodes = new ArrayList<Long>();
                String [] tuketiciSplit = tuketiciLine.split("\\|");      //DWH_PARO_KOD @0 USER_ID
                try
                {
                    tuketici_id = Long.parseLong(tuketiciSplit[0].trim());
                }
                catch (NumberFormatException nfe){
                    System.out.println("<"+tuketiciSplit[0].trim()+">");
                }

                for(UserItemIds id : tuketici_item_id1){
                    if(tuketici_id == id.user_id){
                        barcodes.add(id.item_id);                   //add barcode
                    }
                }

                for(UserItemIds id : tuketici_item_id2){
                    if(tuketici_id == id.user_id){
                        barcodes.add(id.item_id);                   //add barcode
                    }
                }

                writer.print(tuketiciSplit[0]+",");

                for(Long id : urun_id){
                    long occurrences = countNumberEqual(barcodes, id);
                    if(occurrences>0){
                        writer.print(occurrences+",");
                    }else{
                        //writer.print(",");
                        writer.print("0,");
                    }
                }

                writer.print("0\n");
                ++i;
                if(i%100==0) {
                    System.out.println("tuketici: " + tuketici_id + ", done. " + i + "/" + tuketiciSize);
                }
                /*if(i==10)
                    break;*/
            }
            tuketici.close();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class UserItemIds{
        long item_id;
        long user_id;

        UserItemIds(long user_id, long item_id){
            this.item_id=item_id;
            this.user_id=user_id;
        }
    }

    //return number of occurrences in a list
    public static long countNumberEqual(List<Long> itemList, Long itemToCheck) {
        long count = itemList
                .stream()
                .filter(p -> p.equals(itemToCheck))
                .count();
        return count;
    }


    public static void calculateConfusionMatrix(String actualLabels, String predictedLabels){
        try {
            FileInputStream actual = new FileInputStream(actualLabels);
            BufferedReader actualReader = new BufferedReader(new InputStreamReader(actual));

            FileInputStream predicted = new FileInputStream(predictedLabels);
            BufferedReader predictedReader = new BufferedReader(new InputStreamReader(predicted));

            PrintWriter result = new PrintWriter("out_fixed\\Result\\result.txt", "UTF-8");

            String line1, line2;
            int tp=0, fp=0, tn=0, fn=0, number1, number2;
            while((line1=actualReader.readLine())!=null && (line2=predictedReader.readLine())!=null){   //these two files have equal lines, so no problem
                number1=Integer.parseInt(line1);
                number2=Integer.parseInt(line2);
                if(number1==1 && number2==1){           //true positive
                    ++tp;
                }else if(number1==1 && number2==-1){    //false negative
                    ++fn;
                }else if(number1==-1 && number2==1){    //false positive
                    ++fp;
                }else if(number1==-1 && number2==-1){   //true negative
                    ++tn;
                }
            }
            double precision=(double)tp/(double)(tp+fp);
            double recall=(double)tp/(double)(tp+fn);
            double trueNegativeRate=(double)tp/(double)(tp+fp);
            double accuracy=(double)(tp+tn)/(double)(tp+tn+fp+fn);

            result.println("Precision: "+precision+"\nRecall: "+recall+"\nTrue Negative Rate: "+trueNegativeRate+"\nAccuracy: "+accuracy+"\n\nConfusion matrix:");
            result.println((double)tp/(double)(tp+tn+fp+fn)+" "+(double)fp/(double)(tp+tn+fp+fn)+"\n"+(double)fn/(double)(tp+tn+fp+fn)+" "+(double)tn/(double)(tp+tn+fp+fn));
            System.out.println("Precision: "+precision+"\nRecall: "+recall+"\nTrue Negative Rate: "+trueNegativeRate+"\nAccuracy: "+accuracy+"\n\nConfusion matrix:");
            System.out.println((double)tp/(double)(tp+tn+fp+fn)+" "+(double)fp/(double)(tp+tn+fp+fn)+"\n"+(double)fn/(double)(tp+tn+fp+fn)+" "+(double)tn/(double)(tp+tn+fp+fn));

            predictedReader.close();
            actualReader.close();
            result.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    * Double quote elements(for one column) between spaces to fit skytree format
    */
    public static void fixFileFormat(String input, String output) throws IOException {
        String regexDigits = "[0-9]+";
        String regexDate = "\\d{4}-\\d{2}-\\d{2}";
        String regexFordId = "\\d{19}";

        FileInputStream in = null;
        PrintWriter out = null;

        try {
            in = new FileInputStream(input);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            out = new PrintWriter(output, "UTF-8");
            String line;

            //out.print("hurriyet_user_id, page_name, ford_user_id\n");

            while((line = reader.readLine()) != null){  //get all lines
                String tmp;
                String[] word = line.split("\\s+");     //split all whitespaces
                for(int i=0; i<word.length; ++i){
                    if(i==0){
                        out.print(word[i] + ",");       //print hurriyet_user_id
                    }else if(i==1){                      //print link
                        tmp = "\"" + word[i];
                        while(i<word.length && !word[i+1].matches(regexDate)){
                            ++i;
                            tmp = tmp + word[i];
                        }
                        tmp = tmp + "\"";
                        out.print(tmp.hashCode()+",");                     //print page name
                    }else if(word[i].matches(regexDate)){
                        //out.print("\"" + word[i] + ", ");
                        tmp="\"" + word[i] + ",";
                        ++i;
                        //out.print(word[i] + "\", ");          //date&time++i;
                        tmp+=word[i] + "\"";
                        out.print(tmp.hashCode()+",");
                        ++i;

                        tmp="\"" + word[i];
                        while(i<word.length-2){    //FORD : xxxx
                            ++i;
                            tmp = tmp + " " + word[i];
                        }
                        tmp = tmp + "\"";
                        out.print(tmp.hashCode()+",");                     //print page name
                    }else if(i==word.length-1){
                        out.print(word[i]);                 //print ford_user_id
                    }else{
                        //System.out.print("junk_"+i+": "+word[i]+"\n");
                    }
                }
                out.print("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        in.close();
        out.close();
    }


    /*
    * Add label information to matched ids.
    */
    public static void addLabels(int i){
        FileInputStream in_raw = null, labelFileInput = null, in_test_id =null;
        PrintWriter outTrain = null, outTest = null;
        try {
            /*outTrain = new PrintWriter("out_fixed\\Axion_Ford_Data_Combined_Train", "UTF-8");        //with labeled data
            outTest = new PrintWriter("out_fixed\\Axion_Ford_Data_Combined_Test", "UTF-8");         //without labeled data*/
            outTrain = new PrintWriter("out_fixed\\Skytree_Compatible\\hash\\Axion_Ford_Data_Train"+i, "UTF-8");        //with labeled data
            outTest = new PrintWriter("out_fixed\\Skytree_Compatible\\hash\\Axion_Ford_Data_Test"+i, "UTF-8");         //without labeled data
            outTrain.print("Hurriyet_ID, Link, Date, Page_Name, Ford_ID, Result"+"\n");
            outTest.print("Hurriyet_ID, Link, Date, Page_Name, Ford_ID, Result"+"\n");

            //in = new FileInputStream("out_fixed\\Axion_Ford_Data_Combined_Raw");                        //raw input file
            in_raw = new FileInputStream("TANI_original\\input\\webLog1\\Skytree_Compatible\\hash\\Axion_Ford_Data"+i);                        //raw input file
            BufferedReader reader = new BufferedReader(new InputStreamReader(in_raw));
            String labelLine, line;

            while((line = reader.readLine()) != null){                          //until raw file ends
                boolean match = false;
                String [] lineElements = line.split(",");
                labelFileInput = new FileInputStream("TANI_original\\input\\webLabel\\Test_Kamp_Sonuc.csv");    //read label file
                BufferedReader labelReader = new BufferedReader(new InputStreamReader(labelFileInput));

                while((labelLine = labelReader.readLine()) != null) {           //get all lines of label file
                    String [] labels = labelLine.split(",");
                    if(lineElements[0].equals(labels[0])){          //ids match and is not a id with same line
                        outTrain.print(line + "," + labels[1]+"\n");              //add combined data to train data
                        match=true;
                        break;
                    }
                }
                if(match==false){                                               //no match, add it to test data with predicted label
                    in_test_id = new FileInputStream("TANI_original\\result\\customerLastResultRandom.csv");
                    BufferedReader reader_test_id = new BufferedReader(new InputStreamReader(in_test_id));
                    while((labelLine = reader_test_id.readLine()) != null) {           //get all lines of label file
                        String id = labelLine.substring(1,labelLine.length()-1);
                        if(lineElements[0].equals(id)){          //ids match and is not a id with same line
                            outTest.print(line + ",1" +"\n");              //add combined data to train data
                            match=true;
                            break;
                        }
                    }
                    if(match==false) {
                        outTest.print(line + ",0" +"\n");                                       //add normal data to test
                    }
                }
            }
            outTest.close();
            outTrain.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    * Combine n files into 1 file named <String name>
    */
    public static void combineFiles(String name, int howMany){
        FileInputStream in = null;
        PrintWriter out = null;
        try {                                                       //open output file
            out = new PrintWriter("out_fixed\\Skytree_Compatible\\hash\\Axion_Ford_Data_Train", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        out.print("Hurriyet_ID, Link, Date, Page_Name, Ford_ID, Result\n");
        for(int i=1; i<=howMany; ++i){                              //get all files
            String inputFileName = name + new Integer(i).toString();
            try {
                in = new FileInputStream(inputFileName);            //open input file
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                reader.readLine();              //ignore first line
                while((line = reader.readLine()) != null) {         //get all lines of a file
                    out.print(line+"\n");                              //copy it to output file
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
