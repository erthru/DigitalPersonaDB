/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erthru.digitalpersonadb.utils;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.DPFPCapturePriority;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPDataListener;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author supriantodjamalu
 */
public class FingerPrintHelper {
    
    public DPFPTemplate getTemplate(String activeReader, int nFinger) {
        MsgBox.warning("Starting enrollment. System will request multiple scan if needed.");
         
        DPFPTemplate template = null;
         
        try { 
            DPFPFingerIndex finger = DPFPFingerIndex.values()[nFinger];
            DPFPFeatureExtraction featureExtractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
            DPFPEnrollment enrollment = DPFPGlobal.getEnrollmentFactory().createEnrollment();
             
            while (enrollment.getFeaturesNeeded() > 0){
                
                MsgBox.warning("Press OK then Put your finger in device.");
                
                DPFPSample sample = getSample(activeReader, String.format("", "", enrollment.getFeaturesNeeded()));
                
                if (sample == null)
                    continue; 
 
 
                DPFPFeatureSet featureSet;
                
                try { 
                    featureSet = featureExtractor.createFeatureSet(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
                } catch (DPFPImageQualityException e) {
                    MsgBox.error("Bad quality. Try Again.");
                    continue; 
                } 
 
 
                enrollment.addFeatures(featureSet);
            } 
            
            template = enrollment.getTemplate();
            
            MsgBox.success("Enrollment Finish");
            
        } catch (DPFPImageQualityException e) {
            MsgBox.error("Enrollment Failed.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } 
         
        return template;
    }
    
    
    public DPFPSample getSample(String activeReader, String prompt) throws InterruptedException{ 
	    final LinkedBlockingQueue<DPFPSample> samples = new LinkedBlockingQueue<DPFPSample>();
	    DPFPCapture capture = DPFPGlobal.getCaptureFactory().createCapture();
	    capture.setReaderSerialNumber(activeReader);
	    capture.setPriority(DPFPCapturePriority.CAPTURE_PRIORITY_LOW);
	    
            capture.addDataListener(new DPFPDataListener(){ 
	        public void dataAcquired(DPFPDataEvent e) {
	            if (e != null && e.getSample() != null) {
	                try { 
	                    samples.put(e.getSample());
	                } catch (InterruptedException e1) {
	                    e1.printStackTrace();
	                } 
	            } 
	        } 
	    }); 
            
	    capture.addReaderStatusListener(new DPFPReaderStatusAdapter(){ 
	    	int lastStatus = DPFPReaderStatusEvent.READER_CONNECTED;
                
                public void readerConnected(DPFPReaderStatusEvent e) {
                    if (lastStatus != e.getReaderStatus())
                        System.out.println("Reader is connected");
                    
                    lastStatus = e.getReaderStatus();
                } 
                public void readerDisconnected(DPFPReaderStatusEvent e) {
                    if (lastStatus != e.getReaderStatus())
                        System.out.println("Reader is disconnected");
                    
                    lastStatus = e.getReaderStatus();
                } 
	    	 
	    }); 
	    try { 
	        capture.startCapture();
	        System.out.print(prompt);
	        return samples.take();
	    } catch (RuntimeException e) {
                MsgBox.error("Failed. Reader is used by another application");
	        throw e;
	    } finally { 
	        capture.stopCapture();
	    } 
    }
    
    public boolean verify(DPFPSample sample1, DPFPTemplate template) {
			 
        try { 
            DPFPSample sample = sample1;
            if (sample == null)
                throw new Exception();
 
            DPFPFeatureExtraction featureExtractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
            DPFPFeatureSet featureSet = featureExtractor.createFeatureSet(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
			 
            DPFPVerification matcher = DPFPGlobal.getVerificationFactory().createVerification();
            matcher.setFARRequested(DPFPVerification.MEDIUM_SECURITY_FAR);
             
            for (DPFPFingerIndex finger : DPFPFingerIndex.values()) {
                if (template != null) {
                    DPFPVerificationResult result = matcher.verify(featureSet, template);
                    if (result.isVerified()) {
                        return result.isVerified();
                    } 
                } 
            } 
        } catch (Exception e) {
            MsgBox.error("Failed to perform verification.");
        } 
         
        return false; 
    } 
    
}
