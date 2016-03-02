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
 *		https://en.wikipedia.org/wiki/Beaufort_scale
 *		https://en.wikipedia.org/wiki/Points_of_the_compass
 *
 */

metadata {
	definition (name: "My Netatmo Wind", namespace: "TheHundredthIdiot", author: "Andy") {
    
    	capability "Sensor"
    
		attribute "wind", "number"
        attribute "WindAngle", "number"
 		attribute "WindStrength", "number"
		attribute "GustStrength", "number"
		attribute "DecimalUnits", "string"

		attribute "WindAngleText", "string"
        attribute "WindStrengthText", "string"
		attribute "GustStrengthText", "string"
 		attribute "Beaufort", "string"        
		attribute "BeaufortDesctiption", "string"

		command "poll"
        command "windToPref"
        command "windDirection"
        command "windToBeaufort"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	preferences {
		input "windUnits", "enum", title: "Report wind in", description: "Wind speed measurement", required: true, options: ['mph':'Miles per Hour (mph)', 'kph':'Kilometres per Hour (kph)', 'fps':'Feet per Second (fps)', 'mps':'Metre per Second (mps)', 'kn':'Knots (kn)']
    	input "beaufortContext", "enum", title: "Beaufort description for", required: true, options: ['Land':'Land', 'Sea':'Sea']
	}

	tiles (scale: 2)  {
		multiAttributeTile(name:"WindStrength", type:"lighting", width: 6, height: 4, decoration: "flat") {
			tileAttribute("device.WindStrengthText", key: "PRIMARY_CONTROL") {
				attributeState "default",  label: '${currentValue}', icon: "st.Weather.weather1", backgroundColor: "#7eaacd"
 			}
         	tileAttribute("device.Beaufort", key: "SECONDARY_CONTROL") {
             	attributeState "default", label: '${currentValue} Beaufort'
			}
 		}
		valueTile("Beaufort", "device.Beaufort", width: 1, height: 1) {
			state "default", label: '${currentValue}', backgroundColors: [
   					[value: "0",  color: "#ffffff"],
 	  				[value: "1",  color: "#ccffff"],
   					[value: "2",  color: "#99ffcc"],
   					[value: "3",  color: "#99ff99"],
	   				[value: "4",  color: "#99ff66"],
 	  				[value: "5",  color: "#99ff00"],
 	  				[value: "6",  color: "#ccff00"],
    				[value: "7",  color: "#ffff00"],
 	  				[value: "8",  color: "#ffcc00"],
   					[value: "9",  color: "#ff9900"],
   					[value: "10", color: "#ff6600"],
	   				[value: "11", color: "#ff3300"],
 	  				[value: "12", color: "#ff0000"]]           
        }
        
 		valueTile("BeaufortDescription", "device.BeaufortDescription", width: 5, height: 1, decoration: "flat") {
 			state "0 Land",  label: 'Calm, smoke rises vertically'
            state "1 Land",  label: 'Smoke drift indicates wind direction,\nleaves and wind vanes are stationary'
            state "2 Land",  label: 'Wind felt on exposed skin, leaves\nrustle and wind vanes begin to move'
            state "3 Land",  label: 'Leaves and small twigs constantly\nmoving, light flags extended' 
            state "4 Land",  label: 'Dust and loose paper raised, small\nbranches begin to move'
            state "5 Land",  label: 'Branches of a moderate size move,\nsmall trees in leaf begin to sway' 
            state "6 Land",  label: 'Large branches in motion, whistling\nheard in overhead wires, umbrella use\nbecomes difficult'
            state "7 Land",  label: 'Whole trees in motion, effort needed\nto walk against the wind'
            state "8 Land",  label: 'Some twigs broken from trees, cars\nveer on road and progress on foot\nis seriously impeded' 
            state "9 Land",  label: 'Some branches break off trees, some\nsmall trees blow over, temporary signs\nand barricades blow over'
            state "10 Land", label: 'Trees are broken off or uprooted,\nstructural damage likely'
            state "11 Land", label: 'Widespread vegetation and\nstructural damage likely'
            state "12 Land", label: 'Severe widespread damage to\nvegetation and structures, debris and\nunsecured objects are hurled about'
  			state "0 Sea",   label: 'Flat'
            state "1 Sea",   label: 'Ripples without crests'
            state "2 Sea",   label: 'Small wavelets, crests of glassy\nappearance, not breaking'
            state "3 Sea",   label: 'Large wavelets, crests begin to\nbreak, scattered whitecaps' 
            state "4 Sea",   label: 'Small waves with breaking crests,\nfairly frequent whitecaps'
            state "5 Sea",   label: 'Moderate waves of some length, many\nwhitecaps, small amounts of spray' 
            state "6 Sea",   label: 'Long waves begin to form, white foam\ncrests are very frequen, some airborne\nspray is present'
            state "7 Sea",   label: 'Sea heaps up, foam from breaking waves\nblown into streaks along wind direction,\nmoderate amounts of airborne spray'
            state "8 Sea",   label: 'Moderately high waves with breaking\ncrests forming spindrift, considerable\nairborne spray' 
            state "9 Sea",   label: 'High waves whose crests sometimes roll\nover, dense foam blown along wind\ndirection, large amounts of airborne spray'
            state "10 Sea",  label: 'Very high waves with overhanging\ncrests large patches of foamconsiderable\ntumbling of waves with heavy impact'
            state "11 Sea",  label: 'Exceptionally high waves, very large\npatches of foam, driven before the wind,\nvery large amounts of airborne spray'
            state "12 Sea",  label: 'Huge waves, sea is completely white\nwith foam and spray, air is filled with\ndriving spray, greatly reducing visibility'
  	 	}
    
		valueTile("WindAngle", "device.WindAngle", width: 1, height: 1) {
 			state "default", label: '', backgroundColors: [
                [value: "0",   color: "#ff0000"], // North (N)
                [value: "6",   color: "#ff9d80"], // North by East (NbE)
                [value: "17",  color: "#daa520"], // North-northeast (NNE)
                [value: "29",  color: "#daa520"], // Northeast by North (NEbN)
                [value: "40",  color: "#daa520"], // Northeast (NE)
                [value: "51",  color: "#daa520"], // Northeast by East (NEbE)
                [value: "62",  color: "#daa520"], // East-northeast (ENE)
                [value: "74",  color: "#daa520"], // East by North (EbN)
                [value: "85",  color: "#daa520"], // East (E)
                [value: "96",  color: "#daa520"], // East by South (EbS)
                [value: "107", color: "#daa520"], // East-southeast (ESE)
                [value: "119", color: "#daa520"], // Southeast by East (SEbE)
                [value: "130", color: "#daa520"], // Southeast (SE)
                [value: "141", color: "#daa520"], // Southeast by South (SEbS)
                [value: "152", color: "#daa520"], // South-southeast (SSE)
                [value: "164", color: "#f8eed3"], // South by East (SbE)
                [value: "175", color: "#000000"], // South (S)
                [value: "186", color: "#f8eed3"], // South by West (SbW)
                [value: "197", color: "#daa520"], // South-southwest (SSW)
                [value: "209", color: "#daa520"], // Southwest by South (SWbS)
                [value: "220", color: "#daa520"], // Southwest (SW)
                [value: "231", color: "#daa520"], // Southwest by West (SWbW)
                [value: "242", color: "#daa520"], // West-southwest (WSW)
                [value: "254", color: "#e6c300"], // West by South (WbS)
                [value: "265", color: "#ffd700"], // West (W)
                [value: "276", color: "#e6c300"], // West by North (WbN)
                [value: "287", color: "#daa520"], // West-northwest (WNW)
                [value: "299", color: "#daa520"], // Northwest by West (NWbW)
                [value: "310", color: "#daa520"], // Northwest (NW)
                [value: "321", color: "#daa520"], // Northwest by North (NWbN)
                [value: "332", color: "#daa520"], // North-northwest (NNW)
                [value: "344", color: "#ff9d80"], // North by West (NbW)
                [value: "355", color: "#ff0000"]] // North (N)
 		}
		valueTile("WindAngleText", "device.WindAngleText", width: 5, height: 1, decoration: "flat") {
 			state "default", label: 'Wind: ${currentValue}'
 		}

		valueTile("GustStrength", "device.GustStrengthText", width: 5, height: 1, decoration: "flat") {
 			state "default", label:'Gusting to: ${currentValue}'
 		}
		standardTile("refresh", "device.wind", width: 1, height: 1, decoration: "flat") {
 			state "default", action:"poll", icon:"st.secondary.refresh"
 		}

		main (["WindStrength"])
		details(["WindStrength", 
        		 "Beaufort", "BeaufortDescription", 
                 "WindAngle", "WindAngleText", 
                 "refresh", "GustStrength"])
	}
}

def updated() {
	log.debug ("updated")
    windToPref()
    windDirection()
    windToBeaufort()
}

def installed() {
	log.debug ("installed")
    windToPref()
    windDirection()
    windToBeaufort()
}    

// parse events into attributes
def parse(String description) {
	log.debug ("Parsing '${description}'")
}

def poll() {
	log.debug "Polling"
	parent.poll()
}

def windToPref() {
	log.debug "In windToPref"
	def windSpeed
    def gustSpeed 
	switch(settings.windUnits) {
    	case 'kph':
			windSpeed = device.currentValue("WindStrength")
            gustSpeed = device.currentValue("GustStrength")
        	break;
		case 'mph':
			windSpeed = (device.currentValue("WindStrength") as float) * 0.621371
            gustSpeed = (device.currentValue("GustStrength") as float) * 0.621371
			break;
    	case 'fps':
			windSpeed = device.currentValue("WindStrength") * 1.46667
            gustSpeed = device.currentValue("GustStrength") * 1.46667
        	break;
    	case 'mps':
			windSpeed = device.currentValue("WindStrength")* 0.44704 
            gustSpeed = device.currentValue("GustStrength")* 0.44704 
           	break;
    	case 'kn':
			windSpeed = device.currentValue("WindStrength")* 0.868976
            gustSpeed = device.currentValue("GustStrength")* 0.868976
	 		break;
	}
	sendEvent(name: "WindStrengthText", value: String.format("%.${device.currentValue("DecimalUnits")}f", windSpeed as float) + " " + settings.windUnits, unit: settings.windUnits, descriptionText: "Wind Strength Text: ${windSpeed}")
	sendEvent(name: "GustStrengthText", value: String.format("%.${device.currentValue("DecimalUnits")}f", gustSpeed as float) + " " + settings.windUnits, unit: settings.windUnits, descriptionText: "Gust Strength Text: ${gustSpeed}")
}

def windDirection() {
	log.debug ("In windDirection")
	def degrees = device.currentValue("WindAngle") as int
    def degreesText = ""
    switch(degrees) {
		case 0..5:
			degreesText = degrees + '° - North (N)'
			break;
		case 5..16:
			degreesText = degrees + '° - North by East (NbE)'
			break;
		case 16..28:
			degreesText = degrees + '° - North-northeast (NNE)'
			break;
		case 28..39:
			degreesText = degrees + '° - Northeast by North (NEbN)'
			break;
		case 39..50:
			degreesText = degrees + '° - Northeast (NE)'
			break;
		case 50..61:
			degreesText = degrees + '° - Northeast by East (NEbE)'
			break;
		case 61..73:
			degreesText = degrees + '° - East-northeast (ENE)'
			break;
		case 73..84:
			degreesText = degrees + '° - East by North (EbN)'
			break;
		case 84..95:
			degreesText = degrees + '° - East (E)'
			break;
		case 95..106:
			degreesText = degrees + '° - East by South (EbS)'
			break;
		case 106..118:
			degreesText = degrees + '° - East-southeast (ESE)'
			break;
		case 118..129:
			degreesText = degrees + '° - Southeast by East (SEbE)'
			break;
		case 129..140:
			degreesText = degrees + '° - Southeast (SE)'
			break;
		case 140..151:
			degreesText = degrees + '° - Southeast by South (SEbS)'
			break;
		case 151..163:
			degreesText = degrees + '° - South-southeast (SSE)'
			break;
		case 163..174:
			degreesText = degrees + '° - South by East (SbE)'
			break;
		case 174..185:
			degreesText = degrees + '° - South (S)'
			break;
		case 185..196:
			degreesText = degrees + '° - South by West (SbW)'
			break;
		case 196..208:
			degreesText = degrees + '° - South-southwest (SSW)'
			break;
		case 208..219:
			degreesText = degrees + '° - Southwest by South (SWbS)'
			break;
		case 219..230:
			degreesText = degrees + '° - Southwest (SW)'
			break;
		case 230..241:
			degreesText = degrees + '° - Southwest by West (SWbW)'
			break;
		case 241..253:
			degreesText = degrees + '° - West-southwest (WSW)'
			break;
		case 253..264:
			degreesText = degrees + '° - West by South (WbS)'
			break;
		case 264..275:
			degreesText = degrees + '° - West (W)'
			break;
		case 275..286:
			degreesText = degrees + '° - West by North (WbN)'
			break;
		case 286..298:
			degreesText = degrees + '° - West-northwest (WNW)'
			break;
		case 298..309:
			degreesText = degrees + '° - Northwest by West (NWbW)'
			break;
		case 309..320:
			degreesText = degrees + '° - Northwest (NW)'
			break;
		case 320..331:
			degreesText = degrees + '° - Northwest by North (NWbN)'
			break;
		case 331..343:
			degreesText = degrees + '° - North-northwest (NNW)'
			break;
		case 343..354:
			degreesText = degrees + '° - North by West (NbW)'
			break;
		case 354..360:
			degreesText = degrees + '° - North (N)'
			break;
	}
	sendEvent(name: "WindAngleText", value: degreesText as String, unit: "", descriptionText: "Wind Angle Text: ${degreesText}")
}

def windToBeaufort() {
	log.debug "In windToBeaufort"
//	Convert to mph to align with original Beaufort scale measurement units (rather than kph)
	int miles = (device.currentValue("WindStrength") as float) * 0.621371 
	int beaufortStrength = "0" 
    switch(miles) {
		case 1..3:
			beaufortStrength = 1
 			break;
		case 4..7:
			beaufortStrength = 2 
 			break;
 		case 8..12:
			beaufortStrength = 3
 			break;
 		case 13..18:
			beaufortStrength = 4
 			break;
 		case 19..24:
			beaufortStrength = 5
 			break;
 		case 25..31:
			beaufortStrength = 6
 			break;
 		case 32..38:
			beaufortStrength = 7
 			break;
 		case 39..46:
			beaufortStrength = 8
 			break;
 		case 57..54:
			beaufortStrength = 9                   
 			break;
 		case 55..63:
			beaufortStrength = 10                   
 			break;
 		case 64..72:
			beaufortStrength = 11
 			break;
        case 73..999:
          	beaufortStrength = 12
 			break;
 		default:
           	beaufortStrength = 0
 			break;
 	}
	sendEvent(name: 'Beaufort', 		   value: "${beaufortStrength}", unit: 'bf')
    sendEvent(name: 'BeaufortDescription', value: "${beaufortStrength} ${settings.beaufortContext}")
}         
