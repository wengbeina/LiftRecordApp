package cn.edu.zucc.TPF.util;

public class DataDeal {

	public DataDeal() {
		// TODO Auto-generated constructor stub
	}
	
	public static float getCheckedData(float[] source){
		float sum = 0;
		float ave0 = 0;
		float ave1 = 0; 
		float sumVariance = 0;
		float variance = 0;
		float standard = 0;
		int countError = 0;
		float factor = 1;
		for(int i=0; i<source.length; i++){
			sum += source[i];
		}
		ave0 = sum/source.length;
		
		for(int i=0; i<source.length; i++){
			sumVariance += Math.pow(ave0 - source[i], 2);
		}
		variance = sumVariance/source.length;
		standard = (float)Math.sqrt(variance);
		
		for(int i=0;i<source.length;i++){
			if(Math.abs(source[i] - ave0 ) > factor*standard){
				sum -= source[i];
				countError ++;
			}
		}
		if(countError > 0)
		   ave1 = sum/(source.length - countError);
		else
		   ave1 = ave0;
		return ave1;		
	}
	
	public static void main(String[] args){
		float[] aaa ={2,3,4};
		float result = getCheckedData(aaa);
		System.out.println(result);
	}

}
