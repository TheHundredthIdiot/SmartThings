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
	definition (name: "My Netatmo Outdoor Module", namespace: "TheHundredthIdiot", author: "Andy") {
		capability "Relative Humidity Measurement"
		capability "Temperature Measurement"

        attribute "units", "string"
}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles (scale: 2)  {
		multiAttributeTile(name: "temperature", type:"lighting", width: 6, height: 4, decoration: "flat", canChangeIcon: true) {
			tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
				attributeState "default",  label: '${currentValue}°', icon: "st.Home.home1", backgroundColor: "#153591"
 			}
         	tileAttribute("device.units", key: "SECONDARY_CONTROL") {
                attributeState "C", label: '°C - Centigrade'
             	attributeState "F", label: '°F - Fahrenheit' 
             	attributeState "S", label: 'Set by SmartThings' 
 			}
        }
 		valueTile("humidity", "device.humidity", width: 2, height: 2) {
 			state "humidity", label:'${currentValue}%', unit:"Humidity"
 		}
		standardTile("refresh", "device.thermostatMode", width: 2, height: 2, decoration: "flat") {
 			state "default", action:"device.poll", icon:"st.secondary.refresh"
 		}
 		main (["temperature", "humidity"])
 		details(["temperature", "humidity", "refresh"])
	}
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