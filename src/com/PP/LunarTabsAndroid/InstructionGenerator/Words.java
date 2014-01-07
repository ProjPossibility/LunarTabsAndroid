package com.PP.LunarTabsAndroid.InstructionGenerator;

import java.io.*;

import android.util.Log;

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
	 Log.d("TEST","X:"+x);
  switch((int)x)
  {
   case 1:
    return "one ";
   case 2:
    return "two ";
   case 3:
    return "three ";
   case 4:
    return "four ";
   case 5:
    return "five ";
   case 6:
    return "six ";
   case 7:
    return "seven ";
   case 8:
    return "eight ";
   case 9:
    return "nine ";
  }
  return "";
 }
 private String belowTwenty(long x)
 {
  if(x<10)
   return belowTen(x);
  switch((int)x)
  {
   case 10:
    return "ten ";
   case 11:
    return "eleven ";
   case 12:
    return "twelve ";
   case 13:
    return "thirteen ";
   case 14:
    return "fourteen ";
   case 15:
    return "fifteen ";
   case 16:
    return "sixteen ";
   case 17:
    return "seventeen ";
   case 18:
    return "eighteen ";
   case 19:
    return "nineteen ";
  }
  return "";
 }
 private String belowHundred(long x)
 {
  if(x<10)
   return belowTen(x);
  else if(x<20)
   return belowTwenty(x);
  switch((int)leftChars(x,1))
  {
   case 2:
    return "twenty "+belowTen(rightChars(x,1));
   case 3:
    return "thirty "+belowTen(rightChars(x,1));
   case 4:
    return "fourty "+belowTen(rightChars(x,1));
   case 5:
    return "fifty "+belowTen(rightChars(x,1));
   case 6:
    return "sixty "+belowTen(rightChars(x,1));
   case 7:
    return "seventy "+belowTen(rightChars(x,1));
   case 8:
    return "eighty "+belowTen(rightChars(x,1));
   case 9:
    return "ninety "+belowTen(rightChars(x,1));
  }
  return "";
 }
 private String belowThousand(long x)
 {
  if(x<10)
   return belowTen(x);
  else if(x<20)
   return belowTwenty(x);
  else if(x<100)
   return belowHundred(x);
  return belowTen(leftChars(x,1))+"Hundred "+belowHundred(rightChars(x,2));
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
  if(length(x)==4)
   return belowTen(leftChars(x,1))+"Thousand "+belowThousand(rightChars(x,3));
  else
   return belowHundred(leftChars(x,2))+"Thousand "+belowThousand(rightChars(x,3));
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
  if(length(x)==6)
   return belowTen(leftChars(x,1))+"Lakh "+belowLakh(rightChars(x,5));
  else
   return belowHundred(leftChars(x,2))+"Lakh "+belowLakh(rightChars(x,5));
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
  
  if(length(x)==8)
   return belowTen(leftChars(x,1))+"billion "+belowCrore(rightChars(x,7));
  else
   return belowHundred(leftChars(x,2))+"billion "+belowCrore(rightChars(x,7));
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
