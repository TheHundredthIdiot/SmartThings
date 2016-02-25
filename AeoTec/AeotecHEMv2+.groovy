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
 */

metadata {
	definition (name: "My Aeon HEMv2+", namespace: "TheHundredthIdiot", author: "Andy") {
    
    	capability "Energy Meter"
		capability "Power Meter"
		capability "Configuration"
		capability "Sensor"
        capability "Refresh"
        capability "Polling"
        
        attribute "energy", "string"
        attribute "power", "string"
        attribute "volts", "string"
        attribute "voltage", "string"		// We'll deliver both, since the correct one is not defined anywhere
        attribute "amps", "string"
        
        attribute "energyReset", "string"
        attribute "energyOne", "string"
        attribute "energyTwo", "string"
        
        attribute "powerOne", "string"
        attribute "powerTwo", "string"
        
        attribute "voltsOne", "string"
        attribute "voltsTwo", "string"
        
        attribute "ampsOne", "string"
        attribute "ampsTwo", "string"        
        
		command "reset"
        command "configure"
        command "refresh"
        command "poll"
        command "toggleDisplay"
        
		fingerprint deviceId: "0x3101", inClusters: "0x70,0x32,0x60,0x85,0x56,0x72,0x86"
	}

	simulator {
		for (int i = 0; i <= 10000; i += 1000) {
			status "power  ${i} W": new physicalgraph.zwave.Zwave().meterV1.meterReport(
				scaledMeterValue: i, precision: 3, meterType: 33, scale: 2, size: 4).incomingMessage()
		}
		for (int i = 0; i <= 100; i += 10) {
			status "energy  ${i} kWh": new physicalgraph.zwave.Zwave().meterV1.meterReport(
				scaledMeterValue: i, precision: 3, meterType: 33, scale: 0, size: 4).incomingMessage()
		}
        // TODO: Add data feeds for Volts and Amps
	}

	preferences {
        input "kWhCost", "string", title: "Cost per kWh (default 0.10)", required: true, defaultValue: "0.10"
		input "costUnits", "enum", title: "Report cost in", required: true, options: ['£':'Pounds (£)', '€':'Euros (€)', '$':'Dollars ($)']
   		input "kWhDelay", "number", title: "kWh report interval (default 120s)", required: true, defaultValue: 120
    	input "detailDelay", "number", title: "Detail report interval (default 30s)", required: true, defaultValue: 30
	}

	tiles (scale: 2)  {

		multiAttributeTile(name:"main", type:"lighting", width: 6, height: 6) {
				tileAttribute("device.power", key: "PRIMARY_CONTROL") {
    		        attributeState "default", label: '${currentValue} Watts', icon: "st.Lighting.light14", backgroundColors:[
						[value: "0", 	color: "#008000"],
						[value: "500", 	color: "#00b300"],
						[value: "1000", color: "#88cc00"],
						[value: "1500", color: "#f1d801"],
						[value: "2000", color: "#ff9900"],
						[value: "2500", color: "#ff8000"], 
						[value: "3000", color: "#bc2323"]
        			    ]
				}
 	        	tileAttribute("device.amps", key: "SECONDARY_CONTROL") {
 	            	attributeState "default", label:'${currentValue} Amps'
				}
	 		}

	// Energy row

		valueTile("energy", "device.energyReset", width: 2, height: 1, decoration: "flat") {
			state("default", label: '${currentValue}')
		}
		valueTile("energyOne", "device.energyOne", width: 2, height: 1, decoration: "flat") {
			state("default", label: '${currentValue}')
		}
		valueTile("energyTwo", "device.energyTwo", width: 2, height: 1, decoration: "flat") {
			state("default", label: '${currentValue}')
		}

	// Power row
    
		valueTile("power", "device.power", width: 2, height: 1) {
			state ("default", label:'${currentValue}\nWatts', backgroundColors:[
						[value: "0", 	color: "#008000"],
						[value: "500", 	color: "#00b300"],
						[value: "1000", color: "#88cc00"],
						[value: "1500", color: "#f1d801"],
						[value: "2000", color: "#ff9900"],
						[value: "2500", color: "#ff8000"], 
						[value: "3000", color: "#bc2323"]
        			    ]
            )
        } 
		valueTile("powerOne", "device.powerOne", width: 2, height: 1, decoration: "flat") {
			state ("default", label:'${currentValue}')
        } 
        valueTile("powerTwo", "device.powerTwo", width: 2, height: 1, decoration: "flat") {
			state ("default", label:'${currentValue}')
        } 

	// Amps row

		valueTile("amps", "device.amps", width: 2, height: 1, decoration: "flat") {
        	state ("default", label: '${currentValue}\nAmps'
			)
        }    
        valueTile("ampsOne", "device.ampsOne", width: 2, height: 1, decoration: "flat") {
        	state ("default", label: '${currentValue}')
        }    
        valueTile("ampsTwo", "device.ampsTwo", width: 2, height: 1, decoration: "flat") {
        	state ("default", label: '${currentValue}')
        }
        
	// Volts row
       	
        valueTile("volts", "device.volts", width: 2, height: 1, decoration: "flat") {
        	state("default", label: '${currentValue}\nVolts')
		}
       	valueTile("voltsOne", "device.voltsOne", width: 2, height: 1, decoration: "flat") {
        	state("default", label: '${currentValue}')
		}
       	valueTile("voltsTwo", "device.voltsTwo", width: 2, height: 1, decoration: "flat") {
        	state("default", label: '${currentValue}')
		}        

    // Controls row
    
		standardTile("refresh", "command.refresh", width: 2, height: 1, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh-icon"
		}
		standardTile("toggle", "command.toggleDisplay", width: 2, height: 1, decoration: "flat") {
			state "default", label: "", action: "toggleDisplay", icon: "st.motion.motion.inactive"
		}
		standardTile("reset", "command.reset", width: 1, height: 1, decoration: "flat") {
			state "default", label:'', action:"reset", icon: "st.Health & Wellness.health7"
		}
		standardTile("configure", "command.configure", width: 1, height: 1, decoration: "flat") {
			state "configure", label:'', action: "configure", icon:"st.secondary.configure"
		}

		main (["main"])
		details(["main", 
        		 "energy", "energyOne", "energyTwo",
            	 "power", "powerOne", "powerTwo",
            	 "amps", "ampsOne", "ampsTwo",
            	 "volts", "voltsOne", "voltsTwo",
				 "refresh", "toggle", "reset", "configure"
		])
	}
}

def installed() {
	state.display = 1
	reset()						// The order here is important
	configure()					// Since reports can start coming in even before we finish configure()
	refresh()
}

def updated() {
	configure()
	resetDisplay()
	refresh()
}

def parse(String description) {
//  log.debug "Parse received ${description}"
	def result = null
	def cmd = zwave.parse(description, [0x31: 1, 0x32: 1, 0x60: 3])
	if (cmd) {
		result = createEvent(zwaveEvent(cmd))
	}
	if (result) { 
		log.debug "Parse returned ${result?.descriptionText}"
		return result
	} else {
	}
}

def zwaveEvent(physicalgraph.zwave.commands.meterv1.MeterReport cmd) {
    def dispValue
    def newValue
    def formattedValue
    def MAX_AMPS = 220
    def MAX_WATTS = 24000
    
    def dateString = new Date().format("d/M/YY H:mm", location.timeZone)
    
    if (cmd.meterType == 33) {
		if (cmd.scale == 0) {
        	newValue = Math.round(cmd.scaledMeterValue * 100) / 100
			if (newValue != state.energyValue) {
        		formattedValue = String.format("%1.2f", newValue)
				dispValue = "${formattedValue}\nkWh"
                state.energyValueDisp = "${formattedValue}\nkWh"
                sendEvent(name: "energyOne", value: dispValue as String, unit: "", descriptionText: "Display Energy: ${newValue} kWh", displayed: false)
                state.energyValue = newValue
                BigDecimal costDecimal = newValue * ( kWhCost as BigDecimal )
                def costDisplay = String.format("%1.2f",costDecimal)
                state.costDisp = "Cost\n${settings.costUnits}"+costDisplay
                if (state.display == 1) { sendEvent(name: "energyTwo", value: state.costDisp, unit: "", descriptionText: "Display Cost: ${settings.costUnits}${costDisp}", displayed: false) }
                [name: "energy", value: newValue, unit: "kWh", descriptionText: "Total Energy: ${formattedValue} kWh"]
            }
		} 
		else if (cmd.scale==2) {				
        	newValue = Math.round(cmd.scaledMeterValue)		// Not worth the hassle to show decimals for Watts
            if (newValue > MAX_WATTS) { return }			// Ignore ridiculous values (a 200Amp supply @ 120volts is roughly 24000 watts)
        	if (newValue != state.powerValue) {
	   			dispValue = newValue+"\nWatts"
                if (newValue < state.powerLow) {
                	dispValue = newValue+" Watts\n"+dateString
                	if (state.display == 1) { sendEvent(name: "powerOne", value: dispValue as String, unit: "", descriptionText: "Lowest Power: ${newValue} Watts")	}
                    state.powerLow = newValue
                    state.powerLowDisp = dispValue
                }
                if (newValue > state.powerHigh) {
                	dispValue = newValue+" Watts\n"+dateString
                	if (state.display == 1) { sendEvent(name: "powerTwo", value: dispValue as String, unit: "", descriptionText: "Highest Power: ${newValue} Watts")	}
                    state.powerHigh = newValue
                    state.powerHighDisp = dispValue
                }
                state.powerValue = newValue
                [name: "power", value: newValue, unit: "W", descriptionText: "Total Power: ${newValue} Watts"]
            }
		}
 	}
    else if (cmd.meterType == 161) {
    	if (cmd.scale == 0) {
        	newValue = Math.round( cmd.scaledMeterValue * 100) / 100
        	if (newValue != state.voltsValue) {
        		formattedValue = String.format("%1.2f", newValue)
    			dispValue = "${formattedValue}\nVolts"
                if (newValue < state.voltsLow) {
                	dispValue = formattedValue+" Volts\n"+dateString                	
                	if (state.display == 1) { sendEvent(name: "voltsOne", value: dispValue as String, unit: "", descriptionText: "Lowest Voltage: ${formattedValue} Volts")	}
                    state.voltsLow = newValue
                    state.voltsLowDisp = dispValue
                }
                if (newValue > state.voltsHigh) {
                    dispValue = formattedValue+" Volts\n"+dateString
                	if (state.display == 1) { sendEvent(name: "voltsTwo", value: dispValue as String, unit: "", descriptionText: "Highest Voltage: ${formattedValue} Volts") }
                    state.voltsHigh = newValue
                    state.voltsHighDisp = dispValue
                }                
                state.voltsValue = newValue
                sendEvent( name: "voltage", value: newValue, unit: "V", descriptionText: "Total Voltage: ${formattedValue} Volts")
				[name: "volts", value: newValue, unit: "V", descriptionText: "Total Volts: ${formattedValue} Volts"]
            }
        }
        else if (cmd.scale==1) {
        	newValue = Math.round( cmd.scaledMeterValue * 100) / 100
            if ( newValue > MAX_AMPS) { return }						// Ignore silly values for 200Amp service
        	if (newValue != state.ampsValue) {
        		formattedValue = String.format("%1.2f", newValue)
    			dispValue = "${formattedValue}\nAmps"
                if (newValue < state.ampsLow) {
                	dispValue = formattedValue+" Amps\n"+dateString
                	if (state.display == 1) { sendEvent(name: "ampsOne", value: dispValue as String, unit: "", descriptionText: "Lowest Current: ${formattedValue} Amps") }
                    state.ampsLow = newValue
                    state.ampsLowDisp = dispValue
                }
                if (newValue > state.ampsHigh) {
                	dispValue = formattedValue+" Amps\n"+dateString
                	if (state.display == 1) { sendEvent(name: "ampsTwo", value: dispValue as String, unit: "", descriptionText: "Highest Current: ${formattedValue} Amps") }
                    state.ampsHigh = newValue
                    state.ampsHighDisp = dispValue
                }                
                state.ampsValue = newValue
				[name: "amps", value: newValue, unit: "A", descriptionText: "Total Current: ${formattedValue} Amps"]
            }
        }
    }           
}

def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
	def dispValue
	def newValue
	def formattedValue
    def MAX_AMPS = 220
    def MAX_WATTS = 24000

   	if (cmd.commandClass == 50) {    
   		def encapsulatedCommand = cmd.encapsulatedCommand([0x30: 1, 0x31: 1]) 		// Can specify command class versions here like in zwave.parse
		if (encapsulatedCommand) {
			if (cmd.sourceEndPoint == 1) {
				if (encapsulatedCommand.scale == 2 ) {
					newValue = Math.round(encapsulatedCommand.scaledMeterValue)
                    if (newValue > MAX_WATTS) { return }
					formattedValue = newValue as String
					dispValue = "${formattedValue}\nWatts"
					if (dispValue != state.powerL1Disp) {
						state.powerL1Disp = dispValue
						if (state.display == 2) {
							[name: "powerOne", value: dispValue, unit: "", descriptionText: "L1 Power: ${formattedValue} Watts"]
						}
						else {
						}
					}
				} 
				else if (encapsulatedCommand.scale == 0 ){
					newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
					formattedValue = String.format("%1.2f", newValue)
					dispValue = "${formattedValue}\nkWh"
					if (dispValue != state.energyL1Disp) {
						state.energyL1Disp = dispValue
						if (state.display == 2) {
							[name: "energyOne", value: dispValue, unit: "", descriptionText: "L1 Energy: ${formattedValue} kWh"]
						}
						else {
                        	[name: "energyOne", value: state.energyValueDisp, unit: "kWh", descriptionText: "Total Energy: ${state.energyValueDisp}"]
						}
					}
				}
				else if (encapsulatedCommand.scale == 5 ) {
					newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
                    if (newValue > MAX_AMPS) { return }
					formattedValue = String.format("%1.2f", newValue)
					dispValue = "${formattedValue}\nAmps"
					if (dispValue != state.ampsL1Disp) {
						state.ampsL1Disp = dispValue
						if (state.display == 2) {
							[name: "ampsOne", value: dispValue, unit: "", descriptionText: "L1 Current: ${formattedValue} Amps"]
						}
						else {
						}
					}
               	} 
			}            
			else if (cmd.sourceEndPoint == 2) {
				if (encapsulatedCommand.scale == 2 ){
					newValue = Math.round(encapsulatedCommand.scaledMeterValue)
                    if (newValue > MAX_WATTS ) { return }
					formattedValue = newValue as String
					dispValue = "${formattedValue}\nWatts"
					if (dispValue != state.powerL2Disp) {
						state.powerL2Disp = dispValue
						if (state.display == 2) {
							[name: "powerTwo", value: dispValue, unit: "", descriptionText: "L2 Power: ${formattedValue} Watts"]
						}
						else {
						}
					}
				} 
				else if (encapsulatedCommand.scale == 0 ){
					newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
					formattedValue = String.format("%1.2f", newValue)
					dispValue = "${formattedValue}\nkWh"
					if (dispValue != state.energyL2Disp) {
						state.energyL2Disp = dispValue
						if (state.display == 2) {
							[name: "energyTwo", value: dispValue, unit: "", descriptionText: "L2 Energy: ${formattedValue} kWh"]
						}
						else {
						}
					}
				} 
				else if (encapsulatedCommand.scale == 5 ){
               		newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
                    if (newValue > MAX_AMPS) { return } 
					formattedValue = String.format("%1.2f", newValue)
					dispValue = "${formattedValue}\nAmps"
					if (dispValue != state.ampsL2Disp) {
						state.ampsL2Disp = dispValue
						if (state.display == 2) {
							[name: "ampsTwo", value: dispValue, unit: "", descriptionText: "L2 Current: ${formattedValue} Amps"]
						}
						else {
						}
					}
				}
			}
		}
	}
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
	// Handles all Z-Wave commands we aren't interested in
    log.debug "Unhandled event ${cmd}"
	[:]
}

def refresh() {			
	log.debug "refresh()"
    
//	Request HEMv2 to send us the latest values for the 4 we are tracking

	delayBetween([
		zwave.meterV2.meterGet(scale: 0).format(),
		zwave.meterV2.meterGet(scale: 2).format(),
		zwave.meterV2.meterGet(scale: 4).format(),
		zwave.meterV2.meterGet(scale: 5).format()
	])
    resetDisplay()
}

def poll() {
	log.debug "poll()"
	refresh()
}

def toggleDisplay() {
	log.debug "toggleDisplay()"
    
	if (state.display == 1) { 
		state.display = 2 
	}
	else { 
		state.display = 1
	}
	resetDisplay()
}

def resetDisplay() {
	log.debug "resetDisplay()"
	
	if ( state.display == 1 ) {
    	sendEvent(name: "voltsOne", value: state.voltsLowDisp, unit: "")
    	sendEvent(name: "ampsOne", value: state.ampsLowDisp, unit: "")    
		sendEvent(name: "powerOne", value: state.powerLowDisp, unit: "")     
    	sendEvent(name: "energyOne", value: state.energyValueDisp, unit: "")
      	sendEvent(name: "voltsTwo", value: state.voltsHighDisp, unit: "")
    	sendEvent(name: "ampsTwo", value: state.ampsHighDisp, unit: "")
    	sendEvent(name: "powerTwo", value: state.powerHighDisp, unit: "")
    	sendEvent(name: "energyTwo", value: state.costDisp, unit: "")    	
	}
	else {
    	sendEvent(name: "voltsOne", value: "L1", unit: "")
    	sendEvent(name: "ampsOne", value: state.ampsL1Disp, unit: "")    
		sendEvent(name: "powerOne", value: state.powerL1Disp, unit: "")     
    	sendEvent(name: "energyOne", value: state.energyL1Disp, unit: "")
		sendEvent(name: "voltsTwo", value: "L2", unit: "")
    	sendEvent(name: "ampsTwo", value: state.ampsL2Disp, unit: "")
    	sendEvent(name: "powerTwo", value: state.powerL2Disp, unit: "")
    	sendEvent(name: "energyTwo", value: state.energyL2Disp, unit: "")
	}
}

def reset() {
	log.debug "reset()"

	state.energyValue = -1
    state.energyValueDisp = ""
	state.powerValue = -1
	state.ampsValue = -1
	state.voltsValue = -1
	
    state.powerHigh = 0
    state.powerHighDisp = ""
    state.powerLow = 99999
    state.powerLowDisp = ""
    state.ampsHigh = 0
    state.ampsHighDisp = ""
    state.ampsLow = 999
    state.ampsLowDisp = ""
    state.voltsHigh = 0
    state.voltsHighDisp = ""
    state.voltsLow = 999
    state.voltsLowDisp = ""
    
    state.energyL1Disp = ""
    state.energyL2Disp = ""
    state.powerL1Disp = ""
    state.powerL2Disp = ""
    state.ampsL1Disp = ""
    state.ampsL2Disp = ""
    
    if (!state.display) { state.display = 1 }	// Sometimes it appears that installed() isn't called

	state.lastResetTime = "Last Reset\n"+(new Date().format("d/M/YY H:mm", location.timeZone))

	state.costDisp = "Cost\n${settings.costUnits}0.00"
	
    resetDisplay()
   	sendEvent(name: "energyReset", value: state.lastResetTime, unit: "")
	sendEvent(name: "energyValueDisp", value: "", unit: "")
 
//	Reset all values 
	log.debug("reset() - Reset all values") 
	return [zwave.meterV2.meterReset().format()]
    
// 	Request the values we are interested in (0-->1 for kVAh)
/*	def cmd = delayBetween( [
		zwave.meterV2.meterGet(scale: 0).format(),	
		zwave.meterV2.meterGet(scale: 2).format(),
		zwave.meterV2.meterGet(scale: 4).format(),
		zwave.meterV2.meterGet(scale: 5).format()
	], 1000)
    cmd
    
    configure()	*/
}

def configure() {
	log.debug "configure()"

    Long kDelay = settings.kWhDelay as Long
    Long dDelay = settings.detailDelay as Long
    
    if (kDelay == null) {
		kDelay = 15
	}

	if (dDelay == null) {
		dDelay = 15
	}
    
	def cmd = delayBetween([
		zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, scaledConfigurationValue: 0).format(),			// Disable (=0) selective reporting
//		zwave.configurationV1.configurationSet(parameterNumber: 4, size: 2, scaledConfigurationValue: 5).format(),			// Don't send whole HEM unless watts have changed by 30
//		zwave.configurationV1.configurationSet(parameterNumber: 5, size: 2, scaledConfigurationValue: 5).format(),			// Don't send L1 Data unless watts have changed by 15
//		zwave.configurationV1.configurationSet(parameterNumber: 6, size: 2, scaledConfigurationValue: 5).format(),			// Don't send L2 Data unless watts have changed by 15
//      zwave.configurationV1.configurationSet(parameterNumber: 8, size: 1, scaledConfigurationValue: 1).format(),			// Or by 5% (whole HEM)
//		zwave.configurationV1.configurationSet(parameterNumber: 9, size: 1, scaledConfigurationValue: 1).format(),			// Or by 5% (L1)
//      zwave.configurationV1.configurationSet(parameterNumber: 10, size: 1, scaledConfigurationValue: 1).format(),			// Or by 5% (L2)
//		zwave.configurationV1.configurationSet(parameterNumber: 101, size: 4, scaledConfigurationValue: 6145).format(),   	// Whole HEM and L1/L2 power in kWh
//		zwave.configurationV1.configurationSet(parameterNumber: 111, size: 4, scaledConfigurationValue: kDelay).format(), 	// Default every 120 Seconds
//		zwave.configurationV1.configurationSet(parameterNumber: 102, size: 4, scaledConfigurationValue: 1573646).format(),  // L1/L2 for Amps & Watts, Whole HEM for Amps, Watts, & Volts
//		zwave.configurationV1.configurationSet(parameterNumber: 112, size: 4, scaledConfigurationValue: dDelay).format(), 	// Default every 30 seconds

//		zwave.configurationV1.configurationSet(parameterNumber: 100, size: 1, scaledConfigurationValue: 0).format(),		// reset to defaults
		zwave.configurationV1.configurationSet(parameterNumber: 101, size: 4, scaledConfigurationValue: 6149).format(),   	// All L1/L2 kWh, total Volts & kWh
		zwave.configurationV1.configurationSet(parameterNumber: 111, size: 4, scaledConfigurationValue: 60).format(), 		// Every 60 seconds
		zwave.configurationV1.configurationSet(parameterNumber: 102, size: 4, scaledConfigurationValue: 1572872).format(),	// Amps L1, L2, Total
		zwave.configurationV1.configurationSet(parameterNumber: 112, size: 4, scaledConfigurationValue: 30).format(), 		// every 30 seconds
		zwave.configurationV1.configurationSet(parameterNumber: 103, size: 4, scaledConfigurationValue: 770).format(),		// Power (Watts) L1, L2, Total
		zwave.configurationV1.configurationSet(parameterNumber: 113, size: 4, scaledConfigurationValue: 6).format() 		// every 6 seconds
	], 2000)
	log.debug cmd

	cmd
}