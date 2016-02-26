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
 *
 */

metadata {
	definition (name: "My Netatmo Wind", namespace: "TheHundredthIdiot", author: "Andy") {
		attribute "wind", "number"
        attribute "WindAngle", "string"
        attribute "WindStrength", "number"
        attribute "GustStrength", "number"
 		attribute "Beaufort", "number"        
		attribute "BeaufortDesctiption", "string"
        attribute "units", "string"

		command "poll"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	preferences {
	}

	tiles (scale: 2)  {
		multiAttributeTile(name:"WindStrength", type:"lighting", width: 6, height: 4, decoration: "flat") {
			tileAttribute("device.WindStrength", key: "PRIMARY_CONTROL") {
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
    
		valueTile("WindAngle", "device.WindAngle", width: 2, height: 2) {
 			state "default", label: '${currentValue}'
 		}

		valueTile("GustStrength", "device.GustStrength", width: 2, height: 2) {
 			state "default", label:'Gusting\nto ${currentValue}'
 		}
        
 		standardTile("refresh", "device.wind", width: 2, height: 2, decoration: "flat") {
 			state "default", action:"refresh.poll", icon:"st.secondary.refresh"
 		}

		main (["WindStrength"])
		details(["WindStrength", "Beaufort", "BeaufortDescription", "WindAngle", "GustStrength", "refresh"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def poll() {
	parent.poll()
}
