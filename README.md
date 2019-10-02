# Buggy - An Issue Tracker

## You Build It...

    ./gradlew clean build
    
## You Run It!

    ./gradlew bootRun

## You can use it:

[Swagger API Doc - Try IT UI](http://localhost:8080/swagger-ui.html)

## Hints

The persistence is done via an embedded MongoDB. 
There is no need for installing anything before the service is usable. 
The Service will download and rund the mongod instance by its own. 
The Datadir is set to the home directory of the current user. .buggy-storage
You are safe to delete the folder after testing everything. 


