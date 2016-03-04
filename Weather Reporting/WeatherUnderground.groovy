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
 *		http://www.wunderground.com/cgi-bin/findweather/getForecast?query=pws:IDORSETC8
 *	   	http://en.wikipedia.org/wiki/Beaufort_scale#Modern_scale
 *	 
 *		Steel Blue (#4682b4):
 *			http://www.w3schools.com/colors/colors_hex.asp  
 *			http://www.w3schools.com/colors/colors_picker.asp?colorhex=4682B4
 *
 *	{
 *		"response": {
 *		"version": "0.1",
 *		"termsofService": "http://www.wunderground.com/weather/api/d/terms.html",
 *		"features": {
 *			"conditions": 1
 *			}
 *		},
 *		"current_observation": {
 *			"image": {
 *				"url": "http://icons-ak.wxug.com/graphics/wu2/logo_130x80.png",
 *				"title": "Weather Underground",
 *				"link": "http://www.wunderground.com"
 *			},
 *			"display_location": {
 *					"full": "San Francisco, CA",
 *					"city": "San Francisco",
 *					"state": "CA",
 *					"state_name": "California",
 *					"country": "US",
 *					"country_iso3166": "US",
 *					"zip": "94101",
 *					"latitude": "37.77500916",
 *					"longitude": "-122.41825867",
 *					"elevation": "47.00000000"
 *			},
 *			"observation_location": {
 *					"full": "SOMA - Near Van Ness, San Francisco, California",
 *					"city": "SOMA - Near Van Ness, San Francisco",
 *					"state": "California",
 *					"country": "US",
 *					"country_iso3166": "US",
 *					"latitude": "37.773285",
 *					"longitude": "-122.417725",
 *					"elevation": "49 ft"
 *			},
 *			"estimated": {},
 *			"station_id": "KCASANFR58",
 *			"observation_time": "Last Updated on June 27, 5:27 PM PDT",
 *			"observation_time_rfc822": "Wed, 27 Jun 2012 17:27:13 -0700",
 *			"observation_epoch": "1340843233",
 *			"local_time_rfc822": "Wed, 27 Jun 2012 17:27:14 -0700",
 *			"local_epoch": "1340843234",
 *			"local_tz_short": "PDT",
 *			"local_tz_long": "America/Los_Angeles",
 *			"local_tz_offset": "-0700",
 *			"weather": "Partly Cloudy",
 *			"temperature_string": "66.3 F (19.1 C)",
 *			"temp_f": 66.3,
 *			"temp_c": 19.1,
 *			"relative_humidity": "65%",
 *			"wind_string": "From the NNW at 22.0 MPH Gusting to 28.0 MPH",
 *			"wind_dir": "NNW",
 *			"wind_degrees": 346,
 *			"wind_mph": 22.0,
 *			"wind_gust_mph": "28.0",
 *			"wind_kph": 35.4,
 *			"wind_gust_kph": "45.1",
 *			"pressure_mb": "1013",
 *			"pressure_in": "29.93",
 *			"pressure_trend": "+",
 *			"dewpoint_string": "54 F (12 C)",
 *			"dewpoint_f": 54,
 *			"dewpoint_c": 12,
 *			"heat_index_string": "NA",
 *			"heat_index_f": "NA",
 *			"heat_index_c": "NA",
 *			"windchill_string": "NA",
 *			"windchill_f": "NA",
 *			"windchill_c": "NA",
 *			"feelslike_string": "66.3 F (19.1 C)",
 *			"feelslike_f": "66.3",
 *			"feelslike_c": "19.1",
 *			"visibility_mi": "10.0",
 *			"visibility_km": "16.1",
 *			"solarradiation": "",
 *			"UV": "5",
 *			"precip_1hr_string": "0.00 in ( 0 mm)",
 *			"precip_1hr_in": "0.00",
 *			"precip_1hr_metric": " 0",
 *			"precip_today_string": "0.00 in (0 mm)",
 *			"precip_today_in": "0.00",
 *			"precip_today_metric": "0",
 *			"icon": "partlycloudy",
 *			"icon_url": "http://icons-ak.wxug.com/i/c/k/partlycloudy.gif",
 *			"forecast_url": "http://www.wunderground.com/US/CA/San_Francisco.html",
 *			"history_url": "http://www.wunderground.com/history/airport/KCASANFR58/2012/6/27/DailyHistory.html",
 *			"ob_url": "http://www.wunderground.com/cgi-bin/findweather/getForecast?query=37.773285,-122.417725"
 *		}
 *	}
 */

preferences {
}

metadata {
	definition (name: "My Weather Underground", namespace: "TheHundredthIdiot", author: "Andy") {
		capability "Polling"
      	capability "Relative Humidity Measurement"
      	capability "Water Sensor"
      	capability "Temperature Measurement"
	}

	simulator {
	}

	tiles (scale: 2) {
		multiAttributeTile(name:"forecast", type:"lighting", width: 6, height: 4) {
			tileAttribute("device.forecast", key: "PRIMARY_CONTROL") {
				attributeState "default",        label: 'updating...',        icon: "st.unknown.unknown.unknown",	backgroundColor: "#edf3f8"
	        	attributeState "chancesnow",     label: 'Chance of Snow',     icon: "st.Weather.weather6",			backgroundColor: "#dae7f1"
	        	attributeState "chancetstorms",  label: 'Chance of Storms',   icon: "st.Weather.weather9",			backgroundColor: "#c8daea"
	        	attributeState "clear",          label: 'Clear',              icon: "st.Weather.weather14",			backgroundColor: "#b6cee2"
	        	attributeState "cloudy",         label: 'Cloudy',             icon: "st.Weather.weather15",			backgroundColor: "#a3c2db"
	        	attributeState "flurries",       label: 'Flurries',           icon: "st.Weather.weather6",			backgroundColor: "#91b6d4"
				attributeState "hazy",           label: 'Hazy',               icon: "st.Weather.weather13",			backgroundColor: "#7ea9cd"
	        	attributeState "mostlycloudy",   label: 'Mostly Cloudy',      icon: "st.Weather.weather15",			backgroundColor: "#6c9dc6"
	       	 	attributeState "mostlysunny",    label: 'Mostly Sunny',       icon: "st.Weather.weather11",			backgroundColor: "#5a91bf"
	        	attributeState "partlycloudy",   label: 'Partly Cloudy',      icon: "st.Weather.weather11",			backgroundColor: "#4785b8"
	        	attributeState "partlysunny",    label: 'Partly Sunny',       icon: "st.Weather.weather11",			backgroundColor: "#4682b4"
	        	attributeState "sleet",          label: 'Sleet',              icon: "st.Weather.weather10",			backgroundColor: "#4077a5"
	        	attributeState "snow",           label: 'Snow',               icon: "st.Weather.weather7",			backgroundColor: "#396a93"
	        	attributeState "sunny",          label: 'Sunny',              icon: "st.Weather.weather14",			backgroundColor: "#325d81"
	           	attributeState "chancerain",     label: 'Chance of Rain',     icon: "st.Weather.weather9",			backgroundColor: "#2b506e"
	           	attributeState "chancesleet",    label: 'Chance of Sleet',    icon: "st.Weather.weather6",			backgroundColor: "#24425c"
				attributeState "fog",            label: 'Fog',                icon: "st.Weather.weather13",			backgroundColor: "#1d3549"
	           	attributeState "chanceflurries", label: 'Chance of Flurries', icon: "st.Weather.weather6",			backgroundColor: "#152837"
	           	attributeState "rain",           label: 'Rain',               icon: "st.Weather.weather10",			backgroundColor: "#0e1b25"
				attributeState "tstorms",        label: 'Thunder Storms',     icon: "st.Weather.weather10",			backgroundColor: "#070d12"
            }        
			tileAttribute("temperature_time", key: "SECONDARY_CONTROL") {
	   			attributeState "temperature_time", label:'${currentValue}'
			}
		}
	
	
		valueTile("temperature", "device.temperature", width: 2, height: 2, canChangeIcon: true) {
	        state "temperature", label: 'Outside\n${currentValue}°', unit:"C", backgroundColors: [
	                [value: 31, color: "#153591"],
	                [value: 44, color: "#1e9cbb"],
	                [value: 59, color: "#90d2a7"],
			        [value: 74, color: "#44b621"],
	                [value: 84, color: "#f1d801"],
	                [value: 95, color: "#d04e00"],
	                [value: 96, color: "#bc2323"]
	            ]
	        }
	    valueTile("feelslike_c", "device.feelslike_c", inactiveLabel: false, width: 2, height: 2) {
	        state "feelslike_c", label: 'Feels\n${currentValue}°', unit:"C", backgroundColors: [
	                [value: 31, color: "#153591"],
	                [value: 44, color: "#1e9cbb"],
	                [value: 59, color: "#90d2a7"],
	                [value: 74, color: "#44b621"],
	                [value: 84, color: "#f1d801"],
	                [value: 95, color: "#d04e00"],
	                [value: 96, color: "#bc2323"]
	            ]
	    } 
	    valueTile("dewpoint_c", "device.dewpoint_c", inactiveLabel: false, width: 2, height: 2) {
	        state "dewpoint_c", label: 'Dew At\n${currentValue}°', unit:"C", backgroundColors: [
	                [value: 31, color: "#153591"],
	                [value: 44, color: "#1e9cbb"],
	                [value: 59, color: "#90d2a7"],
	                [value: 74, color: "#44b621"],
	                [value: 84, color: "#f1d801"],
	                [value: 95, color: "#d04e00"],
	                [value: 96, color: "#bc2323"]
	            ]
	    }
	
	    valueTile("wind_speed", "device.wind_speed", inactiveLabel: false, width: 2, height: 2) {
	        state "wind_speed", label: 'Wind\n${currentValue} mph', unit: "mph", backgroundColors: [
	            [value: 0,  color: "#ffffff"],
	            [value: 1,  color: "#ccffff"],
	            [value: 4,  color: "#99ffcc"],
	            [value: 8,  color: "#99ff99"],
	            [value: 13, color: "#99ff66"],
	            [value: 18, color: "#99ff00"],
	            [value: 25, color: "#ccff00"],
	            [value: 31, color: "#ffff00"],
	            [value: 39, color: "#ffcc00"],
	            [value: 47, color: "#ff9900"],
	            [value: 55, color: "#ff6600"],
	            [value: 64, color: "#ff3300"],
	            [value: 74, color: "#ff0000"]
	        ]
	    }
	    valueTile("wind_direction", "device.wind_direction", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
	        state "wind_direction", label: 'Heading\n${currentValue}', unit: "Direction"
	    }
	    valueTile("wind_gust_mph", "device.wind_gust_mph", inactiveLabel: false, width: 2, height: 2) {
	        state "wind_gust_mph", label: 'Gust\n${currentValue} mph', unit: "mph", backgroundColors: [
	            [value: 0,  color: "#ffffff"],
	            [value: 1,  color: "#ccffff"],
	            [value: 4,  color: "#99ffcc"],
	            [value: 8,  color: "#99ff99"],
	            [value: 13, color: "#99ff66"],
	            [value: 18, color: "#99ff00"],
	            [value: 25, color: "#ccff00"],
	            [value: 31, color: "#ffff00"],
	            [value: 39, color: "#ffcc00"],
	            [value: 47, color: "#ff9900"],
	            [value: 55, color: "#ff6600"],
	            [value: 64, color: "#ff3300"],
	            [value: 74, color: "#ff0000"]
	        ]
	    }        

		valueTile("wind_string", "device.wind_string", inactiveLabel: false, decoration: "flat", width: 6, height: 2) {
	        state "wind_string", label: '${currentValue}'
	    }
	       
	
		valueTile("precip_1hr_metric", "device.precip_1hr_metric", inactiveabel: false, width: 2, height: 2) {
	        state "default", label: 'Rain in\nLast Hour\n${currentValue} mm\n(Wet)', backgroundColor:"#ff9999"
			state "0.0", 	 label: 'Rain in\nLast Hour\n${currentValue} mm\n(Dry)', backgroundColor:"#44b621"
	    }
	    valueTile("humidity", "device.humidity", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
	        state "default",  label:'${currentValue}%\nHumidity', unit: "Humidity"
	    }
		valueTile("precip_today_metric", "device.precip_today_metric", inactiveabel: false, width: 2, height: 2) {
	        state "default", label: 'Rain Today\n${currentValue} mm\n(Wet)', backgroundColor:"#ff9999"
			state "0.0", 	 label: 'Rain Today\n${currentValue} mm\n(Dry)', backgroundColor:"#44b621"
	    }
	
	
		valueTile("visibility_mi", "device.visibility_mi", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
	        state "visibility_mi", label: 'Visibility\n${currentValue}\nmiles', unit: "Miles"
	    }
		valueTile("uv_index", "device.uv_index", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
	        state "uv_index", label: 'UV Index\n${currentValue}', unit: "UV Index"
	    }
		valueTile("solarradiation", "device.solarradiation", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
	        state "default", label: 'Solar\nRadiation\n${currentValue}', unit: "Irradiance"
			state "--", label: 'Solar\nRadiation\n0\nkWh/m2', unit: "kWh/m2"
	    }
	
		valueTile("pressure_trend", "device.pressure_trend", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
	        state "pressure_trend", label: 'Presure\n${currentValue} mb', unit: "mb"
	    }
		valueTile("display_location", "device.display_location", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
	        state "display_location", label: '${currentValue}'
	    }
//		standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
//	        state "default", action:"polling.poll", icon:"st.secondary.refresh"
//	    }
		valueTile("observation_location", "device.observation_location", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
	        state "observation_location", label: '${currentValue}'
        }
	
		valueTile("observation_details", "device.observation_details", inactiveLabel: false, decoration: "flat", width: 6, height: 2) {
	        state "observation_details", label: '${currentValue}', action:"polling.poll"
	    }
	}
	   
	main (["forecast"])
	
	details(["forecast",
	   		 "temperature", "feelslike_c", "dewpoint_c",
             "wind_string",
	         "wind_speed", "wind_direction", "wind_gust_mph",
	         "precip_1hr_metric", "humidity", "precip_today_metric", 
	         "visibility_mi", "uv_index", "solarradiation",
	         "pressure_trend", "display_location", "observation_location",
	         "observation_details"]
     )

}

// As an object that can be polled, the poll() function will be called on each polling cycle.
def poll() {

    def weather

    log.debug "Post Code: (automatic)"
    weather = getWeatherFeature( "conditions" )

    // Check if the variable is populated, otherwise return.
    if (!weather) {
        log.debug( "Something went wrong, no data found." )
        return false
    }

	def time

	log.debug( "UV Index: ${weather.current_observation.UV}" )
    sendEvent( name: 'uv_index', value: weather.current_observation.UV )

    log.debug( "Forecast: ${weather.current_observation.icon}" )
    sendEvent( name: "forecast", value: weather.current_observation.icon )

    log.debug( "Wind Speed: ${weather.current_observation.wind_mph.toFloat()} mph")
    sendEvent( name: "wind_speed", value: weather.current_observation.wind_mph.toFloat() )

    log.debug( "Wind Direction: ${weather.current_observation.wind_dir}" )
    sendEvent( name: "wind_direction", value: weather.current_observation.wind_dir )

	log.debug( "Wind Gust: ${weather.current_observation.wind_gust_mph.toFloat()}" )
    sendEvent( name: "wind_gust_mph", value: weather.current_observation.wind_gust_mph.toFloat() )

	time = weather.current_observation.observation_time.getAt(16..(weather.current_observation.observation_time.length() - 5))

	log.debug( "Temperature Time: ${weather.current_observation.temp_c.toFloat()}° - ${time}" )
	sendEvent( name: "temperature_time", value: "${weather.current_observation.temp_c.toFloat()}° - ${time}" )

	log.debug( "Wind String: ${weather.current_observation.wind_string}  ("+time+")" )
	sendEvent( name: "wind_string", value: "${weather.current_observation.wind_string}  ("+time+")" )

	log.debug( "Dew Point: ${weather.current_observation.dewpoint_c.toFloat()}" )
    sendEvent( name: "dewpoint_c", value: weather.current_observation.dewpoint_c.toFloat() )

	log.debug( "Observation Details: ${weather.current_observation.observation_time} from Station ${weather.current_observation.station_id} (${weather.current_observation.observation_location.full}) for ${weather.current_observation.display_location.full}" )
    sendEvent( name: "observation_details", value: "${weather.current_observation.observation_time}\nfrom Station ${weather.current_observation.station_id} located in\n${weather.current_observation.observation_location.full}\nfor ${weather.current_observation.display_location.full}" )

	log.debug( "Observation Location: Lat: ${weather.current_observation.observation_location.latitude} Long: ${weather.current_observation.observation_location.longitude} Elev: ${weather.current_observation.observation_location.elevation}" )
	sendEvent( name: "observation_location", value: "Observation\nLat: ${weather.current_observation.observation_location.latitude}\nLong: ${weather.current_observation.observation_location.longitude}\nElev: ${weather.current_observation.observation_location.elevation}" )

	log.debug( "Feels Like: ${weather.current_observation.feelslike_c.toFloat()}" )
    sendEvent( name: 'feelslike_c', value: weather.current_observation.feelslike_c.toFloat() )

    log.debug( "Current Temperature: ${weather.current_observation.temp_c.toFloat()}" )
    sendEvent( name: 'temperature', value: weather.current_observation.temp_c.toFloat() )

    // Sending a value to a valueTile REQUIRES an Integer (on Android, for now)
    log.debug( "Relative Humidity: ${weather.current_observation.relative_humidity.tokenize('%')[0].toInteger()}" )
    sendEvent( name: 'humidity', value: weather.current_observation.relative_humidity.tokenize('%')[0].toInteger() )

    log.debug( "Precipitation Last Hour: ${weather.current_observation.precip_1hr_metric.toFloat()}" )
    sendEvent( name: 'precip_1hr_metric', value: weather.current_observation.precip_1hr_metric.toFloat() )

    log.debug( "Precipitation Today: ${weather.current_observation.precip_today_metric.toFloat()}" )
	sendEvent( name: 'precip_today_metric', value: weather.current_observation.precip_today_metric.toFloat() )
 
    log.debug( "Visibility: ${weather.current_observation.visibility_mi.toFloat()}" )
	sendEvent( name: 'visibility_mi', value: weather.current_observation.visibility_mi.toFloat() )

    log.debug( "Solar Radiation: ${weather.current_observation.solarradiation}" )
	sendEvent( name: 'solarradiation', value: weather.current_observation.solarradiation )

	if (weather.current_observation.pressure_trend == "0") {
    	log.debug( "Pressure: ~${weather.current_observation.pressure_mb}" )
		sendEvent( name: 'pressure_trend', value: "~" + weather.current_observation.pressure_mb )
    }
    else {
    	log.debug( "Pressure: ${weather.current_observation.pressure_trend}${weather.current_observation.pressure_mb}" )
		sendEvent( name: 'pressure_trend', value: weather.current_observation.pressure_trend + weather.current_observation.pressure_mb )
	}
    
    log.debug( "Elevation: ${weather.current_observation.display_location.elevation.toFloat()}" )
	sendEvent( name: 'elevation', value: weather.current_observation.display_location.elevation.toFloat() )

	log.debug( "Display Location: Lat: ${weather.current_observation.display_location.latitude} Long: ${weather.current_observation.display_location.longitude} Elev: ${weather.current_observation.display_location.elevation} ft" )
	sendEvent( name: "display_location", value: "Display\nLat: ${weather.current_observation.display_location.latitude}\nLong: ${weather.current_observation.display_location.longitude}\nElev: ${weather.current_observation.display_location.elevation} ft" )

}

def cToF(temp) {
    return temp * 1.8 + 32
}

def fToC(temp) {
    return (temp - 32) / 1.8
}