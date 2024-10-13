metadata {
    definition (name: "Virtual Airthings Sensor", namespace: "snoopy", author: "snoopy") {
        capability "Carbon Dioxide Measurement"
        capability "Relative Humidity Measurement"
        capability "Temperature Measurement"
        capability "Battery"
        command "setCarbonDioxide", ["Number"]
        command "setRelativeHumidity", ["Number"]
        command "setTemperature", ["Number"]
        command "setRadon", ["Number"]
        command "setVoc", ["Number"]
        command "setPressure", ["Number"]
        command "setBattery", ["Number"]
        attribute "radon", "Number"
        attribute "voc", "Number"
        attribute "pressure", "Number"
        attribute "battery", "Number"
    }
    preferences {
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
        input name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true
        input name: "useFahrenheit", type: "bool", title: "Use Fahrenheit", defaultValue:false
        input name: "usePicoC", type: "bool", title: "Use pCi/L for Radon", defaultValue:false
    }
}

def logsOff(){
    log.warn "debug logging disabled..."
    device.updateSetting("logEnable",[value:"false",type:"bool"])
}

def installed() {
    log.warn "installed..."
    runIn(1800,logsOff)
}

def updated() {
    log.info "updated..."
    log.warn "debug logging is: ${logEnable == true}"
    log.warn "description logging is: ${txtEnable == true}"
    if (logEnable) runIn(1800,logsOff)
}

def setCarbonDioxide(CO2) {
    def unit = "ppm"
    def descriptionText = "${device.displayName}  Carbon Dioxide is ${CO2} ${unit}"
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "carbonDioxide", value: CO2, descriptionText: descriptionText, unit: unit)
}

def setRelativeHumidity(humid) {
    def unit = "%"
    def descriptionText = "${device.displayName} is ${humid}${unit}"
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "humidity", value: humid, descriptionText: descriptionText, unit: unit)
}

def setTemperature(temp) {
    def unit="°C"
    if(useFahrenheit){ 
        unit = "°F"
        temp = celsiusToFahrenheit(Float.valueOf(temp)).toFloat().round(1)
    }
    def descriptionText = "${device.displayName} is ${temp}${unit}"
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "temperature", value: temp, descriptionText: descriptionText, unit: unit)
}

def setRadon(radon) {
    if(usePicoC){
        radon = (Float.valueOf(radon)/37).toFloat().round(1)
        unit="pCi/L"
    }else{
        unit="Bq/m<sup>3</sup>"
    }
    def descriptionText = "${device.displayName} is ${radon} ${unit}"
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "radon", value: radon, descriptionText: descriptionText, unit: unit)
}

def setVoc(voc) {
    def unit = "ppb"
    def descriptionText = "${device.displayName} is ${voc} ${unit}"
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "voc", value: voc, descriptionText: descriptionText, unit: unit)
}

def setPressure(pressure) {
    def unit = "hPa"
    def descriptionText = "${device.displayName} is ${pressure} pressure"
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "pressure", value: pressure, descriptionText: descriptionText, unit: unit)
}

def setBattery(battery) {
    def unit = "%"
    def descriptionText = "${device.displayName} is ${battery}${unit}"
    if (txtEnable) log.info "${descriptionText}"
    sendEvent(name: "battery", value: battery, descriptionText: descriptionText, unit: unit)
}
