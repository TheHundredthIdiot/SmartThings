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

		capability "Sensor"
        
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
		valueTile("rain", "device.rainSumHour", width: 6, height: 4) {
          	state "0.0 mm",  label:'${currentValue}', backgroundColor: "#c8daea", icon: "st.Weather.weather12"
          	state "0.1 mm",  label:'${currentValue}', backgroundColor: "#b6cee2", icon: "st.Weather.weather12"
          	state "0.2 mm",  label:'${currentValue}', backgroundColor: "#a3c2db", icon: "st.Weather.weather12"
          	state "0.3 mm",  label:'${currentValue}', backgroundColor: "#91b6d4", icon: "st.Weather.weather12"
          	state "0.4 mm",  label:'${currentValue}', backgroundColor: "#7ea9cd", icon: "st.Weather.weather5"
          	state "0.5 mm",  label:'${currentValue}', backgroundColor: "#6c9dc6", icon: "st.Weather.weather5"
          	state "0.6 mm",  label:'${currentValue}', backgroundColor: "#5a91bf", icon: "st.Weather.weather5"
          	state "0.7 mm",  label:'${currentValue}', backgroundColor: "#4785b8", icon: "st.Weather.weather5"
          	state "0.8 mm",  label:'${currentValue}', backgroundColor: "#4682b4", icon: "st.Weather.weather5"
          	state "1.9 mm",  label:'${currentValue}', backgroundColor: "#4077a5", icon: "st.Weather.weather5"
          	state "1.0 mm",  label:'${currentValue}', backgroundColor: "#396a93", icon: "st.Weather.weather5"
          	state "1.1 mm",  label:'${currentValue}', backgroundColor: "#325d81", icon: "st.Weather.weather5"
          	state "1.2 mm",  label:'${currentValue}', backgroundColor: "#2b506e", icon: "st.Weather.weather5"
          	state "1.3 mm",  label:'${currentValue}', backgroundColor: "#24425c", icon: "st.Weather.weather5"
          	state "1.4 mm",  label:'${currentValue}', backgroundColor: "#1d3549", icon: "st.Weather.weather5"
          	state "1.5 mm",  label:'${currentValue}', backgroundColor: "#152837", icon: "st.Weather.weather5"
          	state "1.6 mm",  label:'${currentValue}', backgroundColor: "#0e1b25", icon: "st.Weather.weather5"
          	state "1.7 mm",  label:'${currentValue}', backgroundColor: "#070d12", icon: "st.Weather.weather10"
			state "0.00 in", label:'${currentValue}', backgroundColor: "#c8daea", icon: "st.Weather.weather12"
          	state "0.04 in", label:'${currentValue}', backgroundColor: "#b6cee2", icon: "st.Weather.weather12"
          	state "0.12 in", label:'${currentValue}', backgroundColor: "#a3c2db", icon: "st.Weather.weather12"
          	state "0.16 in", label:'${currentValue}', backgroundColor: "#91b6d4", icon: "st.Weather.weather12"
          	state "0.20 in", label:'${currentValue}', backgroundColor: "#7ea9cd", icon: "st.Weather.weather5"
          	state "0.24 in", label:'${currentValue}', backgroundColor: "#6c9dc6", icon: "st.Weather.weather5"
          	state "0.25 in", label:'${currentValue}', backgroundColor: "#5a91bf", icon: "st.Weather.weather5"
          	state "0.30 in", label:'${currentValue}', backgroundColor: "#4785b8", icon: "st.Weather.weather5"
          	state "0.34 in", label:'${currentValue}', backgroundColor: "#4682b4", icon: "st.Weather.weather5"
          	state "0.38 in", label:'${currentValue}', backgroundColor: "#4077a5", icon: "st.Weather.weather5"
          	state "0.42 in", label:'${currentValue}', backgroundColor: "#396a93", icon: "st.Weather.weather5"
          	state "0.46 in", label:'${currentValue}', backgroundColor: "#325d81", icon: "st.Weather.weather5"
            state "0.50 in", label:'${currentValue}', backgroundColor: "#2b506e", icon: "st.Weather.weather5"
            state "0.54 in", label:'${currentValue}', backgroundColor: "#24425c", icon: "st.Weather.weather5"
            state "0.58 in", label:'${currentValue}', backgroundColor: "#1d3549", icon: "st.Weather.weather5"
            state "0.62 in", label:'${currentValue}', backgroundColor: "#152837", icon: "st.Weather.weather5"
            state "0.66 in", label:'${currentValue}', backgroundColor: "#0e1b25", icon: "st.Weather.weather5"
            state "0.72 in", label:'${currentValue}', backgroundColor: "#070d12", icon: "st.Weather.weather10"
 		}
 		valueTile("rainSumDay", "device.rainSumDay", width: 5, height: 1, decoration: "flat") {
            state "default", label: 'Cumulative rainfall: ${currentValue}'
 		}
 		standardTile("refresh", "device.rain", width: 1, height: 1, decoration: "flat") {
 			state "default", action:"refresh.poll", icon:"st.secondary.refresh"
 		}

		main (["rain"])
 		details(["rain", "refresh", "rainSumDay"])
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def poll() {
	parent.poll()
}
