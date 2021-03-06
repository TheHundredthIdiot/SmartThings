﻿/*
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
 *	Date:	February 2016
 *
 *	References:
 *
 *		https://community.smartthings.com/t/netatmo-for-uk-users-temp-workaround/27000
 *
 */

metadata {
	definition (name: "My Netatmo Additional Module", namespace: "TheHundredthIdiot", author: "Andy") {

		capability "Sensor"
		capability "Relative Humidity Measurement"
		capability "Temperature Measurement"

		attribute "carbonDioxide", "string"
	}

	simulator {
		// TODO: define status and reply messages here
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
		valueTile("void", "void", width: 5, height: 1, decoration: "flat") {
 			state "default", label:''
 		}
        
        standardTile("refresh", "device.pressure", width: 1, height: 1, decoration: "flat") {
 			state "default", action:"poll", icon:"st.secondary.refresh"
 		}
 		
        main "temperature"
 		details(["temperature",
        		 "carbonDioxide", "carbonDioxideText", 
                 "refresh", "void"])
	}
}

def updated() {
	log.debug ("updated")
}

def installed() {
	log.debug ("installed")
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"

}

def poll() {
	log.debug "Polling"
	parent.poll()
}
