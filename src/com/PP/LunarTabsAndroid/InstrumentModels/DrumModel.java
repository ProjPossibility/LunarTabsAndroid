package com.PP.LunarTabsAndroid.InstrumentModels;

public class DrumModel {
	
	/**
	 * Singleton instance
	 */
	private static String[] instance = null; 
	
	public static String[] getDrumModel() {
		if(instance==null) {
			instance = new String[88];
			instance[27] = "High Q";
			instance[28] = "Slap";
			instance[29] = "Scratch Push";
			instance[30] = "Scratch Pull";
			instance[31] = "Sticks";
			instance[32] = "Square Click";
			instance[33] = "Metronome Click";
			instance[34] = "Metronome Bell";
			instance[35] = "Acoustic Bass Drum";
			instance[36] = "Bass Drum 1";
			instance[37] = "Slide Stick";
			instance[38] = "Acoustic Snare";
			instance[39] = "Hand Clap";
			instance[40] = "Electric Snare";
			instance[41] = "Low Floor Tom";
			instance[42] = "Closed Hi-Hat";
			instance[43] = "High Floor Tom";
			instance[44] = "Pedal Hi-Hat";
			instance[45] = "Low Tom";
			instance[46] = "Open Hi-Hat";
			instance[47] = "Low-Mid Tom";
			instance[48] = "Hi-Mid Tom";
			instance[49] = "Crash Cymbal 1";
			instance[50] = "High Tom";
			instance[51] = "Ride Cymbal 1";
			instance[52] = "Chinese Cymbal";
			instance[53] = "Ride Bell";
			instance[54] = "Tambourine";
			instance[55] = "Splash Cymbal";
			instance[56] = "Cowbell";
			instance[57] = "Crash Cymbal 2";
			instance[58] = "Vibraslap";
			instance[59] = "Ride Cymbal 2";
			instance[60] = "High Bongo";
			instance[61] = "Low Bongo";
			instance[62] = "Mute Hi Conga";
			instance[63] = "Open Hi Conga";
			instance[64] = "Low Conga";
			instance[65] = "High Timbale";
			instance[66] = "Low Timbale";
			instance[67] = "High Agogo";
			instance[68] = "Low Agogo";
			instance[69] = "Cabasa";
			instance[70] = "Maracas";
			instance[71] = "Short Whistle";
			instance[72] = "Long Whistle";
			instance[73] = "Short Guiro";
			instance[74] = "Long Guiro";
			instance[75] = "Claves";
			instance[76] = "Hi Wood Block";
			instance[77] = "Low Wood Block";
			instance[78] = "Mute Cuica";
			instance[79] = "Open Cuica";
			instance[80] = "Mute Triangle";
			instance[81] = "Open Triangle";
			instance[82] = "Shaker";
			instance[83] = "Jingle Bell";
			instance[84] = "Bell Tree";
			instance[85] = "Castinets";
			instance[86] = "Mute Surdo";
			instance[87] = "Open Surdo";
		}
		return instance;		
	}
	

	public static String getDrumName(int index) {
		if(index >= 27 && index <= 87)
			return DrumModel.getDrumModel()[index];
		else
			return "Unknown Drum";
	}
}
