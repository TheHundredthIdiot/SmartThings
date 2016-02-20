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
 *		https://community.smartthings.com/t/dlink-dch-z510-siren/35634
 *
 */  

metadata {
	definition (name: "My Dlink DCH-Z510", namespace: "TheHundredthIdiot", author: "Andy") {

 	capability "Actuator"
	capability "Alarm"
	capability "Switch"	
	capability "Configuration"
    
	command "Emergency"
	command "FireAlarm"
	command "Ambulance"
	command "PoliceCar"
	command "DoorChime"
    command "ArmDisarm"
	command "Test"

	fingerprint deviceId: "0x1005", inClusters: "0x20,0x70,0x71,0x98"
}

simulator {
}

tiles(scale: 2) {
	multiAttributeTile(name:"alarm", type: "generic", width: 6, height: 4){
		tileAttribute ("device.alarm", key: "PRIMARY_CONTROL") {
			attributeState "off", label:'off', action:'alarm.both', icon:"st.alarm.alarm.alarm", backgroundColor:"#ffffff"
			attributeState "both", label:'alarm!', action:'alarm.off', icon:"st.alarm.alarm.alarm", backgroundColor:"#e86d13"
		}
	}
	standardTile("off", "device.alarm", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
		state "default", label:'', action:"alarm.off", icon:"st.secondary.off"
	}
	standardTile("Emergency", "device.button", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
		state "default", label:'Emergency', action:"Emergency", icon:"st.Weather.weather1"
	}
	standardTile("FireAlarm", "device.button", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
		state "default", label:'Fire Alarm', action:"FireAlarm", icon:"st.Outdoor.outdoor10"
	}
	standardTile("Ambulance", "device.button", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
		state "default", label:'Ambulance', action:"Ambulance", icon:"st.Transportation.transportation2"
	}
	standardTile("PoliceCar", "device.button", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
		state "default", label:'Police Car', action:"PoliceCar", icon:"st.Transportation.transportation8"
	}
	standardTile("DoorChime", "device.button", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
		state "default", label:'Door Chime', action:"DoorChime", icon:"st.Home.home30"
	}
	standardTile("ArmDisarm", "device.button", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
		state "default", label:'Arm/Disarm', action:"ArmDisarm", icon:"st.Home.home30"
	}
	standardTile("test", "device.alarm", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
		state "default", label:'', action:"Test", icon:"st.secondary.test"
	}
	standardTile("strobe", "device.alarm", inactiveLabel: false, width: 1, height: 1) {
		state "default", label:'', action:"alarm.unavailable", icon:"st.secondary.strobe", backgroundColor:"#000000"
	}
	standardTile("siren", "device.alarm", inactiveLabel: false, width: 1, height: 1) {
		state "default", label:'', action:"alarm.unavailable", icon:"st.secondary.siren", backgroundColor:"#000000"
	}
	main "alarm"
	details(["alarm", "off", "DoorChime", "ArmDisarm", "Emergency", "FireAlarm", "Ambulance", "PoliceCar", "test", "strobe", "siren"])
 }
}

def parse(String description) {
	log.debug "parse($description)"
	def result = null
	def cmd = zwave.parse(description, [0x20: 1, 0x25: 1, 0x26: 1, 0x70: 1, 0x80: 1])
	if (cmd) {
		result = zwaveEvent(cmd)
	}
	log.debug "Parse returned ${result?.inspect()}"
	return result
}

def zwaveEvent(physicalgraph.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) {
	def encapsulatedCommand = cmd.encapsulatedCommand([0x20: 1, 0x25: 3, 0x26: 3, 0x70: 1, 0x80: 1])
	// log.debug "encapsulated: $encapsulatedCommand"
	if (encapsulatedCommand) {
		zwaveEvent(encapsulatedCommand)
	}
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
	log.debug "rx $cmd"
	[
		createEvent([name: "switch", value: cmd.value ? "on" : "off", displayed: false]),
		createEvent([name: "alarm", value: cmd.value ? "both" : "off"]),
	]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
	createEvent(displayed: false, descriptionText: "$device.displayName: $cmd")
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

def Test() {
	[
		secure(zwave.basicV1.basicSet(value: 0xFF)),
		"delay 2000",
		secure(zwave.basicV1.basicSet(value: 0x00)),
		secure(zwave.basicV1.basicGet())
	]
}

private secure(physicalgraph.zwave.Command cmd) {
	zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
}

def on() {
	log.debug "sending on"
	[
		secure(zwave.basicV1.basicSet(value: 0xFF)),
		secure(zwave.basicV1.basicGet())
	]
}

def off() {
	log.debug "sending off"
	[
		secure(zwave.basicV1.basicSet(value: 0x00)),
		secure(zwave.basicV1.basicGet())
	]
}

def Emergency() {
	log.debug "Sounding Siren With Emergency"
	[
		secure(zwave.notificationV3.notificationReport(event: 0x01, notificationType: 0x07)),
		secure(zwave.basicV1.basicGet())
	]
}

def FireAlarm() {
	log.debug "Sounding Siren With Fire Alarm"
	[
		secure(zwave.notificationV3.notificationReport(event: 0x02, notificationType: 0x0A)),
        secure(zwave.basicV1.basicGet())
	]
}

def Ambulance() {
	log.debug "Sounding Siren With Ambulance"
	[
		secure(zwave.notificationV3.notificationReport(event: 0x03, notificationType: 0x0A)),
        secure(zwave.basicV1.basicGet())
	]
}

def PoliceCar() {
	log.debug "Sounding Siren With Police Car"
	[
		secure(zwave.notificationV3.notificationReport(event: 0x01, notificationType: 0x0A)),
        secure(zwave.basicV1.basicGet())
	]
}

def DoorChime() {
	log.debug "Sounding Siren With Door Chime"
	[
		secure(zwave.notificationV3.notificationReport(event: 0x16, notificationType: 0x06)),
        secure(zwave.basicV1.basicGet())
	]
}

def ArmDisarm() {
	log.debug "Sounding Siren With Arm/Disarm"
	[
		secure(zwave.notificationV3.notificationReport(event: 0x06, notificationType: 0x0A)),
        secure(zwave.basicV1.basicGet())
	]
}