/*
 * Copyright 2012 Greg Milette and Adam Stroud
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.root.gast.speech.activation;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.PP.LunarTabsAndroid.Activities.MainActivity;
import com.root.gast.speech.SpeechRecognitionUtil;
import com.root.gast.speech.text.WordList;
import com.root.gast.speech.text.match.SoundsLikeWordMatcher;

/**
 * Uses direct speech recognition to activate when the user speaks
 * one of the target words
 * @author Greg Milette &#60;<a
 *         href="mailto:gregorym@gmail.com">gregorym@gmail.com</a>&#62;
 */
public class WordActivator implements SpeechActivator, RecognitionListener
{
	
	public static final long DEFAULT_SLEEP_TIME = 10000;
		
    private static final String TAG = "WordActivator";
    protected MainActivity parent;
    private Context context;
    private SpeechRecognizer recognizer;
    private SoundsLikeWordMatcher matcher;
    protected Intent recognizerIntent = null;
    private SpeechActivationListener resultListener;
    protected volatile boolean listening = false;
    protected TimeoutMonitor cTimeoutMonitor;
    
    public WordActivator(MainActivity parent,
            SpeechActivationListener resultListener, String... targetWords)
    {
    	this.parent = parent;
        this.context = parent.getApplicationContext();
        this.matcher = new SoundsLikeWordMatcher(targetWords);
        this.resultListener = resultListener;        
    }
    
    @Override
    public void detectActivation()
    {
    	//set flag to true
		listening = true;
		
    	//first call
        recognizeSpeechDirectly();        
    }

    private void recognizeSpeechDirectly()
    {
    	if(recognizerIntent==null) {
	        recognizerIntent =
	                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
	        // accept partial results if they come
	        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
    	}
    	if(listening) {
    		    		
    		//fire intent
    		Log.d("FIRED INTENT", "FIRED");
    		SpeechRecognitionUtil.recognizeSpeechDirectly(context,
                recognizerIntent, this, getSpeechRecognizer());
    		
    		//start timeout monitor
    		cTimeoutMonitor = new TimeoutMonitor(10000) {

				@Override
				protected void timeout() {
					
					//restart speech activator on time out
					//if listening
					parent.runOnUiThread(new Runnable() {
						
						
						@Override
						public void run() {
							if(listening) {
								stopListening();
								detectActivation();
							}
			    		}
					});					
				}
    		};
    		cTimeoutMonitor.start();
    		
    	}
    }
        
    @Override
	public void stopListening() {
		listening = false;
    	getSpeechRecognizer().stopListening();
    	getSpeechRecognizer().cancel();
    }
                
	@Override
	public void stop()
    {
        if (getSpeechRecognizer() != null)
        {
            getSpeechRecognizer().stopListening();
            getSpeechRecognizer().cancel();
            getSpeechRecognizer().destroy();
        }
    }
	
    @Override
    public void onResults(Bundle results)
    {
        Log.d(TAG, "full results");
        receiveResults(results);
    }

    @Override
    public void onPartialResults(Bundle partialResults)
    {
        Log.d(TAG, "partial results");
        receiveResults(partialResults);
    }

    /**
     * common method to process any results bundle from {@link SpeechRecognizer}
     */
    private void receiveResults(Bundle results)
    {   
    	//tell success to timeout monitor
    	cTimeoutMonitor.success();    	
    	
    	//parse results
        if ((results != null)
                && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION))
        {
            List<String> heard =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            float[] scores =
                    results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
            receiveWhatWasHeard(heard, scores);
        }
        else
        {
            Log.d(TAG, "no results");
        }
    }

    private void receiveWhatWasHeard(List<String> heard, float[] scores)
    {
        boolean heardTargetWord = false;
        // find the target word
        String wordHeard="";
        for (String possible : heard)
        {
            WordList wordList = new WordList(possible);
            if (matcher.isIn(wordList.getWords()))
            {
                Log.d(TAG, "HEARD IT!");
                heardTargetWord = true;
                wordHeard = possible;
                break;
            }
        }

        if (heardTargetWord)
        {
            stop();
            resultListener.activated(true,wordHeard);
            
            //if listening, keep going.
        	if(listening) {
	            // keep going
	            recognizeSpeechDirectly();
        	}
            
        }
        else
        {
        	if(listening) {
	            // keep going
	            recognizeSpeechDirectly();
        	}
        }
    }

    @Override
    public void onError(int errorCode)
    {
        if ((errorCode == SpeechRecognizer.ERROR_NO_MATCH)
                || (errorCode == SpeechRecognizer.ERROR_SPEECH_TIMEOUT))
        {        	
            Log.d(TAG, "didn't recognize anything");
            
        	//tell success to timeout monitor
        	cTimeoutMonitor.success();    	
            
            // keep going
            if(listening) {
            	recognizeSpeechDirectly();
            }
        }
        else
        {        	
            Log.d(TAG,
                    "FAILED "
                            + SpeechRecognitionUtil
                                    .diagnoseErrorCode(errorCode));
        }
    }

    /**
     * lazy initialize the speech recognizer
     */
    private SpeechRecognizer getSpeechRecognizer()
    {
        if (recognizer == null)
        {
            recognizer = SpeechRecognizer.createSpeechRecognizer(context);
        }
        return recognizer;
    }

    // other unused methods from RecognitionListener...

    @Override
    public void onReadyForSpeech(Bundle params)
    {
        Log.d(TAG, "ready for speech " + params);
    }

    @Override
    public void onEndOfSpeech()
    {
    }

    /**
     * @see android.speech.RecognitionListener#onBeginningOfSpeech()
     */
    @Override
    public void onBeginningOfSpeech()
    {
    }

    @Override
    public void onBufferReceived(byte[] buffer)
    {
    }

    @Override
    public void onRmsChanged(float rmsdB)
    {
    }

    @Override
    public void onEvent(int eventType, Bundle params)
    {
    }
}
