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
 *	Date:	April 2016
 *
 */
 
metadata {
	definition (name: "My Cat Location", namespace: "TheHundredthIdiot", author: "Andy") {
 		
        capability "Actuator"
		
        attribute "catStatus", "string"
        attribute "eventTime", "string"
       
		command "CatIn"
		command "CatOut"
        command "Reset"
	}

	preferences {
		input "cat1", "string", title: "First cats name", description: "Noname"
		input "cat2", "string", title: "Second cats name", description: "There is no second cat"
//		input "cat3", "string", title: "Third cats name", description: "Three, thats pushing it"
//		input "cat4", "string", title: "Fourth cats name", description: "Are you mad?"
		input "resetWhen", "enum", title: "Reset with cats", description: "At Home", options: ['In':'At Home', 'Out':'Out Exploring']
 		input title: "Movement Sensitivity", description: "Treat movements received within this time period as one - default is 1 second (set up 'My Cat Monitor' with the same value)", displayDuringSetup: false, type: "paragraph", element: "paragraph"
		input "movementSensitivity", "number", title: "Milliseconds", description: "1000 milliseconds", displayDuringSetup: false
	}

	tiles (scale: 2) {
    
		valueTile("Status", "device.catStatus", width: 6, height: 4) {
            state "In", label:'At Home', icon:"st.Seasonal Fall.seasonal-fall-010", backgroundColor:"#79b821"
            state "Out", label:'Out Exploring', icon:"st.Outdoor.outdoor1", backgroundColor:"#e63900"
			state "Both", label: 'On the Move', icon:"st.Transportation.transportation1", backgroundColor: "#ffa81e"
			state "Unknown", label: 'Go Get \'Em!', icon:"st.Health & Wellness.health12", backgroundColor: "#e60000"
		}

		valueTile("WhereAreThey", "device.catStatusText", width: 6, height: 2) {
			state "default", label: '${currentValue}'
		}

		standardTile("In", "command.CatIn", inactiveLabel: false, decoration: "flat", width: 3, height: 1) {
			state "default", action:"CatIn", label: '', icon: "st.Seasonal Fall.seasonal-fall-010"
		}
        standardTile("Out", "command.CatOut", inactiveLabel: false, decoration: "flat", width: 3, height: 1) {
			state "default", action:"CatOut", label: '', icon: "st.Outdoor.outdoor1"
		}

		valueTile("DateTime", "device.eventTime", inactiveLabel: false, decoration: "flat", width: 3, height: 1) {
			state "default", label: '${currentValue}'
		}
		standardTile("Reset", "command.Reset", inactiveLabel: false, decoration: "flat", width: 3, height: 1) {
			state "default", action:"Reset", label: '', icon: "st.Office.office8"
		}

		main 	(["Status"])
		details (["Status", 
        		  "WhereAreThey",
        	 	  "In", "Out",
        		  "DateTime", "Reset"])
	}
}

def updated() {
	log.debug ("updated")
    Reset()
}

def installed() {
	log.debug ("installed")
	Reset()
}

def CatIn() {
	log.debug "Cat In"
    def dateNow = new Date()
	if ((dateNow.getTime() - state.lastOpened) > findMovementSensitivity()) {
       	state.lastOpened = dateNow.getTime()
		state.catsIn  = state.catsIn + 1
    	state.catsOut = state.catsOut - 1
    	SendEvent()
    } 
}

def CatOut() {
	log.debug "Cat Out"
    def dateNow = new Date()
	if ((dateNow.getTime() - state.lastOpened) > findMovementSensitivity()) {
       	state.lastOpened = dateNow.getTime()
		state.catsIn  = state.catsIn - 1
	    state.catsOut = state.catsOut + 1
	    SendEvent()
	}
}

private Reset() {
	log.debug "Reset"
    findCatNames()
    
   	if (settings.resetWhen == null || settings.resetWhen == "" || settings.resetWhen == "In") {
		state.catsIn  = state.catTotal
	    state.catsOut = 0
	} else {
    	state.catsIn  = 0
    	state.catsOut = state.catTotal
	}
    
    state.lastOpened = new Date()
	state.lastOpened = state.lastOpened.getTime()

	SendEvent()    
}

private SendEvent() {
	log.debug "Send Event"
	def where = "Unknown"
	def text  = "playing clever\nand could be anywhere, totally lost track!"

	switch(state.catsIn) {
		case 0:
			where = 'Out'
            text  = 'out exploring'
        	break;
    	case state.catTotal:
			where = 'In'
            text  = 'at home'
        	break;
		case 1..state.catTotal:
			where = 'Both'
            text  = 'on the move...'
        	break;
 	}    
    
	if (state.catNames == null || state.catNames == "") {
		state.catNames = "The cat is "
	}

	sendEvent name: "catStatus", value: where 
	sendEvent name: "catStatusText", value: state.catNames + text
    sendEvent name: "eventTime", value: "Last moved on\n" + new Date().format("dd/MM/YY", location.timeZone) + ' at ' + new Date().format("h:mm a", location.timeZone)
}

private findCatNames() {
	log.debug "Find Cat Names"
	state.catTotal = 0
    state.catNames = ''

	if (settings.cat1 != null && settings.cat1 != "") {
//	 	state.catNames = settings.cat1 + ', ' 
    	state.catTotal = state.catTotal + 1
    }
        
	if (settings.cat2 != null && settings.cat2 != "") {
//    	state.catNames = state.catNames + settings.cat2 + ', '
	    state.catTotal = state.catTotal + 1
	}        
        
//	if (settings.cat3 != null && settings.cat3 != "") {
//    	state.catNames = state.catNames + settings.cat3 + ', '
//	    state.catTotal = state.catTotal + 1
//	}        
//
//	if (settings.cat4 != null && settings.cat4 != "") {
//    	state.catNames = state.catNames + settings.cat4 + ', '
//	    state.catTotal = state.catTotal + 1
//	}        

	switch (state.catTotal) {
    	case 0:
			state.catNames = 'Noname the cat is '
		    state.catTotal = 1
            break;
		case 1:
            state.catNames = settings.cat1 + ' the cat is '
            break;
        case 2:
            state.catNames = settings.cat1 + ' and ' + settings.cat2 + ' are '
            break;
 	}
    
    return state.catNames
}

private findMovementSensitivity() {
    (settings.movementSensitivity != null && settings.movementSensitivity != "") ? settings.movementSensitivity : 1000
}