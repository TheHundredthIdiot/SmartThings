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
	definition (name: "My Netatmo Additional Module", namespace: "TheHundredthIdiot", author: "Andy") {
		capability "Relative Humidity Measurement"
		capability "Temperature Measurement"

		attribute "carbonDioxide", "string"
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
 		valueTile("carbonDioxide", "device.carbonDioxide", width: 2, height: 2) {
 			state "carbonDioxide", label:'${currentValue}ppm', unit:"CO2", backgroundColors: [
 				[value: 250, 	color: "#cbf3be"],
            	[value: 350, 	color: "#85e368"],
            	[value: 600, 	color: "#44B621"],
                [value: 1000, 	color: "#ffcc00"],
                [value: 2000, 	color: "#e86d13"],
                [value: 50000, 	color: "#cc3300"]
 				]
        }
 		standardTile("refresh", "device.pressure", width: 2, height: 2, decoration: "flat") {
 			state "default", action:"device.poll", icon:"st.secondary.refresh"
 		}
 		main "temperature"
 		details(["temperature", "humidity", "carbonDioxide", "refresh"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"

}

def poll() {
	parent.poll()
}