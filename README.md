## Quick start ##
To run TwitterSpout: 
``mvn compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=TwitterSpout
-Dexec.args="CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET" ``
... where CONSUMER\_KEY, CONSUMER\_SECRET, ACCESS\_TOKEN, ACCESS\_TOKEN\_SECRET are values from your twitter developer account. For details see below.

## Create keys and tokens ##
1. Go to [Twitter developer site](https://dev.twitter.com) and log in. Create new account if you do not already have one
2. Click on the arrow on top right beside your account image and select "My applications".
3. Create a new application. If you already have one, you can use credentials from that application and skip this and the next step.
4. Fill in the details. You can use dummy values for all required fields.
5. In the application detail page note the **Consumer key** and **Consumer secret** fields under OAuth settings
6. Create a new access token at the bottom of the page. The **Access token** and **Access token secret** will be shown after a moment.