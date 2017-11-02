# Pingers

## How to run the target mock application  that receives status reported
* Install npm & nodejs
* `cd server-mock\express-es6-rest-api`
* `npm install`
* `npm run dev`
* Uncomment **urlHttpReport** property of config.properties file
* Run unit tests. Just PingSchedulerTest depends of this mock application.

## Task list

* Improve log to console output
* Avoid bubble exception
* Parameterize dependency of unit test of URL http
* Comments
* Remove setPinger of Pinger implementations
* Identify and adapt commands to SO
* Remove dependency of external mock in unit tests