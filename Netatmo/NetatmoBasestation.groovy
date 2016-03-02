/*
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *	Author: Andy - TheHundredthIdiot
 *	Date:	February 2015
 *
 *	References:
 *
 *		https://community.smartthings.com/t/netatmo-for-uk-users-temp-workaround/27000
 *
 */

metadata {
	definition (name: "My Netatmo Basestation", namespace: "TheHundredthIdiot", author: "Andy") {
    
    	capability "Sensor"
		capability "Relative Humidity Measurement"
		capability "Temperature Measurement"
//      capability "Sound Pressure Level"
//		capability "Carbon Dioxide Measurement"

		attribute "carbonDioxide", "string"
		attribute "noise", "string"
		attribute "pressure", "string"
		attribute "DecimalUnits", "string"

		attribute "PressureText", "string"
        
		command "poll"
        command "pressureToPref"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	preferences {
    	input "pressureUnits", "enum", title: "Report pressure in", description: "Pressure Measurement", required: true, options: ['bar':'Bar (bar)', 'mbar':'Millibar (mbar)', 'inHg':'Inch Mercury (inHg)', 'mmHg':'Millimeter Mercury (mmHg)']
	}
    
	tiles (scale: 2)  {
		multiAttributeTile(name: "temperature", type:"lighting", width: 6, height: 4, decoration: "flat", canChangeIcon: true) {
			tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
				attributeState "default",  label: '${currentValue}°', icon: "st.Home.home1", backgroundColor: "#7eaacd"
 			}
         	tileAttribute("device.humidity", key: "SECONDARY_CONTROL") {
                attributeState "humidity", label:'${currentValue}% Humidity'
  			}
        }

		valueTile("carbonDioxide", "device.carbonDioxide", width: 1, height: 1) {
 			state "carbonDioxide", label:'', unit:"CO2", backgroundColors: [
 				[value: 250, 	color: "#cbf3be"],
            	[value: 350, 	color: "#85e368"],
            	[value: 600, 	color: "#44B621"],
                [value: 1000, 	color: "#ffcc00"],
                [value: 2000, 	color: "#e86d13"],
                [value: 50000, 	color: "#cc3300"]
 				]
		}
 		valueTile("carbonDioxideText", "device.carbonDioxide", width: 5, height: 1, decoration: "flat") {
 			state "carbonDioxide", label:'Carbon Dioxide level: ${currentValue} ppm', unit:"CO2"
		}

		valueTile("noise", "device.noise", width: 1, height: 1) {
 			state "noise", label:'', unit:"Noise", backgroundColors: [
  				[value: 0, 		color: "#a8ff80"],                
  				[value: 70, 	color: "#c6ff99"],
  				[value: 75, 	color: "#ddffb3"],
  				[value: 80, 	color: "#ededed"],
  				[value: 85, 	color: "#ffd9d9"],
  				[value: 90, 	color: "#ffc7c7"],
  				[value: 95, 	color: "#ffb3b3"],
  				[value: 105, 	color: "#ff8c8c"],
  				[value: 120, 	color: "#ff6666"],
  				[value: 140, 	color: "#ff0000"],
  				[value: 180, 	color: "#000000"]
 				]
 		}
 		valueTile("noiseText", "device.noise", width: 5, height: 1, decoration: "flat") {
  			state "default", label: 'Noise level: ${currentValue} dB'
            state "0",  	 label: 'Noise level: ${currentValue} dB\nThe threshold of hearing'                                                                     
  			state "10",  	 label: 'Noise level: ${currentValue} dB\nNormal breathing'
  			state "20",  	 label: 'Noise level: ${currentValue} dB\nRustling leaves' 
  			state "30",  	 label: 'Noise level: ${currentValue} dB\nQuiet office or library'	 
  			state "40",  	 label: 'Noise level: ${currentValue} dB\nResidential background noise, light rainfall'	 
  			state "50",  	 label: 'Noise level: ${currentValue} dB\nDishwasher'
  			state "55",  	 label: 'Noise level: ${currentValue} dB\nAverage office noise'	 
  			state "60",  	 label: 'Noise level: ${currentValue} dB\nNormal conversation,vacuum cleaner'	 
  			state "70",  	 label: 'Noise level: ${currentValue} dB\nAlarm clock, busy street'	
  			state "75",  	 label: 'Noise level: ${currentValue} dB\nMain road traffic at 30mph'
  			state "77",  	 label: 'Noise level: ${currentValue} dB\n2nd floor flat in town centre'	 
  			state "80",  	 label: 'Noise level: ${currentValue} dB\nLocal construction activity, diesel train at 100ft'	 
  			state "87",  	 label: 'Noise level: ${currentValue} dB\n3rd floor flat on a major road'        	 
  			state "90",  	 label: 'Noise level: ${currentValue} dB\nHeavy goods vehicle'	
            state "95",      label: 'Noise level: ${currentValue} dB\nSustained exposure may result in hearing loss' 
  			state "97",  	 label: 'Noise level: ${currentValue} dB\nJet preparing to land'
            state "98",      label: 'Noise level: ${currentValue} dB\nHand Drill'  
  			state "100", 	 label: 'Noise level: ${currentValue} dB\nElectric drill, beside a mainline railway'	 
            state "107",     label: 'Noise level: ${currentValue} dB\nPower mower at 3ft'
            state "110", 	 label: 'Noise level: ${currentValue} dB\nLive rock music, chainsaw, car horn'	 
  			state "120", 	 label: 'Noise level: ${currentValue} dB\nAeroplane on the runway, emergency services siren'
            state "125",     label: 'Noise level: ${currentValue} dB\nPain begins, pneumatic riveter at 4ft'
            state "130", 	 label: 'Noise level: ${currentValue} dB\nPneumatic drill'	 
  			state "140", 	 label: 'Noise level: ${currentValue} dB\nFireworks, short term exposure can cause permanent damage'
            state "155", 	 label: 'Noise level: ${currentValue} dB\nRifle shot'
  			state "160", 	 label: 'Noise level: ${currentValue} dB\nTurbojet engine, shotgun'	 	 
  			state "165", 	 label: 'Noise level: ${currentValue} dB\n.357 magnum revolver'	 	 
  			state "170", 	 label: 'Noise level: ${currentValue} dB\nSafety airbag'
  			state "175", 	 label: 'Noise level: ${currentValue} dB\nHowitzer cannon'	 	 
  			state "180", 	 label: 'Noise level: ${currentValue} dB\nRocket launch, death of hearing tissue'
            state "194", 	 label: 'Noise level: ${currentValue} dB\nSound waves become shock waves, loudest sound possible'
		}

		valueTile("pressure", "device.PressureText", width: 5, height: 1, decoration: "flat") {
 			state "pressure", label:'Pressure: ${currentValue}', unit:"Pressure"
 		}
 		standardTile("refresh", "device.pressure", width: 1, height: 1, decoration: "flat") {
 			state "default", action:"poll", icon:"st.secondary.refresh"
 		}
 		main(["temperature"])
 		details(["temperature",
        		 "carbonDioxide", "carbonDioxideText", 
                 "noise", "noiseText", 
                 "refresh", "pressure"])
	}
}

def updated() {
	log.debug ("updated")
    pressureToPref()
}

def installed() {
	log.debug ("installed")
    pressureToPref()
}  

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'humidity' attribute
	// TODO: handle 'temperature' attribute
	// TODO: handle 'carbonDioxide' attribute
	// TODO: handle 'noise' attribute
	// TODO: handle 'pressure' attribute

}

def poll() {
	parent.poll()
}

def pressureToPref() {
	log.debug "In pressureToPref"
	def pressureValue = 0
	switch (settings.pressureUnits) {
    	case 'bar':
        	pressureValue = device.currentValue("pressure") * 0.001
			break;        
        case 'mbar':
        	pressureValue = device.currentValue("pressure")
            break;
        case 'inHg':
        	pressureValue = device.currentValue("pressure") * 0.0295301
            break;
        case 'mmHg':
	   		pressureValue = device.currentValue("pressure") * 0.750062
			break;
	}
   	sendEvent(name: "PressureText", value: String.format("%.${device.currentValue("DecimalUnits")}f", pressureValue as float) + " " + settings.pressureUnits, unit: settings.pressureUnits, descriptionText: "Pressure: ${pressureValue}")
}    


