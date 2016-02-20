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
	definition (name: "My Netatmo Rain", namespace: "TheHundredthIdiot", author: "Andy") {
		attribute "rain", "number"
        attribute "rainSumHour", "number"
        attribute "rainSumDay", "number"
        attribute "units", "string"
        
        command "poll"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles (scale: 2)  {
		multiAttributeTile(name:"rain", type:"lighting", width: 6, height: 4, canChangeIcon: true) {
			tileAttribute("device.rainSumHour", key: "PRIMARY_CONTROL") {
				attributeState "0.0",  label: '${currentValue}/h', icon: "st.Weather.weather12", backgroundColor: "#44b621"
				attributeState "default",  label: '${currentValue}/h', icon: "st.Weather.weather12", backgroundColor: "#7eaacd"
 			}
        	tileAttribute("device.units", key: "SECONDARY_CONTROL") {
	   			attributeState "default", label:'${currentValue}'
                attributeState "mm", label:'${currentValue} - Millimetres'
             	attributeState "in", label:'${currentValue} - Inches' 
            }
 		}
 		valueTile("rainSumDay", "device.rainSumDay", width: 2, height: 2) {
 			state "0.0",  label: 'Cumulative\n${currentValue}/h', backgroundColor: "#44b621"
            state "default", label: 'Cumulative\n${currentValue}', backgroundColor: "#7eaacd"
 		}
 		standardTile("refresh", "device.rain", width: 2, height: 2, decoration: "flat") {
 			state "default", action:"refresh.poll", icon:"st.secondary.refresh"
 		}

		main (["rain", "rainSumHour", "rainSumDay"])
 		details(["rain", "rainSumHour", "rainSumDay", "refresh"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def poll() {
	parent.poll()
}