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
 *		https://en.wikipedia.org/wiki/Beaufort_scale
 *		https://en.wikipedia.org/wiki/Points_of_the_compass
 *
 */
 
import java.text.DecimalFormat
import groovy.json.JsonSlurper

private apiUrl() 			{ "https://api.netatmo.com" }
private getVendorName() 	{ "netatmo" }
private getVendorAuthPath()	{ "https://api.netatmo.com/oauth2/authorize?" }
private getVendorTokenPath(){ "https://api.netatmo.com/oauth2/token" }
private getVendorIcon()		{ "https://s3.amazonaws.com/smartapp-icons/Partner/netamo-icon-1%402x.png" }
private getClientId() 		{ appSettings.clientId }
private getClientSecret() 	{ appSettings.clientSecret }
private getServerUrl() 		{ "https://graph-eu01-euwest1.api.smartthings.com" }

// Automatically generated. Make future change here.
definition(
    name: "My Netatmo (Connect)",
    namespace: "TheHundredthIdiot",
    author: "Andy",
    description: "Netatmo Integration",
    category: "SmartThings Labs",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Partner/netamo-icon-1.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Partner/netamo-icon-1%402x.png",
    oauth: true,
    singleInstance: true
){
	appSetting "clientId"
	appSetting "clientSecret"
}

preferences {
	page(name: "Credentials", title: "Fetch OAuth2 Credentials", content: "authPage", install: false)
	page(name: "listDevices", title: "Netatmo Devices", content: "listDevices", install: false)
    page(name: "selectDefaultPreferences", title: "Device Preferences", content: "selectDefaultPreferences", install: false)
    page(name: "selectAdditionalPreferences", title: "Additional Preferences", content: "selectAdditionalPreferences", install: true)

}

mappings {
	path("/receivedToken"){action: [POST: "receivedToken", GET: "receivedToken"]}
	path("/receiveToken"){action: [POST: "receiveToken", GET: "receiveToken"]}
    path("/auth"){action: [GET: "auth"]}
}

def authPage() {
	log.debug "In authPage"
	if(canInstallLabs()) {
		def description = null

		if (state.vendorAccessToken == null) {
			log.debug "About to create access token."

			createAccessToken()
			description = "Tap to enter Credentials."

			return dynamicPage(name: "Credentials", title: "Authorize Connection", nextPage:"listDevices", uninstall: true, install:false) {
				section { href url:buildRedirectUrl("auth"), style:"embedded", required:false, title:"Connect to ${getVendorName()}:", description:description }
			}
		} else {
			description = "Tap 'Next' to proceed"

			return dynamicPage(name: "Credentials", title: "Credentials Accepted!", nextPage:"listDevices", uninstall: true, install:false) {
				section { href url: buildRedirectUrl("receivedToken"), style:"embedded", required:false, title:"${getVendorName()} is now connected to SmartThings!", description:description }
			}
		}
	}
	else
	{
		def upgradeNeeded = """To use SmartThings Labs, your Hub should be completely up to date.

To update your Hub, access Location Settings in the Main Menu (tap the gear next to your location name), select your Hub, and choose "Update Hub"."""

		return dynamicPage(name:"Credentials", title:"Upgrade needed!", nextPage:"", install:false, uninstall: true) {
			section {
				paragraph "$upgradeNeeded"
			}
		}

	}
}

def auth() {
	redirect location: oauthInitUrl()
}

def oauthInitUrl() {
	log.debug "In oauthInitUrl"

	/* OAuth Step 1: Request access code with our client ID */

	state.oauthInitState = UUID.randomUUID().toString()

	def oauthParams = [ response_type: "code",
		client_id: getClientId(),
		state: state.oauthInitState,
		redirect_uri: buildRedirectUrl("receiveToken") ,
		scope: "read_station"
		]

	return getVendorAuthPath() + toQueryString(oauthParams)
}

def buildRedirectUrl(endPoint) {
	log.debug "In buildRedirectUrl"

	return getServerUrl() + "/api/token/${state.accessToken}/smartapps/installations/${app.id}/${endPoint}"
}

def receiveToken() {
	log.debug "In receiveToken"

	def oauthParams = [
		client_secret: getClientSecret(),
		client_id: getClientId(),
		grant_type: "authorization_code",
		redirect_uri: buildRedirectUrl('receiveToken'),
		code: params.code,
		scope: "read_station"
		]

	def tokenUrl = getVendorTokenPath()
	def params = [
		uri: tokenUrl,
		contentType: 'application/x-www-form-urlencoded',
		body: oauthParams,
	]

    log.debug params

	/* OAuth Step 2: Request access token with our client Secret and OAuth "Code" */
	try {
		httpPost(params) { response ->
        	log.debug response.data
			def slurper = new JsonSlurper();

			response.data.each {key, value ->
				def data = slurper.parseText(key);
				log.debug "Data: $data"

				state.vendorRefreshToken = data.refresh_token
				state.vendorAccessToken = data.access_token
				state.vendorTokenExpires = now() + (data.expires_in * 1000)
				return
			}

		}
	} catch (Exception e) {
		log.debug "Error: $e"
	}

	log.debug "State: $state"

	if ( !state.vendorAccessToken ) {  //We didn't get an access token, bail on install
		return
	}

	/* OAuth Step 3: Use the access token to call into the vendor API throughout your code using state.vendorAccessToken. */

	def html = """
        <!DOCTYPE html>
        <html>
        <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>${getVendorName()} Connection</title>
        <style type="text/css">
            * { box-sizing: border-box; }
            @font-face {
                font-family: 'Swiss 721 W01 Thin';
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot');
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot?#iefix') format('embedded-opentype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.woff') format('woff'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.ttf') format('truetype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.svg#swis721_th_btthin') format('svg');
                font-weight: normal;
                font-style: normal;
            }
            @font-face {
                font-family: 'Swiss 721 W01 Light';
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot');
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot?#iefix') format('embedded-opentype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.woff') format('woff'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.ttf') format('truetype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.svg#swis721_lt_btlight') format('svg');
                font-weight: normal;
                font-style: normal;
            }
            .container {
                width: 100%;
                padding: 40px;
                /*background: #eee;*/
                text-align: center;
            }
            img {
                vertical-align: middle;
            }
            img:nth-child(2) {
                margin: 0 30px;
            }
            p {
                font-size: 2.2em;
                font-family: 'Swiss 721 W01 Thin';
                text-align: center;
                color: #666666;
                margin-bottom: 0;
            }
        /*
            p:last-child {
                margin-top: 0px;
            }
        */
            span {
                font-family: 'Swiss 721 W01 Light';
            }
        </style>
        </head>
        <body>
            <div class="container">
                <img src=""" + getVendorIcon() + """ alt="Vendor icon" />
                <img src="https://s3.amazonaws.com/smartapp-icons/Partner/support/connected-device-icn%402x.png" alt="connected device icon" />
                <img src="https://s3.amazonaws.com/smartapp-icons/Partner/support/st-logo%402x.png" alt="SmartThings logo" />
                <p>We have located your """ + getVendorName() + """ account.</p>
                <p>Tap 'Done' to process your credentials.</p>
			</div>
        </body>
        </html>
        """
	render contentType: 'text/html', data: html
}

def receivedToken() {
	log.debug "In receivedToken"

	def html = """
        <!DOCTYPE html>
        <html>
        <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Withings Connection</title>
        <style type="text/css">
            * { box-sizing: border-box; }
            @font-face {
                font-family: 'Swiss 721 W01 Thin';
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot');
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot?#iefix') format('embedded-opentype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.woff') format('woff'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.ttf') format('truetype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.svg#swis721_th_btthin') format('svg');
                font-weight: normal;
                font-style: normal;
            }
            @font-face {
                font-family: 'Swiss 721 W01 Light';
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot');
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot?#iefix') format('embedded-opentype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.woff') format('woff'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.ttf') format('truetype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.svg#swis721_lt_btlight') format('svg');
                font-weight: normal;
                font-style: normal;
            }
            .container {
                width: 560px;
                padding: 40px;
                /*background: #eee;*/
                text-align: center;
            }
            img {
                vertical-align: middle;
            }
            img:nth-child(2) {
                margin: 0 30px;
            }
            p {
                font-size: 2.2em;
                font-family: 'Swiss 721 W01 Thin';
                text-align: center;
                color: #666666;
                margin-bottom: 0;
            }
        /*
            p:last-child {
                margin-top: 0px;
            }
        */
            span {
                font-family: 'Swiss 721 W01 Light';
            }
        </style>
        </head>
        <body>
            <div class="container">
                <img src=""" + getVendorIcon() + """ alt="Vendor icon" />
                <img src="https://s3.amazonaws.com/smartapp-icons/Partner/support/connected-device-icn%402x.png" alt="connected device icon" />
                <img src="https://s3.amazonaws.com/smartapp-icons/Partner/support/st-logo%402x.png" alt="SmartThings logo" />
                <p>Tap 'Done' to continue to Devices.</p>
			</div>
        </body>
        </html>
        """
	render contentType: 'text/html', data: html
}

def refreshToken() {
	log.debug "In refreshToken"

	def oauthParams = [
		client_secret: getClientSecret(),
		client_id: getClientId(),
		grant_type: "refresh_token",
		refresh_token: state.vendorRefreshToken
		]

	def tokenUrl = getVendorTokenPath()
	def params = [
		uri: tokenUrl,
		contentType: 'application/x-www-form-urlencoded',
		body: oauthParams,
	]

	/* OAuth Step 2: Request access token with our client Secret and OAuth "Code" */
	try {
		httpPost(params) { response ->
			def slurper = new JsonSlurper();

			response.data.each {key, value ->
				def data = slurper.parseText(key);
				log.debug "Data: $data"

				state.vendorRefreshToken = data.refresh_token
				state.vendorAccessToken = data.access_token
				state.vendorTokenExpires = now() + (data.expires_in * 1000)
				return true
			}

		}
	} catch (Exception e) {
		log.debug "Error: $e"
	}

	log.debug "State: $state"

	if ( !state.vendorAccessToken ) {  //We didn't get an access token
		return false
	}
}

String toQueryString(Map m) {
	return m.collect { k, v -> "${k}=${URLEncoder.encode(v.toString())}" }.sort().join("&")
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	unschedule()
	initialize()
}

def initialize() {
	log.debug "Initialized with settings: ${settings}"

	// Pull the latest device info into state
	getDeviceList();

	settings.devices.each {
		def deviceId = it
		def detail = state.deviceDetail[deviceId]

		try {
			switch(detail.type) {
				case 'NAMain':
					log.debug "Base station"
					createChildDevice("My Netatmo Basestation", deviceId, "${detail.type}.${deviceId}", detail.module_name)
					break
				case 'NAModule1':
					log.debug "Outdoor module"
					createChildDevice("My Netatmo Outdoor Module", deviceId, "${detail.type}.${deviceId}", detail.module_name)
					break
				case 'NAModule2':
					log.debug "Wind module"
					createChildDevice("My Netatmo Wind", deviceId, "${detail.type}.${deviceId}", detail.module_name)
					break
				case 'NAModule3':
					log.debug "Rain Gauge"
					createChildDevice("My Netatmo Rain", deviceId, "${detail.type}.${deviceId}", detail.module_name)
					break
				case 'NAModule4':
					log.debug "Additional module"
					createChildDevice("My Netatmo Additional Module", deviceId, "${detail.type}.${deviceId}", detail.module_name)
					break
			}
		} catch (Exception e) {
			log.error "Error creating device: ${e}"
		}
	}

	// Cleanup any other devices that need to go away
	def delete = getChildDevices().findAll { !settings.devices.contains(it.deviceNetworkId) }
	log.debug "Delete: $delete"
	delete.each { deleteChildDevice(it.deviceNetworkId) }

	// Do the initial poll
	poll()
	// Schedule it to run every 5 minutes
	runEvery5Minutes("poll")
}

def uninstalled() {
	log.debug "In uninstalled"

	removeChildDevices(getChildDevices())
}

def getDeviceList() {
	log.debug "In getDeviceList"

	def deviceList = [:]
	state.deviceDetail = [:]
	state.deviceState = [:]

	apiGet("/api/devicelist") { response ->
		response.data.body.devices.each { value ->
			def key = value._id
			deviceList[key] = "${value.station_name}: ${value.module_name}"
			state.deviceDetail[key] = value
            state.deviceState[key] = value.dashboard_data
		}
		response.data.body.modules.each { value ->
			def key = value._id
			deviceList[key] = "${state.deviceDetail[value.main_device].station_name}: ${value.module_name}"
			state.deviceDetail[key] = value
            state.deviceState[key] = value.dashboard_data
		}
	}

	return deviceList.sort() { it.value.toLowerCase() }
}

private removeChildDevices(delete) {
	log.debug "In removeChildDevices"

	log.debug "deleting ${delete.size()} devices"

	delete.each {
		deleteChildDevice(it.deviceNetworkId)
	}
}

def createChildDevice(deviceFile, dni, name, label) {
	log.debug "In createChildDevice"

	try {
		def existingDevice = getChildDevice(dni)
		if(!existingDevice) {
			log.debug "Creating child"
			def childDevice = addChildDevice("TheHundredthIdiot", deviceFile, dni, null, [name: name, label: label, completedSetup: true])
		} else {
			log.debug "Device $dni already exists"
		}
	} catch (e) {
		log.error "Error creating device: ${e}"
	}
}

def listDevices() {
	log.debug "In listDevices"

	def devices = getDeviceList()

	dynamicPage(name: "listDevices", title: "Choose devices", install: false, nextPage: "selectDefaultPreferences") {
		section("Devices") {
			input "devices", "enum", title: "Select Device(s)", required: false, multiple: true, options: devices
		}
	}
}

def selectDefaultPreferences() {
	log.debug("In selectDefaultPreferences")
    
	def devicePresent = 0 

	settings.devices.each { deviceId ->
		def detail = state.deviceDetail[deviceId]
		switch(detail.type) {
			case 'NAModule2':
            	devicePresent = devicePresent + 1
                log.debug "In DefaultPreferences Wind device found"
                break
			case 'NAModule3':
				devicePresent = devicePresent + 2
                log.debug "In DefaultPreferences Rain device found"
                break
        }
    }

	if (devicePresent != 0) {
		dynamicPage(name: "listDevices", title: "Preferences", install: false, nextPage: "selectAdditionalPreferences") {
			section("Device prefernces") {
				input "pressUnits", "enum", title: "Report pressure in", description: "Pressure Measurement", required: true, options: ['bar':'Bar (bar)', 'mbar':'Millibar (mbar)', 'inHg':'Inch Mercury (inHg)', 'mmHg':'Millimeter Mercury (mmHg)']
				input "decimalUnits", "enum", title: "Round device measurements to", description: "Decimal accuracy", required: true, options: [0:'Integer', 1:'1 Decimal Place', 2:'2 Decimal Places', 3:'3 Decimal Places', 4:'4 Decimal Places']
			}
		}
	} else {
		dynamicPage(name: "listDevices", title: "Preferences", install: true) {
			section("Device preferences") {
				input "pressUnits", "enum", title: "Report pressure in", description: "Pressure Measurement", required: true, options: ['bar':'Bar (bar)', 'mbar':'Millibar (mbar)', 'inHg':'Inch Mercury (inHg)', 'mmHg':'Millimeter Mercury (mmHg)']
				input "decimalUnits", "enum", title: "Round device measurements to", description: "Decimal accuracy", required: true, options: [0:'Integer', 1:'1 Decimal Place', 2:'2 Decimal Places', 3:'3 Decimal Places', 4:'4 Decimal Places']
			}
        }
    }
}

def selectAdditionalPreferences() {
	log.debug "In selectAdditionalPreferences"

	def devicePresent = 0 

	settings.devices.each { deviceId ->
		def detail = state.deviceDetail[deviceId]
		switch(detail.type) {
			case 'NAModule2':
            	devicePresent = devicePresent + 1
				log.debug "In selectAdditionalPreferences Wind device found"
				break
			case 'NAModule3':
				devicePresent = devicePresent + 2
				log.debug "In selectAdditionalPreferences Rain device found"
				break
        }
    }

	if (devicePresent == 1) {
		dynamicPage(name: "selectAdditionalPreferences", title: "Additional Preferences", install: true) {
    		section("Wind Gauge") {
 			input "windUnits", "enum", title: "Report wind in", description: "Wind speed measurement", required: true, options: ['mph':'Miles per Hour (mph)', 'kph':'Kilometres per Hour (kph)', 'fps':'Feet per Second (fps)', 'mps':'Metre per Second (mps)', 'kn':'Knots (kn)']
 		    input "beaufortDescription", "enum", title: "Beaufort description for", required: true, options: ['Land':'Land', 'Sea':'Sea']
		}
	}
	} else if (devicePresent == 2) {
		dynamicPage(name: "selectAdditionalPreferences", title: "Additional Preferences", install: true) {
    		section("Rain Gauage") {
        		input "rainUnits", "enum", title: "Report rain in", description: "Millimetres or Inches", required: true, options: ['mm':'Millimetres (mm)', 'in':'Inches (in)']
			}
		}
	} else if (devicePresent == 3) {
		dynamicPage(name: "selectAdditionalPreferences", title: "Additional Preferences", install: true) {
    		section("Wind and Rain Gauges") {
 			input "windUnits", "enum", title: "Report wind in", description: "Wind speed measurement", required: true, options: ['mph':'Miles per Hour (mph)', 'kph':'Kilometres per Hour (kph)', 'fps':'Feet per Second (fps)', 'mps':'Metre per Second (mps)', 'kn':'Knots (kn)']
 		    input "beaufortDescription", "enum", title: "Beaufort description for", required: true, options: ['Land':'Land', 'Sea':'Sea']
        	input "rainUnits", "enum", title: "Report rain in", description: "Millimetres or Inches", required: true, options: ['mm':'Millimetres (mm)', 'in':'Inches (in)']
			}
    	}
    }    
}

def apiGet(String path, Map query, Closure callback) {
	if(now() >= state.vendorTokenExpires) {
		refreshToken();
	}

	query['access_token'] = state.vendorAccessToken
	def params = [
		uri: apiUrl(),
		path: path,
		'query': query
	]
	// log.debug "API Get: $params"

	try {
		httpGet(params)	{ response ->
			callback.call(response)
		}
	} catch (Exception e) {
		// This is most likely due to an invalid token. Try to refresh it and try again.
		log.debug "apiGet: Call failed $e"
		if(refreshToken()) {
			log.debug "apiGet: Trying again after refreshing token"
			httpGet(params)	{ response ->
				callback.call(response)
			}
		}
	}
}

def apiGet(String path, Closure callback) {
	apiGet(path, [:], callback);
}

def poll() {
	log.debug "In Poll"
	getDeviceList();
	def children = getChildDevices()
    log.debug "State: ${state.deviceState}"

	settings.devices.each { deviceId ->
		def detail = state.deviceDetail[deviceId]
		def data = state.deviceState[deviceId]
		def child = children.find { it.deviceNetworkId == deviceId }

		log.debug "Update: $child";
		switch(detail.type) {
			case 'NAMain':
				log.debug "Updating NAMain $data"
				child?.sendEvent(name: 'temperature', 	value: (String.format("%.${decimalUnits}f", cToPref(data['Temperature']) as float)), unit: getTemperatureScale())
				child?.sendEvent(name: 'carbonDioxide', value: data['CO2'])
				child?.sendEvent(name: 'humidity', 		value: data['Humidity'])
				child?.sendEvent(name: 'pressure', 		value: (String.format("%.${decimalUnits}f", pressToPref(data['Pressure']) as float))+' '+settings.pressUnits, unit: settings.pressUnits)
				child?.sendEvent(name: 'noise', 		value: data['Noise'])
				break;
			case 'NAModule1':
				log.debug "Updating NAModule1 $data"
				child?.sendEvent(name: 'temperature', 	value: (String.format("%.${decimalUnits}f", cToPref(data['Temperature']) as float)), unit: getTemperatureScale())
				child?.sendEvent(name: 'humidity',		value: data['Humidity'])
				break;
			case 'NAModule2':
				log.debug "Updating NAModule2 $data"
				child?.sendEvent(name: 'WindAngle',				value: data['WindAngle'])
                child?.sendEvent(name: 'WindAngleText',			value: windDirection(data['WindAngle']))
				child?.sendEvent(name: 'WindStrength',			value: (String.format("%.${decimalUnits}f", windToPref(data['WindStrength']) as float))+" "+settings.windUnits, unit: settings.windUnits)
				child?.sendEvent(name: 'GustStrength', 			value: (String.format("%.${decimalUnits}f", windToPref(data['GustStrength']) as float))+" "+settings.windUnits, unit: settings.windUnits)
				child?.sendEvent(name: 'Beaufort',     			value: Math.round(windToBeaufort(data['WindStrength']) as float), unit: 'bf')
                child?.sendEvent(name: 'BeaufortDescription',	value: Math.round(windToBeaufort(data['WindStrength']) as float)+" "+settings.beaufortDescription)
		        child?.sendEvent(name: 'units',					value: settings.windUnits)
 				break;
            		case 'NAModule3':
				log.debug "Updating NAModule3 $data"
				child?.sendEvent(name: 'rain', 			value: (String.format("%.${decimalUnits}f", rainToPref(data['Rain']) as float))+" "+settings.rainUnits, unit: settings.rainUnits)
				child?.sendEvent(name: 'rainSumHour', 	value: (String.format("%.${decimalUnits}f", rainToPref(data['sum_rain_1']) as float))+" "+settings.rainUnits, unit: settings.rainUnits)
				child?.sendEvent(name: 'rainSumDay',  	value: (String.format("%.${decimalUnits}f", rainToPref(data['sum_rain_24']) as float))+" "+settings.rainUnits, unit: settings.rainUnits)
				child?.sendEvent(name: 'units', 		value: settings.rainUnits)
				break;
			case 'NAModule4':
				log.debug "Updating NAModule4 $data"
				child?.sendEvent(name: 'temperature', 	value: (String.format("%.${decimalUnits}f", cToPref(data['Temperature']) as float)), unit: getTemperatureScale())
				child?.sendEvent(name: 'carbonDioxide', value: data['CO2'])
				child?.sendEvent(name: 'humidity',		value: data['Humidity'])
				break;
    		}
	}
}

def cToPref(temp) {
	log.debug "In cToPref"
    if(getTemperatureScale() == 'C') {
    	return temp
    } else {
		return (temp * 1.8) + 32
	}
}

def pressToPref(pressure) {
	log.debug "In mbarToPref"

	switch (settings.pressUnits) {
    	case 'bar':
        	return pressure * 0.001
			break;        
        case 'mbar':
        	return pressure
            break;
        case 'inHg':
        	return pressure * 0.0295301
            break;
        case 'mmHg':
	   		return pressure * 0.750062
			break;
	}
}    

def rainToPref(rain) {
	log.debug "In rainToPref"
	if(settings.rainUnits == 'mm') {
    	return rain
    } else {
    	return rain * 0.0393701
    }
}

def windToPref(wind) {
	log.debug "In windToPref"
	switch(settings.windUnits) {
    	case 'kph':
		return wind
            	break;
	case 'mph':
		return wind * 0.621371
		break;
    	case 'fps':
	        return wind * 1.46667
        	break;
    	case 'mps':
       		return wind * 0.44704
            	break;
    	case 'kn':
        	return wind * 0.868976
 		break;
//    	case 'bf':
//        	return windToBeaufort(wind)
//         	break;
	}
}

def windToBeaufort(miles) {
	log.debug "In windToBeaufort"
//	Convert to mph to align with original Beaufort scale measurement units (rather than kph)
	miles = miles * 0.621371 
	if (miles < 1) {
    		return 0
    	}    
    	int wholeMiles = miles
    	switch(wholeMiles) {
		case 1..3:
			return 1
 			break;
		case 4..7:
			return 2 
 			break;
 		case 8..12:
			return 3
 			break;
 		case 13..18:
			return 4
 			break;
 		case 19..24:
			return 5
 			break;
 		case 25..31:
			return 6
 			break;
 		case 32..38:
			return 7
 			break;
 		case 39..46:
			return 8
 			break;
 		case 57..54:
			return 9                   
 			break;
 		case 55..63:
			return 10                   
 			break;
 		case 64..72:
			return 11
 			break;
        	case 73..999:
          		return 12
 			break;
 		default:
           		return 0
 			break;
	}
}           
  
def windDirection(degrees) {
	log.debug "In windDirection"
    switch(degrees) {
		case 0..5:
			return degrees+'° - North (N)'
			break;
		case 5..16:
			return degrees+'° - North by East (NbE)'
			break;
		case 16..28:
			return degrees+'° - North-northeast (NNE)'
			break;
		case 28..39:
			return degrees+'° - Northeast by North (NEbN)'
			break;
		case 39..50:
			return degrees+'° - Northeast (NE)'
			break;
		case 50..61:
			return degrees+'° - Northeast by East (NEbE)'
			break;
		case 61..73:
			return degrees+'° - East-northeast (ENE)'
			break;
		case 73..84:
			return degrees+'° - East by North (EbN)'
			break;
		case 84..95:
			return degrees+'° - East (E)'
			break;
		case 95..106:
			return degrees+'° - East by South (EbS)'
			break;
		case 106..118:
			return degrees+'° - East-southeast (ESE)'
			break;
		case 118..129:
			return degrees+'° - Southeast by East (SEbE)'
			break;
		case 129..140:
			return degrees+'° - Southeast (SE)'
			break;
		case 140..151:
			return degrees+'° - Southeast by South (SEbS)'
			break;
		case 151..163:
			return degrees+'° - South-southeast (SSE)'
			break;
		case 163..174:
			return degrees+'° - South by East (SbE)'
			break;
		case 174..185:
			return degrees+'° - South (S)'
			break;
		case 185..196:
			return degrees+'° - South by West (SbW)'
			break;
		case 196..208:
			return degrees+'° - South-southwest (SSW)'
			break;
		case 208..219:
			return degrees+'° - Southwest by South (SWbS)'
			break;
		case 219..230:
			return degrees+'° - Southwest (SW)'
			break;
		case 230..241:
			return degrees+'° - Southwest by West (SWbW)'
			break;
		case 241..253:
			return degrees+'° - West-southwest (WSW)'
			break;
		case 253..264:
			return degrees+'° - West by South (WbS)'
			break;
		case 264..275:
			return degrees+'° - West (W)'
			break;
		case 275..286:
			return degrees+'° - West by North (WbN)'
			break;
		case 286..298:
			return degrees+'° - West-northwest (WNW)'
			break;
		case 298..309:
			return degrees+'° - Northwest by West (NWbW)'
			break;
		case 309..320:
			return degrees+'° - Northwest (NW)'
			break;
		case 320..331:
			return degrees+'° - Northwest by North (NWbN)'
			break;
		case 331..343:
			return degrees+'° - North-northwest (NNW)'
			break;
		case 343..354:
			return degrees+'° - North by West (NbW)'
			break;
		case 354..360:
			return degrees+'° - North (N)'
			break;
	}
}           

def debugEvent(message, displayEvent) {

	def results = [
		name: "appdebug",
		descriptionText: message,
		displayed: displayEvent
	]
	log.debug "Generating AppDebug Event: ${results}"
	sendEvent (results)

}

private Boolean canInstallLabs() {
	return hasAllHubsOver("000.011.00603")
}

private Boolean hasAllHubsOver(String desiredFirmware) {
	return realHubFirmwareVersions.every { fw -> fw >= desiredFirmware }
}

private List getRealHubFirmwareVersions() {
	return location.hubs*.firmwareVersionString.findAll { it }
}
