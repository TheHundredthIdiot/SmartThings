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
 *		https://community.smartthings.com/t/momentary-vitrual-switches-normally-on-double-pole/29174
 *
 */

metadata {
	
    definition (name: "My Simulated Alarm", namespace: "TheHundredthIdiot", author: "Andy") {
		capability "Alarm"
		capability "Switch"
		
        command "onPhysical"
		command "offPhysical"
	}

	tiles {
		standardTile("switch", "device.switch", width: 3, height: 2, canChangeIcon: true) {
			state "off", label: '${currentValue}', action: "switch.on", icon: "st.alarm.alarm.alarm", backgroundColor: "#ffffff"
			state "on", label: '${currentValue}', action: "switch.off", icon: "st.alarm.alarm.alarm", backgroundColor:"#e86d13"
		}
//		standardTile("on", "device.switch", decoration: "flat") {
//			state "default", label: 'On', action: "onPhysical", backgroundColor: "#ffffff"
//		}
//		standardTile("off", "device.switch", decoration: "flat") {
//			state "default", label: 'Off', action: "offPhysical", backgroundColor: "#ffffff"
//		}
        main "switch"
		details(["switch"])
	}
}

def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

def strobe() {
	on()
}

def siren() {
	on()
}

def both() {
	on()
}

def on() {
	log.debug "$version on()"
	sendEvent(name: "switch", value: "on")
}

def off() {
	log.debug "$version off()"
	sendEvent(name: "switch", value: "off")
}

def onPhysical() {
	log.debug "$version onPhysical()"
	sendEvent(name: "switch", value: "on", type: "physical")
}

def offPhysical() {
	log.debug "$version offPhysical()"
	sendEvent(name: "switch", value: "off", type: "physical")
}

private getVersion() {
	"PUBLISHED"
}