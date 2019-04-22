# README

## Run

    ./gradlew bootRun
    
    
## Fees

    GET http://localhost:8080/fees/compute
    
    Content-Type: application/json
    
    {
    	"client": {
    		"ip": "2.136.2.52"
    	},
    	"freelance": {
    		"ip": "2.136.2.51"
    	},
    	"mission": {
    		"length": "4months"
    	},
    	"firstMission": "2019-03-21T12:12:12",
    	"lastMission": "2019-03-21T12:12:12"
    }
    
    
## Rules

Rules use MVEL syntax.

    PUT http://localhost:8080/rules
    
    Content-Type: application/json
    
    {
    	"name": "demo",
    	"rule": "name: \"demo\"\ndescription: \"fees == 2\"\npriority: 2\ncondition: \"relationship != null\"\nactions:\n  - \"result.setFees(2);\"\n  - \"result.setRuleName('demo');\""
    }
    
