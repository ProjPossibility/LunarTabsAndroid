package com.PP.LunarTabsAndroid.InstructionGenerator;

import com.PP.LunarTabsAndroid.UI.DataModel;
import com.PP.LunarTabsAndroid.UI.ResourceModel;
import com.example.lunartabsandroid.R;

import android.content.Context;

/**
 * This class specially written to convert the given number into words. It will support upto 1 crore.
 * @author SANTHOSH REDDY MANDADI
 * @version release: 2_0_48
 * @since April 03 2008
 */
public class Words
{
 long num;
 
 private Words()
 {
  num=0;
 }
 
 private Words(long num)
 {
  this.num=num;
 }
 
 public void setNumber(long num)
 {
  this.num=num;
 }
 
 public long getNumber()
 {
  return num;
 }
 
 public static Words getInstance(long num)
 {
  return new Words(num);
 }
 
 public static String leftChars(String str,int n)
 {
  if(str.length()<=n)
   return str;
  else
   return str.substring(0,n);
 }
 
 public static String rightChars(String str,int n)
 {
  if(str.length()<=n)
   return str;
  else
   return str.substring(str.length()-n,str.length());
 }
 
 public long leftChars(int n)
 {
  return new Long(leftChars(new Long(num).toString(),n)).longValue();
 }
 
 public long rightChars(int n)
 {
  return new Long(rightChars(new Long(num).toString(),n)).longValue();
 }
 
 public long leftChars(long num,int n)
 {
  return new Long(leftChars(new Long(num).toString(),n)).longValue();
 }
 
 public long rightChars(long num,int n)
 {
  return new Long(rightChars(new Long(num).toString(),n)).longValue();
 }
 
 public int length(long num)
 {
  return new Long(num).toString().length();
 }
 
 private String belowTen(long x)
 {
	 return ResourceModel.getInstance().BASE_NUMBERS[(int) (x-1)];
 }

 private String belowTwenty(long x)
 {
  if(x<10) {
   return belowTen(x);
  }
  else {
	 return ResourceModel.getInstance().BASE_NUMBERS[(int) (x-1)];
  }

 }
 private String belowHundred(long x) {
	 if(x<10) {
		 return belowTen(x);
	 }
	 else if(x<20) {
		 return belowTwenty(x);
	 }
	 else {
		 int index = (int) leftChars(x,1);
		 return ResourceModel.getInstance().TENS_DIGIT[index-2] + " " + belowTen(rightChars(x,1));
	 }
 }

 private String belowThousand(long x)
 {
  if(x<10)
   return belowTen(x);
  else if(x<20)
   return belowTwenty(x);
  else if(x<100)
   return belowHundred(x);
  return belowTen(leftChars(x,1))+ResourceModel.getInstance().THOUSAND+" "+belowHundred(rightChars(x,2));
 }
 private String belowLakh(long x)
 {
  if(x<10)
   return belowTen(x);
  else if(x<20)
   return belowTwenty(x);
  else if(x<100)
   return belowHundred(x);
  else if(x<1000)
   return belowThousand(x);
  if(length(x)==4) {
	  return belowTen(leftChars(x,1))+ResourceModel.getInstance().THOUSAND+" "+belowThousand(rightChars(x,3));
  }
  else {
	  return belowHundred(leftChars(x,2))+ResourceModel.getInstance().THOUSAND+" "+belowThousand(rightChars(x,3));
  }
 }
 public String belowCrore(long x)
 {
  if(x<10)
   return belowTen(x);
  else if(x<20)
   return belowTwenty(x);
  else if(x<100)
   return belowHundred(x);
  else if(x<1000)
   return belowThousand(x);
  else if(x<100000)
   return belowLakh(x);
  if(length(x)==6) {
	  return belowTen(leftChars(x,1))+ResourceModel.getInstance().MILLION+" "+belowLakh(rightChars(x,5));
  }
  else {
	  return belowHundred(leftChars(x,2))+ResourceModel.getInstance().MILLION+" "+belowLakh(rightChars(x,5));
  }
 }
 
 public String belowBilion(long x)
 {
	 
  if(x<10)
   return belowTen(x);
  else if(x<20)
   return belowTwenty(x);
  else if(x<100)
   return belowHundred(x);
  else if(x<1000)
   return belowThousand(x);
  else if(x<100000)
   return belowLakh(x);
  else if(x<100000000)
   return belowCrore(x);
  
  if(length(x)==8) {
	  return belowTen(leftChars(x,1))+ResourceModel.getInstance().BILLION+" "+belowCrore(rightChars(x,7));
  }
  else {
	  return belowHundred(leftChars(x,2))+ResourceModel.getInstance().BILLION+" "+belowCrore(rightChars(x,7));
  }
 }
 
 public String getNumberInWords()
 {
  if(num<10)
   return belowTen(num);
  else if(num<20)
   return belowTwenty(num);
  else if(num<100)
   return belowHundred(num);
  else if(num<1000)
   return belowThousand(num);
  else if(num<100000)
   return belowLakh(num);
  else if(num<10000000)
   return belowCrore(num);
  else if(num<1000000000)
   return belowBilion(num);
  return "";
 }
}
