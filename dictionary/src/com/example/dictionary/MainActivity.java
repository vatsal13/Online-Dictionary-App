package com.example.dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	
	EditText gettext;
	TextView outtext;
	TextView maintext;
	String mainsound="";
	String durl;
	ProgressDialog progDailog;
	Button b1;
	ImageButton b2;
	int a=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gettext=(EditText) findViewById(R.id.editText1);
		outtext=(TextView) findViewById(R.id.textView1);
		maintext=(TextView) findViewById(R.id.textView2);
		b1=(Button) findViewById(R.id.button1);
		b2=(ImageButton) findViewById(R.id.Button2);
		b1.setEnabled(false);
		b1.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.dbutton));
		b2.setEnabled(false);
		b2.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.daudio));
		


		gettext.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String g = gettext.getText().toString().toLowerCase();
				String splitstr[]=g.split(" ");
				if(g.contentEquals("")){
					b1.setEnabled(false);
					b1.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.dbutton));
					b2.setEnabled(false);
					b2.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.daudio));
				}
				else{
					if(splitstr.length>1){
						b1.setEnabled(false);
						b1.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.dbutton));
						b2.setEnabled(false);
						b2.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.daudio));
					}
					else{
					b1.setEnabled(true);
					b1.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.button));
					b2.setEnabled(true);
					b2.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.audio));
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	
	}
	
	
	public void func(View v){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(gettext.getWindowToken(), 0);
		maintext.setText("");
		DownloadWebPageTask task = new DownloadWebPageTask();
		String g = gettext.getText().toString().toLowerCase().trim();
	    task.execute(new String[] { "http://www.dictionaryapi.com/api/v1/references/collegiate/xml/"+g+"?key=732fabe9-0c1a-4636-a8a5-1253da14194e" });
	}
	public void func2(View v){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(gettext.getWindowToken(), 0);
		if(a==1){
		DownloadAudioTask task2 = new DownloadAudioTask();
		String url ="http://media.merriam-webster.com/soundc11/";
		String subdir="";
	
		if((int)mainsound.charAt(0)>=48 && (int)mainsound.charAt(0)<=57 ){
			int i=0;
			while((int)mainsound.charAt(i)>=48 && (int)mainsound.charAt(i)<=57){
			i++;
			}
			subdir=mainsound.substring(0,i);
		}
		else{
		if(mainsound.substring(0, 2).contentEquals("gg")){
			subdir="gg";
		}
		else if(mainsound.substring(0, 3).contentEquals("bix")){
			subdir="bix";
		}
		else{
			subdir=mainsound.substring(0,1);
		}
				}
		durl=url+subdir+"/"+mainsound;
		//outtext.setText(durl);

	    task2.execute(new String[] {durl });
		
		}
		
	}
	
	private class DownloadAudioTask extends AsyncTask<String, Void, String> {
		  
	    File file;
	    protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(MainActivity.this);
            progDailog.setMessage("Loading..."+"\n If takes too long , check internet connection ");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }
	    
	    protected String doInBackground(String... urls) {
	      String response = "";
	      for (String url : urls) {
	        DefaultHttpClient client = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(url);
	        try {
	          HttpResponse execute = client.execute(httpGet);
	          InputStream content = execute.getEntity().getContent();
	          Log.v("A", "H1");
	          Log.v("A", "H2");
	          
	        File dir =getApplicationContext().getExternalFilesDir(null);
	  		file =new File(dir,"tempaudio2.wav");
	  	    FileOutputStream fos = new FileOutputStream(file);
	  		Log.v("A", "H3");
	  	   byte[] b = new byte[(1024*2)];
    	   int x=0;	    
    	   while((x=content.read(b))!=-1){
    		   fos.write(b, 0, x);	
   	  		Log.v("A6", "H---");

    	  }
    	   fos.close();
/////////////////////////////////////////////////////////////////////
	 
	          
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	      return response;
	    }
	    
	   
	    @Override
	    protected void onPostExecute(String result) {
	    	super.onPostExecute(result);
	    	if(progDailog.isShowing()){
            progDailog.dismiss();}

	    	MediaPlayer mp = new MediaPlayer();
	    	 try{
	    	 FileInputStream fileInputStream = new FileInputStream(file);
	    	 mp.setDataSource(fileInputStream.getFD());         
	    	 mp.prepare();
	    	 fileInputStream.close();}
	    	 catch(Exception e){}
	    	 
	    	// mp.setDataSource("tempaudio2"); 
	    	// mp.setOnPreparedListener(new OnPreparedListener() {
	    		//    @Override
	    		  //  public void onPrepared(MediaPlayer mp) {
	    		    //    // Do something. For example: playButton.setEnabled(true);
	    		    	mp.start();
	    		   // }
	    	//	});
	    	
	    	 //mp.prepareAsync();

	 
	    	
	     // outtext.setText(g);
	    }
	  }
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
	  
	    File file;
	    protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(MainActivity.this);
            progDailog.setMessage("Loading..."+"\n If takes too long , check internet connection ");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }
	    
	    protected String doInBackground(String... urls) {
	      String response = "";
	      for (String url : urls) {
	        DefaultHttpClient client = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(url);
	        try {
	          HttpResponse execute = client.execute(httpGet);
	          InputStream content = execute.getEntity().getContent();
	          
		    //  BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		      
		     
	          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
	        //  BufferedWriter writer = new BufferedWriter(new FileWriter("dict.xml"));
	          
	        File dir =getApplicationContext().getExternalFilesDir(null);
	  		file =new File(dir,"temp");
	  		
	  		
	  		
	          String s = "";
	          while ((s = buffer.readLine()) != null) {
	            response += s;
	          //  writer.write(s);
		 	    //writer.newLine();
	          }
	          try {
		  			
		  		    FileOutputStream fos = new FileOutputStream(file);
		  		    fos.write(response.getBytes());	
		  		    fos.close();
		  		}
	          catch(Exception e){}
	          
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	      }
	      return response;
	    }
	    //tetstststststsssssssssssssssssssssssssssssssssssssssssssssssssssssss
	
	    //
	    @Override
	    protected void onPostExecute(String result) {
	    	super.onPostExecute(result);
	    	if(progDailog.isShowing()){
            progDailog.dismiss();}
	    	String g="nothing";
	    	try{
	    		//File fXmlFile = new File(temp);
	    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    		Document doc = dBuilder.parse(file);
	    		doc.getDocumentElement().normalize();
	    		g=doc.getDocumentElement().getNodeName();
	    		Log.v("TRY", "excetuted");
	    		NodeList nList = doc.getElementsByTagName("entry");
	    		NodeList soundlist=doc.getElementsByTagName("wav");
	    		outtext.setText("");
	    		a=0;
	    		if(soundlist.getLength()!=0){
	    		a=1;
	    		maintext.setText("gggggggggggggg");
	    			Node ns= soundlist.item(0);
	    		Element es =(Element) ns;
	    		
	    		
	    		mainsound=es.getTextContent();}
	    		for (int temp = 0; temp < nList.getLength(); temp++) {
	    		
	    			Node nNode = nList.item(temp);
	    			
	    			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	    						
	    				Element eElement = (Element) nNode;
	    				int y =temp+1;
	    				outtext.append("\n");
	    				outtext.append(Html.fromHtml("<br/><b>"+y+": "+eElement.getElementsByTagName("ew").item(0).getTextContent()+"</b><br/>"));
	    				//outtext.append(temp+": "+eElement.getElementsByTagName("def").item(0).getTextContent()+"\n");
	    	    		Log.v("one", "excetuted");
	    	    		
	    	    		
	    	    		/*
	    				                                NodeList nList2 = eElement.getElementsByTagName("dt");
	    				                                NodeList nList3 = eElement.getElementsByTagName("sn");
	    				                	    		Log.v("len2", String.valueOf(nList2.getLength()));
	    				                	    		Log.v("len3", String.valueOf(nList3.getLength()));
	    				                                //?????????????????????????????????????????????????????????????
	    				                                
	    				                	    		for (int x = 0; x < nList2.getLength(); x++) {
	    				                	    			
	    				                	    			Node nNode2 = nList2.item(x);
	    				                	    			Node nNode3 = nList3.item(x);
	    				                	    			if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
	    				                	    						
	    				                	    				Element eElement2 = (Element) nNode2;
	    				                	    				Element eElement3 = (Element) nNode3;
	    				                	    				outtext.append(Html.fromHtml("<font color=blue><b>" +eElement3.getTextContent()+"</b></font><hr>"));
    				                	    				
	    				                	    				outtext.append(eElement2.getTextContent());
	    				                	    			}
	    				                	    			}
	    				                	    		*/	
	    				                	    		//	Log.v("val",String.valueOf(x));
	    	    										int chk=0;
	    				                	    		NodeList nx =eElement.getElementsByTagName("def");
	    				                	    		Node nx0=nx.item(0);
	    				                	    		Element eElement0 = (Element) nx0;
	    				                	    		NodeList nList4=eElement0.getChildNodes();
	    				                	    		Log.v("LENGTH", String.valueOf(nList4.getLength()));
	    				                	    		for(int i=0;i<nList4.getLength();i++){
	    				                	    			Log.v("count1", String.valueOf(i));
	    				                	    			Node nNode4 = nList4.item(i);
	    				                	    			Log.v("count2", String.valueOf(i));
	    				                	    		
	    				                	    			Log.v("count3", String.valueOf(i));
	    				                	    			if (nNode4.getNodeType() == Node.ELEMENT_NODE) {
	    				                	    				Element e = (Element) nNode4;
	    				                	    			Log.v("iN", e.getNodeName()+"-----"+e.getNodeValue());
	    				                	    			
	    				                	    			if(e.getNodeName().contentEquals("sn")){
	    				                	    				Log.v("iop", "excetuted");
	    				                	    				if(chk==0){
	    				                	    					outtext.append(Html.fromHtml("<font color=blue><i>" +e.getTextContent()+" "+"</i></font><hr>"));
	    				                	    				}
	    				                	    				else
	    				                	    				outtext.append(Html.fromHtml("<br/>"+"<font color=blue><i>" +e.getTextContent()+" "+"</i></font><hr>"));
	    				                	    				chk=1;
	    				                	    			}
	    				                	    			if(e.getNodeName().contentEquals("dt")){
	    				                	    				outtext.append(e.getTextContent());
	    				                	    			}
	    				                	    			Log.v("here", "3333333333333333333");
	    				                	    			}
	    				                	    		}
		    				                	    
	    				                	    		Log.v("two", "excetuted");

	    				                               maintext.setText("   "+gettext.getText().toString().toUpperCase().trim());
								    			
	    				
	    			}//if
	    		}//for
	    	}
	    	catch(Exception e){
	    		Log.v("exc", e.toString());
	    	}
	    }
	  }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

}
