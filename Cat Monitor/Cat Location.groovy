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
 *	Date:	May 2016
 *
 */
 
metadata {
	definition (name: "My Cat Location", namespace: "TheHundredthIdiot", author: "Andy") {
 		
        capability "Actuator"
		
        attribute "catStatus", "string"
        attribute "InTime", "string"
        attribute "OutTime", "string"
       
		command "CatIn"
		command "CatOut"
        command "Reset"
	}

	preferences {
		input "catTotal", "number", title: "Number of Cats", description: "1"
		input "resetWhen", "enum", title: "Reset with cats", description: "At Home", options: ['In':'At Home', 'Out':'Out Exploring']
 		input title: "Movement Sensitivity", description: "Treat movements received within this time period as one with the initial movement defining direction, default is 1 second", displayDuringSetup: false, type: "paragraph", element: "paragraph"
		input "sensitivity", "number", title: "Milliseconds", description: "1000", displayDuringSetup: false
	}

	tiles (scale: 2) {
    
		valueTile("Status", "device.catStatus", width: 6, height: 4) {
			state "default", label: 'Go Find \'Em!', icon:"st.Health & Wellness.health12", backgroundColor: "#e60000"
            state "Out", 	 label: 'Out Exploring', icon:"st.Outdoor.outdoor1", backgroundColor:"#e63900"
			state "Moving",  label: 'On the Move', 	 icon:"st.Transportation.transportation1", backgroundColor: "#ffa81e"
            state "Home", 	 label: 'At Home',		 icon:"st.Seasonal Fall.seasonal-fall-010", backgroundColor:"#79b821"
		}

		valueTile("InTime", "device.inTime", inactiveLabel: false, decoration: "flat", width: 3, height: 1) {
			state "default", label: 'In: ${currentValue}'
		}
		valueTile("OutTime", "device.outTime", inactiveLabel: false, decoration: "flat", width: 3, height: 1) {
			state "default", label: 'Out: ${currentValue}'
		}

		standardTile("In", "command.CatIn", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", action:"CatIn", label: '', icon: "st.Seasonal Fall.seasonal-fall-010", backgroundColor:"#79b821"
		}
		standardTile("Reset", "command.Reset", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", action:"Reset", label: '', icon: "st.Office.office8"
		}
        standardTile("Out", "command.CatOut", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", action:"CatOut", label: '', icon: "st.Outdoor.outdoor1", backgroundColor:"#e63900"
		}

		main 	(["Status"])
		details (["Status", 
        		  "InTime", "OutTime",
                  "In", "Reset", "Out",])
	}
}

def updated() {
	log.debug ("updated")
//	Reset()
}

def installed() {
	log.debug ("installed")
	Reset()
}

def CatIn() {
	log.debug "Cat In"
	SendEvent('In')
}

def CatOut() {
	log.debug "Cat Out"
    SendEvent('Out')
}

private Reset() {
	log.debug "Reset"

	if (settings.resetWhen == null || settings.resetWhen == "" || settings.resetWhen == "In") {
		state.catsIn  = settings.catTotal
	    state.catsOut = 0
        state.statusIs = 'Home'
	} else {
    	state.catsIn  = 0
    	state.catsOut = settings.catTotal
        state.statusIs = 'Out'
	}
    
	state.inTime  = "Reset"
    state.outTime = "Reset"
    
	SendEvent('Reset')    
}

private SendEvent(Type) {
    
	if (Type == 'Reset') {
		log.debug "Send Reset Events"
        state.lastOpened = 0
		sendEvent name: "inTime", 	 value: state.inTime
    	sendEvent name: "outTime", 	 value: state.outTime
	} else {
		def dateNow = new Date()
		if (((dateNow.getTime() - state.lastOpened) > findSensitivity())) {
   			log.debug "Send " + Type + " Events"
    		state.lastOpened = dateNow.getTime()
			if (Type == 'In') {
				state.catsIn  = state.catsIn + 1
    			state.catsOut = state.catsOut - 1
			    state.inTime  = new Date().format("HH:mm:ss", location.timeZone)
			} else if (Type == 'Out') {
				state.catsIn  = state.catsIn - 1
	    		state.catsOut = state.catsOut + 1
			   	state.outTime = new Date().format("HH:mm:ss", location.timeZone)
			}
	
			if (state.catsIn < 0 || state.catsIn > settings.catTotal) {
        		state.statusIs = 'Check'
        	} else if (state.catsIn == settings.catTotal) {
        	    state.statusIs = 'Home'    
        	} else if (state.catsIn == 0) {
        		state.statusIs = 'Out'
        	} else {
               	state.statusIs = 'Moving'
			}
       	
			if (Type == 'In') {
				sendEvent name: "inTime", value: state.inTime
	    	} else {
  	    	 	sendEvent name: "outTime", value: state.outTime
			}
    	}
	}

    sendEvent name: "catStatus", value: state.statusIs

}

private findSensitivity() {
	(settings.sensitivity != null && settings.sensitivity != "") ? settings.sensitivity : 1000
}