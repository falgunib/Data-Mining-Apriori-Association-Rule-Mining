import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class AprioriAlgo {
	
	static Map<String, Integer> GetFrequency = new HashMap<String, Integer>();
	static SortedSet<String> TotalFrequencySet = new TreeSet<String>();
		static boolean wantRules = false;

	public static void main(String[] args) throws Exception
	
	{
		//System.out.println("enter your file path");
		ConcurrentHashMap<String, Integer> FreqMap1 = new ConcurrentHashMap<String, Integer>();
		ConcurrentHashMap<String, Integer> FreqMap2 = new ConcurrentHashMap<String, Integer>();
		ConcurrentHashMap<String, Integer> FreqMap0 = new ConcurrentHashMap<String, Integer>();
		int d = 0;
		Set<Rules> rules = new HashSet<Rules>();
		String[] ruleArray=new String[FreqMap1.size()];
		
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		String file = "/Users/falgunibharadwaj/Downloads/gene_expression.txt";
		String filePath = file;
		filePath = filePath.replace("\\", "/");
		
		System.out.println("Enter support value in decimal (Percentage/100):");
		double support = Double.valueOf(reader.readLine().trim()).doubleValue();
		if(support == 0.5){
			wantRules = true;
		}
		int totalSamples = 0;
		BufferedReader sample = new BufferedReader(new FileReader(file));
		while(sample.readLine()!=null)
			totalSamples+=1;
		int sampleCount = -1;
		double supportItem = (support * totalSamples);
			
		BufferedReader list = new BufferedReader(new FileReader(file));
		String str;
		HashMap<String, Integer> Frequency = new HashMap<String, Integer>();
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("PART ONE RESULTS");
		System.out.println("----------------------------------------------------------------------------------------------------");
		while((str = list.readLine())!= null){
			sampleCount++;
			StringTokenizer token = new StringTokenizer(str, "\t");
			
			token.nextToken();
			int tokenCount = 0;
			while(token.hasMoreTokens()){
				String oneToken = token.nextToken();
				tokenCount++;
				
				if(oneToken.toLowerCase().contains("up")){
					itemMap(Frequency, "G" + Integer.toString(tokenCount) + "up");
					GetFrequency.put(Integer.toString(sampleCount) + "@G" + Integer.toString(tokenCount) + "up", sampleCount);
					
				} else if(oneToken.toLowerCase().contains("down")){
					itemMap(Frequency, "G" + Integer.toString(tokenCount) + "down");
				
					GetFrequency.put(Integer.toString(sampleCount) + "@G" + Integer.toString(tokenCount) + "down", sampleCount);
				} else if(oneToken.toLowerCase().contains("all")){
					itemMap(Frequency, "G" + Integer.toString(tokenCount) + "all");
					GetFrequency.put(Integer.toString(sampleCount) + "@G" + Integer.toString(tokenCount) + "all", sampleCount);
				}else if(oneToken.toLowerCase().contains("all")){
					itemMap(Frequency, "G" + Integer.toString(tokenCount) + "aml");
					GetFrequency.put(Integer.toString(sampleCount) + "@G" + Integer.toString(tokenCount) + "aml", sampleCount);
				}else if(oneToken.toLowerCase().contains("all")){
					itemMap(Frequency, "G" + Integer.toString(tokenCount) + "breast cancer");
					GetFrequency.put(Integer.toString(sampleCount) + "@G" + Integer.toString(tokenCount) + "breast cancer", sampleCount);
				}else if(oneToken.toLowerCase().contains("all")){
					itemMap(Frequency, "G" + Integer.toString(tokenCount) + "colon cancer");
					GetFrequency.put(Integer.toString(sampleCount) + "@G" + Integer.toString(tokenCount) + "colon cancer", sampleCount);
				}
				else
				{
					itemMap(Frequency, oneToken);
					GetFrequency.put(Integer.toString(sampleCount) + "@"+ oneToken, sampleCount);
				}
				
			}
			
		}
		list.close();
		
		SortedSet<String> set1 = FrequencyWithSupport(Frequency, supportItem);
		TotalFrequencySet.addAll(set1);
		Iterator<String> itr = set1.iterator();
		System.out.print("Level 1: " );
		int count = 0;
		while(itr.hasNext()){
			String s = itr.next();
			//System.out.println(s);
			count++;
			
		}
		System.out.println("\t"+count);
		System.out.println("----------------------------------------------------------------");
		//Creating FreqMap0 for rules
		FreqMap0 = PrunedFreqMapNoComma(Frequency, supportItem);
		/*System.out.println("Freq0000000");
		for(String sr: FreqMap0.keySet()){
			System.out.println(sr+" "+FreqMap0.get(sr));
		}*/
		
		int i =2;
		while(!set1.isEmpty()){
			int i1=0,i2=0;
			System.out.print("Level " + i+": ");
			Map<String, Integer> map1 = CandidateSelection(set1, i, supportItem);
			//System.out.println("Back from CS");
			set1.clear();
			set1 = FrequencyWithSupport(map1, supportItem);
			//System.out.println("Back from FWS");
			itr = set1.iterator();
			count=0;
			while(itr.hasNext()){
				String s = itr.next();
				//System.out.println(s);
				count++;
				Frequency.put(s, map1.get(s));
				
			}
			if(i==2){
				FreqMap1 = PrunedFreqMap(Frequency, supportItem);
				for(String sr: FreqMap1.keySet()){
					//System.out.println(sr+" "+FreqMap1.get(sr));
					i1++;
				}
				//System.out.println("Elements in FreqMap1 "+i1);
			}
			if(i==3){
				FreqMap2 = PrunedFreqMap(Frequency, supportItem);
				//Iterator<String> itr77 = F.iterator();
				for(String sr1: FreqMap2.keySet()){
					if(FreqMap1.containsKey(sr1))
						FreqMap2.remove(sr1);
					//System.out.println(sr+" "+FreqMap2.get(sr));
					//i1++;
				}
				/*for(String sr: FreqMap2.keySet()){
					System.out.println(sr+" "+FreqMap2.get(sr));
					i2++;
				}
				System.out.println("Elements in FreqMap1 "+i2);
				//System.out.println("Elements in FreqMap1 "+i1);*/
				
			}
			TotalFrequencySet.addAll(set1);
			i++;
			System.out.println("\t"+count);
			System.out.println("----------------------------------------------------------------");
			
		}
		
		
		System.out.println("Total number of frequent ITEM SETS: " + TotalFrequencySet.size());
		ConcurrentHashMap<String, Integer> FreqMap = new ConcurrentHashMap<String, Integer>();
		FreqMap = PrunedFreqMap(Frequency, supportItem);
		
		
		
		//Rules for FreqMap1-----------------------------------------
		
		if(wantRules == true){//This is set true only when support = 50%
		for(String s:FreqMap1.keySet()){
			ruleArray=FreqMap1.keySet().toArray(new String[FreqMap1.size()]);
			d++;	//System.out.println(s+" "+FreqMap1.get(s));
		}
		int sp;
		String left,right;
		//System.out.println("Value of ruleArray count: "+d);
		double confidence = 0.6, conf;
		
		for(int z=0;z<ruleArray.length;z++){
			//System.out.println(ruleArray[z]);
			String parts[] = ruleArray[z].split(",");
			for(i=0;i<2;i++)
			for(int j=i+1;j<2;j++){
				//System.out.println("i: "+parts[i]+" "+FreqMap0.get(parts[i])+" j: "+parts[j]+FreqMap0.get(parts[j]));
				//A->B
				double conf1 = (double)FreqMap0.get(parts[i])/(double)FreqMap0.get(parts[j]);
				
				sp = FreqMap0.get(parts[i]);
				left=parts[j];
				right=parts[i].replace(parts[j],"");
				Rules rule = new Rules();
				if(conf1>=confidence ){
					rule.setValues(left,right,conf1,sp);
					rules.add(rule);
				}

				
				//B->A
				double conf2 = (double)FreqMap0.get(parts[j])/(double)FreqMap0.get(parts[i]);
					
				sp = FreqMap0.get(parts[j]);
				left=parts[i];
				right=parts[j].replace(parts[i],"");
				Rules rule1 = new Rules();
				if(conf2>=confidence){
					rule1.setValues(left,right,conf2,sp);
					rules.add(rule1);
				}
				
			}
		}
		d = 0;
		for(Rules r:rules){
			//if(r.head.contains("G5up") || r.body.contains("G5up"))
			//System.out.println("head: "+r.head.replace("-",",")+" body: "+r.body.replace("-",",")+" confidence:"+r.confidence+" support: "+r.support);
			d++;
		}
		
	//	System.out.println("Number of rules generated: "+d);
		
		
		//Rules--------level 3 thingy-------------------------------------------------------------
		String[] ruleArray2=new String[FreqMap2.size()];
		for(String s:FreqMap2.keySet()){
			ruleArray2=FreqMap2.keySet().toArray(new String[FreqMap2.size()]);
			d++;	//System.out.println(s+" "+FreqMap2.get(s));
		}
		
		for(int z = 0;z<ruleArray2.length;z++){
			String parts[] = ruleArray2[z].split(",");
			for(i=0;i<3;i++){
				
				for(int j=(i+1)%3;j<3;j=j+3){
					//System.out.println("i="+i+"    j="+j+"    j+1="+(j+1)%3);
					conf = (double)FreqMap2.get(ruleArray2[z])/(double)FreqMap0.get(parts[i]);
					String tempBody = parts[j]+","+parts[(j+1)%3];//could change order
					String tempB = parts[(j+1)%3] +","+parts[j];
					//System.out.println("Parts: "+parts[i]+" Temp: "+parts[j]+",,,,"+parts[(j+1)%3]);
					//System.out.println("TempBody: "+tempBody+" TempB "+tempB+" parts:"+parts[i]);
					//if(parts[i]!=parts[j] && parts[i]!= parts[(j+1)%3]){
					
					right = tempBody;
						
					
					int supp = FreqMap0.get(parts[i]);
					left=parts[i];
					
					//System.out.println(right);
					Rules rule = new Rules();
					if(conf>=confidence ){
						rule.setValues(left,right,conf,supp);
						if(!(rules.contains(rule)))
						rules.add(rule);
					}
				//}
				}
			}
			for(i=0;i<3;i++){
				for(int j=(i+2)%3;j<3;j=j+3){
					String tempBody = parts[i]+","+parts[(i+1)%3];//could change order
					String tempB = parts[(i+1)%3] +","+parts[i];
					if(parts[i]!=parts[j] && parts[j]!= parts[(i+1)%3]){
					if(FreqMap.get(tempBody)==null){
						conf = (double)FreqMap2.get(ruleArray2[z])/(double)FreqMap1.get(tempB);
						//System.out.println("!!!!!!!!"+tempBody);
						left = tempBody;
					}
					else{
						conf = (double)FreqMap2.get(ruleArray2[z])/(double)FreqMap1.get(tempBody);
						//System.out.println("!!!!!!!!"+tempB);
						left = tempB;
					}
					
					int supp = FreqMap0.get(parts[i]);
					right=parts[j];
					
					//System.out.println(right);
					Rules rule = new Rules();
					if(conf>=confidence ){
						rule.setValues(left,right,conf,supp);
						if(!(rules.contains(rule)))
						rules.add(rule);
					}
				}
				}
			}
		}
		
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("PART TWO RESULTS: ");
		System.out.println("----------------------------------------------------------------------------------------------------");
		
		d = 0;
		for(Rules r:rules){
			//System.out.println(r.head+" "+r.body+" "+r.confidence);
			d++;
		}
		
		System.out.println("Number of rules generated: "+d);
		
		/* TEMPLATE 1
		1. RULE HAS ANY OF G6_UP
		2. RULE HAS 1 OF G1_UP
		3. RULE HAS 1 OF (G1_UP, G10_DOWN)
		4. BODY HAS ANY OF G6_UP
		5. BODY HAS NONE OF G72_UP
		6. BODY HAS 1 OF (G1_UP, G10_DOWN)
		7. HEAD HAS ANY OF G6_UP
		8. HEAD HAS NONE OF (G1_UP, G6_UP)
		9. HEAD HAS 1 OF (G6_UP, G8_UP)
		10. RULE HAS 1 OF (G1_UP, G6_UP, G72_UP)
		11. RULE HAS ANY OF (G1_UP, G6_UP, G72_UP)
		*/
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("Template 1 results");
		System.out.println("----------------------------------------------------------------------------------------------------");
		//Q1 RULE HAS ANY OF G6_UP
		
		d = 0;
		for(Rules r:rules){
			if(r.head.contains("G6up") || r.body.contains("G6up"))
			//System.out.println("head: "+r.head.replace("-",",")+" body: "+r.body.replace("-",",")+" confidence:"+r.confidence+" support: "+r.support);
			d++;
		}
		System.out.println("Rules generated for query 1: RULE HAS ANY OF G6_UP: 			"+d);
		
		//Q2 RULE HAS 1 OF G1_UP
		d = 0;
		for(Rules r:rules){
			if(r.head.contains("G1up") && (r.body.contains("G1up")))
			d--;
			if(r.body.contains("G1up") || (r.head.contains("G1up"))) d++;
		}
		System.out.println("Rules generated for query 2: RULE HAS 1 OF G1_UP: 			"+d);
		
		//Q3 RULE HAS 1 OF (G1_UP, G10_DOWN)
		d = 0;
		for(Rules r:rules){
			
			if(r.body.contains("G1up") && (r.head.contains("G1up"))) d--;
			
			else if(r.body.contains("G10down") && (r.head.contains("G10down"))) d--;
			else if(r.body.contains("G1up") && (r.head.contains("G10down"))) d--;
			
			else if(r.body.contains("G10down") && (r.head.contains("G1up"))) d--;
			
			if(r.body.contains("G1up") || (r.head.contains("G1up")) || (r.body.contains("G10down") || (r.head.contains("G10down"))) )d++;
			
		}
		System.out.println("Rules generated for query 3: RULE HAS 1 OF (G1_UP, G10_DOWN): 		"+d);
		
		
		
		//Q4 BODY HAS ANY OF G6_UP
		d = 0;
		for(Rules r:rules){
			if(r.body.contains("G6up"))
			//System.out.println("head: "+r.head.replace("-",",")+" body: "+r.body.replace("-",",")+" confidence:"+r.confidence+" support: "+r.support);
			d++;
		}
		System.out.println("Rules generated for query 4: BODY HAS ANY OF G6_UP: 			"+d);
		
		//Q5 BODY HAS NONE OF G72_UP
		//!!!!!!!!!!Getting 126 should get 124
		d = 0;
		for(Rules r:rules){
			if(!(r.body.contains("G72up"))) d++;
			//else//System.out.println("head: "+r.head.replace("-",",")+" body: "+r.body.replace("-",",")+" confidence:"+r.confidence+" support: "+r.support);
			//d++;
		}
		System.out.println("Rules generated for query 5: BODY HAS NONE OF G72_UP: 			"+d);
		
		//Q6 BODY HAS 1 OF (G1_UP, G10_DOWN)
		d = 0;
		for(Rules r:rules){
			if((r.body.contains("G1up") && (r.body.contains("G10down"))))
				d--;
			else if((r.body.contains("G1up")) || r.body.contains("G10down")) d++;
					
		}
		System.out.println("Rules generated for query 6: BODY HAS 1 OF (G1_UP, G10_DOWN): 		"+d);
		
		//Q7 HEAD HAS ANY OF G6_UP
		d = 0;
		for(Rules r:rules){
			if(r.head.contains("G6up"))
			//System.out.println("head: "+r.head.replace("-",",")+" body: "+r.body.replace("-",",")+" confidence:"+r.confidence+" support: "+r.support);
			d++;
		}
		System.out.println("Rules generated for query 7: HEAD HAS ANY OF G6_UP: 			"+d);
		
		//Q8 HEAD HAS NONE OF (G1_UP, G6_UP)
		//126? getting 133
		d = 0;
		for(Rules r:rules){
			if(!(r.head.contains("G6up")) && !(r.head.contains("G1up")))
			//System.out.println("head: "+r.head.replace("-",",")+" body: "+r.body.replace("-",",")+" confidence:"+r.confidence+" support: "+r.support);
			d++;
		}
		System.out.println("Rules generated for query 8: HEAD HAS NONE OF (G1_UP, G6_UP): 		"+d);
		
		//Q9  HEAD HAS 1 OF (G6_UP, G8_UP)
		d = 0;
		for(Rules r:rules){
			if(r.head.contains("G6up") && r.head.contains("G8up")) d--;
			//System.out.println("head: "+r.head.replace("-",",")+" body: "+r.body.replace("-",",")+" confidence:"+r.confidence+" support: "+r.support);
			else if(r.head.contains("G6up") || r.head.contains("G8up")) d++;
		}
		System.out.println("Rules generated for query 9: HEAD HAS 1 OF (G6_UP, G8_UP):		"+d);
		
		//Q10 RULE HAS 1 OF (G1_UP, G6_UP, G72_UP)
		d = 0;
		for(Rules r:rules){
			if(r.body.contains("G1up") && (r.head.contains("G1up"))) d--;
			
			if(r.body.contains("G6up") && (r.head.contains("G6up"))) d--;
			if(r.body.contains("G1up") && (r.head.contains("G6up"))) d--;
			if(r.body.contains("G6up") && (r.head.contains("G1up"))) d--;
			if(r.body.contains("G72up") && (r.head.contains("G1up"))) d--;			
			if(r.body.contains("G72up") && (r.head.contains("G6up"))) d--;
			 if(r.body.contains("G1up") && (r.head.contains("G72up"))) d--;
			 if(r.body.contains("G6up") && (r.head.contains("G72up"))) d--;
			 if(r.body.contains("G72up") && (r.head.contains("G72up"))) d--;
			
			if(r.body.contains("G1up") || (r.head.contains("G1up")) || (r.body.contains("G6up") || (r.head.contains("G6up")) || r.body.contains("G72up") || (r.head.contains("G72up")))) d++;
			
		}
		System.out.println("Rules generated for query 10: RULE HAS 1 OF (G1_UP, G6_UP, G72_UP): 	"+d);
		
		//Q11 RULE HAS ANY OF (G1_UP, G6_UP, G72_UP)
		d = 0;
		for(Rules r:rules){
			if(r.head.contains("G1up") || r.body.contains("G1up") || (r.body.contains("G6up") || r.head.contains("G6up") || r.body.contains("G72up") || r.head.contains("G72up")))
			d++;
			/*if(r.head.contains("G1up") || r.body.contains("G1up"))	d++;
			if(r.body.contains("G6up") || (r.head.contains("G6up"))) d++;
			if(r.body.contains("G72up") || (r.head.contains("G72up"))) d++;*/
		}
		System.out.println("Rules generated for query 11: RULE HAS ANY OF (G1_UP, G6_UP, G72_UP):	"+d);
		
		
		//Template 2
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("Template 2 results");
		System.out.println("----------------------------------------------------------------------------------------------------");
		//Query 1
		d=0;
		for(Rules r:rules){
			if(r.head.contains(",")|| r.body.contains(",")) 
				{d++; //System.out.println(r.head+","+r.body);
				}		}
		System.out.println("Rules generated for query 1: SIZE OF RULE >= 3: 	"+d);
		
		//Query 2
		d=0;
		for(Rules r:rules){
			if(r.body.contains(",")) 
				{d++; //System.out.println(r.head+","+r.body);
				}		
		}
		System.out.println("Rules generated for query 2: SIZE OF BODY >= 2: 	"+d);
		
		//Query 3
		d=0;
		for(Rules r:rules){
			if(r.head.contains(",")) 
				{d++; //System.out.println(r.head+","+r.body);
				}		
		}
		System.out.println("Rules generated for query 3: SIZE OF HEAD >= 2: 	"+d);
		//Template 3	
		System.out.println("----------------------------------------------------------------------------------------------------");
		System.out.println("Template 3 results");
		System.out.println("----------------------------------------------------------------------------------------------------");
		//1. BODY HAS ANY OF G1_UP AND HEAD HAS 1 OF G59_UP
		d=0;
		for(Rules r:rules){
			//System.out.println(r.head+","+r.body);
			if(r.body.contains("G1up")){
				if(r.head.contains("G59up")) d++;
				//System.out.println(r.head+","+r.body);
			}
			
		}
		System.out.println("Rules generated for query 1 BODY HAS ANY OF G1_UP AND HEAD HAS 1 OF G59_UP:		"+d);
		
		//2. BODY HAS ANY OF G1_UP OR HEAD HAS 1 OF G6_UP
		d=0;
		for(Rules r:rules){
			//System.out.println(r.head+","+r.body);
			if((r.body.contains("G1up")) || (r.head.contains("G6up"))) d++;
				//System.out.println(r.head+","+r.body);
			
			
		}
		System.out.println("Rules generated for query 2 BODY HAS ANY OF G1_UP OR HEAD HAS 1 OF G6_UP: 		"+d);
		
		//3. BODY HAS 1 OF G1_UP OR HEAD HAS 2 OF G6_UP
		d=0;
		for(Rules r:rules){
			//System.out.println(r.head+","+r.body);
			if((r.body.contains("G1up")) )
					if(!(r.head.contains("G1up"))) d++;
				//System.out.println(r.head+","+r.body);
			
			
		}
		System.out.println("Rules generated for query 3 BODY HAS 1 OF G1_UP OR HEAD HAS 2 OF G6_UP: 		"+d);
		
		//4. HEAD HAS 1 OF G1_UP AND BODY HAS 0 OF DISEASE
		//NOT COMING
		d=0;
		for(Rules r:rules){
			//System.out.println(r.head+","+r.body);
			if((r.head.contains("G1up")) && !(r.body.contains("G1up"))) d++;
			//	System.out.println(r.head+","+r.body);}
			
			
		}
		System.out.println("Rules generated for query 4 HEAD HAS 1 OF G1_UP AND BODY HAS 0 OF DISEASE: 		"+d);
		
		//5. HEAD HAS 1 OF DISEASE OR RULE HAS 1 OF (G72_UP, G96_DOWN)
		d=0;
		for(Rules r:rules){
			//System.out.println(r.head+","+r.body);
			
			if(r.body.contains("G72up") && (r.head.contains("G72up"))) d--;
			
			if(r.body.contains("G96down") && (r.head.contains("G96down"))) d--;
			if(r.body.contains("G72up") && (r.head.contains("G96down"))) d--;
			
			if(r.body.contains("G96down") && (r.head.contains("G72up"))) d--;
			if(r.body.contains("G96down") && (r.body.contains("G72up"))) d--;
			if(r.head.contains("G96down") && (r.head.contains("G72up"))) d--;
			
			if((r.head.contains("disease") || r.body.contains("G72up") || (r.head.contains("G72up")) || (r.body.contains("G96down") || (r.head.contains("G96down"))))) d++;
			
			
		}
		System.out.println("Rules generated for query 5 HEAD HAS 1 OF DISEASE OR RULE HAS 1 OF (G72_UP, G96_DOWN): 	"+d);
		
		//6. BODY HAS 1 of (G59_UP, G96_DOWN) AND SIZE OF RULE >=3
		d=0;
		for(Rules r:rules){
			//System.out.println(r.head+","+r.body);
			if(((r.head.contains(",")|| r.body.contains(",")))) {
				if((r.body.contains("G59up") && (r.body.contains("G96down"))))
					d--;
				if((r.body.contains("G59up")) || r.body.contains("G96down")) d++;
				//System.out.println("||"+r.body);
				}
			
			
		}
		System.out.println("Rules generated for query 6 BODY HAS 1 of (G59_UP, G96_DOWN) AND SIZE OF RULE >=3: 	"+d);
		
	
	}
		
		sample.close();
	}// end of main
	
	
	private static Map<String, Integer> CandidateSelection(SortedSet<String> set1, int level, double supportItems) throws IOException{
		int totalSamples = 100;
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		SortedSet<String> sortSet = new TreeSet<String>();
		
		Iterator<String> itr = set1.iterator();
		while(itr.hasNext()){
			String str1 = (String) itr.next();
			StringTokenizer tokenizer = new StringTokenizer(str1,",");
			if(tokenizer.countTokens()!= level -1){
				System.out.println("Error count tokens: " + tokenizer.countTokens() + "level: " + level);
				return returnMap;
			}
			while(tokenizer.hasMoreTokens()){
				String token = tokenizer.nextToken();
				sortSet.add(token);
			}
		}
		String[] ele = sortSet.toArray(new String[0]);
		sortSet.clear();
		
		if(level > ele.length)
			return returnMap;
		
		int[] indices;
		MakeCombination pairs = new MakeCombination(ele.length, level);
		while(pairs.hasMore()){
			SortedSet<String> tempFreqItems = new TreeSet<String>();
			indices = pairs.getNext();
			for(int i=0; i< indices.length; i++){
				tempFreqItems.add(ele[indices[i]].toString());
			}
			if(tempFreqItems.size() == level){
				String temp = (tempFreqItems.toString());
				temp = removeSpace(temp);
				temp = temp.substring(1, temp.length()-1);
				
				for(int i=0; i< totalSamples; i++){
					String key = Integer.toString(i);
					
					int match = 0;
					String newString = temp;
					StringTokenizer tokenString = new StringTokenizer(newString, ",");
					if(tokenString.countTokens()!= level){
						System.out.println("error in token count");
						System.exit(level);
					}
					while(tokenString.hasMoreTokens()){
						try{
						String token = tokenString.nextToken();
						
						if(GetFrequency.containsKey(key + "@" + token))
							match++;
						else
							match=0;
						}catch(Exception e){
							System.out.println("error!");
						}
					}
					
					if(match == level){
						itemMap(returnMap, newString);
					}
				}
			}
		}// end of while
		
		return returnMap;
	}
	
	private static String removeSpace(String s) {
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t = "";
		while (st.hasMoreElements())
			t += st.nextElement();
		return t;
	}
	
	private static void itemMap(Map<String, Integer> map, String str) {
		if (map.containsKey(str)) {
			int i = map.get(str);
			map.put(str, ++i);
			
		} else {
			map.put(str, 1);
		}
		

	}
	
	private static SortedSet<String> FrequencyWithSupport(Map<String, Integer> freqMap, double supportItem) {

		SortedSet<String> returnSortedSet = new TreeSet<String>();
		Iterator<String> itr = freqMap.keySet().iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			int freqVal = freqMap.get(str);
			if (freqVal >= supportItem){
				
				returnSortedSet.add(str);}
		}

		return returnSortedSet;
	}
	private static ConcurrentHashMap<String, Integer> PrunedFreqMap(Map<String, Integer> freqMap, double supportItem) {

		ConcurrentHashMap<String, Integer> returnFreqMap = new ConcurrentHashMap<String, Integer>();
		Iterator<String> itr = freqMap.keySet().iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			int freqVal = freqMap.get(str);
			if (freqVal >= supportItem && str.contains(",")){
				
				returnFreqMap.put(str, freqVal);}
		}

		return returnFreqMap;
	}
	private static ConcurrentHashMap<String, Integer> PrunedFreqMapNoComma(Map<String, Integer> freqMap, double supportItem) {

		ConcurrentHashMap<String, Integer> returnFreqMap = new ConcurrentHashMap<String, Integer>();
		Iterator<String> itr = freqMap.keySet().iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			int freqVal = freqMap.get(str);
			if (freqVal >= supportItem){
				
				returnFreqMap.put(str, freqVal);}
		}

		return returnFreqMap;
	}
	
}
class Rules{
	 
	public String head;
	public String body;
	public int head_count;
	public int body_count;
	public double confidence;
	public int support;
	
	public void setValues(String l,String r, double cf, int sp){
		head=l;
		body=r;
		confidence=cf;
		support=sp;
	}
	public void set_conf(double a){
		
	}
	public void set_support(int a){
		
	}
	
	
	
	
}
