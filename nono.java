package noursalem;
import java.util.*;
import java.io.*;                                        //file reader and file writer

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;
//CRUD CREATE READ UPDATE DELETE
public class nono{
	public static void main (String args[]) {
	}}
class Task {
	String id;                                             //special for every task
	String title;
	String description;
	String priority;                                       //low , med , high
	String category;
	String status;                                         //new , in_progress , done
	int duration;
	boolean completed;
	LocalDate duedate;
	String attach;
	String repeat;                                          // None , Daily , weekly , monthly
	  Task() {                                              //constructor
		 id = java.util.UUID.randomUUID().toString();       // random id
		 completed = false;
		 repeat = "None";
	 }
	                                                         //built in method toString but i edit it to be fit
	  
	  public String toString() {
		return title + " ["+priority+"] ["+status+"] ["+(duedate==null?"":duedate)+"] ["+duration+"min] ["+category+"]" + (attach!=null?" [Attachment]":"") + (completed?" [Completed]":"") + " [Repeat: "+repeat+"]";
				
	  }	
}

class Storage{
	
}