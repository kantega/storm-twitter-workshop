## Quick start ##
To run TwitterSpout: 
``mvn compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=TwitterSpout
-Dexec.args="CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET" ``
... where CONSUMER\_KEY, CONSUMER\_SECRET, ACCESS\_TOKEN, ACCESS\_TOKEN\_SECRET are values from your twitter developer account. For details see below.

## STEP ONE ##
## Twitter Developer: Create keys and tokens ##
1. Go to [Twitter developer site](https://dev.twitter.com) and log in. Create new account if you do not already have one
2. Click on the arrow on top right beside your account image and select "My applications".
3. Create a new application. If you already have one, you can use credentials from that application and skip this and the next step.
4. Fill in the details. You can use dummy values for all required fields.
5. In the application detail page note the **Consumer key** and **Consumer secret** fields under OAuth settings
6. Create a new access token at the bottom of the page. The **Access token** and **Access token secret** will be shown after a moment.

## STEP TWO ##
## Storm: My first topology
Goal: Print out tweets
1. Define your Twitter query (location and topic based)
2. Build a topology that use your twitter query and routes the twitter messages to the PrinterBolt


## STEP THREE ##
## Storm: Extracting hashtags
Goal: Build a topology that extracts and prints the hashtags from Norwegian tweets. We do this by introducing a new bolt.


## STEP FOUR ##
## Storm: Rank the 10 most popular hashtags
Problem: How do we create such ranking if we do not store every event or hashtag?

## STEP FIVE ##
## Storm: Extracting sentiment values
Sentiment values are numerical measures of how positive or negative a given text is. By extracting sentiment values from tweets we can chart how positive people are to certain entities.
A common approach for extracting sentiment values is to apply dictionaries where different words are assosiated  with a sentiment value. Such dictionaries are customized for each language.
Goal: Extract the sentiment value for each of the tweets in your stream
To accomplish this task we need a topology with a bolt for language detection and a bolt for extracting the sentiment value.

## STEP SIX ##
## Storm: Its your playground!
Be creative - Form new twitter queries, create new bolts, combine them and extract knowledge!
For example:
* Find out where people are most happy, Oslo, Trondheim, Stavanger or Bergen?
* How happy are people about the weather?