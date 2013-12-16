package com.PP.MidiServer;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import jp.kshoji.driver.midi.activity.AbstractSingleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;

public abstract class AbstractMidiServerActivity extends AbstractSingleMidiActivity {
	
	//post midi event to midi server queue for processing.
    @Override
    public void onMidiNoteOn(final MidiInputDevice sender, int cable, int channel, int note, int velocity) {
    	long timestamp = System.currentTimeMillis();
    	final org.jfugue.Note noteObj = new org.jfugue.Note((byte)note,0L,(byte)velocity,(byte)0);
    	NoteEvent noteEvent = new NoteEvent(timestamp,noteObj);
    	MidiServer.getInstance().addNoteEvent(noteEvent);
    }
    
    //unused MiDi controller methods
	@Override
	public void onDeviceDetached(UsbDevice usbDevice) {}
	@Override
	public void onDeviceAttached(UsbDevice usbDevice) {}
	@Override
	public void onMidiMiscellaneousFunctionCodes(MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {}
	@Override
	public void onMidiCableEvents(MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {}
	@Override
	public void onMidiSystemCommonMessage(MidiInputDevice sender, int cable, byte[] bytes) {}
	@Override
	public void onMidiSystemExclusive(MidiInputDevice sender, int cable, byte[] systemExclusive) {}
	@Override
	public void onMidiNoteOff(MidiInputDevice sender, int cable, int channel, int note, int velocity) {}
	@Override
	public void onMidiPolyphonicAftertouch(MidiInputDevice sender, int cable, int channel, int note, int pressure) {}
	@Override
	public void onMidiControlChange(MidiInputDevice sender, int cable, int channel, int function, int value) {}
	@Override
	public void onMidiProgramChange(MidiInputDevice sender, int cable, int channel, int program) {}
	@Override
	public void onMidiChannelAftertouch(MidiInputDevice sender, int cable, int channel, int pressure) {}
	@Override
	public void onMidiPitchWheel(MidiInputDevice sender, int cable, int channel, int amount) {}
	@Override
	public void onMidiSingleByte(MidiInputDevice sender, int cable, int byte1) {}	
}